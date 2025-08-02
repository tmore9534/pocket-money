package authservice.services;

import authservice.entities.UserInfo;
import authservice.entities.UserRole;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import java.util.Collection;

// This component/service is used to defined custom user abstraction and it helps during authentication.
public class CustomUserDetails extends UserInfo implements UserDetails {
    private String username;
    private String password;

    // Bounded wildcard: can't add directly because Java doesn't know the exact
    // subtype. So we build a specific List<GrantedAuthority> first and assign it

    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUserInfo) {
        this.username = byUserInfo.getUsername();
        this.password = byUserInfo.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for (UserRole role : byUserInfo.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }

        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

}
