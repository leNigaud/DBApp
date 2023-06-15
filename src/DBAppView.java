import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DBAppView {
    private JFrame frame;
    private JTextField idField;
    private JTextField nameField;
    private DefaultTableModel model;
    private JTable table;

    public DBAppView() {
        frame = new JFrame("Database App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.X_AXIS));
        idField = new JTextField(20);
        nameField = new JTextField(20);
        formPanel.add(new JLabel("ID: "));
        formPanel.add(idField);
        formPanel.add(Box.createRigidArea(new Dimension(10,0)));  // To add some space
        formPanel.add(new JLabel("Name: "));
        formPanel.add(nameField);

        frame.add(formPanel);

        model = new DefaultTableModel(new String[]{"ID", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        frame.add(new JScrollPane(table));
    }

    public void updateTable(List<Person> people) {
        model.setRowCount(0);
        for (Person person : people) {
            model.addRow(new Object[]{person.getId(), person.getName()});
        }
    }

    // Getters
    public JFrame getFrame() {
        return frame;
    }

    public JTextField getIdField() {
        return idField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTable getTable() {
        return table;
    }
}
