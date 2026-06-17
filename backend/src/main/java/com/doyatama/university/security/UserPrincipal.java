package com.doyatama.university.security;

import com.doyatama.university.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserPrincipal implements UserDetails {
    private String id;

    private String name;

    private String username;

    private String schoolId;

    private String schoolName;

    private String roles;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String id, String name, String username, String schoolId, String schoolName, String email,
            String password,
            String roles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.email = email;
        this.password = password;
        this.roles = roles;

        // Convert role ID to GrantedAuthority - CRITICAL FIX
        String roleName;
        switch (roles) {
            case "1":
                roleName = "ROLE_ADMINISTRATOR";
                break;
            case "2":
                roleName = "ROLE_OPERATOR";
                break;
            case "3":
                roleName = "ROLE_TEACHER";
                break;
            case "4":
                roleName = "ROLE_DUDI";
                break;
            case "5":
                roleName = "ROLE_STUDENT";
                break;
            default:
                roleName = "ROLE_STUDENT"; // Default role
        }

        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    public static UserPrincipal create(User user) {
        String schoolId = (user.getSchool() != null) ? user.getSchool().getIdSchool() : null;
        String schoolName = (user.getSchool() != null) ? user.getSchool().getNameSchool() : null;
        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getUsername(),
                schoolId,
                schoolName,
                user.getEmail(),
                user.getPassword(),
                user.getRoles().toString());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getRoles() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", schoolId='" + schoolId + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
