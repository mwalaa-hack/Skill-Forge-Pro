/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;

import Backend.Models.*;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author pola-nasser13
 */
public class StudentDatabase extends Database<Student> {

    public StudentDatabase(String filename) {
        super(filename);
    }

    @Override
    public Student createRecordFrom(JSONObject j) {
        return new Student(j);
    }

    @Override
    public boolean insertRecord(JSONObject j) {
        try {
            int userId = j.getInt("userId");
            String email = j.getString("email");
            for (int i = 0; i < records.size(); i++) {
                Student s = records.get(i);
                if (s.getUserId() == userId || s.getEmail().equalsIgnoreCase(email)) {
                    return false;
                }
            }
            Student newStudent = createRecordFrom(j);
            records.add(newStudent);
            saveToFile();
            return true;
        } catch (Exception e) {
            System.out.println("Failed to insert student: " + e.getMessage());
            return false;
        }
    }

    public Student getStudentById(int studentId) {
        for (int i = 0; i < records.size(); i++) {
            Student s = records.get(i);
            if (s.getUserId() == studentId) {
                if ("student".equalsIgnoreCase(s.getRole())) {
                    return s;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public Student getStudentByEmail(String email) {
        for (int i = 0; i < records.size(); i++) {
            Student s = records.get(i);
            if (s.getEmail().equalsIgnoreCase(email)) {
                if ("student".equalsIgnoreCase(s.getRole())) {
                    return s;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public boolean contains(int studentId) {
        return getStudentById(studentId) != null;
    }

    public boolean enrollCourse(int studentId, int courseId, CourseDatabase courseDB) {
        Student s = getStudentById(studentId);
        if (s == null) {
            return false;
        }
        Course c = courseDB.getCourseById(courseId);
        if (c == null) {
            return false;
        }
        boolean ok1 = s.enrollCourseById(courseId);
        boolean ok2 = c.enrollStudentById(studentId);
        if (ok1 && ok2) {
            saveToFile();
        }
        return ok1 && ok2;
    }

    public boolean dropCourse(int studentId, int courseId, CourseDatabase courseDB) {
        Student s = getStudentById(studentId);
        if (s == null) {
            return false;
        }
        Course c = courseDB.getCourseById(courseId);
        if (c == null) {
            return false;
        }
        boolean ok1 = s.dropCourseById(courseId);
        boolean ok2 = c.removeStudentById(studentId);
        if (ok1 && ok2) {
            saveToFile();
        }
        return ok1 && ok2;
    }

    public boolean markLessonCompleted(int studentId, int courseId, int lessonId, CourseDatabase courseDB) {
        Student s = getStudentById(studentId);
        if (s == null) {
            return false;
        }
        Course c = courseDB.getCourseById(courseId);
        if (c == null) {
            return false;
        }
        boolean lessonExists = false;
        ArrayList<Lesson> lessons = c.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == lessonId) {
                lessonExists = true;
                break;
            }
        }
        if (!lessonExists) {
            return false;
        }
        boolean ok = s.markLessonCompletedById(courseId, lessonId);
        if (ok) {
            saveToFile();
        }
        return ok;
    }
}
