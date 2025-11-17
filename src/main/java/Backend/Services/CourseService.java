/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Database.InstructorDatabase;
import Backend.Database.StudentDatabase;
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
    private StudentDatabase students;
    private InstructorDatabase instructors;
    public CourseService(Course course) {
        students = new StudentDatabase("users.json");
        students.readFromFile();
        instructors = new InstructorDatabase("users.json");
        students.readFromFile();
        instructors.readFromFile();
        this.course = course;
    }
    public String getInstructorName(){
        int instructorId = course.getInstructorId();
        Instructor i = instructors.getInstructorById(instructorId);
        if(i!=null){
            System.out.println("Fetched instructor name successfully!");
            return i.getUsername();
        }
        System.out.println("Couldn't fetch an instructor with that id!");
        return "";
    }
    
    public Lesson getLessonById(int lessonId){
        Lesson l = course.getLessonById(lessonId);
        if(l == null){
            System.out.println("Couldn't find lesson!");
    }
        else{
            System.out.println("Successfully fetched lesson " + l.getTitle());
        }
        return l;
    }
    
    public Student getStudentById(int studentid){
        Student s = students.getStudentById(studentid);
        if(s == null){
            System.out.println("Couldn't find student!");
        }
        else{
           System.out.println("Successfully fetched student " + s.getUsername()); 
        }
        return s;
    }
}
