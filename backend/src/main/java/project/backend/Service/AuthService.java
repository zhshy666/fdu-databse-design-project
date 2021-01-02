package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.*;
import project.backend.Repo.ChiefNurseRepo;
import project.backend.Repo.DoctorRepo;
import project.backend.Repo.EmergencyNurseRepo;
import project.backend.Repo.HospitalNurseRepo;

@Service
public class AuthService {
    private DoctorRepo doctorRepo;
    private ChiefNurseRepo chiefNurseRepo;
    private EmergencyNurseRepo emergencyNurseRepo;
    private HospitalNurseRepo hospitalNurseRepo;

    @Autowired
    public AuthService(DoctorRepo doctorRepo, ChiefNurseRepo chiefNurseRepo,
                       EmergencyNurseRepo emergencyNurseRepo, HospitalNurseRepo hospitalNurseRepo){
        this.doctorRepo = doctorRepo;
        this.chiefNurseRepo = chiefNurseRepo;
        this.emergencyNurseRepo = emergencyNurseRepo;
        this.hospitalNurseRepo = hospitalNurseRepo;
    }

    public Staff login(String id, String psw){
        Doctor doctor = doctorRepo.findUserByIdAndPassword(id, psw);
        if (doctor != null){
            return new Staff(doctor.getId(), "doctor", doctor.getName());
        }

        ChiefNurse chiefNurse = chiefNurseRepo.findUserByIdAndPassword(id, psw);
        if (chiefNurse != null){
            return new Staff(chiefNurse.getId(), "chief_nurse", chiefNurse.getName());
        }

        EmergencyNurse emergencyNurse = emergencyNurseRepo.findUserByIdAndPassword(id, psw);
        if (emergencyNurse != null){
            return new Staff(emergencyNurse.getId(), "emergency_nurse", emergencyNurse.getName());
        }

        HospitalNurse hospitalNurse = hospitalNurseRepo.findUserByIdAndPassword(id, psw);
        if (hospitalNurse != null){
            return new Staff(hospitalNurse.getId(), "hospital_nurse", hospitalNurse.getName());
        }
        return null;
    }

    public String updateUserInfo(String id, String name, String password, int age) {
        char type = id.charAt(0);
        switch (type){
            case 'D':
                doctorRepo.updateDoctorById(id, name, password, age);
                break;
            case 'C':
                chiefNurseRepo.updateNurseById(id, name, password, age);
                break;
            case 'E':
                emergencyNurseRepo.updateNurseById(id, name, password, age);
                break;
            case 'H':
                hospitalNurseRepo.updateNurseById(id, name, password, age);
                break;
            default:
                break;
        }
        return "success";
    }

}
