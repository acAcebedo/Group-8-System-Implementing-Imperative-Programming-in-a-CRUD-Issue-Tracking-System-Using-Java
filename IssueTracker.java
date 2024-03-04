import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IssueTracker extends JFrame {
    private List<Issue> issues;
    private DefaultTableModel tableModel;
    private JTable issuesTable;
    private JButton updateIssueButton;
    private JButton deleteIssueButton;

    public IssueTracker() {
        issues = new ArrayList<>();
        loadData();
        initUI();
    }

    private void initUI() {
        setTitle("Issue Tracking System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    
        JPanel viewPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Title", "Description", "Status", "Assigned Developer", "Date Reported"}, 0);
        issuesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(issuesTable);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel(new FlowLayout()); // Use FlowLayout for buttons
   
    
        // Add the rest of the buttons
        JButton addIssueButton = new JButton("Add Issue");
        updateIssueButton = new JButton("Update Issue");
        updateIssueButton.setEnabled(false);
        deleteIssueButton = new JButton("Delete Issue");
        deleteIssueButton.setEnabled(false);
    
        buttonPanel.add(addIssueButton);
        buttonPanel.add(updateIssueButton);
        buttonPanel.add(deleteIssueButton);
    
        viewPanel.add(buttonPanel, BorderLayout.SOUTH);

        addIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddIssueDialog();
            }
        });

        updateIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedIssue();
            }
        });

        deleteIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedIssue();
            }
        });

        // Set up mouse listener for table cell selection for the delete button
        issuesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = issuesTable.getSelectedRow();
                deleteIssueButton.setEnabled(selectedRow != -1);
                updateIssueButton.setEnabled(selectedRow != -1);
            }
        });

        // Main window layout
        add(viewPanel);

        // Set up table selection listener for disabling the update button
        issuesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!issuesTable.getSelectionModel().isSelectionEmpty()) {
                updateIssueButton.setEnabled(false);
            }
        });

        // Set table as non-editable
        for (int i = 0; i < issuesTable.getColumnCount(); i++) {
            issuesTable.getColumnModel().getColumn(i).setCellEditor(null);
        }

        refreshTable();
        setVisible(true);
    } 

    private void showAddIssueDialog() {
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"OPEN", "IN PROGRESS", "CLOSED"});
        JComboBox<String> developerComboBox = new JComboBox<>(new String[]{"JC", "Angela", "Pol"});
    
        Object[] message = {
                "Title:", titleField,
                "Description:", descriptionField,
                "Status:", statusComboBox,
                "Assigned Developer:", developerComboBox
        };
    
        int option = JOptionPane.showConfirmDialog(null, message, "Add Issue", JOptionPane.OK_CANCEL_OPTION);
    
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String status = (String) statusComboBox.getSelectedItem();
            String assignedDeveloper = (String) developerComboBox.getSelectedItem();
    
            if (!title.isEmpty() && !description.isEmpty()) {
                Issue newIssue = new Issue(title, description, status, assignedDeveloper);
                issues.add(newIssue);
    
                saveData();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(null, "Please enter both title and description.");
            }
        }
    }
    

    private void updateSelectedIssue() {
        int selectedRow = issuesTable.getSelectedRow();
        if (selectedRow != -1) {
            JTextField titleField = new JTextField(issues.get(selectedRow).getTitle());
            JTextField descriptionField = new JTextField(issues.get(selectedRow).getDescription());
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"OPEN", "IN PROGRESS", "CLOSED"});
            JComboBox<String> developerComboBox = new JComboBox<>(new String[]{"JC", "Angela", "Pol"});
    
            statusComboBox.setSelectedItem(issues.get(selectedRow).getStatus());
            developerComboBox.setSelectedItem(issues.get(selectedRow).getAssignedDeveloper());
    
            Object[] message = {
                    "Title:", titleField,
                    "Description:", descriptionField,
                    "Status:", statusComboBox,
                    "Assigned Developer:", developerComboBox
            };
    
            int option = JOptionPane.showConfirmDialog(null, message, "Update Issue", JOptionPane.OK_CANCEL_OPTION);
    
            if (option == JOptionPane.OK_OPTION) {
                String title = titleField.getText();
                String description = descriptionField.getText();
                String status = (String) statusComboBox.getSelectedItem();
                String assignedDeveloper = (String) developerComboBox.getSelectedItem();
    
                if (!title.isEmpty() && !description.isEmpty()) {
                    // Update the selected issue with the new values
                    issues.get(selectedRow).setTitle(title);
                    issues.get(selectedRow).setDescription(description);
                    issues.get(selectedRow).setStatus(status);
                    issues.get(selectedRow).setAssignedDeveloper(assignedDeveloper);
    
                    saveData();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter both title and description.");
                }
            }
        }
    }
    
    private void deleteSelectedIssue() {
        int selectedRow = issuesTable.getSelectedRow();
        if (selectedRow != -1) {
            issues.remove(selectedRow);
            saveData();
            refreshTable();
        }
    }

    private void refreshTable(List<Issue> issuesToDisplay) {
        tableModel.setRowCount(0);
        for (Issue issue : issuesToDisplay) {
            Object[] row = {issue.getTitle(), issue.getDescription(), issue.getStatus(),
                    issue.getAssignedDeveloper(), issue.getDateReported()};
            tableModel.addRow(row);
        }
    }

    private void refreshTable() {
        refreshTable(issues);
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("issue_tracker.txt"))) {
            oos.writeObject(issues);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("issue_tracker.txt"))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<?> rawList = (List<?>) obj;
                issues = new ArrayList<>();

                for (Object element : rawList) {
                    if (element instanceof Issue) {
                        issues.add((Issue) element);
                    } else {
                        // Handle unexpected element type if needed
                    }
                }
            } else {
                issues = new ArrayList<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IssueTracker());
    }
}
