package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ChiefNurse;
import project.backend.Repo.ChiefNurseRepo;

@Service
public class ChiefNurseService {
    private ChiefNurseRepo chiefNurseRepo;

    @Autowired
    public ChiefNurseService(ChiefNurseRepo chiefNurseRepo) {
        this.chiefNurseRepo = chiefNurseRepo;
    }

}
