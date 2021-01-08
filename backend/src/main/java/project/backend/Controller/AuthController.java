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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class AuthController {
    private AuthService authService;
    private TreatmentRegionService treatmentRegionService;
    private PatientService patientService;
    private ChiefNurseService chiefNurseService;
    private HospitalNurseService hospitalNurseService;

    @Autowired
    public AuthController(AuthService authService, TreatmentRegionService treatmentRegionService,
                          PatientService patientService, ChiefNurseService chiefNurseService, HospitalNurseService hospitalNurseService) {
        this.authService = authService;
        this.treatmentRegionService = treatmentRegionService;
        this.patientService = patientService;
        this.chiefNurseService = chiefNurseService;
        this.hospitalNurseService = hospitalNurseService;
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
        List<Patient> patients = patientService.getPatientsByRegions(levels, type);
        // 查找符合指定条件的病人 ID 集合
        List<Integer> patientsCanBeDischarged = patientService.getPatientIdsWhoCanBeDischarged(type, patients);
        List<Integer> patientsNeedTransfer = patientService.getPatientIdsNeedTransfer(type, levels);

        for (Patient patient : patients){
            PatientInfo patientInfo = new PatientInfo(patient.getPatient_id(), patient.getName(), patient.getGender(),
                    patient.getAge(), patient.getDisease_level(), patient.getLife_status(), patient.getNurse_id(),
                    patient.getTreatment_region_level());
            // 2 表示出院或者死亡
            if (patient.getNurse_id() == null && !patient.getTreatment_region_level().equals("quarantine")){
                if (patient.getLife_status().equals("dead")){
                    patientInfo.setCan_be_discharged(3);
                }
                else {
                    patientInfo.setCan_be_discharged(2);
                }
            }
            else {
                if (patientsCanBeDischarged.contains(patient.getPatient_id())) {
                    patientInfo.setCan_be_discharged(1);
                }
                if (patientsNeedTransfer.contains(patient.getPatient_id())) {
                    patientInfo.setNeed_transfer(1);
                }
            }
            info.add(patientInfo);
        }

        return ResponseEntity.ok(info);
    }

    @PostMapping("/getNursesInfo")
    public ResponseEntity<?> getNursesInfo(@RequestBody GetNursesInfoRequest request){
        if (!(request.getId().startsWith("D") || request.getId().startsWith("C"))){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        List<NurseInfo> result = new LinkedList<>();
        String type = Config.DOCTOR;
        if (request.getId().startsWith("C")){
            type = Config.CHIEF_NURSE;
        }
        // 1 护士长信息
        List<ChiefNurse> chiefNurses = chiefNurseService.getChiefNurseByDoctorId(type, request.getId());
        for (ChiefNurse nurse: chiefNurses){
            NurseInfo nurseInfo = new NurseInfo(nurse.getId(), nurse.getName(), nurse.getAge(), nurse.getGender());
            nurseInfo.setType("chief_nurse");
            result.add(nurseInfo);
        }

        // 2 病房护士及各自负责的病人信息
        List<String> levels = treatmentRegionService.getTreatmentRegions(request.getId(), type);
        List<HospitalNurse> hospitalNurses = hospitalNurseService.getHospitalNursesByRegions(type, levels);
        for (HospitalNurse hospitalNurse: hospitalNurses){
            NurseInfo nurseInfo = new NurseInfo(hospitalNurse.getId(), hospitalNurse.getName(), hospitalNurse.getAge(),
                    hospitalNurse.getGender());
            nurseInfo.setType("hospital_nurse");
            nurseInfo.setPatients(new LinkedList<>());
            result.add(nurseInfo);
        }
        // 拿到该区域的所有病人信息
        List<Patient> patients = patientService.getPatientsByRegions(levels, type);
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

}
