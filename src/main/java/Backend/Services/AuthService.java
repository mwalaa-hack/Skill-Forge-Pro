package Backend.Services;

import Backend.Database.UserDatabase;
import Backend.Models.Instructor;
import Backend.Models.Student;
import org.json.JSONObject;

public class AuthService {

    private UserDatabase userDB;

    public AuthService() {
        userDB = new UserDatabase("users.json");
    }

    public Student loginStudent(String email, String enteredPassword) {
        Student student = userDB.getStudentByEmail(email);
        if (student != null && student.verifyPassword(enteredPassword)) {
            return student;
        }
        return null;
    }

    public Instructor loginInstructor(String email, String enteredPassword) {
        Instructor instructor = userDB.getInstructorByEmail(email);
        if (instructor != null && instructor.verifyPassword(enteredPassword)) {
            return instructor;
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
        }
        return false;
    }

    public UserDatabase getUserDatabase() {
        return userDB;
    }
}