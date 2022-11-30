package cscb07.group4.androidproject.manager;

import java.util.ArrayList;
import java.util.List;

public class StudentCourses {

    private  List<String> wantedCourses = new ArrayList<>();
    private  List<String> takenCourses = new ArrayList<>();

    public List<String> getTakenCourses() {
        return takenCourses;
    }

    public List<String> getWantedCourses() {
        return wantedCourses;
    }
}
