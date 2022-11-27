package cscb07.group4.androidproject;

import java.util.ArrayList;
import java.util.List;

public class CourseManger {

    private List<Courses> courses;

    private static final CourseManger INSTANCE = new CourseManger();

    public static CourseManger getInstance() {
        return INSTANCE;
    }

    public List<Courses> getCourses() {
        return courses;
    }
}
