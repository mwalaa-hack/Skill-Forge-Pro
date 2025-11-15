/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Models.Course;
import Backend.Models.Instructor;
import Backend.Models.Lesson;
import Backend.Models.Student;
import java.util.ArrayList;

/**
 *
 * @author pola-nasser13
 */
public class InstructorService {

    private CourseDatabase courses;
    private Instructor instructor;

    InstructorService(Instructor instructor) {
        courses = new CourseDatabase("courses.json");
        courses.readFromFile();
        this.instructor = instructor;
    }

   public boolean createCourse(String courseId, String title, String description) {
        Course newCourse = new Course(courseId, title, description, instructor.id, new ArrayList<Lesson>(), new ArrayList<Student>());
      boolean addStatus = instructor.addCourse(newCourse);
      if(addStatus){
          System.out.println("Added Course successfully!");
          courses.saveToFile();
      }
        return addStatus;
    }
   public boolean editCourse(Course c){
        
    }
    
   public boolean deleteCourse(Course c){
    boolean deleteStatus = instructor.deleteCourse(c);
    if(deleteStatus){
        System.out.println("Deleted Course successfully!");
          courses.saveToFile();
    }
    return deleteStatus;
    }
    
   public boolean addLesson(Course c, String lessonId, String title, String content, ArrayList<String> optionalResources) {
        Lesson newLesson = new Lesson(lessonId, title, content, optionalResources);
      boolean addStatus = c.addLesson(newLesson);
      if(addStatus){
          System.out.println("Added Lesson successfully!");
          courses.saveToFile();
      }
        return addStatus;
    }
   public boolean deleteLesson(Course c, String lessonId, String title, String content, ArrayList<String> optionalResources){
        Lesson l = new Lesson(lessonId, title, content, optionalResources);
    boolean deleteStatus = c.removeLesson(l);
    if(deleteStatus){
        System.out.println("Deleted Course successfully!");
          courses.saveToFile();
    }
    return deleteStatus;
    }
    
   public ArrayList<Student> getEnrolledStudents(Course c){
        return c.getStudents();
    }
}
