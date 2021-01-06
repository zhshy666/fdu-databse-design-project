package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Checklist;
import project.backend.Entity.Patient;
import project.backend.Repo.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class PatientService {
    private PatientRepo patientRepo;
    private PatientStatusRepo patientStatusRepo;
    private ChecklistRepo checklistRepo;
    private HospitalNurseRepo hospitalNurseRepo;
    private BedRepo bedRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo, PatientStatusRepo patientStatusRepo, ChecklistRepo checklistRepo,
                          HospitalNurseRepo hospitalNurseRepo, BedRepo bedRepo) {
        this.patientRepo = patientRepo;
        this.patientStatusRepo = patientStatusRepo;
        this.checklistRepo = checklistRepo;
        this.hospitalNurseRepo = hospitalNurseRepo;
        this.bedRepo = bedRepo;
    }

    public List<Patient> getAllPatients(List<String> levels, String type) {
        List<Patient> list = new LinkedList<>();
        for (String level : levels) {
            patientRepo.findPatientsByTreatmentRegionLevel(level, list, type);
        }
        return list;
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
            if (checkRes.size() < 2) continue;
            // 获取时间戳
            if (minusDate(checkRes.get(0), checkRes.get(1)) >= 24){
                if (checkRes.get(0).getTest_result().equals("negative") &&
                        checkRes.get(1).getTest_result().equals("negative")){
                    checkMatch.add(patient);
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
        return (int) ((a.getDate().getTime() - b.getDate().getTime()) / (1000 * 60 * 60));
    }

    public Patient getPatientById(String type, int patientId) {
        return patientRepo.findPatientById(type, patientId);
    }

    public boolean canDischarge(String type, int patientId) {
        List<Double> temperatureRes = patientStatusRepo.findTemperaturesByPatientId(type, patientId);
        boolean flag = true;
        for (Double d : temperatureRes){
            if (d >= 37.3){
                flag = false;
                break;
            }
        }
        if (!flag){
            return false;
        }

        List<Checklist> checkRes = checklistRepo.findByPatientId(type, patientId);
        if (checkRes.size() < 2) return false;
        // 获取时间戳
        if (minusDate(checkRes.get(0), checkRes.get(1)) >= 24){
            return checkRes.get(0).getTest_result().equals("negative") &&
                    checkRes.get(1).getTest_result().equals("negative");
        }
        return false;
    }

    public void updateLifeStatus(String type, int patientId, String newStatus) {
        patientRepo.updatePatientLifeStatusById(type, patientId, newStatus);
        if (!newStatus.equals("dead")){
            return;
        }
        Patient patient = patientRepo.findPatientById(type, patientId);
        // 1 hospital_nurse
        hospitalNurseRepo.decreaseRespPatientNum(type, patient.getNurse_id());
        // 2 patient - 更新 treatment_region_level 和 nurse_id
        patientRepo.updateTreatmentRegionLevelAndNurseIdById(type, patientId);
        // 3 bed
        bedRepo.updateBedToFreeByPatientId(type, patientId);
    }
}
