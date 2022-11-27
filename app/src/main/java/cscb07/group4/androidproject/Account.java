package cscb07.group4.androidproject;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private String idToken;
    private String email;
    private String pwd;
    private AccountType type;
    private List<String> courses_taken = new ArrayList<>();

    public Account() {}

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public List<String> getCourses_taken() {
        return courses_taken;
    }

    public void setCourses_taken(List<String> courses_taken) {
        this.courses_taken = courses_taken;
    }
}
