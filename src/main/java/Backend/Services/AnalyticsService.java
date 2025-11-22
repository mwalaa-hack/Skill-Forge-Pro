/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Models.Course;
import Backend.Models.Lesson;
import Backend.Models.Student;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author HP_Laptop
 */
public class AnalyticsService {
    private Course course;
    
    public AnalyticsService(Course course){
        this.course = course;
    }
    
    public ArrayList<HashMap<String, Integer>> getStudentsProgress(){
    ArrayList<HashMap<String, Integer>> totalProgress = new ArrayList<HashMap<String,Integer>>();
    ArrayList<Integer> enrolledStudentsIds = course.getStudentIds();
    int numberOfEnrolledStudents = course.getStudentIds().size();
    CourseService courseService = new CourseService(course);
    for(int i = 0; i < numberOfEnrolledStudents; i++){
        HashMap<String, Integer> studentProgress = new HashMap<String, Integer>();
        Student s = courseService.getStudentById(i);
        String name = s.getUsername();
        int progress = calculateCourseProgress(s);
        studentProgress.put(name, progress);
        totalProgress.add(studentProgress);
    }
    return totalProgress;
    }
    private int calculateCourseProgress(Student s){
        ArrayList<Integer> completedLessons = s.getCompletedLessonsByCourseId(course.getCourseId());
        ArrayList<Lesson> totalLessons = course.getLessons();
        int progress = (completedLessons.size()/totalLessons.size())*100;
        return progress;
    }
    
    
    
} 
    
