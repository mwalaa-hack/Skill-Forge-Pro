package Backend.Models;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Mohamed Walaa
 */
public class Student extends User {
    private ArrayList<Integer> enrolledCourseIds;
    private HashMap<Integer, ArrayList<Integer>> progress;
    private HashMap<String, Double> quizScores;
    private ArrayList<String> certificates;

    public Student(int userId, String username, String email, String password) {
        super(userId, username, email, password, "student");
        this.enrolledCourseIds = new ArrayList<>();
        this.progress = new HashMap<>();
        this.quizScores = new HashMap<>();
        this.certificates = new ArrayList<>();
    }

    public Student(JSONObject j) {
        super(j);
        this.enrolledCourseIds = new ArrayList<>();
        this.progress = new HashMap<>();
        this.quizScores = new HashMap<>();
        this.certificates = new ArrayList<>();
        
        JSONArray arr = j.optJSONArray("enrolledCourses");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject cobj = arr.getJSONObject(i);
                int cid = cobj.getInt("courseId");
                enrolledCourseIds.add(cid);
                ArrayList<Integer> completed = new ArrayList<>();
                JSONArray comp = cobj.optJSONArray("completedLessons");
                if (comp != null) {
                    for (int k = 0; k < comp.length(); k++) completed.add(comp.getInt(k));
                }
                progress.put(cid, completed);
            }
        }
        
        JSONObject quizObj = j.optJSONObject("quizScores");
        if (quizObj != null) {
            for (String key : quizObj.keySet()) {
                quizScores.put(key, quizObj.getDouble(key));
            }
        }
        
        JSONArray certArr = j.optJSONArray("certificates");
        if (certArr != null) {
            for (int i = 0; i < certArr.length(); i++) {
                certificates.add(certArr.getString(i));
            }
        }
    }

    public boolean enrollCourseById(int courseId) {
        for (int i = 0; i < enrolledCourseIds.size(); i++) if (enrolledCourseIds.get(i) == courseId) return false;
        enrolledCourseIds.add(courseId);
        progress.put(courseId, new ArrayList<Integer>());
        return true;
    }
    
    
    @Override
       public String getRole() {
        return "student";
    }
 

    public boolean markLessonCompletedById(int courseId, int lessonId) {
        ArrayList<Integer> completed = progress.get(courseId);
        if (completed == null) {
            completed = new ArrayList<Integer>();
            progress.put(courseId, completed);
        }
        for (int i = 0; i < completed.size(); i++) if (completed.get(i) == lessonId) return false;
        completed.add(lessonId);
        return true;
    }

    public ArrayList<Integer> getCompletedLessonsByCourseId(int courseId) {
        ArrayList<Integer> res = progress.get(courseId);
        if (res == null) return new ArrayList<Integer>();
        return res;
    }

    public ArrayList<Integer> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }

    public void setQuizScore(int courseId, int lessonId, double score) {
        String key = courseId + "_" + lessonId;
        quizScores.put(key, score);
        if (score >= 50) {
            markLessonCompletedById(courseId, lessonId);
        }
    }
    
    public Double getQuizScore(int courseId, int lessonId) {
        String key = courseId + "&" + lessonId;
        return quizScores.get(key);
    }
    
    public boolean hasPassedQuiz(int courseId, int lessonId) {
        Double score = getQuizScore(courseId, lessonId);
        return score != null && score >= 70;
    }
    
    public void addCertificate(String certId) {
        certificates.add(certId);
    }
    
    public ArrayList<String> getCertificates() {
        return certificates;
    }
    
    public boolean hasCertificate(String certId) {
        return certificates.contains(certId);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject j = super.toJSON();
        JSONArray arr = new JSONArray();
        for (int i = 0; i < enrolledCourseIds.size(); i++) {
            int cid = enrolledCourseIds.get(i);
            JSONObject cobj = new JSONObject();
            cobj.put("courseId", cid);
            JSONArray comp = new JSONArray();
            ArrayList<Integer> completed = progress.get(cid);
            if (completed != null) {
                for (int k = 0; k < completed.size(); k++) comp.put(completed.get(k));
            }
            cobj.put("completedLessons", comp);
            arr.put(cobj);
        }
        j.put("enrolledCourses", arr);
        
        JSONObject quizObj = new JSONObject();
        for (String key : quizScores.keySet()) {
            quizObj.put(key, quizScores.get(key));
        }
        j.put("quizScores", quizObj);
        
        JSONArray certArr = new JSONArray();
        for (int i = 0; i < certificates.size(); i++) {
            certArr.put(certificates.get(i));
        }
        j.put("certificates", certArr);
        
        return j;
    }
}