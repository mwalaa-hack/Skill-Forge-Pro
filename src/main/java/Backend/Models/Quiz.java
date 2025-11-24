/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
public class Quiz implements Info {
    private ArrayList<Question> questions;
    private int quizId;
    private double passingScore;
    
    public Quiz(int quizId, double passingScore) {
        setQuizId(quizId);
        setPassingScore(passingScore);
        this.questions = new ArrayList<>();
    }
    
    public Quiz(JSONObject json) {
        this.quizId = json.getInt("quizId");
        this.passingScore = json.getDouble("passingScore");
        this.questions = new ArrayList<>();
        JSONArray questionArr = json.optJSONArray("questions");
        if (questionArr != null) {
            for (int i = 0; i < questionArr.length(); i++) {
                questions.add(new Question(questionArr.getJSONObject(i)));
            }
        }
    }
    
    public boolean addQuestion(Question q) {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getQuestionId() == q.getQuestionId()) {
                return false;
            }
        }
        questions.add(q);
        return true;
    }
    
    public boolean removeQuestion(int questionId) {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getQuestionId() == questionId) {
                questions.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public boolean isPassed(double score) {
        return score >= passingScore;
    }
    
    public boolean isValidForLesson() {
        return questions.size() >= 1;
    }
    
    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("quizId", quizId);
        obj.put("passingScore", passingScore);
        JSONArray questionArr = new JSONArray();
        for (int i = 0; i < questions.size(); i++) {
            questionArr.put(questions.get(i).toJSON());
        }
        obj.put("questions", questionArr);
        return obj;
    }
    
    public int getQuizId() {
        return quizId;
    }

    public double getPassingScore() {
        return passingScore;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
    
    public void setQuizId(int quizId) {
        if (quizId < 0) {
            throw new IllegalArgumentException("quizId must be greater than 0");
        }
        this.quizId = quizId;
    }
    
    public void setPassingScore(double passingScore) {
        if (passingScore < 1) {
            throw new IllegalArgumentException("passingScore must be at least 1");
        }  
        this.passingScore = passingScore;
    }
}