package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Database.UserDatabase;
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
        boolean enrolled = courses.enrollStudent(courseId, student.getUserId());
        if (enrolled) {
            student.enrollCourseById(courseId);
            users.updateUser(student);
        }
        return enrolled;
    }

    public boolean dropCourse(int courseId) {
        Course course = courses.getCourseById(courseId);
        if (course != null) {
            course.removeStudentById(student.getUserId());
        }
        student.dropCourseById(courseId);
        return users.updateUser(student);
    }

    public boolean completeLesson(int courseId, int lessonId) {
        student.markLessonCompletedById(courseId, lessonId);
        return users.updateUser(student);
    }

    public boolean unmarkLesson(int courseId, int lessonId) {
        ArrayList<Integer> completedLessons = student.getCompletedLessonsByCourseId(courseId);
        boolean removed = false;
        for (int i = 0; i < completedLessons.size(); i++) {
            if (completedLessons.get(i) == lessonId) {
                completedLessons.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) {
            return users.updateUser(student);
        }
        return false;
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
}