/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Models.Course;
import Backend.Models.Lesson;
import Backend.Models.Quiz;
import Backend.Models.Student;

/**
 *
 * @author HP_Laptop
 */
public class QuizService {
    private Quiz quiz;
    private Student student;
    private int courseId;
    private int lessonId;
    private CourseService courseService;
    private Course c;
    private Lesson l;
    private CourseDatabase courses;
    
    public QuizService(Quiz quiz, Student student, int courseId, int lessonId){
        this.quiz = quiz;
        this.student = student;
        this.courseId = courseId;
        this.lessonId = lessonId;
        c = getCourseById(courseId);
        courseService = new CourseService(c);
        l = courseService.getLessonById(lessonId);
         this.courses = new CourseDatabase("courses.json");
    }
    

    public Course getCourseById(int id) {
        return courses.getCourseById(id);
    }

    private boolean markQuestion(int questionNumber, int choice){
        if(choice == quiz.getQuestions().get(questionNumber).getCorrectChoice()){
            return true;
        }
        else {
            return false;
        }
    }
    
    public int getQuizScore(String answers){
        String[] answersArray = answers.split(",");
        int correctAnswersCount = 0;
        for(int i=0; i < answersArray.length; i++){
            int choice = -1;
            try{
                choice = Integer.parseInt(answersArray[i]);
            }
            catch(NumberFormatException e){
                System.out.println("Error! Choice not a number!");
            }
            boolean answerCorrect = markQuestion(i,choice);
            if(answerCorrect){
                correctAnswersCount++;
            }
        }
        int score = (correctAnswersCount / answersArray.length) * 100;
        student.setQuizScore(courseId, lessonId, score);
        return score;
    }
    
    public boolean isQuizPassed(int score){
        return quiz.isPassed(score);
    }
}
