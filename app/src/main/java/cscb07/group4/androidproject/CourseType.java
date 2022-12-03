package cscb07.group4.androidproject;

import android.widget.LinearLayout;

import java.util.List;

import cscb07.group4.androidproject.databinding.FragmentManageBinding;
import cscb07.group4.androidproject.manager.StudentCourseManager;

public enum CourseType{
    WANTED, TAKEN;
    public List<String> getCourses(){
        if (this==WANTED)
            return StudentCourseManager.getInstance().getWantedCourses();
        return StudentCourseManager.getInstance().getTakenCourses();
    }
    public void addCourses(String courseCode){
        if(this==TAKEN){
            StudentCourseManager.getInstance().addTakenCourse(courseCode);
            return;
        }
        StudentCourseManager.getInstance().addWantedCourse(courseCode);
    }
    public void deleteCourses(String courseCode){
        if(this==TAKEN){
            StudentCourseManager.getInstance().removeTakenCourse(courseCode);
            return;
        }
        StudentCourseManager.getInstance().removeWantedCourse(courseCode);
    }
    public LinearLayout getLayout(FragmentManageBinding binding){
        if (this==WANTED)
            return binding.wantedCourseLinearLayout;
        return binding.takenCourseLinearLayout;
    }
}