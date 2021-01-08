package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.backend.Controller.Request.GetChecklistToDoRequest;
import project.backend.Controller.Request.GetPatientsInfoRequest;
import project.backend.Controller.Request.RecordChecklistRequest;
import project.backend.Controller.Request.RecordPatientStatusRequest;
import project.backend.Entity.*;
import project.backend.Service.*;
import project.backend.Utils.Config;
import project.backend.Utils.Util;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HospitalNurseController {
    private PatientService patientService;
    private ChecklistService checklistService;
    private PatientStatusService patientStatusService;
    private MessageService messageService;
    private TreatmentRegionService treatmentRegionService;

    @Autowired
    public HospitalNurseController(PatientService patientService, ChecklistService checklistService,
                                   PatientStatusService patientStatusService, MessageService messageService,
                                   TreatmentRegionService treatmentRegionService) {
        this.patientService = patientService;
        this.checklistService = checklistService;
        this.patientStatusService = patientStatusService;
        this.messageService = messageService;
        this.treatmentRegionService = treatmentRegionService;
    }

    @PostMapping("/getRelatedPatientsInfo")
    public ResponseEntity<?> getRelatedPatientsInfo(@RequestBody GetPatientsInfoRequest request){
        if (!request.getId().startsWith("H")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String hospitalNurseId = request.getId();
        List<PatientRelatedInfo> result = new LinkedList<>();

        List<Patient> patients = patientService.getPatientsByNurseId(Config.HOSPITAL_NURSE, hospitalNurseId);
        List<Integer> patientsCanBeDischarged = patientService.getPatientIdsWhoCanBeDischarged(Config.HOSPITAL_NURSE, patients);

        for (Patient patient: patients){
            PatientRelatedInfo info = new PatientRelatedInfo(patient.getPatient_id(), patient.getName(), patient.getGender(),
                    patient.getAge(), patient.getDisease_level(), patient.getLife_status(), patient.getNurse_id(), patient.getTreatment_region_level());
            if (patientsCanBeDischarged.contains(patient.getPatient_id())) {
                info.setCan_be_discharged(1);
            }
            result.add(info);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/recordChecklist")
    public ResponseEntity<?> recordChecklist(@RequestBody RecordChecklistRequest request){
        if (!request.getHospital_nurse_id().startsWith("H")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        int checklistId = request.getChecklist_id();
        Checklist checklist = checklistService.getChecklistById(Config.HOSPITAL_NURSE, checklistId);
        checklist.setDisease_level(request.getDisease_level());
        checklist.setTest_result(request.getTest_result());
        Timestamp timestamp = Util.transferDateFormat(request.getDate());
        checklist.setDate(timestamp);

        checklistService.recordChecklist(Config.HOSPITAL_NURSE, checklist);

        // 检查病人是否可以出院
        this.getCanBeDischargedAndSendMessage(checklist.getPatient_id());

        return ResponseEntity.ok("success");
    }

    @PostMapping("/recordPatientStatus")
    public ResponseEntity<?> recordPatientStatus(@RequestBody RecordPatientStatusRequest request){
        if (!request.getHospital_nurse_id().startsWith("H")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        Patient patient = patientService.getPatientById(Config.HOSPITAL_NURSE, request.getId());
        if (patient == null){
            return new ResponseEntity<>("No such patient", HttpStatus.BAD_REQUEST);
        }
        PatientStatus patientStatus = new PatientStatus();
        // 1 基本信息
        patientStatus.setPatient_id(request.getId());
        patientStatus.setLife_status(request.getLife_status());
        patientStatus.setNurse_id(request.getHospital_nurse_id());
        patientStatus.setTemperature(request.getTemperature());
        patientStatus.setSymptom(request.getSymptom());

        Timestamp timestamp = Util.transferDateFormat(request.getDate());
        patientStatus.setDate(timestamp);

        // 2 找最近一次的核酸检测单
        Checklist checklist = checklistService.getNewestChecklist(Config.HOSPITAL_NURSE, patientStatus.getPatient_id());
        assert checklist != null;
        patientStatus.setChecklist_id(checklist.getId());

        // 3 写进对应的表中
        patientStatusService.addNewPatientStatus(Config.HOSPITAL_NURSE, patientStatus);

        // 检查病人是否可以出院
        this.getCanBeDischargedAndSendMessage(request.getId());

        return ResponseEntity.ok("success");
    }

    @PostMapping("/getChecklistToDo")
    public ResponseEntity<?> getChecklistToDo(@RequestBody GetChecklistToDoRequest request){
        if (!request.getId().startsWith("H")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String nurseId = request.getId();
        Map<String, Integer> result = checklistService.getEarliestChecklistId(Config.HOSPITAL_NURSE, nurseId);
        return ResponseEntity.ok(result);
    }

    private void getCanBeDischargedAndSendMessage(int patientId) {
        SimpleDateFormat f = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date now = new Date();
        f.format(now);
        Timestamp time = new Timestamp(now.getTime());
        Patient patient = patientService.getPatientById(Config.ROOT, patientId);
        boolean canDischarge = false;
        if (patient.getTreatment_region_level().equals("light") && patient.getDisease_level().equals("light")){
            canDischarge = patientService.canDischarge(Config.ROOT, patient.getPatient_id());
        }
        if (canDischarge){
            Message message = new Message();
            message.setStatus(0);
            String doctorId = treatmentRegionService.getDoctorIdByRegion(Config.ROOT, patient.getTreatment_region_level());
            message.setReceiver_id(doctorId);
            message.setContent("Patient " + patient.getPatient_id() + " can be discharged now.");
            message.setTime(time);
            messageService.addNewMessage(Config.ROOT, message);
        }
    }
}
