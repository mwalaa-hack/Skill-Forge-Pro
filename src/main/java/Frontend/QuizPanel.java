/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Frontend;

import Backend.Models.Course;
import Backend.Models.Lesson;
import Backend.Models.Question;
import Backend.Models.Quiz;
import Backend.Models.Student;
import Backend.Services.QuizService;
import Backend.Services.StudentService;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author pola-nasser13
 */
public class QuizPanel extends javax.swing.JPanel {
    private QuizService quizService;
    private StudentService studentService;
    private Student currentStudent;
    private Quiz currentQuiz;
    private ArrayList<Question> questions;
    private ArrayList<Integer> userAnswers;
    private int currentQuestionIndex;
    private int courseId;
    private int lessonId;
    private javax.swing.JPanel parentPanel; 
    /**
     * Creates new form QuizPanel
     */
    public QuizPanel(Student student, int courseId, int lessonId, javax.swing.JPanel parentPanel) {
        initComponents();
        this.currentStudent = student;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.parentPanel = parentPanel;
        this.studentService = new StudentService(student);
        initializeQuiz();
    }
private void initializeQuiz() {
        try {
            Course course = studentService.getCourse(courseId); 
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }

            if (!studentService.canAccessLesson(course, lessonId)) {
                JOptionPane.showMessageDialog(this, "You cannot access this lesson yet!");
                return;
            }

            Lesson lesson = course.getLessonById(lessonId);
            if (lesson == null) {
                JOptionPane.showMessageDialog(this, "Lesson not found!");
                return;
            }

            currentQuiz = lesson.getQuiz();
            questions = currentQuiz.getQuestions();
            
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions available for this quiz!");
                return;
            }

            quizService = new QuizService(currentQuiz, currentStudent, courseId, lessonId);
            userAnswers = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                userAnswers.add(-1);
            }

            currentQuestionIndex = 0;
            updateUI();
            loadQuestion(currentQuestionIndex);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing quiz: " + e.getMessage());
        }
    }

    private void loadQuestion(int questionIndex) {
        if (questionIndex < 0 || questionIndex >= questions.size()) {
            return;
        }

        Question question = questions.get(questionIndex);
        
        tfquestionNum.setText(String.valueOf(questionIndex + 1));
        tfnumOfQuestions.setText(String.valueOf(questions.size()));
        tfcourseId.setText(String.valueOf(courseId));
        tfLessonId.setText(String.valueOf(lessonId));
        
        tfQuestion.setText(question.getText());
        
        ArrayList<String> choices = question.getChoices();
        tfChoiceA.setText(choices.size() > 0 ? choices.get(0) : "");
        tfChoiceB.setText(choices.size() > 1 ? choices.get(1) : "");
        tfChoiceC.setText(choices.size() > 2 ? choices.get(2) : "");
        tfChoiceD.setText(choices.size() > 3 ? choices.get(3) : "");

        clearCheckboxes();
        int previousAnswer = userAnswers.get(questionIndex);
        if (previousAnswer != -1) {
            setCheckbox(previousAnswer);
        }

        updateNavigationButtons();
    }

    private void clearCheckboxes() {
        checkboxA.setState(false);
        checkboxB.setState(false);
        checkboxC.setState(false);
        checkboxD.setState(false);
    }

    private void setCheckbox(int choice) {
        switch (choice) {
            case 0: checkboxA.setState(true); break;
            case 1: checkboxB.setState(true); break;
            case 2: checkboxC.setState(true); break;
            case 3: checkboxD.setState(true); break;
        }
    }

    private void saveCurrentAnswer() {
        int selectedChoice = getSelectedChoice();
        userAnswers.set(currentQuestionIndex, selectedChoice);
    }

    private int getSelectedChoice() {
        if (checkboxA.getState()) return 0;
        if (checkboxB.getState()) return 1;
        if (checkboxC.getState()) return 2;
        if (checkboxD.getState()) return 3;
        return -1;
    }

    private void updateNavigationButtons() {
        btnPreviousQuestion.setEnabled(currentQuestionIndex > 0);
        btnNextQuestion1.setEnabled(currentQuestionIndex < questions.size() - 1);
    }

    public void updateUI() {
        int attempts = quizService.getQuizAttempts();
        jLabel7.setText("Attempt: " + attempts);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textArea1 = new java.awt.TextArea();
        jLabel1 = new javax.swing.JLabel();
        tfcourseId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfLessonId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnPreviousQuestion = new javax.swing.JButton();
        btnNextQuestion1 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        tfGotonumQuestion = new javax.swing.JTextField();
        btnGotoQuestionNum = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        checkboxD = new java.awt.Checkbox();
        tfChoiceD = new javax.swing.JTextField();
        tfQuestion = new javax.swing.JTextField();
        checkboxA = new java.awt.Checkbox();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        checkboxC = new java.awt.Checkbox();
        checkboxB = new java.awt.Checkbox();
        jLabel10 = new javax.swing.JLabel();
        tfChoiceA = new javax.swing.JTextField();
        tfChoiceB = new javax.swing.JTextField();
        tfChoiceC = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfnumOfQuestions = new javax.swing.JTextField();
        tfquestionNum = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();

        jLabel4.setText("jLabel4");

        jLabel7.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel7.setText("retry num");

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 48)); // NOI18N
        jLabel1.setText("QUIZ");

        tfcourseId.addActionListener(this::tfcourseIdActionPerformed);

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel3.setText("Course");

        jLabel6.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel6.setText("Lesson");

        jLabel8.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel8.setText("Question num:");

        btnPreviousQuestion.setText("Previous Question");
        btnPreviousQuestion.addActionListener(this::btnPreviousQuestionActionPerformed);

        btnNextQuestion1.setText("Next Question");
        btnNextQuestion1.addActionListener(this::btnNextQuestion1ActionPerformed);

        jLabel15.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel15.setText(" Go to Question num:");

        tfGotonumQuestion.addActionListener(this::tfGotonumQuestionActionPerformed);

        btnGotoQuestionNum.setText("Go");
        btnGotoQuestionNum.addActionListener(this::btnGotoQuestionNumActionPerformed);

        jLabel16.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel16.setText("Number of Questions:");

        tfChoiceD.addActionListener(this::tfChoiceDActionPerformed);

        tfQuestion.addActionListener(this::tfQuestionActionPerformed);

        jLabel12.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel12.setText("B)");

        jLabel14.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel14.setText("C)");

        jLabel13.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel13.setText("D)");

        jLabel11.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel11.setText("A)");

        jLabel10.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel10.setText("Choices:");

        tfChoiceA.addActionListener(this::tfChoiceAActionPerformed);

        tfChoiceB.addActionListener(this::tfChoiceBActionPerformed);

        tfChoiceC.addActionListener(this::tfChoiceCActionPerformed);

        jLabel9.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel9.setText("Question:");

        jDesktopPane1.setLayer(checkboxD, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfChoiceD, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfQuestion, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(checkboxA, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel12, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel14, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel13, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(checkboxC, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(checkboxB, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfChoiceA, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfChoiceB, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfChoiceC, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(30, 30, 30)
                        .addComponent(checkboxA, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfChoiceA, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(checkboxB, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfChoiceB, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(checkboxC, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfChoiceC, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(checkboxD, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfChoiceD, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tfChoiceA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfChoiceB, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel12)
                                        .addComponent(tfChoiceC, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(checkboxD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(checkboxC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(checkboxB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addComponent(checkboxA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tfChoiceD, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        tfnumOfQuestions.addActionListener(this::tfnumOfQuestionsActionPerformed);

        tfquestionNum.addActionListener(this::tfquestionNumActionPerformed);

        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(this::btnSubmitActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfGotonumQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGotoQuestionNum)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnPreviousQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNextQuestion1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tfcourseId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(43, 43, 43)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfLessonId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(104, 104, 104)
                                        .addComponent(jLabel1)
                                        .addGap(82, 82, 82)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tfnumOfQuestions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 29, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfquestionNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(406, 406, 406))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(368, 368, 368))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfcourseId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(tfLessonId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfnumOfQuestions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfquestionNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPreviousQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNextQuestion1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(tfGotonumQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGotoQuestionNum))
                .addGap(18, 18, 18)
                .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPreviousQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousQuestionActionPerformed
        saveCurrentAnswer();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion(currentQuestionIndex);
        }    }//GEN-LAST:event_btnPreviousQuestionActionPerformed

    private void btnNextQuestion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextQuestion1ActionPerformed
        saveCurrentAnswer();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        }    }//GEN-LAST:event_btnNextQuestion1ActionPerformed

    private void tfcourseIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfcourseIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfcourseIdActionPerformed

    private void tfnumOfQuestionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfnumOfQuestionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfnumOfQuestionsActionPerformed

    private void tfQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfQuestionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfQuestionActionPerformed

    private void tfChoiceAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceAActionPerformed

    private void tfChoiceBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceBActionPerformed

    private void tfquestionNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfquestionNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfquestionNumActionPerformed

    private void tfChoiceCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceCActionPerformed

    private void tfChoiceDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceDActionPerformed

    private void tfGotonumQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfGotonumQuestionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfGotonumQuestionActionPerformed

    private void btnGotoQuestionNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGotoQuestionNumActionPerformed
        try {
            saveCurrentAnswer();
            int questionNum = Integer.parseInt(tfGotonumQuestion.getText()) - 1;
            if (questionNum >= 0 && questionNum < questions.size()) {
                currentQuestionIndex = questionNum;
                loadQuestion(currentQuestionIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid question number! Must be between 1 and " + questions.size());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }    }//GEN-LAST:event_btnGotoQuestionNumActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        saveCurrentAnswer();

        if (!areAllQuestionsAnswered()) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Some questions are unanswered. Submit anyway?", 
                "Confirm Submission", 
                JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            double score = quizService.calculateScore(userAnswers);
            boolean passed = quizService.isPassed(score);
            int attempts = quizService.getQuizAttempts();
            showQuizResults(score, passed, attempts);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error submitting quiz: " + e.getMessage());
        }    }//GEN-LAST:event_btnSubmitActionPerformed
    private void showQuizResults(double score, boolean passed, int attempts) {
        SumbitQuiz resultsPanel = new SumbitQuiz(score, passed, attempts, questions, userAnswers);
        
        if (parentPanel != null) {
            parentPanel.removeAll();
            parentPanel.add(resultsPanel);
            parentPanel.revalidate();
            parentPanel.repaint();
        }
    }

    private boolean areAllQuestionsAnswered() {
        for (int answer : userAnswers) {
            if (answer == -1) {
                return false;
            }
        }
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGotoQuestionNum;
    private javax.swing.JButton btnNextQuestion1;
    private javax.swing.JButton btnPreviousQuestion;
    private javax.swing.JButton btnSubmit;
    private java.awt.Checkbox checkboxA;
    private java.awt.Checkbox checkboxB;
    private java.awt.Checkbox checkboxC;
    private java.awt.Checkbox checkboxD;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private java.awt.TextArea textArea1;
    private javax.swing.JTextField tfChoiceA;
    private javax.swing.JTextField tfChoiceB;
    private javax.swing.JTextField tfChoiceC;
    private javax.swing.JTextField tfChoiceD;
    private javax.swing.JTextField tfGotonumQuestion;
    private javax.swing.JTextField tfLessonId;
    private javax.swing.JTextField tfQuestion;
    private javax.swing.JTextField tfcourseId;
    private javax.swing.JTextField tfnumOfQuestions;
    private javax.swing.JTextField tfquestionNum;
    // End of variables declaration//GEN-END:variables
}
