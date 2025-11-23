/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Database.UserDatabase;
import Backend.Models.Course;
import Backend.Models.Lesson;
import Backend.Models.Question;
import Backend.Models.Quiz;
import Backend.Models.Student;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author HP_Laptop
 */
public class QuizService {
    private Quiz quiz;
    private Student student;
    private int courseId;
    private int lessonId;
    private StudentService studentService;

    public QuizService(Quiz quiz, Student student, int courseId, int lessonId) {
        this.quiz = quiz;
        this.student = student;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.studentService = new StudentService(student);
    }

    public double calculateScore(ArrayList<Integer> userAnswers) {
        if (userAnswers.size() != quiz.getQuestions().size()) {
            throw new IllegalArgumentException("Number of answers must match number of questions");
        }

        int correctCount = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            if (userAnswers.get(i) == quiz.getQuestions().get(i).getCorrectChoice()) {
                correctCount++;
            }
        }

        double score = (correctCount * 100.0) / quiz.getQuestions().size();
        
        studentService.submitQuiz(courseId, lessonId, score);
        
        return score;
    }

    public boolean isPassed(double score) {
        return score >= quiz.getPassingScore();
    }

    public int getQuizAttempts() {
        return student.getQuizAttempts(courseId, lessonId);
    }

    public ArrayList<Question> getQuestions() {
        return quiz.getQuestions();
    }

}