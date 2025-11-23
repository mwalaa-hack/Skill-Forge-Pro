/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Services;

import Backend.Models.Course;
import Backend.Models.Lesson;
import Backend.Models.Student;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author HP_Laptop
 */
public class AnalyticsService {
    private Course course;
    private CourseDatabase courseDB;
    private UserDatabase userDB;
    
    public AnalyticsService(Course course){
        this.course = course;
        this.courseDB = new CourseDatabase("courses.json");
        this.userDB = new UserDatabase("users.json");
    }
    
    public AnalyticsService() {
        this.courseDB = new CourseDatabase("courses.json");
        this.userDB = new UserDatabase("users.json");
    }
    
    public HashMap<String, Object> getInstructorInsights(int instructorId) {
        HashMap<String, Object> insights = new HashMap<>();
        ArrayList<Course> instructorCourses = getCoursesByInstructor(instructorId);
        
        insights.put("totalCourses", instructorCourses.size());
        
        int totalEnrollments = 0;
        double overallCompletionRate = 0.0;
        HashMap<String, Double> coursePerformance = new HashMap<>();
        HashMap<String, Integer> courseEnrollments = new HashMap<>();
        
        for (int i = 0; i < instructorCourses.size(); i++) {
            Course course = instructorCourses.get(i);
            int enrollments = course.getStudentIds().size();
            totalEnrollments += enrollments;
            
            double completionRate = calculateCourseCompletionRate(course.getCourseId());
            overallCompletionRate += completionRate;
            
            coursePerformance.put(course.getTitle(), completionRate);
            courseEnrollments.put(course.getTitle(), enrollments);
        }
        
        insights.put("totalEnrollments", totalEnrollments);
        if (instructorCourses.size() > 0) {
            insights.put("averageCompletionRate", overallCompletionRate / instructorCourses.size());
        } else {
            insights.put("averageCompletionRate", 0.0);
        }
        insights.put("coursePerformance", coursePerformance);
        insights.put("courseEnrollments", courseEnrollments);
        
        return insights;
    }
    
    public HashMap<String, Object> getCourseAnalytics(int courseId) {
        HashMap<String, Object> analytics = new HashMap<>();
        Course course = courseDB.getCourseById(courseId);
        
        if (course != null) {
            ArrayList<Lesson> lessons = course.getLessons();
            HashMap<String, Double> lessonQuizAverages = new HashMap<>();
            HashMap<String, Integer> lessonCompletionCounts = new HashMap<>();
            HashMap<String, Double> lessonPassRates = new HashMap<>();
            
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                double avgScore = calculateLessonAverageScore(courseId, lesson.getLessonId());
                int completionCount = getLessonCompletionCount(courseId, lesson.getLessonId());
                double passRate = calculateLessonPassRate(courseId, lesson.getLessonId());
                
                lessonQuizAverages.put(lesson.getTitle(), avgScore);
                lessonCompletionCounts.put(lesson.getTitle(), completionCount);
                lessonPassRates.put(lesson.getTitle(), passRate);
            }
            
            analytics.put("lessonQuizAverages", lessonQuizAverages);
            analytics.put("lessonCompletionCounts", lessonCompletionCounts);
            analytics.put("lessonPassRates", lessonPassRates);
            analytics.put("totalEnrollments", course.getStudentIds().size());
            analytics.put("completionRate", calculateCourseCompletionRate(courseId));
            analytics.put("averageCourseScore", calculateCourseAverageScore(courseId));
        }
        
        return analytics;
    }
    
    public ArrayList<HashMap<String, Integer>> getStudentsProgress(){
        ArrayList<HashMap<String, Integer>> totalProgress = new ArrayList<HashMap<String,Integer>>();
        ArrayList<Student> students = getEnrolledStudents();
        for (int i = 0; i < students.size(); i++) {
            HashMap<String, Integer> studentProgress = new HashMap<String, Integer>();
            Student s = students.get(i);
            String name = s.getUsername();
            int progress = calculateCourseProgress(s);
            studentProgress.put(name, progress);
            totalProgress.add(studentProgress);
        }
        return totalProgress;
    }
    
    private int calculateCourseProgress(Student s){
        ArrayList<Integer> completedLessons = s.getCompletedLessonsByCourseId(course.getCourseId());
        ArrayList<Lesson> totalLessons = course.getLessons();
        if (totalLessons.size() == 0) {
            return 0;
        } else {
            int progress = (completedLessons.size() * 100) / totalLessons.size();
            return progress;
        }
    }
    
    public double getAllLessonsAverage(){
        double sum = 0;
        ArrayList<Student> enrolledStudents = getEnrolledStudents();
        ArrayList<Lesson> totalLessons = course.getLessons();
        if (enrolledStudents.size() == 0 || totalLessons.size() == 0) {
            return 0.0;
        }
        
        for (int i = 0; i < totalLessons.size(); i++) {
            sum += getLessonAverage(totalLessons.get(i).getLessonId());
        }
        double average = sum / totalLessons.size();        
        return average;
    }
    
    public double getLessonAverage(int lessonId){
        double sum = 0.0;
        int count = 0;
        ArrayList<Student> enrolledStudents = getEnrolledStudents();
        
        for (int i = 0; i < enrolledStudents.size(); i++) {
            Student student = enrolledStudents.get(i);
            Double score = student.getQuizScore(course.getCourseId(), lessonId);
            if (score != null) {
                sum += score;
                count++;
            }
        }
        
        if (count == 0) {
            return 0.0;
        } else {
            double average = sum / count;        
            return average;
        }
    }
    
    public HashMap<String, Double> getStudentPerformance(int studentId) {
        HashMap<String, Double> performance = new HashMap<>();
        Student student = (Student) userDB.getUserById(studentId, "student");
        
        if (student != null) {
            ArrayList<Lesson> lessons = course.getLessons();
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                Double score = student.getQuizScore(course.getCourseId(), lesson.getLessonId());
                if (score != null) {
                    performance.put(lesson.getTitle(), score);
                } else {
                    performance.put(lesson.getTitle(), 0.0);
                }
            }
        }
        
        return performance;
    }
    
    private ArrayList<Student> getEnrolledStudents(){
        ArrayList<Integer> studentsIds = course.getStudentIds();
        ArrayList<Student> students = new ArrayList<Student>();
        
        for (int i = 0; i < studentsIds.size(); i++) {
            Student student = (Student) userDB.getUserById(studentsIds.get(i), "student");
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }
    
    private ArrayList<Course> getCoursesByInstructor(int instructorId) {
        ArrayList<Course> allCourses = courseDB.getAllCourses();
        ArrayList<Course> instructorCourses = new ArrayList<>();
        
        for (int i = 0; i < allCourses.size(); i++) {
            Course course = allCourses.get(i);
            if (course.getInstructorId() == instructorId) {
                instructorCourses.add(course);
            }
        }
        
        return instructorCourses;
    }
    
    private double calculateCourseCompletionRate(int courseId) {
        Course course = courseDB.getCourseById(courseId);
        if (course == null) {
            return 0.0;
        }
        
        ArrayList<Integer> studentIds = course.getStudentIds();
        int completedCount = 0;
        
        for (int i = 0; i < studentIds.size(); i++) {
            Student student = (Student) userDB.getUserById(studentIds.get(i), "student");
            if (student != null) {
                ArrayList<Integer> completedLessons = student.getCompletedLessonsByCourseId(courseId);
                if (completedLessons.size() == course.getLessons().size()) {
                    completedCount++;
                }
            }
        }
        
        if (studentIds.size() > 0) {
            return (completedCount * 100.0) / studentIds.size();
        } else {
            return 0.0;
        }
    }
    
    private double calculateCourseAverageScore(int courseId) {
        Course course = courseDB.getCourseById(courseId);
        if (course == null) {
            return 0.0;
        }
        
        ArrayList<Integer> studentIds = course.getStudentIds();
        double totalScore = 0.0;
        int count = 0;
        
        for (int i = 0; i < studentIds.size(); i++) {
            Student student = (Student) userDB.getUserById(studentIds.get(i), "student");
            if (student != null) {
                ArrayList<Lesson> lessons = course.getLessons();
                double studentTotal = 0.0;
                int lessonCount = 0;
                
                for (int j = 0; j < lessons.size(); j++) {
                    Double score = student.getQuizScore(courseId, lessons.get(j).getLessonId());
                    if (score != null) {
                        studentTotal += score;
                        lessonCount++;
                    }
                }
                
                if (lessonCount > 0) {
                    totalScore += (studentTotal / lessonCount);
                    count++;
                }
            }
        }
        
        if (count > 0) {
            return totalScore / count;
        } else {
            return 0.0;
        }
    }
    
    private double calculateLessonAverageScore(int courseId, int lessonId) {
        Course course = courseDB.getCourseById(courseId);
        if (course == null) {
            return 0.0;
        }
        
        ArrayList<Integer> studentIds = course.getStudentIds();
        double totalScore = 0.0;
        int count = 0;
        
        for (int i = 0; i < studentIds.size(); i++) {
            Student student = (Student) userDB.getUserById(studentIds.get(i), "student");
            if (student != null) {
                Double score = student.getQuizScore(courseId, lessonId);
                if (score != null) {
                    totalScore += score;
                    count++;
                }
            }
        }
        
        if (count > 0) {
            return totalScore / count;
        } else {
            return 0.0;
        }
    }
    
    private int getLessonCompletionCount(int courseId, int lessonId) {
        Course course = courseDB.getCourseById(courseId);
        if (course == null) {
            return 0;
        }
        
        ArrayList<Integer> studentIds = course.getStudentIds();
        int completionCount = 0;
        
        for (int i = 0; i < studentIds.size(); i++) {
            Student student = (Student) userDB.getUserById(studentIds.get(i), "student");
            if (student != null) {
                ArrayList<Integer> completedLessons = student.getCompletedLessonsByCourseId(courseId);
                for (int j = 0; j < completedLessons.size(); j++) {
                    if (completedLessons.get(j) == lessonId) {
                        completionCount++;
                        break;
                    }
                }
            }
        }
        
        return completionCount;
    }
    
    private double calculateLessonPassRate(int courseId, int lessonId) {
        Course course = courseDB.getCourseById(courseId);
        if (course == null) {
            return 0.0;
        }
        
        ArrayList<Integer> studentIds = course.getStudentIds();
        int passCount = 0;
        int attemptCount = 0;
        
        for (int i = 0; i < studentIds.size(); i++) {
            Student student = (Student) userDB.getUserById(studentIds.get(i), "student");
            if (student != null) {
                Double score = student.getQuizScore(courseId, lessonId);
                if (score != null) {
                    attemptCount++;
                    if (score >= 70.0) {
                        passCount++;
                    }
                }
            }
        }
        
        if (attemptCount > 0) {
            return (passCount * 100.0) / attemptCount;
        } else {
            return 0.0;
        }
    }
}