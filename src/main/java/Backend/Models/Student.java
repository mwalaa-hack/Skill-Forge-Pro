/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Models;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mohamed Walaa
 */





public class Student extends User {
    
    
    private ArrayList<Integer> enrolledCourseIds = new ArrayList<>(); //id list

    private HashMap<Integer, ArrayList<Integer>> progress = new HashMap<>(); //map

    public Student(int id, String username, String email, String passwordHash){
        super(id, username, email, passwordHash, "student");
    }


    public boolean enrollCourse(Course course) {

        int courseId = course.getCourseId();

        if(enrolledCourseIds.contains(courseId)) {
            System.out.println("Already enrolled in course " + courseId);
            return false;   //already enrolled
        }

        enrolledCourseIds.add(courseId);

        progress.put(courseId, new ArrayList<>());

        course.enroll(this.getUserId());

        System.out.println("Student " + this.getUserId() + " successfully enrolled in Course " + courseId);
        return true;
    }


    public boolean markLessonCompleted(int courseId, int lessonId){

        if (!enrolledCourseIds.contains(courseId)) {
            System.out.println("Student is not enrolled in this course!");
            return false;  //student not enrolled
        }

        ArrayList<Integer> completedLessons = progress.get(courseId);

        if (completedLessons == null) {
            completedLessons = new ArrayList<>();
            progress.put(courseId, completedLessons);
        }

        
        if (completedLessons.contains(lessonId)) {
            System.out.println("Lesson already completed.");
            return false;   //lesson already done
        }

        //mark lesson complete
        completedLessons.add(lessonId);

        System.out.println("Marked lesson " + lessonId + " completed for course " + courseId);
        return true;
    }

    
    
    
    

    public ArrayList<Integer> getCompletedLessons(int courseId) {
        return progress.getOrDefault(courseId, new ArrayList<>());
    }

    public ArrayList<Integer> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }


    //for gui
    @Override
    public void openDashboard(JFrame parentFrame) {
    }
    
}
