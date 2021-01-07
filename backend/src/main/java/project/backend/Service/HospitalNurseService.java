package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.HospitalNurse;
import project.backend.Entity.TreatmentRegion;
import project.backend.Repo.HospitalNurseRepo;
import project.backend.Repo.PatientRepo;

import java.util.LinkedList;
import java.util.List;

@Service
public class HospitalNurseService {
    private HospitalNurseRepo hospitalNurseRepo;
    private PatientRepo patientRepo;

    @Autowired
    public HospitalNurseService(HospitalNurseRepo hospitalNurseRepo, PatientRepo patientRepo) {
        this.hospitalNurseRepo = hospitalNurseRepo;
        this.patientRepo = patientRepo;
    }

    public List<HospitalNurse> getHospitalNursesByRegions(String type, List<String> levels) {
        List<HospitalNurse> hospitalNurses = new LinkedList<>();
        for (String level: levels){
            hospitalNurseRepo.findHospitalNursesByRegion(type, level, hospitalNurses);
        }
        return hospitalNurses;
    }

    public void deleteHospitalNurse(String type, String nurseId) {
        hospitalNurseRepo.deleteHospitalNurseById(type, nurseId);
    }

    public boolean addHospitalNurse(String type, String nurseId, String level) {
        // 1 查找该病房护士是否存在且不属于任何治疗区域
        HospitalNurse hospitalNurse = hospitalNurseRepo.findFreeHospitalNurseByNurseId(type, nurseId);
        if (hospitalNurse == null){
            return false;
        }
        // 2 改该病房护士对应的治疗区域
        hospitalNurseRepo.updateTreatmentRegionLevel(type, hospitalNurse.getId(), level);
        return true;
    }

}
