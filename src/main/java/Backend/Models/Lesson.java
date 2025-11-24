package Backend.Models;

import Backend.Database.Info;
import Backend.Models.Quiz;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Lesson implements Info {
    private int lessonId;
    private String title;
    private String content;
    private ArrayList<String> resources; 
    private Quiz quiz;
    private boolean hasQuiz;

    public Lesson(int lessonId, String title, String content, ArrayList<String> resources, Quiz quiz) {
        setLessonId(lessonId);
        setTitle(title);
        setContent(content);
        setResources(resources);
        setQuiz(quiz);
        this.hasQuiz = (quiz != null);
    }

    public Lesson(int lessonId, String title, String content, ArrayList<String> resources) {
        this(lessonId, title, content, resources, null);
    }

    public Lesson(JSONObject json) {
        this.lessonId = json.getInt("lessonId");
        this.title = json.getString("title");
        this.content = json.getString("content");
        this.hasQuiz = json.optBoolean("hasQuiz", false);
        
        if (json.has("quiz") && !json.isNull("quiz")) {
            this.quiz = new Quiz(json.getJSONObject("quiz"));
            this.hasQuiz = true;
        } else {
            this.quiz = null;
            this.hasQuiz = false;
        }
        
        this.resources = new ArrayList<>();
        JSONArray arr = json.optJSONArray("resources");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                resources.add(arr.getString(i));
            }
        }
    }

    public boolean addQuiz(Quiz quiz) {
        if (quiz == null) {
            return false;
        }
        this.quiz = quiz;
        this.hasQuiz = true;
        return true;
    }

    public boolean hasQuiz() {
        return hasQuiz;
    }

    public boolean addResource(String resource) {
        if (resource == null || resource.trim().isEmpty() || resources.contains(resource)) return false;
        resources.add(resource);
        return true;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("lessonId", lessonId);
        obj.put("title", title);
        obj.put("content", content);
        obj.put("hasQuiz", hasQuiz);
        
        if (quiz != null) {
            obj.put("quiz", quiz.toJSON());
        } else {
            obj.put("quiz", JSONObject.NULL);
        }
        
        JSONArray resourcesArr = new JSONArray();
        for (int i = 0; i < resources.size(); i++) {
            resourcesArr.put(resources.get(i));
        }
        obj.put("resources", resourcesArr);
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

    public Quiz getQuiz() {
        return quiz;
    }

    public void setLessonId(int lessonId) {
        if (lessonId <= 0) throw new IllegalArgumentException("lessonId must be > 0");
        this.lessonId = lessonId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public void setResources(ArrayList<String> resources) {
        if (resources != null) {
            this.resources = new ArrayList<>(resources);
        } else {
            this.resources = new ArrayList<>();
        }
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.hasQuiz = (quiz != null);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Lesson)) return false;
        Lesson other = (Lesson) obj;
        return this.lessonId == other.lessonId;
    }

    public int hashCode() {
        return Integer.hashCode(lessonId);
    }

    public boolean isSameLesson(Lesson other) {
        return this.lessonId == other.lessonId;
    }
}