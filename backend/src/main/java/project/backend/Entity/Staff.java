package project.backend.Entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class Staff implements UserDetails {

    private String id;
    private String type;
    private String name;
    private String password;
    private String age;
    private String gender;
    private Integer current_resp_num;
    private Integer treatment_region_level;

    public Staff(){}

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCurrent_resp_num() {
        return current_resp_num;
    }

    public void setCurrent_resp_num(Integer current_resp_num) {
        this.current_resp_num = current_resp_num;
    }

    public Integer getTreatment_region_level() {
        return treatment_region_level;
    }

    public void setTreatment_region_level(Integer treatment_region_level) {
        this.treatment_region_level = treatment_region_level;
    }
}
