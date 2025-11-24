package Backend.Services;

import Backend.Database.CourseDatabase;
import Backend.Database.UserDatabase;
import Backend.Models.Course;
import Backend.Models.Instructor;
import Backend.Models.Lesson;
import Backend.Models.Question;
import Backend.Models.Quiz;
import java.util.ArrayList;

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
        boolean createStatus = courses.insertRecord(newCourse.toJSON());
        if (createStatus) {
            instructor.addCourseId(courseId);
            users.updateUser(instructor);
        }
        return createStatus;
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
        
        boolean updateStatus = courses.updateCourse(course);
        if (updateStatus) {
            users.updateUser(instructor);
        }
        return updateStatus;
    }

    public boolean deleteCourse(Course c) {
        boolean deleteStatus = courses.deleteCourse(c.getCourseId());
        if (deleteStatus) {
            instructor.removeCourseId(c.getCourseId());
            users.updateUser(instructor);
        }
        return deleteStatus;
    }

    public Course getCourseById(int id) {
        return courses.getCourseById(id);
    }

    public ArrayList<Integer> getCreatedCoursesIds() {
        return instructor.getCreatedCourseIds();
    }

    public boolean addLesson(Course c, int lessonId, String title, String content, ArrayList<String> optionalResources, Quiz quiz) {
        Lesson newLesson = new Lesson(lessonId, title, content, optionalResources, quiz);
        return courses.addLesson(c.getCourseId(), newLesson);
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

    public boolean addQuizToLesson(int courseId, int lessonId, int quizId,int passingScore,ArrayList<Question> questions){
        Course course = getCourseById(courseId);
        if(course == null){
            return false;
        }
        
         Lesson lesson = course.getLessonById(lessonId);
        if (lesson == null) {
            return false;
        }

         Quiz quiz = new Quiz(quizId, passingScore);
        for (Question question : questions) {
            quiz.addQuestion(question);
        }

        lesson.addQuiz(quiz);
        
        boolean updateStatus = courses.updateLesson(courseId, lesson);
        return updateStatus;
    }
    
    public boolean updateQuiz(int courseId, int lessonId, int quizId,int passingScore,ArrayList<Question> questions){
        Course course = getCourseById(courseId);
        if(course == null){
            return false;
        }
        
         Lesson lesson = course.getLessonById(lessonId);
        if (lesson == null) {
            return false;
        }
        
        Quiz quiz = new Quiz(quizId, passingScore);
        for (Question question : questions) {
            quiz.addQuestion(question);
        }
       
        
        lesson.setQuiz(quiz);
        boolean updateStatus = courses.updateLesson(courseId, lesson);
        return updateStatus;
    }
    
    public ArrayList<Lesson> getLessons(Course c) {
        return c.getLessons();
    }

    public ArrayList<Lesson> getLessonsWithQuizzes(Course c){
        ArrayList<Lesson> totalLessons = c.getLessons();
        ArrayList<Lesson> lessonsWithQuizzes = new ArrayList<Lesson>();
        for (int i  = 0; i < totalLessons.size(); i++){
            if(totalLessons.get(i).hasQuiz()){
                lessonsWithQuizzes.add(totalLessons.get(i));
            }
        }
        return lessonsWithQuizzes;
    }
    
    public ArrayList<Integer> getEnrolledStudentsIds(Course c) {
        return c.getStudentIds();
    }
}