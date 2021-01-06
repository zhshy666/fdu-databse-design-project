package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Checklist;
import project.backend.Repo.ChecklistRepo;

import java.util.List;

@Service
public class ChecklistService {
    private ChecklistRepo checklistRepo;

    @Autowired
    public ChecklistService(ChecklistRepo checklistRepo) {
        this.checklistRepo = checklistRepo;
    }

    public List<Checklist> getChecklists(String type, int patientId) {
        return checklistRepo.findByPatientId(type, patientId);
    }

    public String getTestResultById(String type, int checklistId) {
        return checklistRepo.findResultByChecklistId(type, checklistId);
    }
}
