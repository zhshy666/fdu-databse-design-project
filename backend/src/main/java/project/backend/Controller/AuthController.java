package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.backend.Controller.Request.*;
import project.backend.Entity.Patient;
import project.backend.Entity.PatientInfo;
import project.backend.Entity.Staff;
import project.backend.Service.*;
import project.backend.Utils.Config;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {
    private AuthService authService;
    private TreatmentRegionService treatmentRegionService;
    private PatientService patientService;

    @Autowired
    public AuthController(AuthService authService, TreatmentRegionService treatmentRegionService, PatientService patientService) {
        this.authService = authService;
        this.treatmentRegionService = treatmentRegionService;
        this.patientService = patientService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        String id = request.getId();
        String password = request.getPassword();
        Staff staff = authService.login(id, password);
        if (staff == null){
            return new ResponseEntity<>("Login failed", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(staff);
    }

    @PostMapping("/modifyPersonalInfo")
    public ResponseEntity<?> modifyPersonalInfo(@RequestBody ModifyPersonalInfoRequest request){
        String id = request.getId();
        String message = authService.updateUserInfo(id, request.getPassword(), request.getAge());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/getPatientsInfo")
    public ResponseEntity<?> getPatientsInfo(@RequestBody GetPatientsInfoRequest request){
        // 1 检查是否是医生身份
        if (!(request.getId().startsWith("D") || request.getId().startsWith("C"))){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        List<PatientInfo> info = new ArrayList<>();  // 返回值
        String type = Config.DOCTOR;
        if (request.getId().startsWith("C")){
            type = Config.CHIEF_NURSE;
        }
        String id = request.getId();

        // 2 找到该医生对应的治疗区域
        List<String> levels = treatmentRegionService.getTreatmentRegions(id, type);
        // 获取所有的 patients
        List<Patient> patients = patientService.getAllPatients(levels, type);
        // 查找符合指定条件的病人 ID 集合
        List<Integer> patientsCanBeDischarged = patientService.getPatientIdsWhoCanBeDischarged(type, patients);
        List<Integer> patientsNeedTransfer = patientService.getPatientIdsNeedTransfer(type, levels);

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
}
