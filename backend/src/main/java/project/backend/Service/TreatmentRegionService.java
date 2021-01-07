package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.TreatmentRegion;
import project.backend.Repo.HospitalNurseRepo;
import project.backend.Repo.TreatmentRegionRepo;
import project.backend.Utils.Config;

import java.util.List;

@Service
public class TreatmentRegionService {
    private TreatmentRegionRepo treatmentRegionRepo;
    private HospitalNurseRepo hospitalNurseRepo;

    @Autowired
    public TreatmentRegionService(TreatmentRegionRepo treatmentRegionRepo, HospitalNurseRepo hospitalNurseRepo) {
        this.treatmentRegionRepo = treatmentRegionRepo;
        this.hospitalNurseRepo = hospitalNurseRepo;
    }

    public List<String> getTreatmentRegions(String id, String type) {
        if (type.equals(Config.CHIEF_NURSE)){
            return treatmentRegionRepo.findLevelByChiefNurseId(id, type);
        }
        return treatmentRegionRepo.findLevelByDoctorId(id, type);

    }

    public TreatmentRegion getTreatmentRegionByLevel(String type, String diseaseLevel) {
        return treatmentRegionRepo.findByLevel(type, diseaseLevel);
    }

    public TreatmentRegion getTreatmentRegionByChiefNurseId(String type, String chiefNurseId) {
        return treatmentRegionRepo.findByChiefNurseId(type, chiefNurseId);
    }

    public String getChiefNurseIdByRegion(String type, String level) {
        return treatmentRegionRepo.findChiefNurseIdByRegion(type, level);
    }
}
