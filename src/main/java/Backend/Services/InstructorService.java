/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Database.InstructorDatabase;
import Backend.Models.Course;
import Backend.Models.Instructor;
import Backend.Models.Lesson;
import Backend.Models.Student;
import java.util.ArrayList;
import org.json.JSONObject;
/**
 *
 * @author pola-nasser13
 */
public class InstructorService {

    private CourseDatabase courses;
    private Instructor instructor;
    private InstructorDatabase instructors;
    
    public InstructorService(Instructor instructor) {
        courses = new CourseDatabase("courses.json");
        courses.readFromFile();
        instructors.readFromFile();
        this.instructor = instructor;
    }

   public boolean createCourse(int courseId, String title, String description) {
        Course newCourse = new Course(courseId, title, description, instructor.getUserId());
        JSONObject jsonCourse = newCourse.toJSON();
      boolean addStatus = courses.insertRecord(jsonCourse);
      if(addStatus){
          System.out.println("Added Course successfully!");
      }
        return addStatus;
    }
   public boolean editCourse(Course c){
        boolean updateStatus = courses.updateCourse(c);
        if(updateStatus){
            System.out.println("Course updated successfully!");
            return true;
        }
        System.out.println("Failed to edit! Course not found.");
        return false;
    }
    
   public boolean deleteCourse(Course c){
    boolean deleteStatus = courses.deleteCourse(c.getCourseId());
    if(deleteStatus){
        System.out.println("Deleted Course successfully!");
    }
    return deleteStatus;
    }
    
   public boolean addLesson(Course c, int lessonId, String title, String content, ArrayList<String> optionalResources) {
        Lesson newLesson = new Lesson(lessonId, title, content);
      boolean addStatus = courses.addLesson(c.getCourseId(), newLesson);
      if(addStatus){
          System.out.println("Added Lesson successfully!");
      }
        return addStatus;
    }
   public boolean deleteLesson(Course c, int lessonId, String title, String content){
        
    boolean deleteStatus = courses.deleteLesson(c.getCourseId(), lessonId);
    if(deleteStatus){
        System.out.println("Deleted Course successfully!");
    }
    return deleteStatus;
    }
    
   public boolean editLesson(Course c, Lesson l){
       boolean updateStatus = courses.updateLesson(c.getCourseId(), l);
        if(updateStatus){
            System.out.println("Lesson updated successfully!");
            return true;
        }
        System.out.println("Failed to edit! Course not found.");
        return false;
   }
   
   public ArrayList<Integer> getEnrolledStudentsIds(Course c){
        return c.getStudentIds();
    }
   
   public ArrayList<Lesson> getLessons(Course c){
        return c.getLessons();
    }
    
   public ArrayList<Integer> getCreatedCoursesIds(){
       return instructor.getCreatedCourseIds();
   }
   
   private void save(){
        courses.saveToFile();
        instructors.saveToFile();
    }   
   
    public void logout(){
        save();
}
}
