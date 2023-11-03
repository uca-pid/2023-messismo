package com.messismo.bar.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User(String username, String email, String password){
        this.username=username;
        this.email=email;
        this.password=password;
        this.role=Role.EMPLOYEE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
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

    public String getFunctionalUsername(){
        return this.username;
    }

    public void updatePassword(String encodedPassword) {
        this.password=encodedPassword;
    }

    public boolean isEmployee() {
        return (this.role == Role.EMPLOYEE);
    }

    public void upgradeToValidateEmployee() {
        this.role=Role.VALIDATEDEMPLOYEE;
    }

    public boolean isValidatedEmployee() {
        return (this.role==Role.VALIDATEDEMPLOYEE);
    }

    public void upgradeToManager() {
        this.role=Role.MANAGER;
    }
}