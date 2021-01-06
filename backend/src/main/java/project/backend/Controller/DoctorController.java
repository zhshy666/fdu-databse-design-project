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

import java.util.*;

@RestController
public class DoctorController {
    private TreatmentRegionService treatmentRegionService;
    private PatientService patientService;
    private ChecklistService checklistService;
    private PatientStatusService patientStatusService;
    private HospitalNurseService hospitalNurseService;
    private ChiefNurseService chiefNurseService;

    @Autowired
    public DoctorController(TreatmentRegionService treatmentRegionService, PatientService patientService,
                            ChecklistService checklistService, PatientStatusService patientStatusService,
                            HospitalNurseService hospitalNurseService, ChiefNurseService chiefNurseService) {
        this.treatmentRegionService = treatmentRegionService;
        this.patientService = patientService;
        this.checklistService = checklistService;
        this.patientStatusService = patientStatusService;
        this.hospitalNurseService = hospitalNurseService;
        this.chiefNurseService = chiefNurseService;
    }

    @PostMapping("/getPatientsInfo")
    public ResponseEntity<?> getPatientsInfo(@RequestBody GetPatientsInfoRequest request){
        // 1 检查是否是医生身份
        if (!request.getId().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        List<PatientInfo> info = new ArrayList<>();  // 返回值
        String id = request.getId();

        // 2 找到该医生对应的治疗区域
        List<String> levels = treatmentRegionService.getTreatmentRegions(id, Config.DOCTOR);
        // 获取所有的 patients
        List<Patient> patients = patientService.getAllPatients(levels, Config.DOCTOR);
        // 查找符合指定条件的病人 ID 集合
        List<Integer> patientsCanBeDischarged = patientService.getPatientIdsWhoCanBeDischarged(Config.DOCTOR, patients);
        List<Integer> patientsNeedTransfer = patientService.getPatientIdsNeedTransfer(Config.DOCTOR, levels);

        for (Patient patient : patients){
            PatientInfo patientInfo = new PatientInfo(patient.getPatient_id(), patient.getName(), patient.getGender(),
                    patient.getAge(), patient.getDisease_level(), patient.getLife_status(), patient.getNurse_id(),
                    patient.getTreatment_region_level());
            if (patientsCanBeDischarged.contains(patient.getPatient_id())){
                patientInfo.setCan_be_discharged(1);
            }
            if (patientsNeedTransfer.contains(patient.getPatient_id())){
                patientInfo.setNeed_transfer(1);
            }
            info.add(patientInfo);
        }

        return ResponseEntity.ok(info);
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
        boolean canDischarge = patientService.canDischarge(Config.DOCTOR, patientId);
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
        if (canDischarge) patientInfo.setCan_be_discharged(1);
        if (needTransfer) patientInfo.setNeed_transfer(1);
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

    @PostMapping("/getNursesInfo")
    public ResponseEntity<?> getNursesInfo(@RequestBody GetNursesInfoRequest request){
        if (!request.getId().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        List<NurseInfo> result = new LinkedList<>();
        // 1 护士长信息
        List<ChiefNurse> chiefNurses = chiefNurseService.getChiefNurseByDoctorId(Config.DOCTOR, request.getId());
        for (ChiefNurse nurse: chiefNurses){
            NurseInfo nurseInfo = new NurseInfo(nurse.getId(), nurse.getName(), nurse.getAge(), nurse.getGender());
            nurseInfo.setType("chief_nurse");
            result.add(nurseInfo);
        }

        // 2 病房护士及各自负责的病人信息
        List<String> levels = treatmentRegionService.getTreatmentRegions(request.getId(), Config.DOCTOR);
        List<HospitalNurse> hospitalNurses = hospitalNurseService.getHospitalNursesByRegions(Config.DOCTOR, levels);
        for (HospitalNurse hospitalNurse: hospitalNurses){
            NurseInfo nurseInfo = new NurseInfo(hospitalNurse.getId(), hospitalNurse.getName(), hospitalNurse.getAge(),
                    hospitalNurse.getGender());
            nurseInfo.setType("hospital_nurse");
            nurseInfo.setPatients(new LinkedList<>());
            result.add(nurseInfo);
        }
        // 拿到该区域的所有病人信息
        List<Patient> patients = patientService.getAllPatients(levels, Config.DOCTOR);
        for (Patient patient: patients){
            String hospitalNurseId = patient.getNurse_id();
            for (int i = levels.size(); i < result.size(); i++){
                if (result.get(i).getId().equals(hospitalNurseId)){
                    result.get(i).getPatients().add(patient);
                    break;
                }
            }
        }
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
            Patient patientNeedTransfer;
            // 1 查是否有病人等待转入该治疗区域，处于隔离区的病人优先
            List<Patient> patientsWaitToTransfer = patientService.getPatientsByDiseaseLevel(Config.ROOT, region.getLevel());
            if (patientsWaitToTransfer.isEmpty()){
                return ResponseEntity.ok("success");
            }
            patientNeedTransfer = patientsWaitToTransfer.get(0);
            for (Patient p : patientsWaitToTransfer){
                if (p.getTreatment_region_level().equals("quarantine")){
                    patientNeedTransfer = p;
                    break;
                }
            }
            // 2 转移
            // ToDo: 属于系统自动做的事情，发站内信
            patientService.appointHospitalNurse(Config.ROOT, patientNeedTransfer, region);
            patientService.appointBed(Config.ROOT, patientNeedTransfer, region);
        }

        return ResponseEntity.ok("success");
    }

    @PostMapping("/modifyDiseaseLevel")
    public ResponseEntity<?> modifyDiseaseLevel(@RequestBody ModifyDiseaseLevelRequest request){

        return null;
    }

}
