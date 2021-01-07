package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.*;
import project.backend.Repo.*;
import project.backend.Utils.Config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class PatientService {
    private TreatmentRegionRepo treatmentRegionRepo;
    private PatientRepo patientRepo;
    private PatientStatusRepo patientStatusRepo;
    private ChecklistRepo checklistRepo;
    private HospitalNurseRepo hospitalNurseRepo;
    private BedRepo bedRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo, PatientStatusRepo patientStatusRepo, ChecklistRepo checklistRepo,
                          HospitalNurseRepo hospitalNurseRepo, BedRepo bedRepo, TreatmentRegionRepo treatmentRegionRepo) {
        this.patientRepo = patientRepo;
        this.patientStatusRepo = patientStatusRepo;
        this.checklistRepo = checklistRepo;
        this.hospitalNurseRepo = hospitalNurseRepo;
        this.bedRepo = bedRepo;
        this.treatmentRegionRepo = treatmentRegionRepo;
    }

    public List<Patient> getPatientsByRegions(List<String> levels, String type) {
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
            if (!patient.getTreatment_region_level().equals("light")){
                continue;
            }
            if (!patient.getDisease_level().equals("light")){
                continue;
            }
            List<Double> temperatureRes = patientStatusRepo.findTemperaturesByPatientId(type, patient.getPatient_id());
            if (temperatureRes.size() < 3) continue;
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
        hospitalNurseRepo.updateRespPatientNum(type, patient.getNurse_id(), -1);
        // 2 patient - 更新 treatment_region_level 和 nurse_id 为 null
        patientRepo.updateTreatmentRegionLevelAndNurseIdById(type, null, null, patientId);
        // 3 bed
        bedRepo.updateBedToFreeByPatientId(type, patientId);
    }

    public List<Patient> getPatientsByDiseaseLevel(String type, String diseaseLevel) {
        return patientRepo.findPatientByDiseaseLevel(type, diseaseLevel);
    }

    public void appointHospitalNurse(String type, Patient patientNeedTransfer, TreatmentRegion region) {
        // 1 找该区域一个有空的病房护士，增加其 resp_patient_num
        int num = region.getNurse_resp_num();
        HospitalNurse hospitalNurse = hospitalNurseRepo.findHospitalNurseByRegionAndRespNum(type, region.getLevel(), num);
        hospitalNurseRepo.updateRespPatientNum(type, hospitalNurse.getId(), 1);
        // 2 更新 patient 表
        patientRepo.updateTreatmentRegionLevelAndNurseIdById(type, region.getLevel(), hospitalNurse.getId(), patientNeedTransfer.getPatient_id());
    }

    public void appointBed(String type, Patient patientNeedTransfer, TreatmentRegion region) {
        // 1 找该区域一个空闲的床位
        int bedId = bedRepo.findFreeBedByRegion(type, region.getLevel());
        // 2 更新床位信息
        bedRepo.updateBedByBedIdAndPatientId(type, bedId, patientNeedTransfer.getPatient_id());
    }

    public void updateDiseaseLevel(String type, int patientId, String newDiseaseLevel) {
        patientRepo.updatePatientDiseaseLevelById(type, patientId, newDiseaseLevel);
    }

    // 判断能不能转入 level 区域
    public boolean canTransfer(String type, String level, Patient patient) {
        int patientId = patient.getPatient_id();
        TreatmentRegion treatmentRegion = treatmentRegionRepo.findByLevel(type, level);
        // 1 护士有空闲
        HospitalNurse hospitalNurse = hospitalNurseRepo.findHospitalNurseByRegionAndRespNum(type, level, treatmentRegion.getNurse_resp_num());
        if (hospitalNurse == null) return false;
        // 2 病床有空闲
        int bedId = bedRepo.findFreeBedByRegion(type, level);
        if (bedId == -1) return false;
        // 3 转移
        // 原治疗区域
        if (!patient.getTreatment_region_level().equals("quarantine")) {
            hospitalNurseRepo.updateRespPatientNum(type, patient.getNurse_id(), -1);
            bedRepo.updateBedToFreeByPatientId(type, patientId);
        }
        // 新治疗区域
        hospitalNurseRepo.updateRespPatientNum(type, hospitalNurse.getId(), 1);
        patientRepo.updateTreatmentRegionLevelAndNurseIdById(type, level, hospitalNurse.getId(), patientId);
        bedRepo.updateBedByBedIdAndPatientId(type, bedId, patientId);
        return true;
    }

    public void discharge(String type, Patient patient) {
        // 1 hospital_nurse
        hospitalNurseRepo.updateRespPatientNum(type, patient.getNurse_id(), -1);
        // 2 patient - 更新 treatment_region_level 和 nurse_id 为 null
        patientRepo.updateTreatmentRegionLevelAndNurseIdById(type, null, null, patient.getPatient_id());
        // 3 bed
        bedRepo.updateBedToFreeByPatientId(type, patient.getPatient_id());
    }

    public int addBasicInfo(String type, Patient patient) {
        return patientRepo.insertBasicInfo(type, patient);
    }

    public List<Patient> getAllPatients(String type) {
        return patientRepo.findPatients(type);
    }

    public List<Patient> getPatientsByNurseId(String type, String hospitalNurseId) {
        return patientRepo.findPatientsByNurseId(type, hospitalNurseId);
    }

    public String getHospitalNurseIdByPatientId(String type, int patientId) {
        return patientRepo.findNurseIdByPatientId(type, patientId);
    }
}
