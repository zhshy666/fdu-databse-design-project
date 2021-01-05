package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Checklist;
import project.backend.Entity.Patient;
import project.backend.Entity.PatientInfo;
import project.backend.Repo.ChecklistRepo;
import project.backend.Repo.PatientRepo;
import project.backend.Repo.PatientStatusRepo;
import project.backend.Utils.Config;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public void getAllPatients(String level, List<Patient> list, String type, String condition) {
        patientRepo.findPatientsByTreatmentRegionLevelAndCondition(level, list, type, condition);
    }

    public List<Integer> getPatientIdsWhoCanBeDischarged(String type, List<Patient> allPatients) {
        List<Patient> temperatureMatch = new LinkedList<>();
        List<Patient> checkMatch = new LinkedList<>();
        List<Integer> patients = new LinkedList<>();
        for (Patient patient : allPatients){
            List<Double> temperatureRes = patientStatusRepo.findTemperaturesByPatientId(type, patient.getPatient_id());
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
                patients.add(patient.getPatient_id());
            }
        }
        return patients;
    }

    public List<Integer> getPatientIdsNeedTransfer(String type, List<String> levels) {
        List<Integer> patients = new ArrayList<>();
        for (String level : levels) {
            patientRepo.findPatientsNeedTransfer(type, patients, level);
        }
        return patients;
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
        long to = d1.getTime();
        long from = d2.getTime();
        return (int) ((to - from) / (1000 * 60 * 60));
    }

}
