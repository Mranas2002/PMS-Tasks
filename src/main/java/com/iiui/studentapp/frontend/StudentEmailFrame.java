package com.iiui.studentapp.frontend;

import com.iiui.studentapp.backend.dao.DatabaseConnection;
import com.iiui.studentapp.backend.dao.StudentDao;
import com.iiui.studentapp.backend.model.StudentInfo;
import com.iiui.studentapp.backend.service.StudentEmailParser;
import com.iiui.studentapp.backend.service.ValidationException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

public class StudentEmailFrame extends JFrame {
    private final JTextField emailField = new JTextField("rimsha.bsit822@iiu.edu.pk", 28);
    private final JTextField batchField = new JTextField("F22", 12);
    private final JLabel regLabel = new JLabel("-");
    private final JLabel fullRegLabel = new JLabel("-");
    private final JLabel nameLabel = new JLabel("-");
    private final JLabel degreeLabel = new JLabel("-");
    private final JLabel batchLabel = new JLabel("-");
    private final JLabel statusLabel = new JLabel("Enter IIUI email and press Save/Submit.");

    private final StudentEmailParser parser = new StudentEmailParser();
    private final StudentDao studentDao = new StudentDao(new DatabaseConnection());

    public StudentEmailFrame() {
        super("IIUI Student Email App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(620, 430);
        setMinimumSize(new Dimension(500, 340));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(14, 14));

        JPanel contentPanel = new JPanel(new BorderLayout(14, 14));
        contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        contentPanel.add(createFormPanel(), BorderLayout.CENTER);
        contentPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
        registerLiveExtraction();
        updateExtractedInfo();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 4, 22));

        JLabel title = new JLabel("IIUI Student Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Task A: Extraction   Task B: GUI Form   Task C: Save to Database");
        subtitle.setForeground(new Color(92, 92, 92));

        panel.add(title, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addSectionLabel(panel, gbc, 0, "Task B - GUI Input Form");
        addRow(panel, gbc, 1, "IIUI Email", emailField);
        addRow(panel, gbc, 2, "Batch", batchField);

        addSectionLabel(panel, gbc, 3, "Task A - Extracted Student Information");
        addInfoRow(panel, gbc, 4, "Reg #", regLabel);
        addInfoRow(panel, gbc, 5, "Full Reg No", fullRegLabel);
        addInfoRow(panel, gbc, 6, "Name", nameLabel);
        addInfoRow(panel, gbc, 7, "Degree Title", degreeLabel);
        addInfoRow(panel, gbc, 8, "Batch", batchLabel);

        addSectionLabel(panel, gbc, 9, "Task C - Database Save");
        JButton saveButton = new JButton("Save/Submit");
        saveButton.addActionListener(event -> saveStudent());

        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.weightx = 1;
        panel.add(saveButton, gbc);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 22, 18, 22));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(statusLabel, BorderLayout.CENTER);
        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String title, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(createLabel(title), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void addSectionLabel(JPanel panel, GridBagConstraints gbc, int row, String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(36, 76, 132));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(label, gbc);
        gbc.gridwidth = 1;
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String title, JLabel valueLabel) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(createLabel(title), gbc);

        valueLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(valueLabel, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private void saveStudent() {
        try {
            StudentInfo studentInfo = parser.parse(emailField.getText(), batchField.getText());
            showStudentInfo(studentInfo);
            studentDao.save(studentInfo);
            setStatus("Saved successfully in MySQL database.", new Color(18, 112, 58));
            JOptionPane.showMessageDialog(this, "Student information saved successfully.");
        } catch (ValidationException exception) {
            clearStudentInfo();
            setStatus(exception.getMessage(), new Color(170, 32, 32));
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException exception) {
            setStatus("Database error: " + exception.getMessage(), new Color(170, 96, 20));
            JOptionPane.showMessageDialog(this,
                    "Database connection/save failed.\nPlease check XAMPP MySQL, database table, and connector JAR.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerLiveExtraction() {
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                updateExtractedInfo();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                updateExtractedInfo();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                updateExtractedInfo();
            }
        };

        emailField.getDocument().addDocumentListener(listener);
        batchField.getDocument().addDocumentListener(listener);
    }

    private void updateExtractedInfo() {
        try {
            StudentInfo studentInfo = parser.parse(emailField.getText(), batchField.getText());
            showStudentInfo(studentInfo);
            setStatus("Task A extraction complete. Press Save/Submit for Task C.", new Color(60, 82, 120));
        } catch (ValidationException exception) {
            clearStudentInfo();
            setStatus(exception.getMessage(), new Color(170, 32, 32));
        }
    }

    private void showStudentInfo(StudentInfo studentInfo) {
        regLabel.setText(studentInfo.getRegNo());
        fullRegLabel.setText(studentInfo.getFormattedRegNo());
        nameLabel.setText(studentInfo.getName());
        degreeLabel.setText(studentInfo.getDegreeTitle());
        batchLabel.setText(studentInfo.getBatch());
    }

    private void clearStudentInfo() {
        regLabel.setText("-");
        fullRegLabel.setText("-");
        nameLabel.setText("-");
        degreeLabel.setText("-");
        batchLabel.setText("-");
    }

    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public static void showApp() {
        SwingUtilities.invokeLater(() -> new StudentEmailFrame().setVisible(true));
    }
}
