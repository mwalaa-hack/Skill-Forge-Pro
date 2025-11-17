package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Database.UserDatabase;
import Backend.Models.Course;
import Backend.Models.Instructor;
import Backend.Models.Lesson;
import java.util.ArrayList;
import org.json.JSONObject;

public class InstructorService {

    private CourseDatabase courses;
    private UserDatabase users;
    private Instructor instructor;

    public InstructorService(Instructor instructor) {
        this.instructor = instructor;
        courses = new CourseDatabase("courses.json");
        users = new UserDatabase("users.json");
    }

    public boolean createCourse(int courseId, String title, String description) {
        Course newCourse = new Course(courseId, title, description, instructor.getUserId());
        boolean success = courses.insertRecord(newCourse.toJSON());
        if (success) {
            instructor.addCourseId(courseId);
            users.updateUser(instructor);
        }
        return success;
    }

    public boolean updateCourse(int oldCourseId, int newCourseId, String title, String description) {
        Course course = courses.getCourseById(oldCourseId);
        if (course == null) {
            return false;
        }

        if (oldCourseId != newCourseId) {
            course.setCourseId(newCourseId);
            instructor.removeCourseId(oldCourseId);
            instructor.addCourseId(newCourseId);
        }

        course.setTitle(title);
        course.setDescription(description);
        
        boolean courseUpdated = courses.updateCourse(course);
        if (courseUpdated) {
            users.updateUser(instructor);
        }
        return courseUpdated;
    }

    public boolean deleteCourse(Course c) {
        boolean deleted = courses.deleteCourse(c.getCourseId());
        if (deleted) {
            instructor.removeCourseId(c.getCourseId());
            users.updateUser(instructor);
        }
        return deleted;
    }

    public Course getCourseById(int id) {
        return courses.getCourseById(id);
    }

    public ArrayList<Integer> getCreatedCoursesIds() {
        return instructor.getCreatedCourseIds();
    }

    public boolean addLesson(Course c, int lessonId, String title, String content, ArrayList<String> optionalResources) {
        Lesson newLesson = new Lesson(lessonId, title, content, optionalResources);
        return courses.addLesson(c.getCourseId(), newLesson);
    }

    public boolean updateLesson(int courseId, int oldLessonId, int newLessonId, String title, String content, ArrayList<String> resources) {
        Course course = courses.getCourseById(courseId);
        if (course == null) {
            return false;
        }

        Lesson lesson = course.getLessonById(oldLessonId);
        if (lesson == null) {
            return false;
        }

        if (oldLessonId != newLessonId) {
            lesson.setLessonId(newLessonId);
        }

        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setResources(resources);
        
        return courses.updateLesson(courseId, lesson);
    }

    public boolean deleteLesson(Course c, int lessonId) {
        return courses.deleteLesson(c.getCourseId(), lessonId);
    }

    public ArrayList<Lesson> getLessons(Course c) {
        return c.getLessons();
    }

    public ArrayList<Integer> getEnrolledStudentsIds(Course c) {
        return c.getStudentIds();
    }
}