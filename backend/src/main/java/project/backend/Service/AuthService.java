package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.Staff;
import project.backend.Repo.StaffRepo;

@Service
public class AuthService {
    private StaffRepo staffRepo;

    @Autowired
    public AuthService(StaffRepo repo){
        this.staffRepo = repo;
    }

    public Staff login(String name, String psw){
        Staff staff = staffRepo.findUserByUsernameAndPassword(name, psw);
        if (staff == null){
            return null;
        }
        return staffRepo.findUserByUsernameAndPassword(name, psw);
    }
}
