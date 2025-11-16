package Backend.Models;

import Backend.Database.Info;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Lesson implements Info {

    private int lessonId;
    private String title;
    private String content;
    private ArrayList<String> resources;

    public Lesson(int lessonId, String title, String content) {
        setLessonId(lessonId);
        setTitle(title);
        setContent(content);
        this.resources = new ArrayList<>();
    }

    public Lesson(JSONObject obj) {
        this.lessonId = obj.getInt("lessonId");
        this.title = obj.getString("title");
        this.content = obj.getString("content");
        this.resources = new ArrayList<>();
        JSONArray arr = obj.optJSONArray("resources");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                resources.add(arr.getString(i));
            }
        }
    }

    public boolean addResource(String resource) {
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).equals(resource)) {
                return false;
            }
        }
        resources.add(resource);
        return true;
    }

    public boolean removeResource(String resource) {
        boolean removed = false;
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).equals(resource)) {
                resources.remove(i);
                removed = true;
                i--;
            }
        }
        return removed;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("lessonId", lessonId);
        obj.put("title", title);
        obj.put("content", content);
        JSONArray arr = new JSONArray();
        for (int i = 0; i < resources.size(); i++) {
            arr.put(resources.get(i));
        }
        obj.put("resources", arr);
        return obj;
    }

    public int getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getResources() {
        return resources;
    }

    public void setLessonId(int lessonId) {
        if (lessonId <= 0) {
            throw new IllegalArgumentException("lessonId must be > 0");
        }
        this.lessonId = lessonId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
