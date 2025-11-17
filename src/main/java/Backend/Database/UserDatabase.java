/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;

/**
 *
 * @author pola-nasser13
 */

import Backend.Models.Student;
import Backend.Models.Instructor;
import Backend.Models.User;
import org.json.JSONObject;
import java.util.ArrayList;

import Backend.Models.User;

public class UserDatabase extends Database<User> {

    public UserDatabase(String filename) {
        super(filename);
    }

    @Override
    public User createRecordFrom(JSONObject j) {
        String role = j.getString("role");
        if ("student".equalsIgnoreCase(role)) {
            return new Student(j);
        } else if ("instructor".equalsIgnoreCase(role)) {
            return new Instructor(j);
        }
        return null;
    }

    @Override
    public boolean insertRecord(JSONObject j) {
        try {
            int userId = j.getInt("userId");
            String email = j.getString("email");
            
            for (int i = 0; i < records.size(); i++) {
                User user = records.get(i);
                if (user.getUserId() == userId || user.getEmail().equalsIgnoreCase(email)) {
                    return false;
                }
            }
            
            User newUser = createRecordFrom(j);
            if (newUser != null) {
                records.add(newUser);
                saveToFile();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Student getStudentById(int studentId) {
        for (int i = 0; i < records.size(); i++) {
            User user = records.get(i);
            if (user.getUserId() == studentId && user instanceof Student) {
                return (Student) user;
            }
        }
        return null;
    }

    public Instructor getInstructorById(int instructorId) {
        for (int i = 0; i < records.size(); i++) {
            User user = records.get(i);
            if (user.getUserId() == instructorId && user instanceof Instructor) {
                return (Instructor) user;
            }
        }
        return null;
    }

    public Student getStudentByEmail(String email) {
        for (int i = 0; i < records.size(); i++) {
            User user = records.get(i);
            if (user.getEmail().equalsIgnoreCase(email) && user instanceof Student) {
                return (Student) user;
            }
        }
        return null;
    }

    public Instructor getInstructorByEmail(String email) {
        for (int i = 0; i < records.size(); i++) {
            User user = records.get(i);
            if (user.getEmail().equalsIgnoreCase(email) && user instanceof Instructor) {
                return (Instructor) user;
            }
        }
        return null;
    }

    public boolean updateUser(User updatedUser) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getUserId() == updatedUser.getUserId()) {
                records.set(i, updatedUser);
                saveToFile();
                return true;
            }
        }
        return false;
    }
}