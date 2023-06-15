import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class DBAppController {
    private DBAppView view;
    private PersonDAO dao;
    // private JFrame editDeleteFrame;
    // private JTextField editField;

    public DBAppController(DBAppView view, PersonDAO dao) {
        this.view = view;
        this.dao = dao;

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = view.getIdField().getText();
                String name = view.getNameField().getText();
                try {
                    dao.addPerson(new Person(id, name));
                    refreshTable();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                    int selectedRow = view.getTable().getSelectedRow();
                    if (selectedRow != -1) {
                        String id = (String) view.getTable().getModel().getValueAt(selectedRow, 0);
                        String newName = JOptionPane.showInputDialog(view.getFrame(), "New Name for ID " + id);
                        if (newName != null && !newName.trim().isEmpty()) {
                            try {
                                Person person = new Person(id, newName);
                                dao.updatePerson(person);
                                refreshTable();
                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
        }
    });



        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getTable().getSelectedRow();
                if (selectedRow != -1) {
                    String id = (String) view.getTable().getValueAt(selectedRow, 0);
                    try {
                        dao.deletePerson(id);
                        refreshTable();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        view.getFrame().add(buttonPanel);
    }

    public void refreshTable() {
        List<Person> people;
        try {
            people = dao.getAllPeople();
            view.updateTable(people);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
