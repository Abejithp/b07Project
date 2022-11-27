package cscb07.group4.androidproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Courses {
    private String courseName;
    private String courseCode;
    private Set<String> prerequisites;
    private Set<Session> sessions;

    public Courses(String courseName, String courseCode, Set<String> prerequisites, Set<Session> sessions){
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
        this.sessions = sessions;
    }





}
