package cscb07.group4.androidproject.manager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CourseManger {

    private static final CourseManger INSTANCE = new CourseManger();

    private Map<String, Course> courses = new HashMap<>();

    public static CourseManger getInstance() {
        return INSTANCE;
    }

    /**
     * Query the database to update the list of available courses.
     * Use {@link #getCourses()} once the query is complete.
     */
    public void refreshCourses() {
        this.refreshCourses(null);
    }

    /**
     * Query the database to update the list of available courses.
     * Use {@link #getCourses()} once the query is complete.
     *
     * @param onComplete A task to run once the query is complete.
     */
    public void refreshCourses(Runnable onComplete) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot courseData : task.getResult().getChildren()) {
                    String courseCode = courseData.getKey();
                    Course course = courseData.getValue(Course.class);
                    // Repair course code if it doesn't match the inside object
                    if (course != null && !courseCode.equals(course.getCode())) {
                        course.setCode(courseCode);
                        addCourse(course);
                    } else {
                        this.courses.put(courseCode, course);
                    }
                }
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    /**
     * Add an available course to both the local list and remote firebase.
     *
     * @param course The course you want to add.
     */
    public void addCourse(Course course) {
        if (course == null || courses.containsKey(course.getCode())) {
            return;
        }

        this.courses.put(course.getCode(), course);
        FirebaseDatabase.getInstance().getReference().child("courses")
                .child(course.getCode()).setValue(course);
    }

    /**
     * Remove an available course from both the local list and remote firebase.
     *
     * @param course The course you want to remove.
     */
    public void removeCourse(Course course) {
        if (course == null) {
            return;
        }

        removeCourse(course.getCode());
    }

    /**
     * Remove an available course from both the local list and remote firebase.
     *
     * @param courseCode The code of the course you want to remove.
     */
    public void removeCourse(String courseCode) {
        if (courseCode == null || !courses.containsKey(courseCode)) {
            return;
        }

        this.courses.remove(courseCode);
        FirebaseDatabase.getInstance().getReference().child("courses")
                .child(courseCode).setValue(null);
    }

    public Course getCourseByID(String courseCode) {
        return this.courses.get(courseCode);
    }

    /**
     * Get the list of course codes that the student is planning to take.
     * This list cannot be modified. See {@link #addCourse(Course)}
     * and {@link #removeCourse(Course)} instead.
     */
    public Collection<Course> getCourses() {
        return Collections.unmodifiableCollection(courses.values());
    }
}
