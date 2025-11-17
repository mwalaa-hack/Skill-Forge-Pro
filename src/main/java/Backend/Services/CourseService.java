/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Database.UserDatabase;
import Backend.Models.Course;
import Backend.Models.Instructor;
import Backend.Models.Lesson;
import Backend.Models.Student;

/**
 *
 * @author HP_Laptop
 */
public class CourseService {
    private Course course;
    private UserDatabase users;
    
    public CourseService(Course course) {
        users = new UserDatabase("users.json");
        this.course = course;
    }
    
    public String getInstructorName(){
        int instructorId = course.getInstructorId();
        Instructor i = users.getInstructorById(instructorId);
        if(i != null){
            return i.getUsername();
        }
        return "";
    }
    
    public Lesson getLessonById(int lessonId){
        return course.getLessonById(lessonId);
    }
    
    public Student getStudentById(int studentid){
        return users.getStudentById(studentid);
    }
}