package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Patient;
import project.backend.Repo.DoctorRepo;

import java.util.List;
import java.util.Set;

@Service
public class DoctorService {
    private DoctorRepo doctorRepo;

    @Autowired
    public DoctorService(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }
}
