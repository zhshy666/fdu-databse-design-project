package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Checklist;
import project.backend.Repo.ChecklistRepo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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

    public int addChecklist(String type, String doctorId, int patientId, Timestamp time) {
        checklistRepo.insertNewChecklist(type, doctorId, patientId, time);
        return checklistRepo.findChecklistNum(type);
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

    public Checklist getNewestChecklist(String type, int patient_id) {
        List <Checklist> checklists = checklistRepo.findByPatientId(type, patient_id);
        if (checklists.isEmpty()) return null;
        int index = 0;
        for (Checklist checklist: checklists){
            if (checklist.getTest_result().equals("-"))
                index++;
            else
                break;
        }
        return checklists.get(index);
    }

    public Map<String, Integer> getEarliestChecklistId(String type, String nurseId) {
        return checklistRepo.findEarliestChecklistIdAndPatientIdByHospitalNurseId(type, nurseId);
    }
}
