package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.backend.Controller.Request.GetPatientsInfoRequest;
import project.backend.Entity.Patient;
import project.backend.Entity.PatientInfo;
import project.backend.Entity.PatientRelatedInfo;
import project.backend.Service.PatientService;
import project.backend.Utils.Config;

import java.util.LinkedList;
import java.util.List;

@Controller
public class HospitalNurseController {
    private PatientService patientService;

    @Autowired
    public HospitalNurseController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/getRelatedPatientsInfo")
    public ResponseEntity<?> getRelatedPatientsInfo(@RequestBody GetPatientsInfoRequest request){
        if (!request.getId().startsWith("H")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String hospitalNurseId = request.getId();
        List<PatientInfo> result = new LinkedList<>();

        List<Patient> patients = patientService.getPatientsByNurseId(Config.HOSPITAL_NURSE, hospitalNurseId);
        List<Integer> patientsCanBeDischarged = patientService.getPatientIdsWhoCanBeDischarged(Config.HOSPITAL_NURSE, patients);

        for (Patient patient: patients){
            PatientRelatedInfo info = new PatientRelatedInfo(patient.getPatient_id(), patient.getName(), patient.getGender(),
                    patient.getAge(), patient.getDisease_level(), patient.getLife_status(), patient.getNurse_id(), patient.getTreatment_region_level());
            if (patientsCanBeDischarged.contains(patient.getPatient_id())) {
                info.setCan_be_discharged(1);
            }
        }
        return ResponseEntity.ok(result);
    }
}
