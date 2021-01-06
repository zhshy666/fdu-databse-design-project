package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.HospitalNurse;
import project.backend.Repo.HospitalNurseRepo;

import java.util.LinkedList;
import java.util.List;

@Service
public class HospitalNurseService {
    private HospitalNurseRepo hospitalNurseRepo;

    @Autowired
    public HospitalNurseService(HospitalNurseRepo hospitalNurseRepo) {
        this.hospitalNurseRepo = hospitalNurseRepo;
    }

    public List<HospitalNurse> getHospitalNursesByRegions(String type, List<String> levels) {
        List<HospitalNurse> hospitalNurses = new LinkedList<>();
        for (String level: levels){
            hospitalNurseRepo.findHospitalNursesByRegion(type, level, hospitalNurses);
        }
        return hospitalNurses;
    }
}
