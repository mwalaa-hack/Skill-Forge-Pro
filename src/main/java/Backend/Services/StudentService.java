package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Database.UserDatabase;
import Backend.Models.Certificate;
import Backend.Models.Course;
import Backend.Models.Lesson;
import Backend.Models.Student;
import java.util.ArrayList;

public class StudentService {

    private CourseDatabase courses;
    private UserDatabase users;
    private Student student;

    public StudentService(Student student) {
        this.courses = new CourseDatabase("courses.json");
        this.users = new UserDatabase("users.json");
        this.student = student;
    }

    public ArrayList<Course> getAllCourses() {
        return courses.getAllCourses();
    }

    public boolean enrollInCourse(int courseId) {
        boolean enrollStatus = courses.enrollStudent(courseId, student.getUserId());
        if (enrollStatus) {
            student.enrollCourseById(courseId);
            users.updateUser(student);
        }
        return enrollStatus;
    }
    
    public ArrayList<Course> getEnrolledCourses() {
        ArrayList<Course> enrolledCourses = new ArrayList<>();
        ArrayList<Integer> enrolledIds = student.getEnrolledCourseIds();
        
        for (int i = 0; i < enrolledIds.size(); i++) {
            Course course = courses.getCourseById(enrolledIds.get(i));
            if (course != null) {
                enrolledCourses.add(course);
            }
        }
        return enrolledCourses;
    }

    public boolean isLessonCompleted(int courseId, int lessonId) {
        ArrayList<Integer> completed = student.getCompletedLessonsByCourseId(courseId);
        for (int i = 0; i < completed.size(); i++) {
            if (completed.get(i) == lessonId) {
                return true;
            }
        }
        return false;
    }

    public Course getCourse(int courseId) {
        return courses.getCourseById(courseId);
    }

    public int getCompletedLessonsCount(int courseId) {
        ArrayList<Integer> completedLessons = student.getCompletedLessonsByCourseId(courseId);
        return completedLessons.size();
    }
    
    public ArrayList<Certificate> getCertificates(){
        return student.getCertificates();
    }
    
    public boolean checkCourseCompletion(Course course){
        ArrayList<Integer> completedLessons = student.getCompletedLessonsByCourseId(course.getCourseId());
        ArrayList<Lesson> totalLessons = course.getLessons();
        if (completedLessons.size() == totalLessons.size()) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean addCertificateToStudent(Certificate certificate) {
        student.addCertificate(certificate);
        return users.updateUser(student);
    }

    public boolean generateCertificate(Course course){
        if (checkCourseCompletion(course)) {
            String name = student.getUsername();
            String courseTitle = course.getTitle();
            CourseService courseService = new CourseService(course);
            String instructorName = courseService.getInstructorName();
            String certId = "CERT_" + course.getCourseId() + "_" + student.getUserId() + "_" + System.currentTimeMillis();
            Certificate certificate = new Certificate(certId, student.getUserId(), course.getCourseId(), 
                                                    name, courseTitle, instructorName);
            
            return addCertificateToStudent(certificate);
        } else {
            return false;
        }
    }
    
    public boolean canAccessLesson(Course c, int lessonId){
        ArrayList<Lesson> lessons = c.getLessons();
        int currentLessonIndex = -1;
        
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == lessonId) {
                currentLessonIndex = i;
                break;
            }
        }
        
        if (currentLessonIndex == 0) {
            return true;
        }
        
        if (currentLessonIndex > 0) {
            int previousLessonId = lessons.get(currentLessonIndex - 1).getLessonId();
            return isLessonCompleted(c.getCourseId(), previousLessonId);
        }
        
        return false;
    }
    
    public boolean submitQuiz(int courseId, int lessonId, double score) {
        student.setQuizScore(courseId, lessonId, score);
        boolean updated = users.updateUser(student);
        
        if (updated) {
            Course course = getCourse(courseId);
            if (course != null && checkCourseCompletion(course)) {
                generateCertificate(course);
            }
        }
        
        return updated;
    }
    
    public Certificate getCertificateById(String certificateId) {
        return student.getCertificateById(certificateId);
    }
    
    public double getCourseCompletionPercentage(int courseId) {
        Course course = getCourse(courseId);
        if (course == null) {
            return 0.0;
        }
        
        int completed = getCompletedLessonsCount(courseId);
        int total = course.getLessons().size();
        
        if (total > 0) {
            return (completed * 100.0) / total;
        } else {
            return 0.0;
        }
    }
    
    public boolean hasPassedQuiz(int courseId, int lessonId) {
        return student.hasPassedQuiz(courseId, lessonId);
    }
    
    public int getQuizAttempts(int courseId, int lessonId) {
        return student.getQuizAttempts(courseId, lessonId);
    }
}