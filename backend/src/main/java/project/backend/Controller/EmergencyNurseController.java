package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.backend.Controller.Request.GetAllPatientsInfoRequest;
import project.backend.Controller.Request.RegisterPatientInfoRequest;
import project.backend.Entity.Checklist;
import project.backend.Entity.Patient;
import project.backend.Entity.PatientInfo;
import project.backend.Service.ChecklistService;
import project.backend.Service.PatientService;
import project.backend.Utils.Config;

import java.util.LinkedList;
import java.util.List;

@Controller
public class EmergencyNurseController {
    private PatientService patientService;
    private ChecklistService checklistService;

    @Autowired
    public EmergencyNurseController(PatientService patientService, ChecklistService checklistService) {
        this.patientService = patientService;
        this.checklistService = checklistService;
    }

    @PostMapping("/registerPatientInfo")
    public ResponseEntity<?> registerPatientInfo(@RequestBody RegisterPatientInfoRequest request){
        if (!request.getEmergency_nurse_id().startsWith("E")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        // 1 病人的注册
        // 1.1 基本信息
        Patient patient = new Patient(request.getName(), request.getGender(), request.getAge(),
                request.getDisease_level());
        int patientId = patientService.addBasicInfo(Config.EMERGENCY_NURSE, patient);
        patient = patientService.getPatientById(Config.EMERGENCY_NURSE, patientId);
        // 1.2 查待转入的区域是否允许转入，允许则设置对应的护士和床位，并更新相关表
        boolean canTransfer = patientService.canTransfer(Config.EMERGENCY_NURSE, request.getDisease_level(), patient);

        // 2 加入检测单
        Checklist checklist = new Checklist(request.getTest_result(), request.getDate(), request.getDisease_level());
        checklist.setPatient_id(patientId);
        checklistService.addInitChecklist(Config.EMERGENCY_NURSE, checklist);

        return ResponseEntity.ok(canTransfer);
    }

    @PostMapping("/getAllPatientsInfo")
    public ResponseEntity<?> getAllPatientsInfo(@RequestBody GetAllPatientsInfoRequest request){
        if (!request.getId().startsWith("E")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        List<Patient> patients = patientService.getAllPatients(Config.EMERGENCY_NURSE);

        return ResponseEntity.ok(patients);
    }
}
