package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Doctor;
import project.backend.Repo.DoctorRepo;


@Service
public class DoctorService {
    private DoctorRepo doctorRepo;

    @Autowired
    public DoctorService(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public Doctor getDoctorById(String type, String id) {
        return doctorRepo.findById(type, id);
    }
}
