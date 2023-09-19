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
    }
    void FetchDate(){
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd' of' MMMM yyyy");
        String formattedDate = currentDate.format(customFormatter);
        txtDateTime.setText(formattedDate);
    }
    
    public void FetchTime() 
    {
		new Timer(0, new ActionListener() 
                {
			@Override
			public void actionPerformed(ActionEvent e) 
                        {
				java.util.Date d = new java.util.Date();
				SimpleDateFormat form = new SimpleDateFormat("hh:mm:ss a");
				txtDateTime2.setText(form.format(d));
			}
		}
                ).start();
    }
    
    public void FetchCombo()
    {
        try{
        String sql3="SELECT * FROM batchdetails";
        st=conn.prepareStatement(sql3);
        rs=st.executeQuery(sql3);
        
        while(rs.next())
        {
            String batchname=rs.getString("batch");
            cmbSelectBatch.addItem(batchname);
        }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    void AddNewBatch()
    {
             String userInput = JOptionPane.showInputDialog(null, "Enter New Batch:", "Add New Batch", JOptionPane.PLAIN_MESSAGE);
        if (userInput != null && !userInput.isEmpty()) {
            try{ 
                st = conn.createStatement();
                String sql2="DELETE FROM batchdetails";
                st.executeUpdate(sql2);
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbSelectBatch.getModel();
                    model.insertElementAt(userInput, 1);
                    for(int i=0; i<model.getSize();i++){
                    String sql="INSERT INTO batchdetails(batch) VALUES ('"+model.getElementAt(i)+"')";
                    st.executeUpdate(sql);
                    }
                    JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
                    cmbSelectBatch.setSelectedIndex(0);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Details entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void RemoveBatch()
    {
             String userInput2 = JOptionPane.showInputDialog(null, "Select Batch to Delete by Position(Topmost is 1):", "Delete Batch", JOptionPane.PLAIN_MESSAGE);
             int maxIndex=Integer.parseInt(userInput2);
        if (userInput2 != null && !userInput2.isEmpty() && userInput2!="0" && maxIndex <cmbSelectBatch.getItemCount()-1) {
            try{   
                st = conn.createStatement();
                DefaultComboBoxModel<String> model2 = (DefaultComboBoxModel<String>) cmbSelectBatch.getModel();
                String deleteAt=model2.getElementAt(Integer.parseInt(userInput2));
                String sql2="DELETE FROM batchdetails WHERE batch='"+deleteAt+"'";
                st.executeUpdate(sql2);
                
                JOptionPane.showMessageDialog(null, "Delete successful!, Please Restart the System", "Success", JOptionPane.INFORMATION_MESSAGE, DeleteSuccessIcon);
                System.exit(0);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Details entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }  
    }
    
    public void ShowStudents() {
        try {
            String batch=cmbSelectBatch.getSelectedItem().toString();
            st = conn.createStatement();
            String sql = "SELECT misl2,namewithin FROM students WHERE batch='"+batch+"'";
            ResultSet rs = st.executeQuery(sql);
            StudentsTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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
        jPanel1 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Home - ClassRoom Management System");

        jPanel2.setBackground(new java.awt.Color(204, 51, 0));

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 583, Short.MAX_VALUE)
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
                .addContainerGap(447, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Home", jPanel3);

        cmbSelectBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSelectBatchActionPerformed(evt);
            }
        });

        StudentsTable.setBackground(new java.awt.Color(0, 0, 0));
        StudentsTable.setForeground(new java.awt.Color(0, 153, 0));
        StudentsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        StudentsTable.setGridColor(new java.awt.Color(255, 255, 0));
        StudentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                StudentsTableMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentsTableMouseClicked(evt);
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
                                .addComponent(edtDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                                .addComponent(lblAge, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(72, 72, 72))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(edtName)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(jLabel7)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel8))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(lblWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addComponent(edtMIS))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lblMisL2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(edtNameWithInit))
                                                .addGap(7, 7, 7)))
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
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(edtGuardian, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edtGuardianPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel13)
                                    .addComponent(edtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(cmbSelectBatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1))))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtNameWithInit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(0, 60, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Students", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        JFileChooser  chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        Filename = f.getAbsolutePath();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(Filename).getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(),Image.SCALE_DEFAULT));
        lblImage.setIcon(imageIcon);

        try{
            File image = new File(Filename);
            FileInputStream fix = new  FileInputStream(image);
            ByteArrayOutputStream bos  = new   ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for(int number;(number = fix.read(buf))!= -1;){
                bos.write(buf, 0, number);
            }
            PersonImage = bos.toByteArray();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_lblImageMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        int x=JOptionPane.showConfirmDialog(null, "Confirm Saving Changes");
        if(x==0){
            try{
            String batch=cmbSelectBatch.getSelectedItem().toString();
            String mis=edtMIS.getText();
            String misl2=lblMisL2.getText();
            String name=edtName.getText();
            String namewithin=edtNameWithInit.getText();
            
            Date dob=edtDOB.getDate();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormatter.format(dob);
            
            String age="TB";
            String nic=edtNIC.getText();
            String phone=edtPhone.getText();
            String hqual=cmbHighestQual.getSelectedItem().toString();
            String gender=cmbGender.getSelectedItem().toString();
            String email=edtEmail.getText();
            String address=edtAddress.getText();
            String nameofguardian=edtGuardian.getText();
            String guardianphone=edtGuardianPhone.getText();
            
            
    String insertSQL = "INSERT INTO person_images (person_id, image_data) VALUES (?, ?)";
    PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
    
    String personId =batch+misl2; // Replace with the actual person ID
    preparedStatement.setString(1, personId);
    preparedStatement.setBytes(2, PersonImage); // Set the image data as bytes
    preparedStatement.executeUpdate();
            
            st=conn.createStatement();   
            String sql="INSERT INTO `students`(`batch`, `mis`, `misl2`, `name`, `namewithin`, `dob`, `age`,"
                    + "`nic`, `phone`, `hqual`, `gender`, `email`, `address`, `nameofguardian`, `guardianphone`,"
                    + "`image`) VALUES "
                    + "('"+batch+"','"+mis+"','"+misl2+"','"+name+"','"+namewithin+"','"+formattedDate+"','"+age+"','"+nic+"',"
                    + "'"+phone+"','"+hqual+"','"+gender+"','"+email+"','"+address+"','"+nameofguardian+"','"+guardianphone+"',"
                    + "'"+PersonImage+"')";
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Operation successful!", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
            ShowStudents();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        RemoveBatch();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AddNewBatch();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void StudentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentsTableMouseClicked
       try {
        int R=StudentsTable.getSelectedRow();
        String batch=cmbSelectBatch.getSelectedItem().toString();
        String MisL2=StudentsTable.getValueAt(R, 0).toString();    
            st = conn.createStatement();
            String sql = "SELECT * FROM students WHERE batch='"+batch+"' && misl2='"+MisL2+"'";
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
//        byte[] imageData = rs.getBytes("image");
//
//               if (imageData != null) {
//            // Convert the byte array to a BufferedImage
//            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
//
//            ImageIcon imageIcon = new ImageIcon(image);
//
//            // Set the imageIcon as the icon for lblImage
//            lblImage.setIcon(imageIcon);
//        } else {
//            // Handle the case where the "image" column is empty (null)
//            // You can set a default image or display a message
//            lblImage.setIcon(null); // Clear the existing icon
//            JOptionPane.showMessageDialog(null, "Image not available.");
//        }
//===========================================================================

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





    } 
           else{
               JOptionPane.showMessageDialog(null, "Ërror occured");
           }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }//GEN-LAST:event_StudentsTableMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
try{
        String batch=cmbSelectBatch.getSelectedItem().toString();
        String MisL2=lblMisL2.getText().toString();
        String id=batch+MisL2;
        st=conn.createStatement();
        String sql="DELETE FROM students WHERE batch='"+batch+"' && mis='"+MisL2+"'";
        String sql2="DELETE FROM person_images WHERE person_id='"+id+"'";
        st.executeUpdate(sql);
        st.executeUpdate(sql2);
        JOptionPane.showMessageDialog(null, "Delete Successful");
}catch(Exception e){
    JOptionPane.showMessageDialog(null, e);
}     
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void StudentsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentsTableMousePressed
 try {
        int R=StudentsTable.getSelectedRow();
        String batch=cmbSelectBatch.getSelectedItem().toString();
        String MisL2=StudentsTable.getValueAt(R, 0).toString();    
            st = conn.createStatement();
            String sql = "SELECT * FROM students WHERE batch='"+batch+"' && misl2='"+MisL2+"'";
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
    } 
           else{
               JOptionPane.showMessageDialog(null, "Ërror occured");
           }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_StudentsTableMousePressed

    private void edtMISKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edtMISKeyReleased
        String x=edtMIS.getText().toString();
        int y=x.length();
        if (y >= 2) {
    String lastTwoDigits = x.substring(y - 2, y);
    lblMisL2.setText(lastTwoDigits);
    lblWarning.setVisible(false);
} else {
    lblWarning.setVisible(true);
    lblWarning.setText("Incomplete MIS");
    
}
        
    }//GEN-LAST:event_edtMISKeyReleased

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
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbGender;
    private javax.swing.JComboBox<String> cmbHighestQual;
    private javax.swing.JComboBox<String> cmbSelectBatch;
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblMisL2;
    private javax.swing.JLabel lblWarning;
    private javax.swing.JLabel txtDateTime;
    private javax.swing.JLabel txtDateTime2;
    // End of variables declaration//GEN-END:variables
}
