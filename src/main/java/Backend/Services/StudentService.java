/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Database.StudentDatabase;
import Backend.Models.Course;
import Backend.Models.Lesson;
import Backend.Models.Student;
import java.util.ArrayList;

/**
 *
 * @author pola-nasser13
 */
public class StudentService {
    private CourseDatabase courses;
    private Student student;
    private StudentDatabase students;
    StudentService(Student student) {
        courses = new CourseDatabase("courses.json");
        courses.readFromFile();
        students.readFromFile();
        this.student = student;
    }
    
    public ArrayList<Course> getCourses(){
        return courses.getRecords();
    }
    
    public boolean enrollInCourse(Course c){
        boolean enrollStatus = courses.enrollStudent(c.getCourseId(), student);
        if(enrollStatus){
        System.out.println("Enrolled successfully!");
          return true;
    }
    return false;
    }
    
    public ArrayList<Lesson> getLessons(Course c){
        return c.getLessons();
    }
    
    public boolean markLessonAsCompleted(Course c, Lesson l){
        boolean markStatus = students.markLessonCompleted(student.getUserID(), c.getCourseId(), l.getLessonId());
        if(markStatus){
            System.out.println("Lesson marked successfully");
            return true;
        }
        System.out.println("Lesson could not be marked");
        return false;
    }
    
    private void save(){
        courses.saveToFile();
        students.saveToFile();
    }
    

    public void logout(){
        save();
}
}
