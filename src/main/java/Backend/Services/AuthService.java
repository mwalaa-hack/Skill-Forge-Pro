package Backend.Services;

import Backend.Database.UserDatabase;
import Backend.Models.Instructor;
import Backend.Models.Student;
import Backend.Models.User;
import org.json.JSONObject;

public class AuthService {

    private UserDatabase userDB;

    public AuthService() {
        userDB = new UserDatabase("users.json");
    }

    public User login(String email, String enteredPassword, String role){
        User user = userDB.getUserByEmail(email, role);
        if (user != null && user.verifyPassword(enteredPassword)) {
            return user;
        }
        return null;
    }
    
    public boolean signup(int id, String role, String username, String email, String password) {
        if ("student".equalsIgnoreCase(role)) {
            Student student = new Student(id, username, email, password);
            return userDB.insertRecord(student.toJSON());
        } else if ("instructor".equalsIgnoreCase(role)) {
            Instructor instructor = new Instructor(id, username, email, password);
            return userDB.insertRecord(instructor.toJSON());
        } else if ("admin".equalsIgnoreCase(role)) {
            Admin admin = new Admin(id, username, email, password);
            return userDB.insertRecord(admin.toJSON());
        }
        return false;
    }
}