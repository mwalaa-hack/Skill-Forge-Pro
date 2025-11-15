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
                if (records.get(i).getCourseId() == courseId) {
                    return false;
                }
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

    public void deleteCourse(int courseId) {
        boolean deleted = false;

        for (int i = 0; i < records.size(); i++) {
            Course c = records.get(i);
            if (c.getId() == courseId) {
                records.remove(i);
                deleted = true;
                i--;
            }
        }

        if (deleted) {
            System.out.println("Course deleted: " + courseId);
            saveToFile();
        } else {
            System.out.println("No course found with ID: " + courseId);
        }
    }
    
        public Course getCourseById(int courseId) {
        for (int i = 0; i < records.size(); i++) {
            Course c = records.get(i);
            if (c.getId() == courseId) {
                return c;
            }
        }
        return null;
    }
        
    public boolean contains(int courseId) {
        return getCourseById(courseId) != null;
    }
}