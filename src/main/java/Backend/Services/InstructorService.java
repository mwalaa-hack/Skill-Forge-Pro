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
    
    InstructorService(Instructor instructor) {
        courses = new CourseDatabase("courses.json");
        courses.readFromFile();
        instructors.readFromFile();
        this.instructor = instructor;
    }

   public boolean createCourse(String courseId, String title, String description) {
        Course newCourse = new Course(courseId, title, description, instructor.id, new ArrayList<Lesson>(), new ArrayList<Student>());
        JSONObject jsonCourse = newCourse.toJson();
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
   
   public ArrayList<Student> getEnrolledStudents(Course c){
        return c.getStudents();
    }
   
   public ArrayList<Lesson> getLessons(Course c){
        return c.getLessons();
    }
    
   public ArrayList<Course> getCourses(){
       return instructor.getCourses();
   }
   
   private void save(){
        courses.saveToFile();
        instructors.saveToFile();
    }

//   public boolean addResource(Lesson l, String resource){
//       boolean addStatus = l.addResource(resource);
//       if(addStatus){
//           System.out.println("Resource added successfully!");
//           return true;
//       }
//           System.out.println("Failed to add! Resource already added.");
//          return false;
//   }
//
//   public boolean removeResource(Lesson l, String resource){
//       boolean removeStatus = l.removeResource(resource);
//       if(removeStatus){
//           System.out.println("Resource removed successfully!");
//           return true;
//       }
//           System.out.println("Failed to remove! Resource wasn't added for lesson:" + l.getTitle() + ".");
//          return false;
//   }
   
    public void logout(){
        save();
}
}
