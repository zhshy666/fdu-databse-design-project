package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.backend.Controller.Request.*;
import project.backend.Entity.Staff;
import project.backend.Service.AuthService;

@RestController
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        String id = request.getId();
        String password = request.getPassword();
        System.out.println("input: id = " + id + ", password = " + password);
        Staff staff = authService.login(id, password);
        if (staff == null){
            return new ResponseEntity<>("Login failed", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(staff);
    }
}
