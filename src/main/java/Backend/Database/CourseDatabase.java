/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author pola-nasser13
 */
public class CourseDatabase extends Database {

    public CourseDatabase(String filename) {
        super(filename);
    }

    public boolean insertRecord(JSONObject j) {
        try {
            int newId = j.getInt("courseId");
            for (int i = 0; i < records.length(); i++) {
                JSONObject existing = records.getJSONObject(i);
                if (existing.getInt("courseId") == newId) {
                    return false;
                }
            }

            if (!j.has("lessons")) j.put("lessons", new JSONArray());
            if (!j.has("students")) j.put("students", new JSONArray());

            records.put(j);
            saveToFile();
            return true;

        } catch (Exception e) {
            System.out.println("Failed to insert course: " + e.getMessage());
            return false;
        }
    }

    public JSONObject getCourseById(int courseId) {
        for (int i = 0; i < records.length(); i++) {
            JSONObject j = records.getJSONObject(i);
            if (j.getInt("courseId") == courseId) return j;
        }
        return null;
    }

    public boolean contains(int courseId) {
        return getCourseById(courseId) != null;
    }

    public ArrayList<JSONObject> getAllCourses() {
        ArrayList<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < records.length(); i++) {
            list.add(records.getJSONObject(i));
        }
        return list;
    }

    public boolean updateCourse(JSONObject courseObj) {
        try {
            int courseId = courseObj.getInt("courseId");

            for (int i = 0; i < records.length(); i++) {
                JSONObject existing = records.getJSONObject(i);
                if (existing.getInt("courseId") == courseId) {
                    records.put(i, courseObj);
                    saveToFile();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Failed to update course: " + e.getMessage());
            return false;
        }
    }

    public void deleteCourse(int courseId) {
        JSONArray newRecords = new JSONArray();
        for (int i = 0; i < records.length(); i++) {
            JSONObject j = records.getJSONObject(i);
            if (j.getInt("courseId") != courseId) {
                newRecords.put(j);
            }
        }
        records = newRecords;
        saveToFile();
    }


    public void enrollStudent(int courseId, int studentId) {
        JSONObject course = getCourseById(courseId);
        if (course == null) return;

        JSONArray students = course.optJSONArray("students");
        if (students == null) students = new JSONArray();

        boolean exists = false;
        for (int i = 0; i < students.length(); i++) {
            if (students.getInt(i) == studentId) {
                exists = true;
                break;
            }
        }

        if (!exists) students.put(studentId);
        course.put("students", students);
        updateCourse(course);
    }

    public void addLesson(int courseId, JSONObject lessonObj) {
        JSONObject course = getCourseById(courseId);
        if (course == null) return;

        JSONArray lessons = course.optJSONArray("lessons");
        if (lessons == null) lessons = new JSONArray();

        lessons.put(lessonObj);
        course.put("lessons", lessons);

        updateCourse(course);
    }


    public void removeLesson(int courseId, int lessonId) {
        JSONObject course = getCourseById(courseId);
        if (course == null) return;

        JSONArray lessons = course.optJSONArray("lessons");
        if (lessons == null) return;

        JSONArray newLessons = new JSONArray();
        for (int i = 0; i < lessons.length(); i++) {
            JSONObject l = lessons.getJSONObject(i);
            if (l.getInt("lessonId") != lessonId) {
                newLessons.put(l);
            }
        }
        course.put("lessons", newLessons);
        updateCourse(course);
    }
}

