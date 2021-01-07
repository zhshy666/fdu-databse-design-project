package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Checklist;
import project.backend.Repo.ChecklistRepo;

import java.sql.Timestamp;
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

    public void addChecklist(String type, String doctorId, int patientId, Timestamp time) {
        checklistRepo.insertNewChecklist(type, doctorId, patientId, time);
    }

    public void addInitChecklist(String type, Checklist checklist) {
        checklistRepo.insertInitialChecklist(type, checklist);
    }

    public Checklist getChecklistById(String type, int checklistId) {
        return checklistRepo.findByChecklistId(type, checklistId);
    }

    public void recordChecklist(String type, Checklist checklist) {
        checklistRepo.updateChecklist(type, checklist);
    }
}
