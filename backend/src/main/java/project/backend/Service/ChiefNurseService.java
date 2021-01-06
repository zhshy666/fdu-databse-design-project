package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ChiefNurse;
import project.backend.Repo.ChiefNurseRepo;
import project.backend.Repo.TreatmentRegionRepo;

import java.util.List;

@Service
public class ChiefNurseService {
    private ChiefNurseRepo chiefNurseRepo;
    private TreatmentRegionRepo treatmentRegionRepo;

    @Autowired
    public ChiefNurseService(ChiefNurseRepo chiefNurseRepo, TreatmentRegionRepo treatmentRegionRepo) {
        this.chiefNurseRepo = chiefNurseRepo;
        this.treatmentRegionRepo = treatmentRegionRepo;
    }

    public List<ChiefNurse> getChiefNurseByDoctorId(String type, String id) {
        // 1 找治疗区域
        List<String> ids = treatmentRegionRepo.findChiefNurseIdsByDoctorId(type, id);
        return chiefNurseRepo.findByIds(type, ids);
    }
}
