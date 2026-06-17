package com.doyatama.university.payload;

public class UserRequest {

    private String idUser;
    private String name;
    private String username;
    private String email;
    private String password;
    private String schoolId;
    private String roles;

    public UserRequest() {
    }

    public UserRequest(String idUSer, String name, String username, String email, String password, String schoolId,
            String roles) {
        this.idUser = idUSer;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.schoolId = schoolId;
        this.roles = roles;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "idUser='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='***HIDDEN***'" +
                ", schoolId='" + schoolId + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "idUser":
                this.idUser = value;
                break;
            case "name":
                this.name = value;
                break;
            case "username":
                this.username = value;
                break;
            case "email":
                this.email = value;
                break;
            case "password":
                this.password = value;
                break;
            case "schoolId":
                this.schoolId = value;
                break;
            case "roles":
                this.roles = value;
                break;
        }
    }
}
