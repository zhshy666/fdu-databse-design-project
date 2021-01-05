package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.backend.Controller.Request.GetPatientsInfoRequest;
import project.backend.Entity.Patient;
import project.backend.Entity.PatientInfo;
import project.backend.Service.DoctorService;
import project.backend.Service.PatientService;
import project.backend.Service.TreatmentRegionService;
import project.backend.Utils.Config;

import java.util.*;

@RestController
public class DoctorController {
    private DoctorService doctorService;
    private TreatmentRegionService treatmentRegionService;
    private PatientService patientService;

    @Autowired
    public DoctorController(DoctorService doctorService, TreatmentRegionService treatmentRegionService, PatientService patientService){
        this.doctorService = doctorService;
        this.treatmentRegionService = treatmentRegionService;
        this.patientService = patientService;
    }

    @PostMapping("/getPatientsInfo")
    public ResponseEntity<?> getPatientsInfo(@RequestBody GetPatientsInfoRequest request){
        // 1 检查是否是医生身份
        if (!request.getId().startsWith("D")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        List<PatientInfo> info = new ArrayList<>();  // 返回值
        String id = request.getId();
        String condition = request.getCondition();

        // 2 找到该医生对应的治疗区域
        List<String> levels = treatmentRegionService.getTreatmentRegions(id);
        // 获取所有的 patients
        List<Patient> patients = new LinkedList<>();
        for(String level : levels) {
            patientService.getAllPatients(level, patients, Config.DOCTOR, condition);
        }

        List<Integer> patientsCanBeDischarged = patientService.getPatientIdsWhoCanBeDischarged(Config.DOCTOR, patients);
        List<Integer> patientsNeedTransfer = patientService.getPatientIdsNeedTransfer(Config.DOCTOR, levels);

        for (Patient patient : patients){
            PatientInfo patientInfo = new PatientInfo(patient.getPatient_id(), patient.getName(), patient.getGender(),
                    patient.getAge(), patient.getDisease_level(), patient.getLife_status(), patient.getNurse_id(),
                    patient.getTreatment_region_level());
            if (patientsCanBeDischarged.contains(patient.getPatient_id())){
                patientInfo.setCanBeDischarged(1);
            }
            if (patientsNeedTransfer.contains(patient.getPatient_id())){
                patientInfo.setNeedTransfer(1);
            }
            info.add(patientInfo);
        }

        return ResponseEntity.ok(info);
    }
}
