/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Frontend;

import Backend.Database.CourseDatabase;
import Backend.Models.Course;
import Backend.Models.Instructor;
import Backend.Models.Lesson;
import Backend.Models.Question;
import Backend.Models.Quiz;
import Backend.Services.InstructorService;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pola-nasser13
 */
public class CreateQuiz extends javax.swing.JPanel {

    /**
     * Creates new form CreateQuiz
     */

    private Instructor instructor;
    private InstructorService instructorService;
    private CourseDatabase courseDatabase;
    private Course selectedCourse;
    private Lesson selectedLesson;
    private ArrayList<Question> questionsList;
    private DefaultTableModel courseTableModel;
    private DefaultTableModel lessonTableModel;
    private DefaultTableModel questionTableModel;

    public CreateQuiz() {
        initComponents();
    }

    public CreateQuiz(Instructor instructor) {
        this.instructor = instructor;
        this.instructorService = new InstructorService(instructor);
        this.courseDatabase = new CourseDatabase("courses.json");
        this.questionsList = new ArrayList<>();
        initComponents();
        initializeTables();
        loadInstructorCourses();
    }

    private void initializeTables() {
        courseTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"courseId", "courseName", "Status"}
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        courseTable.setModel(courseTableModel);

        lessonTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"lessonId", "lessonName"}
        );
        lessonTable.setModel(lessonTableModel);

        questionTableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"questionId", "question", "Choices", "CorrectAnswer"}
        );
        questionTable.setModel(questionTableModel);
    }

    private void loadInstructorCourses() {
        courseTableModel.setRowCount(0);
        ArrayList<Integer> courseIds = instructor.getCreatedCourseIds();

        for (Integer courseId : courseIds) {
            Course course = courseDatabase.getCourseById(courseId);
            if (course != null && course.getApprovalStatus().toString().equals("APPROVED")) {
                courseTableModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getTitle(),
                    course.getApprovalStatus().toString()
                });
            }
        }
    }

    private void loadLessonsForCourse(int courseId) {
        lessonTableModel.setRowCount(0);
        questionTableModel.setRowCount(0);
        questionsList.clear();
        clearQuestionForm();

        selectedCourse = courseDatabase.getCourseById(courseId);
        if (selectedCourse != null) {
            ArrayList<Lesson> lessons = selectedCourse.getLessons();
            for (Lesson lesson : lessons) {
                lessonTableModel.addRow(new Object[]{
                    lesson.getLessonId(),
                    lesson.getTitle()
                });
            }
        }
    }

    private void loadQuestionsForLesson(int lessonId) {
        questionTableModel.setRowCount(0);
        questionsList.clear();
        clearQuestionForm();

        if (selectedCourse != null) {
            selectedLesson = selectedCourse.getLessonById(lessonId);
            if (selectedLesson != null && selectedLesson.hasQuiz()) {
                Quiz quiz = selectedLesson.getQuiz();
                if (quiz != null) {
                    ArrayList<Question> questions = quiz.getQuestions();
                    questionsList.addAll(questions);

                    for (Question question : questions) {
                        String choices = formatChoices(question.getChoices());
                        String correctAnswer = getChoiceLetter(question.getCorrectChoice());
                        questionTableModel.addRow(new Object[]{
                            question.getQuestionId(),
                            question.getText(),
                            choices,
                            correctAnswer
                        });
                    }
                }
            }
        }
    }

    private String formatChoices(ArrayList<String> choices) {
        StringBuilder sb = new StringBuilder();
        char letter = 'A';
        for (String choice : choices) {
            sb.append(letter).append(") ").append(choice).append("  ");
            letter++;
        }
        return sb.toString();
    }

    private String getChoiceLetter(int choiceIndex) {
        return String.valueOf((char) ('A' + choiceIndex));
    }

    private int getChoiceIndex(char choiceLetter) {
        return choiceLetter - 'A';
    }

    private void clearQuestionForm() {
        tfQuestionId.setText("");
        tfQuestion.setText("");
        tfChoiceA.setText("");
        tfChoiceB.setText("");
        tfChoiceC.setText("");
        tfChoiceD.setText("");
        checkboxA.setState(false);
        checkbox1.setState(false);
        checkboxC.setState(false);
        checkboxD.setState(false);
    }

    private void addQuestion() {
        try {
            if (tfQuestionId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Question ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tfQuestion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Question text", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tfChoiceA.getText().trim().isEmpty() || tfChoiceB.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "At least choices A and B are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int correctChoice = -1;
            if (checkboxA.getState()) {
                correctChoice = 0;
            } else if (checkbox1.getState()) {
                correctChoice = 1;
            } else if (checkboxC.getState()) {
                correctChoice = 2;
            } else if (checkboxD.getState()) {
                correctChoice = 3;
            }

            if (correctChoice == -1) {
                JOptionPane.showMessageDialog(this, "Please select the correct answer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!tfChoiceC.getText().trim().isEmpty() && correctChoice == 2) {
            } else if (!tfChoiceD.getText().trim().isEmpty() && correctChoice == 3) {
            } else if (correctChoice >= 2) {
                if ((correctChoice == 2 && tfChoiceC.getText().trim().isEmpty())
                        || (correctChoice == 3 && tfChoiceD.getText().trim().isEmpty())) {
                    JOptionPane.showMessageDialog(this, "Selected correct answer choice cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int questionId = Integer.parseInt(tfQuestionId.getText().trim());

            for (Question q : questionsList) {
                if (q.getQuestionId() == questionId) {
                    JOptionPane.showMessageDialog(this, "Question ID already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            ArrayList<String> choices = new ArrayList<>();
            choices.add(tfChoiceA.getText().trim());
            choices.add(tfChoiceB.getText().trim());
            if (!tfChoiceC.getText().trim().isEmpty()) {
                choices.add(tfChoiceC.getText().trim());
            }
            if (!tfChoiceD.getText().trim().isEmpty()) {
                choices.add(tfChoiceD.getText().trim());
            }

            if (correctChoice >= choices.size()) {
                JOptionPane.showMessageDialog(this, "Correct choice is out of range", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Question newQuestion = new Question(questionId, tfQuestion.getText().trim(), choices, correctChoice);
            questionsList.add(newQuestion);

            String choicesText = formatChoices(choices);
            String correctAnswerText = getChoiceLetter(correctChoice);
            questionTableModel.addRow(new Object[]{
                questionId,
                tfQuestion.getText().trim(),
                choicesText,
                correctAnswerText
            });

            clearQuestionForm();
            JOptionPane.showMessageDialog(this, "Question added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Question ID must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeQuestion() {
        int selectedRow = questionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question to remove", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int questionId = (Integer) questionTableModel.getValueAt(selectedRow, 0);

        for (int i = 0; i < questionsList.size(); i++) {
            if (questionsList.get(i).getQuestionId() == questionId) {
                questionsList.remove(i);
                break;
            }
        }

        questionTableModel.removeRow(selectedRow);
        JOptionPane.showMessageDialog(this, "Question removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveQuiz() {
        if (selectedLesson == null) {
            JOptionPane.showMessageDialog(this, "Please select a lesson first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (questionsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one question to the quiz", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quizId = selectedLesson.getLessonId();

            boolean success;
            if (selectedLesson.hasQuiz()) {
                success = instructorService.updateQuiz(
                        selectedCourse.getCourseId(),
                        selectedLesson.getLessonId(),
                        quizId,
                        questionsList
                );
            } else {
                success = instructorService.addQuizToLesson(
                        selectedCourse.getCourseId(),
                        selectedLesson.getLessonId(),
                        quizId,
                        questionsList
                );
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Quiz saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save quiz", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving quiz: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        courseTable = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        lessonTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfChoiceB = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnAddQuestion = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        checkboxA = new java.awt.Checkbox();
        checkbox1 = new java.awt.Checkbox();
        checkboxC = new java.awt.Checkbox();
        checkboxD = new java.awt.Checkbox();
        jLabel5 = new javax.swing.JLabel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        tfChoiceA = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfQuestion = new javax.swing.JTextField();
        tfChoiceC = new javax.swing.JTextField();
        tfChoiceD = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        tfQuestionId = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        questionTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        label1 = new java.awt.Label();
        btnRemoveQuestion = new javax.swing.JButton();
        btnSaveQuiz = new javax.swing.JToggleButton();

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel3.setText("Select Lesson:");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        courseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "courseId", "courseName"
            }
        ));
        jScrollPane2.setViewportView(courseTable);

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lessonTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "lessonId", "lessonName"
            }
        ));
        jScrollPane1.setViewportView(lessonTable);

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        jLabel2.setText("Select Course:");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel9.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel9.setText("D)");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel4.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel4.setText("Add Choices:");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tfChoiceB.addActionListener(this::tfChoiceBActionPerformed);

        jLabel8.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel8.setText("B)");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnAddQuestion.setText("Add Question");
        btnAddQuestion.addActionListener(this::btnAddQuestionActionPerformed);

        jLabel10.setText("Correct Answer:");

        checkboxA.setLabel("A");

        checkbox1.setLabel("B");

        checkboxC.setLabel("C");

        checkboxD.setLabel("D");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkboxA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(checkbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(checkboxC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkboxD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAddQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(btnAddQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkboxA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkbox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkboxD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jLabel5.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel5.setText("Add Question:");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tfChoiceA.addActionListener(this::tfChoiceAActionPerformed);

        jLabel7.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel7.setText("A)");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLayeredPane2.setLayer(tfChoiceA, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(tfChoiceA, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(tfChoiceA, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel6.setText("C)");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tfQuestion.addActionListener(this::tfQuestionActionPerformed);

        tfChoiceC.addActionListener(this::tfChoiceCActionPerformed);

        tfChoiceD.addActionListener(this::tfChoiceDActionPerformed);

        jLabel12.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel12.setText("QuestionId:");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tfQuestionId.addActionListener(this::tfQuestionIdActionPerformed);

        jDesktopPane1.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfChoiceB, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLayeredPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfQuestion, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfChoiceC, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfChoiceD, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel12, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(tfQuestionId, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDesktopPane1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel4))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDesktopPane1Layout.createSequentialGroup()
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tfQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 712, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(tfChoiceC, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(90, 90, 90)
                                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel9))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(tfChoiceB)
                                            .addComponent(tfChoiceD, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                        .addComponent(tfQuestionId, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(48, 48, 48)))))
                        .addContainerGap())
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addGap(80, 80, 80))))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12))
                .addGap(14, 14, 14)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfQuestionId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tfChoiceB, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tfChoiceD, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDesktopPane1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tfChoiceC, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        questionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "questionId", "question", "Choices", "CorrectAnswer"
            }
        ));
        jScrollPane3.setViewportView(questionTable);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 36)); // NOI18N
        jLabel1.setText("Make Quiz");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        label1.setForeground(new java.awt.Color(255, 0, 0));
        label1.setText("To remove Question, Select Question and press remove question button:");

        btnRemoveQuestion.setText("remove question");
        btnRemoveQuestion.addActionListener(this::btnRemoveQuestionActionPerformed);

        btnSaveQuiz.setText("Save Quiz");
        btnSaveQuiz.addActionListener(this::btnSaveQuizActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(111, 111, 111)
                                .addComponent(btnRemoveQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(btnSaveQuiz, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(btnSaveQuiz, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tfQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfQuestionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfQuestionActionPerformed

    private void btnAddQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddQuestionActionPerformed
        addQuestion();    }//GEN-LAST:event_btnAddQuestionActionPerformed

    private void tfQuestionIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfQuestionIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfQuestionIdActionPerformed

    private void tfChoiceAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceAActionPerformed

    private void tfChoiceBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceBActionPerformed

    private void tfChoiceCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceCActionPerformed

    private void tfChoiceDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfChoiceDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfChoiceDActionPerformed

    private void btnRemoveQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveQuestionActionPerformed
        removeQuestion();    }//GEN-LAST:event_btnRemoveQuestionActionPerformed

    private void btnSaveQuizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveQuizActionPerformed
        saveQuiz();    }//GEN-LAST:event_btnSaveQuizActionPerformed

    private void courseTableMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow != -1) {
            int courseId = (Integer) courseTableModel.getValueAt(selectedRow, 0);
            loadLessonsForCourse(courseId);
        }
    }

    private void lessonTableMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = lessonTable.getSelectedRow();
        if (selectedRow != -1) {
            int lessonId = (Integer) lessonTableModel.getValueAt(selectedRow, 0);
            loadQuestionsForLesson(lessonId);
        }
    }

    private void checkboxAItemStateChanged(java.awt.event.ItemEvent evt) {
        if (checkboxA.getState()) {
            checkbox1.setState(false);
            checkboxC.setState(false);
            checkboxD.setState(false);
        }
    }

    private void checkbox1ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (checkbox1.getState()) {
            checkboxA.setState(false);
            checkboxC.setState(false);
            checkboxD.setState(false);
        }
    }

    private void checkboxCItemStateChanged(java.awt.event.ItemEvent evt) {
        if (checkboxC.getState()) {
            checkboxA.setState(false);
            checkbox1.setState(false);
            checkboxD.setState(false);
        }
    }

    private void checkboxDItemStateChanged(java.awt.event.ItemEvent evt) {
        if (checkboxD.getState()) {
            checkboxA.setState(false);
            checkbox1.setState(false);
            checkboxC.setState(false);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddQuestion;
    private javax.swing.JButton btnRemoveQuestion;
    private javax.swing.JToggleButton btnSaveQuiz;
    private java.awt.Checkbox checkbox1;
    private java.awt.Checkbox checkboxA;
    private java.awt.Checkbox checkboxC;
    private java.awt.Checkbox checkboxD;
    private javax.swing.JTable courseTable;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private java.awt.Label label1;
    private javax.swing.JTable lessonTable;
    private javax.swing.JTable questionTable;
    private javax.swing.JTextField tfChoiceA;
    private javax.swing.JTextField tfChoiceB;
    private javax.swing.JTextField tfChoiceC;
    private javax.swing.JTextField tfChoiceD;
    private javax.swing.JTextField tfQuestion;
    private javax.swing.JTextField tfQuestionId;
    // End of variables declaration//GEN-END:variables
}
