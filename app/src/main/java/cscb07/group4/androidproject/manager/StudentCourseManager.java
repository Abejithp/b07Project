package cscb07.group4.androidproject.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

public final class StudentCourseManager {

    private final static StudentCourseManager INSTANCE = new StudentCourseManager();

    static StudentCourses courses = new StudentCourses();

    public static StudentCourseManager getInstance() {
        return INSTANCE;
    }

    /**
     * Query the database to update the lists of the current user's courses.
     * Use {@link #getWantedCourses()} and {@link #getTakenCourses()} once the query is complete.
     */
    public void refreshCourses() {
        refreshCourses(null);
    }

    /**
     * Query the database to update the lists of the current user's courses.
     * Use {@link #getWantedCourses()} and {@link #getTakenCourses()} once the query is complete.
     *
     * @param onComplete A task to run once the query is complete.
     */
    public void refreshCourses(Runnable onComplete) {
        if (!AccountManager.getInstance().isLoggedIn()) {
            return;
        }
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child("studentCourses").child(AccountManager.getInstance().getAccount().getIdToken()).
                get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                StudentCourses courses = task.getResult().getValue(StudentCourses.class);
                if (courses == null) {
                    courses = new StudentCourses();
                }
                StudentCourseManager.courses = courses;
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    /**
     * Add a course the user is planning to take to both the local list and remote firebase.
     *
     * @param courseCode The code of the course you want to add.
     */
    public void addWantedCourse(String courseCode) {
        addCourse(courses.getWantedCourses(), courseCode);
    }

    /**
     * Add a course the user has taken in the past to both the local list and remote firebase.
     *
     * @param courseCode The code of the course you want to add.
     */
    public void addTakenCourse(String courseCode) {
        addCourse(courses.getTakenCourses(), courseCode);
    }

    private void addCourse(List<String> courses, String courseCode) {
        if (!AccountManager.getInstance().isLoggedIn() || courses.contains(courseCode)) {
            return;
        }
        StudentCourseManager.courses.getTakenCourses().add(courseCode);
        FirebaseDatabase.getInstance().getReference().child("studentCourses")
                .child(AccountManager.getInstance().getAccount().getIdToken())
                .setValue(StudentCourseManager.courses);
    }

    /**
     * Remove a course the user is planning to take from both the local list and remote firebase.
     *
     * @param courseCode The code of the course you want to remove.
     */
    public void removeWantedCourse(String courseCode) {
        removeCourse(courses.getWantedCourses(), courseCode);
    }

    /**
     * Remove a course the user has taken in the past from both the local list and remote firebase.
     *
     * @param courseCode The code of the course you want to remove.
     */
    public void removeTakenCourse(String courseCode) {
        removeCourse(courses.getTakenCourses(), courseCode);
    }

    private void removeCourse(List<String> courses, String courseCode) {
        if (!AccountManager.getInstance().isLoggedIn() || !courses.contains(courseCode)) {
            return;
        }
        StudentCourseManager.courses.getTakenCourses().remove(courseCode);
        FirebaseDatabase.getInstance().getReference().child("studentCourses")
                .child(AccountManager.getInstance().getAccount().getIdToken())
                .setValue(StudentCourseManager.courses);
    }

    /**
     * Get the list of course codes that the student is planning to take.
     * This list cannot be modified. See {@link #addWantedCourse(String)} 
     * and {@link #removeWantedCourse(String)} instead.
     */
    public List<String> getWantedCourses() {
        return Collections.unmodifiableList(courses.getWantedCourses());
    }

    /**
     * Get the list of course codes that the student has taken in the past.
     * This list cannot be modified. See {@link #addTakenCourse(String)} 
     * and {@link #removeTakenCourse(String)} (String)} instead.
     */
    public List<String> getTakenCourses() {
        return Collections.unmodifiableList(courses.getTakenCourses());
    }
}
