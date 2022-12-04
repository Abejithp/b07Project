package cscb07.group4.androidproject.manager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
                    String courseID = courseData.getKey();
                    Course course = courseData.getValue(Course.class);
                    if (course != null && course.getName() != null) {
                        course.setId(courseID);
                        this.courses.put(courseID, course);
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
        if (course == null || courses.containsKey(course.getId())) {
            return;
        }

        this.courses.put(course.getId(), course);
        FirebaseDatabase.getInstance().getReference().child("courses")
                .child(course.getId()).setValue(course);
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

        removeCourse(course.getId());
    }

    /**
     * Remove an available course from both the local list and remote firebase.
     *
     * @param courseID The code of the course you want to remove.
     */
    public void removeCourse(String courseID) {
        if (courseID == null || !courses.containsKey(courseID)) {
            return;
        }

        this.courses.remove(courseID);
        FirebaseDatabase.getInstance().getReference().child("courses")
                .child(courseID).setValue(null);
    }

    public Course getCourseByID(String courseID) {
        return this.courses.get(courseID);
    }

    public Course getCourseByCourseCode(String courseCode) {
        for (Course course : this.courses.values()) {
            if (courseCode.equals(course.getCode())) {
                return course;
            }
        }
        return null;
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
