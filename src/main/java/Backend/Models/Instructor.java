package Backend.Models;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 *
 * @author Mohamed Walaa
 */
public class Instructor extends User {

    private ArrayList<Integer> createdCourseIds;

    public Instructor(int userId, String username, String email, String password) {
        super(userId, username, email, password, "instructor");
        this.createdCourseIds = new ArrayList<>();
    }

    public Instructor(JSONObject j) {
        super(j);
        this.createdCourseIds = new ArrayList<>();
        JSONArray arr = j.optJSONArray("createdCourses");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                createdCourseIds.add(arr.getInt(i));
            }
        }
    }

    @Override
    public String getRole() {
        return "instructor";
    }

    public boolean addCourseId(int courseId) {
        if (createdCourseIds.contains(courseId)) return false;
        createdCourseIds.add(courseId);
        return true;
    }

    public boolean removeCourseId(int courseId) {
        return createdCourseIds.remove((Integer) courseId);
    }

    public boolean ownsCourse(int courseId) {
        return createdCourseIds.contains(courseId);
    }

    public ArrayList<Integer> getCreatedCourseIds() {
        return createdCourseIds;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject j = super.toJSON();
        JSONArray arr = new JSONArray();
        for (int i = 0; i < createdCourseIds.size(); i++) {
            arr.put(createdCourseIds.get(i));
        }
        j.put("createdCourses", arr);
        return j;
    }
}
