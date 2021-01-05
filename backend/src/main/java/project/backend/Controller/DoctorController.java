package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.backend.Controller.Request.GetPatientsInfoRequest;
import project.backend.Entity.Patient;
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
        Map<String, List<Patient>> ans = new HashMap<>();  // 返回值
        String id = request.getId();
        String condition = request.getCondition();

        // 2 找到该医生对应的治疗区域
        List<String> levels = treatmentRegionService.getTreatmentRegions(id);
        // 获取所有的 patients
        List<Patient> patients = new LinkedList<>();
        for(String level : levels) {
            patientService.getAllPatients(level, patients, Config.DOCTOR, condition);
        }

        // 3 找到该区域满足出院条件的病人
        if (condition.equals("1")){
            List<Patient> canBeDischarged = new LinkedList<>();
            patientService.getPatientsWhoCanBeDischarged(Config.DOCTOR, canBeDischarged, patients);
            ans.put("1", canBeDischarged);
            List<Patient> cannotBeDischarged = new LinkedList<>();
            for (Patient patient : patients){
                if (canBeDischarged.contains(patient)){
                    continue;
                }
                cannotBeDischarged.add(patient);
            }
            ans.put("2", cannotBeDischarged);
        }
        else if (condition.equals("2")){
            List<Patient> needTransfer = new LinkedList<>();
            for (String level : levels) {
                patientService.getPatientsNeedTransfer(Config.DOCTOR, needTransfer, level);
            }
            ans.put("1", needTransfer);
            List<Patient> noNeedToTransfer = new LinkedList<>();
            for (Patient patient : patients){
                if (needTransfer.contains(patient)){
                    continue;
                }
                noNeedToTransfer.add(patient);
            }
            ans.put("2", noNeedToTransfer);
        }
        else {
            ans.put(condition, patients);
        }

        return ResponseEntity.ok(ans);
    }
}
