package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.TreatmentRegion;
import project.backend.Repo.TreatmentRegionRepo;

import java.util.LinkedList;
import java.util.List;

@Service
public class TreatmentRegionService {
    private TreatmentRegionRepo treatmentRegionRepo;

    @Autowired
    public TreatmentRegionService(TreatmentRegionRepo treatmentRegionRepo) {
        this.treatmentRegionRepo = treatmentRegionRepo;
    }

    public List<String> getTreatmentRegions(String id) {
        List<String> list;
        list = treatmentRegionRepo.findLevelByDoctorId(id);
        return list;
    }
}
