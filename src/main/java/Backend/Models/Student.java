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

    public Student(int userId, String username, String email, String password) {
        super(userId, username, email, password, "student");
        this.enrolledCourseIds = new ArrayList<>();
        this.progress = new HashMap<>();
    }

    public Student(JSONObject j) {
        super(j);
        this.enrolledCourseIds = new ArrayList<>();
        this.progress = new HashMap<>();
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
 
    

    public boolean dropCourseById(int courseId) {
        boolean removed = false;
        for (int i = 0; i < enrolledCourseIds.size(); i++) {
            if (enrolledCourseIds.get(i) == courseId) {
                enrolledCourseIds.remove(i);
                removed = true;
                break;
            }
        }
        progress.remove(courseId);
        return removed;
    }

    public boolean markLessonCompletedById(int courseId, int lessonId) {
        boolean enrolled = false;
        for (int i = 0; i < enrolledCourseIds.size(); i++) {
            if (enrolledCourseIds.get(i) == courseId) {
                enrolled = true;
                break;
            }
        }
        if (!enrolled) return false;
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
        return j;
    }
}
