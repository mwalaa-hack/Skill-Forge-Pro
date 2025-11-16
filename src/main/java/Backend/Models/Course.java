/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package courses;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Pc
 */
public class Courses {

    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private ArrayList<Lesson> lessons;
    private ArrayList<Student> students;
    
    public Courses(int courseId, String title, String description, int instructorId){
    setTitle(title);
    setCourseId(courseId);
    setDescription(description);
    setInstructorId(instructorId);
    this.lessons= new ArrayList<>();
    this.students= new ArrayList<>();
    }
    public Courses(JSONObject json){
    this.courseId=json.getInt("courseId");
    this.title=json.getString("title");
    this.description=json.getString("description");
    this.instructorId=json.getInt("instructorId");
    this.lessons= new ArrayList<>();
    JSONArray lessonArr=json.optJSONArray("lessons");
    if(lessonArr!=null){
    for(int i=0;i<lessonArr.length();i++){
        this.lessons.add(new Lesson(lessonArr.getJSONObject(i)));
    }    
    }
    this.students= new ArrayList<>();
    JSONArray studentArr= json.optJSONArray("students");
    if(studentArr!=null){
    for(int i=0;i<studentArr.length();i++){
    this.students.add(new Student(studentArr.getJSONObject(i)));
    }
    }
    }
    public void setTitle (String title){
    this.title=title;
    
    }
    public void setCourseId(int courseId){
    this.courseId=courseId;
    
    }
    public void setDescription(String description){
    this.description=description;
    
    }
    public void setInstructorId(int instructorId){
    this.instructorId=instructorId;
    }
    public void addLesson(Lesson lesson){
    lessons.add(lesson);
    
    
    }
    public void removeLesson(Lesson lesson){
        lessons.remove(lesson);
    }
    public void enrollStudent(Student student){
    boolean found=false;
    for(int i=0;i<students.size();i++){
    if(students.indexOf(i).getUserId()== student.getUserId){
    found=true;
    break;
    }
    }
    if(!found){
    students.add(student);
    }
   
    }
    public void removeStudent(Student student){
        for(int i=0;i<students.size();i++){
            if(students.indexOf(i).getUserId()==student.getUserId()){
                students.remove(i);
                i--;
        }
    }
    }
    public JSONObject toJson(){
      JSONObject obj= new JSONObject();
      obj.put("courseId",courseId);
      obj.put("title", title);
      obj.put("description", description);
      obj.put("instructorId", instructorId);
      JSONArray lessonArr=new JSONArray();
      for(int i=0;i<lessons.size();i++){
          lessonArr.put(lessons.get(i).toJson());
      }
      obj.put("lessons", lessonArr);
      JSONArray studentArr = new JSONArray();
        for (int i = 0; i < students.size(); i++) {
            studentArr.put(students.get(i).toJson());
        }
        obj.put("students", studentArr);
      
      return obj;
    }
    public int getCourseId() { 
    return courseId; }
    public String getTitle() { 
    return title; }
    public String getDescription() {
    return description; }
    public int getInstructorId() { 
    return instructorId; }
    public ArrayList<Lesson>getLessons() { 
    return lessons; }
    public ArrayList<Student>getStudents() { 
    return students; }
}

