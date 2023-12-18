package com.example.payment.dto;

import com.example.payment.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
public class CustomUser implements UserDetails {
    private long id;
    private String email;
    private String phoneNo;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private Role role;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    public static long getId(Authentication authentication) {
        CustomUser iUserDetails = (CustomUser) authentication.getPrincipal();
        return iUserDetails.getId();
    }

    public CustomUser toUser() {
        return new CustomUser(this.id, this.email, null, role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public CustomUser() {

    }

    public CustomUser(Long id, String password, String email, Role role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

}
