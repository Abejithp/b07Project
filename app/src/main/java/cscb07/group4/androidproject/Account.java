package cscb07.group4.androidproject;

import java.util.ArrayList;
import java.util.List;

import cscb07.group4.androidproject.manager.AccountType;

public class Account {

    private String name = "First Last"; // TODO Add to registration.
    private String idToken;
    private String email;
    private String pwd;
    private AccountType type;
    private List<String> courses_taken = new ArrayList<>();
    private List<String> courses_wanted = new ArrayList<>();

    public Account() {}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

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

    public List<String> getCourses_wanted() {
        return courses_wanted;
    }

    public void setCourses_wanted(List<String> courses_wanted) {
        this.courses_wanted = courses_wanted;
    }
}
