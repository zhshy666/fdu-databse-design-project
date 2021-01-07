package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.PatientStatus;
import project.backend.Repo.PatientStatusRepo;

import java.util.List;

@Service
public class PatientStatusService {
    private PatientStatusRepo patientStatusRepo;

    @Autowired
    public PatientStatusService(PatientStatusRepo patientStatusRepo) {
        this.patientStatusRepo = patientStatusRepo;
    }

    public List<PatientStatus> getPatientStatuses(String type, int patientId) {
        return patientStatusRepo.findByPatientId(type, patientId);
    }

    public void addNewPatientStatus(String type, PatientStatus patientStatus) {
        patientStatusRepo.insertPatientStatus(type, patientStatus);
    }
}
