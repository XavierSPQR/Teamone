/*
 * TeamOne Final Project - ClassRoom Management System
 * Jayodya R, Bimalka S, Madushi K, Inusha M, Kaveesha P, Tharanga D
 */
package Interfaces;

import DataBase.databaseConnection;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import net.proteanit.sql.DbUtils;
import java.sql.SQLException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 *
 * @author TharangaD
 */
public class HomeScreen extends javax.swing.JFrame {

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    String Filename = null;
    byte[] PersonImage = null;
    URL iconUrl = JOptionPane.class.getResource("/resources/deletesuccess.png");
    Icon DeleteSuccessIcon = new ImageIcon(iconUrl);
    URL iconUrl2 = JOptionPane.class.getResource("/resources/success.png");
    Icon successIcon = new ImageIcon(iconUrl2);

    /**
     * Creates new form HomeScreen
     */
    public HomeScreen() {
        initComponents();
        lblWarning.setVisible(false);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            gd.setFullScreenWindow(this);
        }
        conn = databaseConnection.connection();
        FetchDate();
        FetchTime();
        FetchCombo();
        ShowStudents();
        fetchSemester();
        FetchAssignmentNumber();
    }

    void FetchDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd' of' MMMM yyyy");
        String formattedDate = currentDate.format(customFormatter);
        txtDateTime.setText(formattedDate);
    }

    public void FetchTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.Date d = new java.util.Date();
                SimpleDateFormat form = new SimpleDateFormat("hh:mm:ss a");
                txtDateTime2.setText(form.format(d));
            }
        }
        ).start();
    }

    public void FetchCombo() {
        try {
            String sql3 = "SELECT * FROM batchdetails";
            st = conn.prepareStatement(sql3);
            rs = st.executeQuery(sql3);

            while (rs.next()) {
                String batchname = rs.getString("batch");
                cmbSelectBatch.addItem(batchname);
                cmbSelectBatch1.addItem(batchname);
                cmbSelectBatchExamResult.addItem(batchname);
                 cmbSelectBatchOJT.addItem(batchname);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void AddNewBatch() {
        String userInput = JOptionPane.showInputDialog(null, "Enter New Batch:", "Add New Batch", JOptionPane.PLAIN_MESSAGE);
        if (userInput != null && !userInput.isEmpty()) {
            try {
                st = conn.createStatement();
                String sql2 = "DELETE FROM batchdetails";
                st.executeUpdate(sql2);
                DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbSelectBatch.getModel();
                model.insertElementAt(userInput, 1);
                for (int i = 0; i < model.getSize(); i++) {
                    String sql = "INSERT INTO batchdetails(batch) VALUES ('" + model.getElementAt(i) + "')";
                    st.executeUpdate(sql);
                }
                JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                cmbSelectBatch.setSelectedIndex(0);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Details entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ShowStudents() {
        try {
            String batch = cmbSelectBatch.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT  misl2 AS 'MIS-Last two digits', namewithin AS 'Name With Initials' FROM students WHERE batch='" + batch + "'";
            ResultSet rs = st.executeQuery(sql);
            StudentsTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public void ShowStudentsAssignment() {
        try {
            String batch = cmbSelectBatch1.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT  misl2 AS 'MIS-Last two digits', namewithin AS 'Name With Initials' FROM students WHERE batch='" + batch + "'";
            ResultSet rs = st.executeQuery(sql);
            tableStudentAssignments.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public void ShowStudentsExamResult() {
        try {
            String batch = cmbSelectBatchExamResult.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT  misl2 AS 'MIS-Last two digits', namewithin AS 'Name With Initials' FROM students WHERE batch='" + batch + "'";
            ResultSet rs = st.executeQuery(sql);
            tableStudentExamResults.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void fetchSemester()
    {
        try {
            String sql3 = "SELECT * FROM semesternumber";
            st = conn.prepareStatement(sql3);
            rs = st.executeQuery(sql3);
            cmbSemesterNumber.removeAllItems();
            while (rs.next()) {
                String semesternumber = rs.getString("semesterno");
               cmbSemesterNumber.addItem(semesternumber);
               cmbSemesterNumberExamResult.addItem(semesternumber);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    void AddNewSemester() {
        String userInput = JOptionPane.showInputDialog(null, "Enter New Semester:", "Add New Semester", JOptionPane.PLAIN_MESSAGE);
        if (userInput != null && !userInput.isEmpty()) {
            try {
                st = conn.createStatement();
                String sql2 = "DELETE FROM semesternumber";
                st.executeUpdate(sql2);
                DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbSemesterNumber.getModel();
                model.insertElementAt(userInput, 1);
                for (int i = 0; i < model.getSize(); i++) {
                    String sql = "INSERT INTO semesternumber(semesterno) VALUES ('" + model.getElementAt(i) + "')";
                    st.executeUpdate(sql);
                }
                JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                cmbSemesterNumber.setSelectedIndex(0);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Details entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void fetchSubjects() {
        try {
            String semesterno = cmbSemesterNumber.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT  subjectname FROM subjectdetails WHERE semesterno='" + semesterno + "'";
            ResultSet rs = st.executeQuery(sql);
            cmbSelectSubject.removeAllItems();
             while (rs.next()) {
                String subjectname = rs.getString("subjectname");
               cmbSelectSubject.addItem(subjectname);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
void AddNewSubject() {
        String userInput = JOptionPane.showInputDialog(null, "Enter New Subject:", "Add New Subject", JOptionPane.PLAIN_MESSAGE);
        if (userInput != null && !userInput.isEmpty()) {
            try {
                String semesterno = cmbSemesterNumber.getSelectedItem().toString();
                st = conn.createStatement();
                String sql2 = "DELETE FROM subjectdetails";
                st.executeUpdate(sql2);
                DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbSelectSubject.getModel();
                model.insertElementAt(userInput, 1);
                for (int i = 0; i < model.getSize(); i++) {
                    String sql = "INSERT INTO subjectdetails(semesterno,subjectname) VALUES ('" +semesterno+ "','" + model.getElementAt(i) + "')";
                    st.executeUpdate(sql);
                }
                JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                cmbSemesterNumber.setSelectedIndex(0);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Details entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
public void FetchAssignmentNumber() {
        try {
            String sql3 = "SELECT * FROM assignmentnumber";
            st = conn.prepareStatement(sql3);
            rs = st.executeQuery(sql3);

            while (rs.next()) {
                String assignmentno = rs.getString("assignmentno");
                cmbSelectAssignmentNumber.addItem(assignmentno);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
void AddNewAssignmentNumber() {
        String userInput = JOptionPane.showInputDialog(null, "Enter New Assignment Number:", "Add New Number", JOptionPane.PLAIN_MESSAGE);
        if (userInput != null && !userInput.isEmpty()) {
            try {
                st = conn.createStatement();
                String sql2 = "DELETE FROM assignmentnumber";
                st.executeUpdate(sql2);
                DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbSelectAssignmentNumber.getModel();
                model.insertElementAt(userInput, 1);
                for (int i = 0; i < model.getSize(); i++) {
                    String sql = "INSERT INTO assignmentnumber(assignmentno) VALUES ('" + model.getElementAt(i) + "')";
                    st.executeUpdate(sql);
                }
                JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                cmbSelectAssignmentNumber.setSelectedIndex(0);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Details entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
private void showStudentNo(){
    try {
            String batch = cmbSelectBatch1.getSelectedItem().toString();
            lblShowBatch.setText(batch);
            st = conn.createStatement();
            String sql = "SELECT COUNT(*) FROM students WHERE batch='" + batch + "'";
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                int count = rs.getInt(1);
                lblNumberOfStudents.setText("Number of Students:"+count);
            }        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
}
private void LoadSubject(){
            if(cmbSemesterNumber.getSelectedIndex()!=0){
                lblShowSemester.setText("Semester "+cmbSemesterNumber.getSelectedItem().toString());
                lblShowSubject.setText(cmbSelectSubject.getSelectedItem().toString());
            }
}
private void LoadClassReport(){
    if(cmbSemesterNumber.getSelectedIndex()!=0){       
    try {
        st = conn.createStatement();
        String subject=cmbSelectSubject.getSelectedItem().toString();
         String sql = "SELECT MAX(assignmentnumber) FROM assignments WHERE subject='" + subject + "'";
         ResultSet rs = st.executeQuery(sql);
         if(rs.next()){
                int count = rs.getInt(1);
                lblAssignmentCount.setText(Integer.toString(count));
            } 
         Statement st2 = conn.createStatement();
         String sqlAverage = "SELECT AVG(marks) FROM assignments WHERE subject='" + subject + "'";
         ResultSet rsAverage = st2.executeQuery(sqlAverage);
         if(rsAverage.next()){
                int Average = rsAverage.getInt(1);
                lblShowAverage.setText(Integer.toString(Average));
            } 
         Statement st3 = conn.createStatement();
         String sqlClassMax = "SELECT MAX(marks) FROM assignments WHERE subject='" + subject + "'";
         ResultSet rsClassMax = st3.executeQuery(sqlClassMax);
         if(rsClassMax.next()){
                int ClassMax = rsClassMax.getInt(1);
                lblShowClassMaximumAssignment.setText(Integer.toString(ClassMax));
            } 
         Statement st4 = conn.createStatement();
         String sqlClassMin = "SELECT MIN(marks) FROM assignments WHERE subject='" + subject + "'";
         ResultSet rsClassMin = st4.executeQuery(sqlClassMin);
         if(rsClassMin.next()){
                int ClassMin = rsClassMin.getInt(1);
                lblShowClassMinimumAssignment.setText(Integer.toString(ClassMin));
            }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
    }
}
private void LoadStudentReport(){      
    try {
        if(cmbSemesterNumber.getSelectedIndex()!=0){
        String Batch=cmbSelectBatch1.getSelectedItem().toString();
        int R=tableStudentAssignments.getSelectedRow();
        String MisL2=tableStudentAssignments.getValueAt(R, 0).toString();
        st = conn.createStatement();
        String subject=cmbSelectSubject.getSelectedItem().toString();
         String sql = "SELECT MAX(marks) FROM assignments WHERE subject='" + subject + "' && mis='" + MisL2 + "' && batch='" + Batch + "'";
         ResultSet rs = st.executeQuery(sql);
         if(rs.next()){
                int count = rs.getInt(1);
                lblShowStudentMaximum2.setText(Integer.toString(count));
            } 
         Statement st2 = conn.createStatement();
         String sqlAverage = "SELECT AVG(marks) FROM assignments WHERE subject='" + subject + "'&& mis='" + MisL2 + "' && batch='" + Batch + "'";
         ResultSet rsAverage = st2.executeQuery(sqlAverage);
         if(rsAverage.next()){
                int Average = rsAverage.getInt(1);
                lblShowStudentAverage.setText(Integer.toString(Average));
            } 
         Statement st3 = conn.createStatement();
          String sqlMin = "SELECT MIN(marks) FROM assignments WHERE subject='" + subject + "' && mis='" + MisL2 + "' && batch='" + Batch + "'";
         ResultSet rsMin = st3.executeQuery(sqlMin);
         if(rsMin.next()){
                int studentMin = rsMin.getInt(1);
                lblShowStudentMinimum.setText(Integer.toString(studentMin));
            } 
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
    }
public void ShowStudentsRecordAssignments() {
    if(cmbSemesterNumber.getSelectedIndex()!=0)
    {
    try {
            String batch = cmbSelectBatch1.getSelectedItem().toString();
            String subject = cmbSelectSubject.getSelectedItem().toString();
            int R=tableStudentAssignments.getSelectedRow();
            String MisL2=tableStudentAssignments.getValueAt(R, 0).toString();
            st = conn.createStatement();
            String sql = "SELECT  assignmentnumber AS 'ASSIGNMENT NUMBER', marks AS 'MARKS' "
                    + "FROM assignments "
                    + "WHERE batch='" + batch + "' && mis='" +MisL2 + "' && subject='" +subject + "'";
            ResultSet rs = st.executeQuery(sql);
            tableAssignmentStudentRecord.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    }
public void ShowClassRecordAssignments() {
    if(cmbSemesterNumber.getSelectedIndex()!=0)
    {
    try {
            String batch = cmbSelectBatch1.getSelectedItem().toString();
            String subject = cmbSelectSubject.getSelectedItem().toString();
            String assignmentNumber=cmbSelectAssignmentNumber.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT  mis AS 'MIS', marks AS 'MARKS' "
                    + "FROM assignments "
                    + "WHERE batch='" + batch + "' && subject='" +subject + "'"
                    + "&& assignmentnumber='" +assignmentNumber + "'";
            ResultSet rs = st.executeQuery(sql);
            tableAssignmentClassRecord.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    }
// ExamResults==========================(Methods used explicitly in ExamResults JPanel)

public void fetchSubjectsExamResult() {
        try {
            String semesterno = cmbSemesterNumberExamResult.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT  subjectname FROM subjectdetails WHERE semesterno='" + semesterno + "'";
            ResultSet rs = st.executeQuery(sql);
            cmbSelectSubjectExamResult.removeAllItems();
             while (rs.next()) {
                String subjectname = rs.getString("subjectname");
               cmbSelectSubjectExamResult.addItem(subjectname);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

public void ShowStudentsRecordExamResults() {
        try {
            String batch = cmbSelectBatchExamResult.getSelectedItem().toString();
            int R=tableStudentExamResults.getSelectedRow();
            String MisL2=tableStudentExamResults.getValueAt(R, 0).toString();
            st = conn.createStatement();
            String sql = "SELECT  subject AS 'SUBJECT', result AS 'RESULT' FROM examresults WHERE batch='" + batch + "' && misl2='" +MisL2 + "'";
            ResultSet rs = st.executeQuery(sql);
            tableStudentRecordExamResults.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
public void ShowClassRecordExamResults(){
    if(cmbSemesterNumberExamResult.getSelectedIndex()!=0)
    {
    try {
            String batch = cmbSelectBatchExamResult.getSelectedItem().toString();
            String subject=cmbSelectSubjectExamResult.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT  misl2 AS 'MIS', result AS 'RESULT' FROM examresults WHERE batch='" + batch + "' && subject='" +subject + "'";
            ResultSet rs = st.executeQuery(sql);
            tableStudentClassRecords.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
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

        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel()

        {
            public void paintComponent(Graphics g)
            {
                ImageIcon im = new ImageIcon("homeone.jpg");
                Image i=im.getImage();
                g.drawImage(i,0,0,this.getSize().width,this.getSize().height,this);
            }
        }

        ;
        jLabel1 = new javax.swing.JLabel();
        txtDateTime = new javax.swing.JLabel();
        txtDateTime2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel()

        {
            public void paintComponent(Graphics g)
            {
                ImageIcon im = new ImageIcon("Students.jpg");
                Image i=im.getImage();
                g.drawImage(i,0,0,this.getSize().width,this.getSize().height,this);
            }
        }

        ;
        cmbSelectBatch = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        StudentsTable = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        edtMIS = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblMisL2 = new javax.swing.JLabel();
        lblWarning = new javax.swing.JLabel();
        edtName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        edtNameWithInit = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edtDOB = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        lblAge = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        edtNIC = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        edtPhone = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        edtAddress = new javax.swing.JTextField();
        cmbHighestQual = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        edtGuardian = new javax.swing.JTextField();
        cmbGender = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        edtEmail = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        edtGuardianPhone = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel()

        {
            public void paintComponent(Graphics g)
            {
                ImageIcon im = new ImageIcon("Students.jpg");
                Image i=im.getImage();
                g.drawImage(i,0,0,this.getSize().width,this.getSize().height,this);
            }
        }

        ;
        jScrollPane2 = new javax.swing.JScrollPane();
        tableStudentAssignments = new javax.swing.JTable();
        cmbSelectBatch1 = new javax.swing.JComboBox<>();
        lblImage1 = new javax.swing.JLabel();
        cmbSelectSubject = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        cmbSemesterNumber = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        cmbSelectAssignmentNumber = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();
        txtAssignmentMarks = new javax.swing.JTextField();
        btnSave1 = new javax.swing.JButton();
        panelAssignmentsHeader = new javax.swing.JPanel();
        lblShowBatch = new javax.swing.JLabel();
        lblNumberOfStudents = new javax.swing.JLabel();
        lblShowSemester = new javax.swing.JLabel();
        lblShowSubject = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblAssignmentCount = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblShowAverage = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblShowStudentAverage = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblShowClassMaximumAssignment = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblShowClassMinimumAssignment = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblShowStudentMaximum2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblShowStudentMinimum = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableAssignmentStudentRecord = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableAssignmentClassRecord = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        cmbSelectBatchExamResult = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableStudentExamResults = new javax.swing.JTable();
        lblImageExamResult = new javax.swing.JLabel();
        cmbSemesterNumberExamResult = new javax.swing.JComboBox<>();
        cmbSelectSubjectExamResult = new javax.swing.JComboBox<>();
        cmbSelectResultExamResults = new javax.swing.JComboBox<>();
        btnSaveExamResult = new javax.swing.JButton();
        btnDeleteExamResults = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableStudentRecordExamResults = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableStudentClassRecords = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        cmbSelectBatchOJT = new javax.swing.JComboBox<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableStudentOJT = new javax.swing.JTable();
        lblImageOJT = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home - ClassRoom Management System");

        jPanel2.setBackground(new java.awt.Color(204, 51, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 51, 0));
        jLabel1.setText("Welcome");

        txtDateTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtDateTime.setForeground(new java.awt.Color(204, 51, 0));
        txtDateTime.setText("jLabel2");

        txtDateTime2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtDateTime2.setForeground(new java.awt.Color(204, 51, 0));
        txtDateTime2.setText("jLabel2");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 830, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDateTime2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDateTime2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(533, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Home", jPanel3);

        cmbSelectBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSelectBatchActionPerformed(evt);
            }
        });

        StudentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "MIS-Last two digits", "Name With Initials"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        StudentsTable.setGridColor(new java.awt.Color(255, 255, 0));
        StudentsTable.getTableHeader().setReorderingAllowed(false);
        StudentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentsTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                StudentsTableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(StudentsTable);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/save.png"))); // NOI18N
        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/delete.png"))); // NOI18N
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));

        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setText("MIS");

        edtMIS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                edtMISKeyReleased(evt);
            }
        });

        jLabel3.setText("NAME");

        lblMisL2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMisL2.setText("L2");
        lblMisL2.setOpaque(true);

        lblWarning.setForeground(new java.awt.Color(255, 0, 0));
        lblWarning.setText("jLabel5");

        jLabel6.setText("NAME WITH INITIALS");

        jLabel7.setText("DATE OF BIRTH");

        edtDOB.setDateFormatString("yyyy-MM-dd");

        jLabel8.setText("AGE");

        lblAge.setBackground(new java.awt.Color(204, 255, 204));

        jLabel9.setText("NIC");

        jLabel10.setText("PHONE");

        jLabel11.setText("ADDRESS");

        cmbHighestQual.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Highest Qualification", "NVQ LEVEL 4 in ICT", "NVQ LEVEL 4 in Computer Hardware Technician", "Advanced Level" }));

        jLabel12.setText("NAME OF GUARDIAN");

        cmbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Gender", "Male", "Female" }));

        jLabel13.setText("EMAIL");

        jLabel14.setText("PHONE");

        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("-");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(jLabel7)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel8))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(lblWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addComponent(edtMIS)
                                                            .addComponent(edtNameWithInit))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lblMisL2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addGap(0, 0, Short.MAX_VALUE)))
                                                .addGap(7, 7, 7))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(edtNIC, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbHighestQual, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addGap(87, 87, 87))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(edtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(8, 8, 8)))))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(edtAddress)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(edtDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 286, Short.MAX_VALUE)
                                .addComponent(lblAge, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(72, 72, 72))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(edtGuardian, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtGuardianPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel13)
                                    .addComponent(edtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edtName))
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(cmbSelectBatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelete))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(160, 160, 160)
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSelectBatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblWarning)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMisL2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edtMIS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNameWithInit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(edtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edtDOB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbHighestQual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtGuardian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edtGuardianPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 146, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Students", jPanel4);

        tableStudentAssignments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "MIS-Last two digits", "Name With Initials"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableStudentAssignments.setGridColor(new java.awt.Color(255, 255, 0));
        tableStudentAssignments.getTableHeader().setReorderingAllowed(false);
        tableStudentAssignments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableStudentAssignmentsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableStudentAssignmentsMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tableStudentAssignments);

        cmbSelectBatch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSelectBatch1ActionPerformed(evt);
            }
        });

        lblImage1.setBackground(new java.awt.Color(102, 255, 204));
        lblImage1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImage1MouseClicked(evt);
            }
        });

        cmbSelectSubject.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Subject" }));
        cmbSelectSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSelectSubjectActionPerformed(evt);
            }
        });

        jButton3.setText("-");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("+");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("-");
        jButton5.setToolTipText("Remove a subject");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        cmbSemesterNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterNumberActionPerformed(evt);
            }
        });

        jButton6.setText("+");
        jButton6.setToolTipText("Add a subject");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("-");
        jButton7.setToolTipText("Remove a subject");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("+");
        jButton8.setToolTipText("Add a subject");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        btnSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/save.png"))); // NOI18N
        btnSave1.setText("SAVE");
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });

        panelAssignmentsHeader.setBackground(new java.awt.Color(255, 153, 153));

        lblShowBatch.setBackground(new java.awt.Color(255, 255, 255));
        lblShowBatch.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblShowBatch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowBatch.setText("Show Batch");
        lblShowBatch.setOpaque(true);

        lblNumberOfStudents.setBackground(new java.awt.Color(204, 204, 204));
        lblNumberOfStudents.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNumberOfStudents.setText("Students");
        lblNumberOfStudents.setOpaque(true);

        lblShowSemester.setBackground(new java.awt.Color(204, 255, 204));
        lblShowSemester.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblShowSemester.setText("Semester");
        lblShowSemester.setOpaque(true);

        lblShowSubject.setBackground(new java.awt.Color(204, 204, 255));
        lblShowSubject.setFont(new java.awt.Font("Tahoma", 3, 20)); // NOI18N
        lblShowSubject.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowSubject.setText("jLabel5");
        lblShowSubject.setOpaque(true);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Assignments Given");

        lblAssignmentCount.setBackground(new java.awt.Color(255, 255, 153));
        lblAssignmentCount.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblAssignmentCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAssignmentCount.setText("9");
        lblAssignmentCount.setOpaque(true);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Class Average");

        lblShowAverage.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblShowAverage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowAverage.setText("100");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Student Average");

        lblShowStudentAverage.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblShowStudentAverage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowStudentAverage.setText("100");
        lblShowStudentAverage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Student Maximum");

        lblShowClassMaximumAssignment.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblShowClassMaximumAssignment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowClassMaximumAssignment.setText("100");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Class Maximum");

        lblShowClassMinimumAssignment.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblShowClassMinimumAssignment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowClassMinimumAssignment.setText("100");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Class Minimum");

        lblShowStudentMaximum2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblShowStudentMaximum2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowStudentMaximum2.setText("100");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Student Minimum");

        lblShowStudentMinimum.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblShowStudentMinimum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblShowStudentMinimum.setText("100");

        javax.swing.GroupLayout panelAssignmentsHeaderLayout = new javax.swing.GroupLayout(panelAssignmentsHeader);
        panelAssignmentsHeader.setLayout(panelAssignmentsHeaderLayout);
        panelAssignmentsHeaderLayout.setHorizontalGroup(
            panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                        .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(lblAssignmentCount, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblShowSubject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                                .addComponent(lblShowAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblShowStudentAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblShowClassMaximumAssignment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblShowStudentMaximum2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblShowClassMinimumAssignment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblShowStudentMinimum, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 20, Short.MAX_VALUE))
                    .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                        .addComponent(lblShowBatch, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblNumberOfStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblShowSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)))
                .addContainerGap())
        );
        panelAssignmentsHeaderLayout.setVerticalGroup(
            panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShowBatch, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumberOfStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblShowSemester, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAssignmentsHeaderLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAssignmentCount, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel19)))
                    .addComponent(lblShowSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAssignmentsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblShowAverage)
                        .addComponent(lblShowStudentAverage)
                        .addComponent(lblShowClassMaximumAssignment, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblShowClassMinimumAssignment, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblShowStudentMaximum2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblShowStudentMinimum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(204, 255, 255));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Student Record");

        tableAssignmentStudentRecord.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tableAssignmentStudentRecord);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(271, 271, 271))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane7)
                    .addContainerGap()))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(375, Short.MAX_VALUE)))
        );

        jPanel10.setBackground(new java.awt.Color(255, 204, 204));

        jLabel15.setText("Class Record");

        tableAssignmentClassRecord.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tableAssignmentClassRecord);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(256, 256, 256))
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtAssignmentMarks, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(48, 48, 48)
                        .addComponent(cmbSemesterNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(cmbSelectBatch1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addComponent(jButton7))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(cmbSelectSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(cmbSelectAssignmentNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelAssignmentsHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cmbSelectBatch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton4)
                                .addComponent(cmbSemesterNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(cmbSelectSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton7)
                            .addComponent(cmbSelectAssignmentNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAssignmentMarks, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lblImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(panelAssignmentsHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Assignments", jPanel6);

        cmbSelectBatchExamResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSelectBatchExamResultActionPerformed(evt);
            }
        });

        tableStudentExamResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "MIS-Last two digits", "Name With Initials"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableStudentExamResults.setGridColor(new java.awt.Color(255, 255, 0));
        tableStudentExamResults.getTableHeader().setReorderingAllowed(false);
        tableStudentExamResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableStudentExamResultsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableStudentExamResultsMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tableStudentExamResults);

        lblImageExamResult.setBackground(new java.awt.Color(102, 255, 204));
        lblImageExamResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageExamResultMouseClicked(evt);
            }
        });

        cmbSemesterNumberExamResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterNumberExamResultActionPerformed(evt);
            }
        });

        cmbSelectSubjectExamResult.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Subject" }));
        cmbSelectSubjectExamResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSelectSubjectExamResultActionPerformed(evt);
            }
        });

        cmbSelectResultExamResults.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Result", "C", "Repeat" }));

        btnSaveExamResult.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/save.png"))); // NOI18N
        btnSaveExamResult.setText("SAVE");
        btnSaveExamResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveExamResultActionPerformed(evt);
            }
        });

        btnDeleteExamResults.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/delete.png"))); // NOI18N
        btnDeleteExamResults.setText("DELETE");
        btnDeleteExamResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteExamResultsActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(204, 204, 255));

        jLabel20.setText("Student Record");

        tableStudentRecordExamResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tableStudentRecordExamResults);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addGap(191, 191, 191))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));

        jLabel21.setText("Class Record for this subject");

        tableStudentClassRecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tableStudentClassRecords);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(155, 155, 155))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSelectResultExamResults, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbSelectSubjectExamResult, 0, 365, Short.MAX_VALUE)
                    .addComponent(cmbSemesterNumberExamResult, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbSelectBatchExamResult, 0, 365, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblImageExamResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSaveExamResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDeleteExamResults, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbSelectBatchExamResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblImageExamResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSaveExamResult, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbSemesterNumberExamResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbSelectSubjectExamResult, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbSelectResultExamResults, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteExamResults, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Exam Results", jPanel1);

        cmbSelectBatchOJT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSelectBatchOJTActionPerformed(evt);
            }
        });

        tableStudentOJT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "MIS-Last two digits", "Name With Initials"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableStudentOJT.setGridColor(new java.awt.Color(255, 255, 0));
        tableStudentOJT.getTableHeader().setReorderingAllowed(false);
        tableStudentOJT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableStudentOJTMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableStudentOJTMousePressed(evt);
            }
        });
        jScrollPane8.setViewportView(tableStudentOJT);

        lblImageOJT.setBackground(new java.awt.Color(102, 255, 204));
        lblImageOJT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageOJTMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8)
                    .addComponent(cmbSelectBatchOJT, 0, 470, Short.MAX_VALUE))
                .addGap(31, 31, 31)
                .addComponent(lblImageOJT, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(282, 282, 282))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblImageOJT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(cmbSelectBatchOJT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(336, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("OJT", jPanel11);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbSelectBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSelectBatchActionPerformed
        ShowStudents();
    }//GEN-LAST:event_cmbSelectBatchActionPerformed

    private void lblImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMouseClicked
        lblImage.setVisible(true);
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        Filename = f.getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(Filename).getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_DEFAULT));
        lblImage.setIcon(imageIcon);

        try {
            File image = new File(Filename);
            FileInputStream fix = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int number; (number = fix.read(buf)) != -1;) {
                bos.write(buf, 0, number);
            }
            PersonImage = bos.toByteArray();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_lblImageMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        int x = JOptionPane.showConfirmDialog(null, "Confirm Saving Changes");
        if (x == 0) {
            try {
                String batch = cmbSelectBatch.getSelectedItem().toString();
                String mis = edtMIS.getText();
                String misl2 = lblMisL2.getText();
                String name = edtName.getText();
                String namewithin = edtNameWithInit.getText();

                Date dob = edtDOB.getDate();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormatter.format(dob);

                String age = "TB";
                String nic = edtNIC.getText();
                String phone = edtPhone.getText();
                String hqual = cmbHighestQual.getSelectedItem().toString();
                String gender = cmbGender.getSelectedItem().toString();
                String email = edtEmail.getText();
                String address = edtAddress.getText();
                String nameofguardian = edtGuardian.getText();
                String guardianphone = edtGuardianPhone.getText();

                String insertSQL = "INSERT INTO person_images (person_id, image_data) VALUES (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);

                String personId = batch + misl2; // Replace with the actual person ID
                preparedStatement.setString(1, personId);
                preparedStatement.setBytes(2, PersonImage); // Set the image data as bytes
                preparedStatement.executeUpdate();

                st = conn.createStatement();
                String sql = "INSERT INTO `students`(`batch`, `mis`, `misl2`, `name`, `namewithin`, `dob`, `age`,"
                        + "`nic`, `phone`, `hqual`, `gender`, `email`, `address`, `nameofguardian`, `guardianphone`,"
                        + "`image`) VALUES "
                        + "('" + batch + "','" + mis + "','" + misl2 + "','" + name + "','" + namewithin + "','" + formattedDate + "','" + age + "','" + nic + "',"
                        + "'" + phone + "','" + hqual + "','" + gender + "','" + email + "','" + address + "','" + nameofguardian + "','" + guardianphone + "',"
                        + "'" + PersonImage + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                ShowStudents();
            } catch (MySQLIntegrityConstraintViolationException e) {
                try {
                    String batch = cmbSelectBatch.getSelectedItem().toString();
                    String mis = edtMIS.getText();
                    String misl2 = lblMisL2.getText();
                    String name = edtName.getText();
                    String namewithin = edtNameWithInit.getText();

                    Date dob = edtDOB.getDate();
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = dateFormatter.format(dob);

                    String age = "TB";
                    String nic = edtNIC.getText();
                    String phone = edtPhone.getText();
                    String hqual = cmbHighestQual.getSelectedItem().toString();
                    String gender = cmbGender.getSelectedItem().toString();
                    String email = edtEmail.getText();
                    String address = edtAddress.getText();
                    String nameofguardian = edtGuardian.getText();
                    String guardianphone = edtGuardianPhone.getText();

                    String sql = "UPDATE `students` SET "
                            + "`batch`='" + batch + "', "
                            + "`misl2`='" + misl2 + "', "
                            + "`name`='" + name + "', "
                            + "`namewithin`='" + namewithin + "', "
                            + "`dob`='" + formattedDate + "', "
                            + "`age`='" + age + "', "
                            + "`nic`='" + nic + "', "
                            + "`phone`='" + phone + "', "
                            + "`hqual`='" + hqual + "', "
                            + "`gender`='" + gender + "', "
                            + "`email`='" + email + "', "
                            + "`address`='" + address + "', "
                            + "`nameofguardian`='" + nameofguardian + "', "
                            + "`guardianphone`='" + guardianphone + "', "
                            + "`image`='" + PersonImage + "' "
                            + "WHERE `mis`='" + mis + "'";
                    st = conn.createStatement();
                    st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Uptade successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                    ShowStudents();
                    String updateSQL = "UPDATE person_images SET image_data = ? WHERE person_id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
                    preparedStatement.setBytes(1, PersonImage); // Set the image data as bytes (first parameter)
                    String personId = batch + misl2; // Replace with the actual person ID
                    preparedStatement.setString(2, personId); // Set the person_id (second parameter)
                    preparedStatement.executeUpdate();

                } catch (Exception IVCS) {
                 JOptionPane.showMessageDialog(null, IVCS);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setVisible(false);
        RemoveBatch removeBatch = new RemoveBatch();
        removeBatch.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AddNewBatch();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked

    }//GEN-LAST:event_jButton2MouseClicked

    private void StudentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentsTableMouseClicked
        try {
            int R = StudentsTable.getSelectedRow();
            String batch = cmbSelectBatch.getSelectedItem().toString();
            String MisL2 = StudentsTable.getValueAt(R, 0).toString();
            st = conn.createStatement();
            String sql = "SELECT * FROM students WHERE batch='" + batch + "' && misl2='" + MisL2 + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                // Extract data from the "columnName" column, replace "columnName" with the actual column name
                edtMIS.setText(rs.getString("mis"));
                lblMisL2.setText(MisL2);
                edtName.setText(rs.getString("name"));
                edtNameWithInit.setText(rs.getString("namewithin"));

                String dateStringFromSQL = rs.getString("dob");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dateStringFromSQL);
                edtDOB.setDate(date);

                lblAge.setText(rs.getString("age"));
                edtNIC.setText(rs.getString("nic"));
                edtPhone.setText(rs.getString("phone"));
                cmbHighestQual.setSelectedItem(rs.getString("hqual"));
                cmbGender.setSelectedItem(rs.getString("gender"));
                edtEmail.setText(rs.getString("email"));
                edtAddress.setText(rs.getString("address"));
                edtGuardian.setText(rs.getString("nameofguardian"));
                edtGuardianPhone.setText(rs.getString("guardianphone"));

                String selectSQL = "SELECT image_data FROM person_images WHERE person_id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

                // Set the person ID in the query
                String personId = batch + MisL2; // Replace with the actual person ID
                preparedStatement.setString(1, personId);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve the image data as bytes
                    byte[] imageData = resultSet.getBytes("image_data");

                    if (imageData != null) {
                        // Convert the byte array to a BufferedImage
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

                        if (image != null) {
                            // Get the dimensions of the JLabel
                            int labelWidth = lblImage.getWidth();
                            int labelHeight = lblImage.getHeight();

                            // Scale the image to fit the dimensions of the JLabel
                            Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

                            // Create an ImageIcon with the scaled image
                            ImageIcon imageIcon = new ImageIcon(scaledImage);

                            // Set the imageIcon as the icon for your JLabel (replace lblImage with your JLabel)
                            lblImage.setIcon(imageIcon);
                        } else {
                            // Handle the case where the image data is not a valid image format
                            lblImage.setIcon(null); // Clear the existing icon
                            JOptionPane.showMessageDialog(null, "Invalid image format.");
                        }
                    } else {
                        // Handle the case where the image data is null
                        lblImage.setIcon(null); // Clear the existing icon
                        JOptionPane.showMessageDialog(null, "Image not found for the person.");
                    }
                } else {
                    // Handle the case where no data is found for the person ID
                    lblImage.setIcon(null); // Clear the existing icon
                    JOptionPane.showMessageDialog(null, "No image found for the person.");
                }

//===========================================================================
            } else {
                JOptionPane.showMessageDialog(null, "Ërror occured");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }//GEN-LAST:event_StudentsTableMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int x = JOptionPane.showConfirmDialog(null, "Confirm Delete");
        if (x == 0) {
            try {
                String batch = cmbSelectBatch.getSelectedItem().toString();
                String MisL2 = lblMisL2.getText().toString();
                String id = batch + MisL2;
                st = conn.createStatement();
                String sql = "DELETE FROM students WHERE batch='" + batch + "' && misl2='" + MisL2 + "'";
                String sql2 = "DELETE FROM person_images WHERE person_id='" + id + "'";
                st.executeUpdate(sql);
                st.executeUpdate(sql2);
                JOptionPane.showMessageDialog(null, "Student Deleted");
                ShowStudents();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            //TODO
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void StudentsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentsTableMousePressed
        try {
            int R = StudentsTable.getSelectedRow();
            String batch = cmbSelectBatch.getSelectedItem().toString();
            String MisL2 = StudentsTable.getValueAt(R, 0).toString();
            st = conn.createStatement();
            String sql = "SELECT * FROM students WHERE batch='" + batch + "' && misl2='" + MisL2 + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                // Extract data from the "columnName" column, replace "columnName" with the actual column name
                edtMIS.setText(rs.getString("mis"));
                lblMisL2.setText(MisL2);
                edtName.setText(rs.getString("name"));
                edtNameWithInit.setText(rs.getString("namewithin"));

                String dateStringFromSQL = rs.getString("dob");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dateStringFromSQL);
                edtDOB.setDate(date);

                lblAge.setText(rs.getString("age"));
                edtNIC.setText(rs.getString("nic"));
                edtPhone.setText(rs.getString("phone"));
                cmbHighestQual.setSelectedItem(rs.getString("hqual"));
                cmbGender.setSelectedItem(rs.getString("gender"));
                edtEmail.setText(rs.getString("email"));
                edtAddress.setText(rs.getString("address"));
                edtGuardian.setText(rs.getString("nameofguardian"));
                edtGuardianPhone.setText(rs.getString("guardianphone"));
            } else {
                JOptionPane.showMessageDialog(null, "Ërror occured");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_StudentsTableMousePressed

    private void edtMISKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtMISKeyReleased
        String x = edtMIS.getText().toString();
        int y = x.length();
        if (y >= 2) {
            String lastTwoDigits = x.substring(y - 2, y);
            lblMisL2.setText(lastTwoDigits);
            lblWarning.setVisible(false);
        } else {
            lblWarning.setVisible(true);
            lblWarning.setText("Incomplete MIS");

        }

    }//GEN-LAST:event_edtMISKeyReleased

    private void tableStudentAssignmentsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableStudentAssignmentsMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableStudentAssignmentsMousePressed

    private void tableStudentAssignmentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableStudentAssignmentsMouseClicked
        try {
            int R = tableStudentAssignments.getSelectedRow();
            String batch = cmbSelectBatch1.getSelectedItem().toString();
            String MisL2 = tableStudentAssignments.getValueAt(R, 0).toString();
             String selectSQL = "SELECT image_data FROM person_images WHERE person_id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

                // Set the person ID in the query
                String personId = batch + MisL2; // Replace with the actual person ID
                preparedStatement.setString(1, personId);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve the image data as bytes
                    byte[] imageData = resultSet.getBytes("image_data");

                    if (imageData != null) {
                        // Convert the byte array to a BufferedImage
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

                        if (image != null) {
                            // Get the dimensions of the JLabel
                            int labelWidth = lblImage.getWidth();
                            int labelHeight = lblImage.getHeight();

                            // Scale the image to fit the dimensions of the JLabel
                            Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

                            // Create an ImageIcon with the scaled image
                            ImageIcon imageIcon = new ImageIcon(scaledImage);

                            // Set the imageIcon as the icon for your JLabel (replace lblImage with your JLabel)
                            lblImage1.setIcon(imageIcon);
                        } else {
                            // Handle the case where the image data is not a valid image format
                            lblImage1.setIcon(null); // Clear the existing icon
                            JOptionPane.showMessageDialog(null, "Invalid image format.");
                        }
                    } else {
                        // Handle the case where the image data is null
                        lblImage1.setIcon(null); // Clear the existing icon
                        JOptionPane.showMessageDialog(null, "Image not found for the person.");
                    }
                } else {
                    // Handle the case where no data is found for the person ID
                    lblImage1.setIcon(null); // Clear the existing icon
                    JOptionPane.showMessageDialog(null, "No image found for the person.");
                }
                 LoadStudentReport();
                 ShowStudentsRecordAssignments();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_tableStudentAssignmentsMouseClicked

    private void cmbSelectBatch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSelectBatch1ActionPerformed
       ShowStudentsAssignment();
       showStudentNo();
    }//GEN-LAST:event_cmbSelectBatch1ActionPerformed

    private void lblImage1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImage1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblImage1MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
 setVisible(false);
        RemoveSemester removeSemester = new RemoveSemester();
        removeSemester.setVisible(true);
        this.dispose();        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        AddNewSemester();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
 setVisible(false);
        RemoveSubject removeSubject = new RemoveSubject();
        removeSubject.setVisible(true);
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        AddNewSubject();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cmbSemesterNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterNumberActionPerformed
        fetchSubjects(); 
        LoadSubject();
        LoadClassReport();
    }//GEN-LAST:event_cmbSemesterNumberActionPerformed

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
AddNewAssignmentNumber();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        int x=JOptionPane.showConfirmDialog(null, "Save Changes?");
        if(x==0){
            try {
                int R = tableStudentAssignments.getSelectedRow();
            String batch = cmbSelectBatch1.getSelectedItem().toString();
            String MisL2 = tableStudentAssignments.getValueAt(R, 0).toString();
            String semesterno=cmbSemesterNumber.getSelectedItem().toString();
            String subject=cmbSelectSubject.getSelectedItem().toString();
            String assignmentno=cmbSelectAssignmentNumber.getSelectedItem().toString();
            String marks=txtAssignmentMarks.getText().toString();
            st = conn.createStatement();
            String sql="INSERT INTO `assignments`(`batch`, `mis`, `subject`, `assignmentnumber`, `marks`) VALUES "
                    + "('" + batch + "','" + MisL2 + "','" + subject + "','" + assignmentno + "','" + marks + "')";
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
            
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_btnSave1ActionPerformed

    private void cmbSelectSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSelectSubjectActionPerformed
        LoadSubject();
        LoadClassReport();
        ShowClassRecordAssignments();
    }//GEN-LAST:event_cmbSelectSubjectActionPerformed

    private void cmbSelectBatchExamResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSelectBatchExamResultActionPerformed
        ShowStudentsExamResult();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSelectBatchExamResultActionPerformed

    private void tableStudentExamResultsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableStudentExamResultsMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableStudentExamResultsMousePressed

    private void tableStudentExamResultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableStudentExamResultsMouseClicked
try {
            int R = tableStudentExamResults.getSelectedRow();
            String batch = cmbSelectBatchExamResult.getSelectedItem().toString();
            String MisL2 = tableStudentExamResults.getValueAt(R, 0).toString();
             String selectSQL = "SELECT image_data FROM person_images WHERE person_id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);

                // Set the person ID in the query
                String personId = batch + MisL2; // Replace with the actual person ID
                preparedStatement.setString(1, personId);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve the image data as bytes
                    byte[] imageData = resultSet.getBytes("image_data");

                    if (imageData != null) {
                        // Convert the byte array to a BufferedImage
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

                        if (image != null) {
                            // Get the dimensions of the JLabel
                            int labelWidth = lblImageExamResult.getWidth();
                            int labelHeight = lblImageExamResult.getHeight();

                            // Scale the image to fit the dimensions of the JLabel
                            Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

                            // Create an ImageIcon with the scaled image
                            ImageIcon imageIcon = new ImageIcon(scaledImage);

                            // Set the imageIcon as the icon for your JLabel (replace lblImage with your JLabel)
                            lblImageExamResult.setIcon(imageIcon);
                        } else {
                            // Handle the case where the image data is not a valid image format
                            lblImageExamResult.setIcon(null); // Clear the existing icon
                            JOptionPane.showMessageDialog(null, "Invalid image format.");
                        }
                    } else {
                        // Handle the case where the image data is null
                        lblImageExamResult.setIcon(null); // Clear the existing icon
                        JOptionPane.showMessageDialog(null, "Image not found for the person.");
                    }
                } else {
                    // Handle the case where no data is found for the person ID
                    lblImageExamResult.setIcon(null); // Clear the existing icon
                    JOptionPane.showMessageDialog(null, "No image found for the person.");
                }
                 ShowStudentsRecordExamResults();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tableStudentExamResultsMouseClicked

    private void lblImageExamResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageExamResultMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblImageExamResultMouseClicked

    private void cmbSemesterNumberExamResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterNumberExamResultActionPerformed
        fetchSubjectsExamResult();
        ShowClassRecordExamResults();
    }//GEN-LAST:event_cmbSemesterNumberExamResultActionPerformed

    private void cmbSelectSubjectExamResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSelectSubjectExamResultActionPerformed
       ShowClassRecordExamResults();
    }//GEN-LAST:event_cmbSelectSubjectExamResultActionPerformed

    private void btnSaveExamResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveExamResultActionPerformed
int x = JOptionPane.showConfirmDialog(null, "Confirm Saving Changes");
        if (x == 0) {
            try {
                String batch = cmbSelectBatchExamResult.getSelectedItem().toString();
                int R=tableStudentExamResults.getSelectedRow();
                String misl2 = tableStudentExamResults.getValueAt(R, 0).toString();
                String semester=cmbSemesterNumberExamResult.getSelectedItem().toString();
                String subject=cmbSelectSubjectExamResult.getSelectedItem().toString();
                String result=cmbSelectResultExamResults.getSelectedItem().toString();
                
                st = conn.createStatement();
                String sql = " INSERT INTO `examresults`(`batch`, `misl2`, `semester`, `subject`, `result`) VALUES "
                        + "('" + batch + "','" + misl2 + "','" + semester + "','" + subject + "','" + result + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                
            } catch (MySQLIntegrityConstraintViolationException e) {
                try {
                  String batch = cmbSelectBatchExamResult.getSelectedItem().toString();
                int R=tableStudentExamResults.getSelectedRow();
                String misl2 = tableStudentExamResults.getValueAt(R, 0).toString();
                String semester=cmbSemesterNumberExamResult.getSelectedItem().toString();
                String subject=cmbSelectSubjectExamResult.getSelectedItem().toString();
                String result=cmbSelectResultExamResults.getSelectedItem().toString();
                
                st = conn.createStatement();
                String sql = "UPDATE `examresults` SET `batch`='" + batch + "',`misl2`='" + misl2 + "',"
                        + "`semester`='" + semester + "',`subject`='" + subject + "',`result`='" + result + "'"
                        + " WHERE  batch='" + batch + "' && misl2='" + misl2 + "'";
                st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Uptade successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                   } catch (Exception IVCS) {
                 JOptionPane.showMessageDialog(null, IVCS);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveExamResultActionPerformed

    private void btnDeleteExamResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteExamResultsActionPerformed
int x = JOptionPane.showConfirmDialog(null, "Confirm Delete");
        if (x == 0) {
            try {
                String batch = cmbSelectBatchExamResult.getSelectedItem().toString();
                int R=tableStudentExamResults.getSelectedRow();
                String MisL2=tableStudentExamResults.getValueAt(R, 0).toString();
                st = conn.createStatement();
                String sql = "DELETE FROM examresults WHERE batch='" + batch + "' && misl2='" + MisL2 + "'";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Exam Result Deleted");
                ShowStudents();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            //TODO
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteExamResultsActionPerformed

    private void cmbSelectBatchOJTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSelectBatchOJTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSelectBatchOJTActionPerformed

    private void tableStudentOJTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableStudentOJTMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableStudentOJTMouseClicked

    private void tableStudentOJTMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableStudentOJTMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableStudentOJTMousePressed

    private void lblImageOJTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageOJTMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblImageOJTMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable StudentsTable;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteExamResults;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSave1;
    private javax.swing.JButton btnSaveExamResult;
    private javax.swing.JComboBox<String> cmbGender;
    private javax.swing.JComboBox<String> cmbHighestQual;
    private javax.swing.JComboBox<String> cmbSelectAssignmentNumber;
    private javax.swing.JComboBox<String> cmbSelectBatch;
    private javax.swing.JComboBox<String> cmbSelectBatch1;
    private javax.swing.JComboBox<String> cmbSelectBatchExamResult;
    private javax.swing.JComboBox<String> cmbSelectBatchOJT;
    private javax.swing.JComboBox<String> cmbSelectResultExamResults;
    private javax.swing.JComboBox<String> cmbSelectSubject;
    private javax.swing.JComboBox<String> cmbSelectSubjectExamResult;
    private javax.swing.JComboBox<String> cmbSemesterNumber;
    private javax.swing.JComboBox<String> cmbSemesterNumberExamResult;
    private javax.swing.JTextField edtAddress;
    private com.toedter.calendar.JDateChooser edtDOB;
    private javax.swing.JTextField edtEmail;
    private javax.swing.JTextField edtGuardian;
    private javax.swing.JTextField edtGuardianPhone;
    private javax.swing.JTextField edtMIS;
    private javax.swing.JTextField edtNIC;
    private javax.swing.JTextField edtName;
    private javax.swing.JTextField edtNameWithInit;
    private javax.swing.JTextField edtPhone;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblAssignmentCount;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblImage1;
    private javax.swing.JLabel lblImageExamResult;
    private javax.swing.JLabel lblImageOJT;
    private javax.swing.JLabel lblMisL2;
    private javax.swing.JLabel lblNumberOfStudents;
    private javax.swing.JLabel lblShowAverage;
    private javax.swing.JLabel lblShowBatch;
    private javax.swing.JLabel lblShowClassMaximumAssignment;
    private javax.swing.JLabel lblShowClassMinimumAssignment;
    private javax.swing.JLabel lblShowSemester;
    private javax.swing.JLabel lblShowStudentAverage;
    private javax.swing.JLabel lblShowStudentMaximum2;
    private javax.swing.JLabel lblShowStudentMinimum;
    private javax.swing.JLabel lblShowSubject;
    private javax.swing.JLabel lblWarning;
    private javax.swing.JPanel panelAssignmentsHeader;
    private javax.swing.JTable tableAssignmentClassRecord;
    private javax.swing.JTable tableAssignmentStudentRecord;
    private javax.swing.JTable tableStudentAssignments;
    private javax.swing.JTable tableStudentClassRecords;
    private javax.swing.JTable tableStudentExamResults;
    private javax.swing.JTable tableStudentOJT;
    private javax.swing.JTable tableStudentRecordExamResults;
    private javax.swing.JTextField txtAssignmentMarks;
    private javax.swing.JLabel txtDateTime;
    private javax.swing.JLabel txtDateTime2;
    // End of variables declaration//GEN-END:variables
}
