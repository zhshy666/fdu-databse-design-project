package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Bed;
import project.backend.Entity.HospitalNurse;
import project.backend.Entity.TreatmentRegion;
import project.backend.Repo.BedRepo;
import project.backend.Repo.HospitalNurseRepo;
import project.backend.Repo.TreatmentRegionRepo;

import java.util.List;

@Service
public class TreatmentRegionService {
    private TreatmentRegionRepo treatmentRegionRepo;
    private HospitalNurseRepo hospitalNurseRepo;
    private BedRepo bedRepo;

    @Autowired
    public TreatmentRegionService(TreatmentRegionRepo treatmentRegionRepo, HospitalNurseRepo hospitalNurseRepo,
                                  BedRepo bedRepo) {
        this.treatmentRegionRepo = treatmentRegionRepo;
        this.hospitalNurseRepo = hospitalNurseRepo;
        this.bedRepo = bedRepo;
    }

    public List<String> getTreatmentRegions(String id, String type) {
        return treatmentRegionRepo.findLevelByDoctorId(id, type);
    }

    public TreatmentRegion getTreatmentRegionByLevel(String type, String diseaseLevel) {
        return treatmentRegionRepo.findByLevel(type, diseaseLevel);
    }

}
