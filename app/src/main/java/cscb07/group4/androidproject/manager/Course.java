package cscb07.group4.androidproject.manager;

import java.util.List;
import java.util.Objects;

public class Course {

    private String name;
    private String code;
    private List<String> prerequisites;
    private List<Session> sessions;

    // For Firebase
    public Course() {}

    public Course(String name, String code, List<String> prerequisites, List<Session> sessions){
        this.name = name;
        this.code = code;
        this.prerequisites = prerequisites;
        this.sessions = sessions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public int checkSession(Session session){
        if(session== Session.FALL){
            return 0;
        }
        if(session==Session.WINTER){
            return 1;
        }
        return 2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return code.equals(course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
