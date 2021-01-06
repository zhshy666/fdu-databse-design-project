package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Bed;
import project.backend.Repo.BedRepo;

import java.util.List;

@Service
public class BedService {
    private BedRepo bedRepo;

    @Autowired
    public BedService(BedRepo bedRepo) {
        this.bedRepo = bedRepo;
    }

    public boolean hasEmptyBed(String type, String level) {
        int bedId = bedRepo.findFreeBedByRegion(type, level);
        return bedId != -1;
    }
    public List<Bed> getBedsByRegionLevel(String type, String level) {
        return bedRepo.findBedsByLevel(type, level);
    }
}
