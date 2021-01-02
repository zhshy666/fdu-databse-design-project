package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.backend.Controller.Request.GetPatientsInfoRequest;
import project.backend.Entity.Patient;
import project.backend.Entity.TreatmentRegion;
import project.backend.Service.DoctorService;
import project.backend.Service.PatientService;
import project.backend.Service.TreatmentRegionService;
import project.backend.Utils.Config;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        String id = request.getId();
        // 2 找到该医生对应的治疗区域
        List<Integer> levels = treatmentRegionService.getTreatmentRegions(id);
        List<Patient> list = new LinkedList<>();
        for(Integer level : levels) {
            patientService.getPatients(level, list, Config.DOCTOR);
        }
        return ResponseEntity.ok(list);
    }
}
