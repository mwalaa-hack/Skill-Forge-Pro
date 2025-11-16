package Backend.Models;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mohamed Walaa
 */
public class Student extends User {
    
    private ArrayList<Course> enrolledCourses = new ArrayList<>(); // store Course objects
    private HashMap<Course, ArrayList<Integer>> progress = new HashMap<>(); // map course,completed lesson IDs

    public Student(int id, String username, String email, String passwordHash){
        super(id, username, email, passwordHash, "student");
    }

    // Enroll in a course
    public boolean enrollCourse(Course course) {

        if(enrolledCourses.contains(course)) {
            System.out.println("Already enrolled in course " + course.getCourseId());
            return false;   // already enrolled
        }

        enrolledCourses.add(course);
        progress.put(course, new ArrayList<>());

        course.enroll(this.getUserId());

        System.out.println("Student " + this.getUserId() + " successfully enrolled in Course " + course.getCourseId());
        return true;
    }

    // Mark a lesson completed
    public boolean markLessonCompleted(Course course, int lessonId){

        if (!enrolledCourses.contains(course)) {
            System.out.println("Student is not enrolled in this course!");
            return false;  // student not enrolled
        }

        ArrayList<Integer> completedLessons = progress.get(course);
        if (completedLessons == null) {
            completedLessons = new ArrayList<>();
            progress.put(course, completedLessons);
        }

        if (completedLessons.contains(lessonId)) {
            System.out.println("Lesson already completed.");
            return false;   // lesson already done
        }

        completedLessons.add(lessonId);
        System.out.println("Marked lesson " + lessonId + " completed for course " + course.getCourseId());
        return true;
    }

    //get completed lessons
    public ArrayList<Integer> getCompletedLessons(Course course) {
        return progress.getOrDefault(course, new ArrayList<>());
    }

    //get enrolled courses
    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    // for GUI
    @Override
    public void openDashboard(JFrame parentFrame) {
    }
}
