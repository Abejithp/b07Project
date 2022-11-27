package cscb07.group4.androidproject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CourseManger {

    private static final CourseManger INSTANCE = new CourseManger();

    private Set<Course> courses = new HashSet<>();

    public void refreshCourses() {
        this.refreshCourses(null);
    }

    public void refreshCourses(Runnable onComplete) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot result = task.getResult();
                for (DataSnapshot courseData : result.getChildren()) {
                    String courseCode = courseData.getKey();
                    Course course = courseData.getValue(Course.class);
                    if (course != null && !courseCode.equals(course.getCode())) {
                        // Repair course code if it doesn't match object
                        course.setCode(courseCode);
                        addCourse(course);
                    } else {
                        this.courses.add(course);
                    }
                    System.out.println("Course Added: "+ course.getCode() + "|" + course.getName()
                            + "|" + course.getPrerequisites() + "|" + course.getSessions());
                }
            }
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public void addCourse(Course course) {
        if (course == null || courses.contains(course)) {
            return;
        }

        this.courses.add(course);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child("courses").child(course.getCode()).setValue(course);
    }

    public void removeCourse(Course course) {
        if (course == null || !courses.contains(course)) {
            return;
        }

        this.courses.remove(course);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child("courses").child(course.getCode()).setValue(null);
    }

    public Set<Course> getCourses() {
        return Collections.unmodifiableSet(courses);
    }

    public static CourseManger getInstance() {
        return INSTANCE;
    }
}
