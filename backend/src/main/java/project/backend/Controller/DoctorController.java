package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.backend.Controller.Request.*;
import project.backend.Entity.*;
import project.backend.Service.*;
import project.backend.Utils.Config;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class DoctorController {
    private TreatmentRegionService treatmentRegionService;
    private PatientService patientService;
    private ChecklistService checklistService;
    private PatientStatusService patientStatusService;
    private MessageService messageService;

    @Autowired
    public DoctorController(TreatmentRegionService treatmentRegionService, PatientService patientService,
                            ChecklistService checklistService, PatientStatusService patientStatusService,
                            MessageService messageService) {
        this.treatmentRegionService = treatmentRegionService;
        this.patientService = patientService;
        this.checklistService = checklistService;
        this.patientStatusService = patientStatusService;
        this.messageService = messageService;
    }

    @PostMapping("/getPatientInfo")
    public ResponseEntity<?> getPatientInfo(@RequestBody GetPatientInfoRequest request){
        if (!request.getId().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        int patientId = request.getPatient_id();
        Map<String, Object> result = new HashMap<>();
        // 1 基础信息
        List<String> levels = treatmentRegionService.getTreatmentRegions(request.getId(), Config.DOCTOR);
        Patient patient = patientService.getPatientById(Config.DOCTOR, patientId);
        boolean canDischarge = false;
        if (patient.getTreatment_region_level().equals("light") && patient.getDisease_level().equals("light")){
            canDischarge = patientService.canDischarge(Config.DOCTOR, patientId);
        }

        boolean needTransfer = false;
        for (String level : levels){
            if (patient.getTreatment_region_level().equals(level) && !patient.getDisease_level().equals(level)) {
                needTransfer = true;
                break;
            }
        }
        PatientInfo patientInfo = new PatientInfo(patient.getPatient_id(), patient.getName(), patient.getGender(),
                patient.getAge(), patient.getDisease_level(), patient.getLife_status(), patient.getNurse_id(),
                patient.getTreatment_region_level());
        if (patient.getNurse_id() == null && !patient.getTreatment_region_level().equals("quarantine")){
            if (patient.getLife_status().equals("dead")){
                patientInfo.setCan_be_discharged(3);
            }
            else {
                patientInfo.setCan_be_discharged(2);
            }
        }
        else {
            if (canDischarge) patientInfo.setCan_be_discharged(1);
            if (needTransfer) patientInfo.setNeed_transfer(1);
        }

        result.put("1", patientInfo);

        // 2 核酸检测单信息
        List<Checklist> checklists = checklistService.getChecklists(Config.DOCTOR, patientId);
        result.put("2", checklists);

        // 3 状态
        List<PatientStatusInfo> patientStatusInfos = new LinkedList<>();
        List<PatientStatus> patientStatuses = patientStatusService.getPatientStatuses(Config.DOCTOR, patientId);
        for (PatientStatus patientStatus: patientStatuses){
            PatientStatusInfo patientStatusInfo = new PatientStatusInfo(patientStatus.getId(), patientStatus.getTemperature(),
                    patientStatus.getSymptom(), patientStatus.getLife_status(), patientStatus.getDate(), patientStatus.getNurse_id());
            int checklistId = patientStatus.getChecklist_id();
            String testResult = checklistService.getTestResultById(Config.DOCTOR, checklistId);
            patientStatusInfo.setResult(testResult);
            patientStatusInfos.add(patientStatusInfo);
        }
        result.put("3", patientStatusInfos);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/modifyLifeStatus")
    public ResponseEntity<?> modifyLifeStatus(@RequestBody ModifyLifeStatusRequest request){
        if (!request.getDoctor_id().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String newStatus = request.getNew_life_status();
        int patientId = request.getPatient_id();
        if (!(newStatus.equals("dead") || newStatus.equals("healthy") || newStatus.equals("treating"))){
            return new ResponseEntity<>("Illegal status", HttpStatus.BAD_REQUEST);
        }
        Patient patient = patientService.getPatientById(Config.DOCTOR, patientId);
        TreatmentRegion region = treatmentRegionService.getTreatmentRegionByLevel(Config.DOCTOR, patient.getTreatment_region_level());
        patientService.updateLifeStatus(Config.DOCTOR, patientId, newStatus);

        // 如果更新为死亡状态，则因为病房护士和床位会空出来，判断是否存在需要进行区域转移的病人，并进行转移
        if (newStatus.equals("dead")){
            transferToCurrentRegion(region);
        }

        return ResponseEntity.ok("success");
    }

    @PostMapping("/modifyDiseaseLevel")
    public ResponseEntity<?> modifyDiseaseLevel(@RequestBody ModifyDiseaseLevelRequest request){
        if (!request.getDoctor_id().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        int patientId = request.getPatient_id();
        String newDiseaseLevel = request.getNew_disease_level();
        Patient patient = patientService.getPatientById(Config.DOCTOR, patientId);
        TreatmentRegion region = treatmentRegionService.getTreatmentRegionByLevel(Config.DOCTOR, patient.getTreatment_region_level());
        // 1 改病情评级
        patientService.updateDiseaseLevel(Config.DOCTOR, patientId, newDiseaseLevel);
        // 2 判断能否转到指定的治疗区域，不能的话还停留在当前区域
        boolean canTransfer = patientService.canTransfer(Config.DOCTOR, newDiseaseLevel, patient);
        if (canTransfer) {
            transferToCurrentRegion(region);
            SimpleDateFormat f = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date now = new Date();
            f.format(now);
            Timestamp time = new Timestamp(now.getTime());
            String chiefNurseId = treatmentRegionService.getChiefNurseIdByRegion(Config.ROOT, newDiseaseLevel);
            Message message = new Message();
            message.setStatus(0);
            message.setReceiver_id(chiefNurseId);
            message.setTime(time);
            message.setContent("A new patient has been transferred to " + newDiseaseLevel + ".");
            messageService.addNewMessage(Config.ROOT, message);
        }
        return ResponseEntity.ok(canTransfer);
    }

    @PostMapping("/newChecklist")
    public ResponseEntity<?> newChecklist(@RequestBody NewChecklistRequest request){
        if (!request.getDoctor_id().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String doctorId = request.getDoctor_id();
        int patientId = request.getPatient_id();
        SimpleDateFormat f = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date now = new Date();
        f.format(now);
        Timestamp time = new Timestamp(now.getTime());
        int checklistId = checklistService.addChecklist(Config.DOCTOR, doctorId, patientId, time);

        // TODO: 站内信，提醒对应的病房护士
        Message message = new Message();
        message.setStatus(0);
        String hospitalNurseId = patientService.getHospitalNurseIdByPatientId(Config.ROOT, patientId);
        message.setReceiver_id(hospitalNurseId);
        message.setContent("A new checklist has been appointed for patient " + patientId + ". " +
                "The checklist id is " + checklistId + ".");
        message.setTime(time);
        messageService.addNewMessage(Config.ROOT, message);

        return ResponseEntity.ok("success");
    }

    @PostMapping("/permitDischarge")
    public ResponseEntity<?> permitDischarge(@RequestBody PermitDischargeRequest request){
        if (!request.getDoctor_id().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        int patientId = request.getPatient_id();
        Patient patient = patientService.getPatientById(Config.DOCTOR, patientId);
        patientService.discharge(Config.DOCTOR, patient);
        TreatmentRegion region = treatmentRegionService.getTreatmentRegionByLevel(Config.DOCTOR, patient.getTreatment_region_level());
        transferToCurrentRegion(region);
        return ResponseEntity.ok("success");
    }

    // 获取待转入该区域的病人并进行转入
    private void transferToCurrentRegion(TreatmentRegion region){
        Patient patientNeedTransfer;
        // 1 查是否有病人等待转入该治疗区域，处于隔离区的病人优先
        List<Patient> patientsWaitToTransfer = patientService.getPatientsByDiseaseLevel(Config.ROOT, region.getLevel());
        if (patientsWaitToTransfer.isEmpty()){
            return;
        }
        patientNeedTransfer = patientsWaitToTransfer.get(0);
        for (Patient p : patientsWaitToTransfer){
            if (p.getTreatment_region_level() != null && p.getTreatment_region_level().equals("quarantine")){
                patientNeedTransfer = p;
                break;
            }
        }
        // 2 转移
        // ToDo: 属于系统自动做的事情，发站内信
        patientService.appointHospitalNurse(Config.ROOT, patientNeedTransfer, region);
        patientService.appointBed(Config.ROOT, patientNeedTransfer, region);

        SimpleDateFormat f = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date now = new Date();
        f.format(now);
        Timestamp time = new Timestamp(now.getTime());
        String chiefNurseId = treatmentRegionService.getChiefNurseIdByRegion(Config.ROOT, region.getLevel());
        Message message = new Message();
        message.setStatus(0);
        message.setReceiver_id(chiefNurseId);
        message.setTime(time);
        message.setContent("A new patient has been transferred to " + region.getLevel() + ".");
        messageService.addNewMessage(Config.ROOT, message);
    }
}
