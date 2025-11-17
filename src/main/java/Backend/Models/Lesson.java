package Backend.Models;

import Backend.Database.Info;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Lesson implements Info {

    private int lessonId;
    private String title;
    private String content;
    private List<String> resources; 

    public Lesson(int lessonId, String title, String content, List<String> resources) {
        setLessonId(lessonId);
        setTitle(title);
        setContent(content);
        this.resources = resources != null ? new ArrayList<>(resources) : new ArrayList<>();
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
        if (resource == null || resources.contains(resource)) return false;
        resources.add(resource);
        return true;
    }

    public boolean removeResource(String resource) {
        return resources.remove(resource);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("lessonId", lessonId);
        obj.put("title", title);
        obj.put("content", content);
        obj.put("resources", new JSONArray(resources));
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

    public List<String> getResources() {
        return resources;
    }

    public void setLessonId(int lessonId) {
        if (lessonId <= 0) throw new IllegalArgumentException("lessonId must be > 0");
        this.lessonId = lessonId;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public void setContent(String content) {
        this.content = content != null ? content : "";
    }
    
    public void setResources(List<String> newResources) {
    if (newResources != null) {
        this.resources = new ArrayList<>(newResources);
    } else {
        this.resources = new ArrayList<>();
    }
}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Lesson)) return false;
        Lesson other = (Lesson) obj;
        return this.lessonId == other.lessonId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(lessonId);
    }
}
