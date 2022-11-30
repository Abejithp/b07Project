package cscb07.group4.androidproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public final class StudentCourseManager {
    private final static StudentCourseManager INSTANCE = new StudentCourseManager();
    static StudentCourses courses = new StudentCourses();



    public static StudentCourseManager getInstance() {
        return INSTANCE;
    }


    public void refreshCourses(Runnable onComplete) {
        if(!AccountManager.getInstance().isLoggedIn()){
            return;
        }
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child("studentCourses").child(AccountManager.getInstance().getAccount().getIdToken()).
                get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                this.courses = task.getResult().getValue(StudentCourses.class);
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });



    }

    public List<String> getWantedCourses(){
        if(courses==null){
            return null;
        }
        return courses.getWantedCourses();
    }

    public List<String> getTakenCourses(){
        if(courses==null){
            return null;
        }
        return courses.getTakenCourses();
    }



}
