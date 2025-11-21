/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Backend.Models;

import Backend.Database.Info;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Pc
 */
public class Course implements Info {

    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private ArrayList<Lesson> lessons;
    private ArrayList<Integer> studentIds;
    private ApprovalStatus approvalStatus;

    public Course(int courseId, String title, String description, int instructorId) {
        setCourseId(courseId);
        setTitle(title);
        setDescription(description);
        setInstructorId(instructorId);
        this.lessons = new ArrayList<>();
        this.studentIds = new ArrayList<>();
        setApprovalStatus(ApprovalStatus.PENDING);
    }

    public Course(JSONObject json) {
        this.courseId = json.getInt("courseId");
        this.title = json.getString("title");
        this.description = json.getString("description");
        this.instructorId = json.getInt("instructorId");
        this.approvalStatus= ApprovalStatus.valueOf(json.optString("approvalStatus","PENDING"));
        this.lessons = new ArrayList<>();
        JSONArray lessonArr = json.optJSONArray("lessons");
        if (lessonArr != null) {
            for (int i = 0; i < lessonArr.length(); i++) {
                lessons.add(new Lesson(lessonArr.getJSONObject(i)));
            }
        }
        this.studentIds = new ArrayList<>();
        JSONArray studentArr = json.optJSONArray("students");
        if (studentArr != null) {
            for (int i = 0; i < studentArr.length(); i++) {
                studentIds.add(studentArr.getInt(i));
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourseId(int courseId) {
        if (courseId <= 0) {
            throw new IllegalArgumentException("courseId must be > 0");
        }
        this.courseId = courseId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstructorId(int instructorId) {
        if (instructorId <= 0) {
            throw new IllegalArgumentException("instructorId must be > 0");
        }
        this.instructorId = instructorId;
    }
    public void setApprovalStatus(ApprovalStatus status){
    this.approvalStatus=status;
    }

    public boolean addLesson(Lesson lesson) {
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == lesson.getLessonId()) {
                return false;
            }
        }
        lessons.add(lesson);
        return true;
    }

    public boolean removeLessonById(int lessonId) {
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == lessonId) {
                lessons.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public Lesson getLessonById(int lessonId) {
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == lessonId) {
                return lessons.get(i);
                
            }
        }
        return null;
    }

    public boolean enrollStudentById(int studentId) {
        for (int i = 0; i < studentIds.size(); i++) {
            if (studentIds.get(i) == studentId) {
                return false;
            }
        }
        studentIds.add(studentId);
        return true;
    }

    public boolean removeStudentById(int studentId) {
        boolean removed = false;
        for (int i = 0; i < studentIds.size(); i++) {
            if (studentIds.get(i) == studentId) {
                studentIds.remove(i);
                removed = true;
                i--;
            }
        }
        return removed;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("courseId", courseId);
        obj.put("title", title);
        obj.put("description", description);
        obj.put("instructorId", instructorId);
        obj.put("approvalStatus", approvalStatus.toString());
        JSONArray lessonArr = new JSONArray();
        for (int i = 0; i < lessons.size(); i++) {
            lessonArr.put(lessons.get(i).toJSON());
        }
        obj.put("lessons", lessonArr);
        JSONArray studentArr = new JSONArray();
        for (int i = 0; i < studentIds.size(); i++) {
            studentArr.put(studentIds.get(i));
        }
        obj.put("students", studentArr);
        return obj;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }
    public ApprovalStatus getApprovalStatus(){
        return approvalStatus;
    }

    public String getDescription() {
        return description;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public ArrayList<Integer> getStudentIds() {
        return studentIds;
    }
}
