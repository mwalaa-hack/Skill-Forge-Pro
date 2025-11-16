package Backend.Models;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 *
 * @author Mohamed Walaa
 */
public class Instructor extends User {

    private ArrayList<Course> createdCourses; // store Course objects

    public Instructor(int userId, String username, String email, String password) {
        super(userId, username, email, password, "instructor");
        this.createdCourses = new ArrayList<>();
    }

    public Instructor(JSONObject j) {
        super(j);
        this.createdCourses = new ArrayList<>();

        JSONArray arr = j.optJSONArray("createdCourses");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject courseJson = arr.getJSONObject(i);
                createdCourses.add(new Course(courseJson)); // reconstruct Course object
            }
        }
    }

    // Add course
    public boolean addCourse(Course course) {
        if (createdCourses.contains(course)) return false;
        createdCourses.add(course);
        return true;
    }

    // Remove course
    public boolean removeCourse(Course course) {
        return createdCourses.remove(course);
    }

    public ArrayList<Course> getCreatedCourses() {
        return createdCourses;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject j = super.toJSON();
        JSONArray arr = new JSONArray();
        for (Course c : createdCourses) {
            arr.put(c.toJSON()); // store full object
        }
        j.put("createdCourses", arr);
        return j;
    }
}
