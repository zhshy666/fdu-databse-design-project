package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.backend.Controller.Request.GetPatientsInfoRequest;
import project.backend.Controller.Request.RegisterPatientInfoRequest;
import project.backend.Entity.Checklist;
import project.backend.Entity.Message;
import project.backend.Entity.Patient;
import project.backend.Service.ChecklistService;
import project.backend.Service.MessageService;
import project.backend.Service.PatientService;
import project.backend.Service.TreatmentRegionService;
import project.backend.Utils.Config;
import project.backend.Utils.Util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EmergencyNurseController {
    private PatientService patientService;
    private ChecklistService checklistService;
    private MessageService messageService;
    private TreatmentRegionService treatmentRegionService;

    @Autowired
    public EmergencyNurseController(PatientService patientService, ChecklistService checklistService,
                                    MessageService messageService, TreatmentRegionService treatmentRegionService) {
        this.patientService = patientService;
        this.checklistService = checklistService;
        this.messageService = messageService;
        this.treatmentRegionService = treatmentRegionService;
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
        Timestamp timestamp = Util.transferDateFormat(request.getDate());
        Checklist checklist = new Checklist(request.getTest_result(), timestamp, request.getDisease_level());
        checklist.setPatient_id(patientId);
        checklistService.addInitChecklist(Config.EMERGENCY_NURSE, checklist);

        // 发站内信通知护士长
        if (canTransfer) {
            SimpleDateFormat f = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
            Date now = new Date();
            f.format(now);
            Timestamp time = new Timestamp(now.getTime());
            Message message = new Message();
            message.setStatus(0);
            String chiefNurseId = treatmentRegionService.getChiefNurseIdByRegion(Config.ROOT, request.getDisease_level());
            message.setReceiver_id(chiefNurseId);
            message.setContent("A new patient has been transferred to " + patient.getDisease_level() + ".");
            message.setTime(time);
            messageService.addNewMessage(Config.ROOT, message);
        }

        return ResponseEntity.ok(canTransfer);
    }

    @PostMapping("/getAllPatientsInfo")
    public ResponseEntity<?> getAllPatientsInfo(@RequestBody GetPatientsInfoRequest request){
        if (!request.getId().startsWith("E")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        List<Patient> patients = patientService.getAllPatients(Config.EMERGENCY_NURSE);

        return ResponseEntity.ok(patients);
    }
}
