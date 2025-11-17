/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import Backend.Models.*;

/**
 *
 * @author pola-nasser13
 */
public class CourseDatabase extends Database<Course> {

    public CourseDatabase(String filename) {
        super(filename);
    }

    @Override
    public Course createRecordFrom(JSONObject j) {
        return new Course(j);
    }

    @Override
    public boolean insertRecord(JSONObject j) {
        try {
            int courseId = j.getInt("courseId");
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i).getCourseId() == courseId) return false;
            }
            if (!j.has("students")) j.put("students", new JSONArray());
            if (!j.has("lessons")) j.put("lessons", new JSONArray());
            Course c = createRecordFrom(j);
            records.add(c);
            saveToFile();
            return true;
        } catch (Exception e) {
            System.out.println("Failed to insert course: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        boolean deleted = false;
        for (int i = 0; i < records.size(); i++) {
            Course c = records.get(i);
            if (c.getCourseId() == courseId) {
                records.remove(i);
                deleted = true;
                i--;
            }
        }
        if (deleted) saveToFile();
        return deleted;
    }

    public Course getCourseById(int courseId) {
        for (int i = 0; i < records.size(); i++) {
            Course c = records.get(i);
            if (c.getCourseId() == courseId) return c;
        }
        return null;
    }

    public boolean contains(int courseId) {
        return getCourseById(courseId) != null;
    }

    public boolean addLesson(int courseId, Lesson lesson) {
        Course c = getCourseById(courseId);
        if (c == null) return false;
        boolean added = c.addLesson(lesson);
        if (added) saveToFile();
        return added;
    }

    public boolean updateLesson(int courseId, Lesson lesson) {
        Course c = getCourseById(courseId);
        if (c == null) return false;
        ArrayList<Lesson> lessons = c.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == lesson.getLessonId()) {
                lessons.set(i, lesson);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public boolean deleteLesson(int courseId, int lessonId) {
        Course c = getCourseById(courseId);
        if (c == null) return false;
        ArrayList<Lesson> lessons = c.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == lessonId) {
                lessons.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public boolean enrollStudent(int courseId, int studentId) {
        Course c = getCourseById(courseId);
        if (c == null) return false;
        boolean enrolled = c.enrollStudentById(studentId);
        if (enrolled) saveToFile();
        return enrolled;
    }

    public boolean removeStudent(int courseId, int studentId) {
        Course c = getCourseById(courseId);
        if (c == null) return false;
        boolean removed = c.removeStudentById(studentId);
        if (removed) saveToFile();
        return removed;
    }

    public boolean updateCourse(Course course) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getCourseId() == course.getCourseId()) {
                records.set(i, course);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Course> getAllCourses() {
        return records;
    }
}