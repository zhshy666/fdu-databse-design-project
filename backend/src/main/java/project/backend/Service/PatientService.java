package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Checklist;
import project.backend.Entity.Patient;
import project.backend.Repo.ChecklistRepo;
import project.backend.Repo.PatientRepo;
import project.backend.Repo.PatientStatusRepo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
public class PatientService {
    private PatientRepo patientRepo;
    private PatientStatusRepo patientStatusRepo;
    private ChecklistRepo checklistRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo, PatientStatusRepo patientStatusRepo, ChecklistRepo checklistRepo) {
        this.patientRepo = patientRepo;
        this.patientStatusRepo = patientStatusRepo;
        this.checklistRepo = checklistRepo;
    }

    public void getAllPatients(Integer level, List<Patient> list, String type, String condition) {
        patientRepo.findPatientsByTreatmentRegionLevelAndCondition(level, list, type, condition);
    }

    public void getPatientsWhoCanBeDischarged(String type, List<Patient> list, List<Patient> allPatients) {
        List<Patient> temperatureMatch = new LinkedList<>();
        List<Patient> checkMatch = new LinkedList<>();
        for (Patient patient : allPatients){
            List<Double> temperatureRes = patientStatusRepo.findTemperaturesByPatientId(type, patient.getPatient_id());
            if (temperatureRes.size() == 3){
                boolean flag = true;
                for (Double d : temperatureRes){
                    if (d >= 37.3){
                        flag = false;
                        break;
                    }
                }
                if (flag){
                    temperatureMatch.add(patient);
                }
            }

            List<Checklist> checkRes = checklistRepo.findByPatientId(type, patient.getPatient_id());
            if (checkRes.size() == 2){
                // 获取时间戳
                if (minusDate(checkRes.get(0), checkRes.get(1)) >= 24){
                    if (checkRes.get(0).getTest_result().equals("negative") &&
                            checkRes.get(1).getTest_result().equals("negative")){
                        checkMatch.add(patient);
                    }
                }
            }
        }

        for (Patient patient : allPatients){
            if (temperatureMatch.contains(patient) && checkMatch.contains(patient)){
                list.add(patient);
            }
        }

    }

    public void getPatientsNeedTransfer(String type, List<Patient> needTransfer, Integer level) {
        patientRepo.findPatientsNeedTransfer(type, needTransfer, level);
    }

    private int minusDate(Checklist a, Checklist b) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date d1 = null;
        java.util.Date d2 = null;
        try {
            d1 = sdf.parse(a.getDate());
            d2 = sdf.parse(b.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert d1 != null;
        assert d2 != null;
        long from2 = d1.getTime();
        long to2 = d2.getTime();
        return (int) ((to2 - from2) / (1000 * 60 * 60));
    }
}
