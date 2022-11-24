package cscb07.group4.androidproject;

public class Student {
    private String idToken;
    private String email;
    private String pwd;
    private String status;
    private String courses_taken;

    public String getCourses_taken() { return courses_taken; }

    public void setCourses_taken(String  courses_taken) {
        this.courses_taken = courses_taken;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student(){}

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
}
