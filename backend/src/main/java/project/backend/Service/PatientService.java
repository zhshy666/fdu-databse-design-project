package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Patient;
import project.backend.Repo.PatientRepo;

import java.util.List;

@Service
public class PatientService {
    private PatientRepo patientRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public void getPatients(Integer level, List<Patient> list, String type) {
        patientRepo.findPatientsByTreatmentRegionLevel(level, list, type);
    }
}
