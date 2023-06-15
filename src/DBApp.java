import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DBApp {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASS = "";

    JFrame frame;
    JTextField nameField;
    JTextField idField;
    JButton addButton;
    JTable table;
    DefaultTableModel model;
    
    DBApp() {
        frame = new JFrame("DBApp");
        nameField = new JTextField(20);
        idField = new JTextField(20);
        addButton = new JButton("Add");
        
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        
        model = new DefaultTableModel(new String[]{"ID", "Name", ""}, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //Only the second column is editable.
            }
        };
                
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");

        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        frame.add(buttonPanel);
        

        frame.setLayout(new FlowLayout());
        frame.setSize(500, 500);
        frame.add(new JLabel("Adding"));
        frame.add(new JLabel("ID:"));
        frame.add(idField);
        frame.add(new JLabel("Name:"));
        frame.add(nameField);
        frame.add(addButton);
        frame.add(new JLabel("Display"));
        frame.add(new JScrollPane(table));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        /*
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");

        frame.add(deleteButton);
        frame.add(editButton);
        */
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) model.getValueAt(selectedRow, 0);
                try {
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String deleteQuery = "DELETE FROM person WHERE id = ?";
                    PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                    deleteStatement.setString(1, id);
                    deleteStatement.executeUpdate();
                    refreshTable();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) model.getValueAt(selectedRow, 0);
                String newName = JOptionPane.showInputDialog(frame, "New Name for ID " + id);
                if(newName != null && !newName.trim().isEmpty()) {
                    try {
                        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                        String editQuery = "UPDATE person SET name = ? WHERE id = ?";
                        PreparedStatement editStatement = conn.prepareStatement(editQuery);
                        editStatement.setString(1, newName);
                        editStatement.setString(2, id);
                        editStatement.executeUpdate();
                        refreshTable();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        

        addButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String id = idField.getText();
        String name = nameField.getText();
        if(id.trim().isEmpty() || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid input");
            return;
        }

        // Add to DB
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String queryCheck = "SELECT * from person WHERE id = ?";
            PreparedStatement psCheck = conn.prepareStatement(queryCheck);
            psCheck.setString(1, id);
            ResultSet resultSet = psCheck.executeQuery();
            if(resultSet.next()) {
                JOptionPane.showMessageDialog(frame, "ID already exists");
                return;
            }

            String query = "INSERT INTO person (id, name) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        idField.setText("");
        nameField.setText("");
        refreshTable();
    }
});


        refreshTable();
    }

    void refreshTable() {
    model.setRowCount(0);  // Clears the table
    try {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        String query = "SELECT * FROM person";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");

            model.addRow(new Object[]{id, name});
        }
        if (model.getRowCount() == 0) {
            frame.add(new JLabel("No data in the database"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    public static void main(String[] args) {
        new DBApp();
    }
}
