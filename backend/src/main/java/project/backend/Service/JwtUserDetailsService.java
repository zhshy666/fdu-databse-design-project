package project.backend.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.backend.Entity.Staff;
import project.backend.Repo.StaffRepo;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private StaffRepo staffRepo;

    @Autowired
    public JwtUserDetailsService(StaffRepo staffRepo) {
        this.staffRepo = staffRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Staff staff = staffRepo.findUserByUsername(username);
        return staff;
    }
}
