/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;
import Backend.Database.InstructorDatabase;
import Backend.Database.StudentDatabase;
import Backend.Models.Instructor;
import Backend.Models.Student;
import Backend.Models.User;
import org.json.JSONObject;
/**
 *
 * @author pola-nasser13
 */
public class AuthService {
    private StudentDatabase students;
    private InstructorDatabase instructors;
    AuthService(){
        students = new StudentDatabase("users.json");
        instructors = new InstructorDatabase("users.json");
        students.readFromFile();
        instructors.readFromFile();
    }
    
    public User login(String email, String enteredPassword, String role){
        User user = null;
        if(role.equals("Student")){
        user = students.getStudentByEmail(email);
        }
         else if(role.equals("Instructor")){
            user = instructors.getInstructorByEmail(email);
        }
        if(user == null){
            System.out.println("No User found");
            return null;
        }
        if(!user.verifyPassword(enteredPassword)){
            System.out.println("Wrong Password!");
            return null;
        }
       return user; 
    }
    
   public boolean signup(String userId, String role, String username, String password, String id){
         User user = null;
         boolean insertStatus = false;
        if(role.equals("Student")){
             user = new Student(userId, role, username, password,id);
             JSONObject JSONUser = user.toJSON();
             insertStatus = students.insertRecord(JSONUser);
            
        }
        else if(role.equals("Instructor")){
            user = new Instructor(userId, role, username, password,id);
            JSONObject JSONUser = user.toJSON();
            insertStatus = instructors.insertRecord(JSONUser);
        }
        return insertStatus;
    }
}
