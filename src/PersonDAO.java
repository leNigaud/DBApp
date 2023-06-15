import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private final String USER = "root";
    private final String PASS = "";

    public void addPerson(Person person) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO person (id, name) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, person.getId());
            ps.setString(2, person.getName());
            ps.executeUpdate();
        }
    }

    public void updatePerson(Person person) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        String editQuery = "UPDATE person SET name = ? WHERE id = ?";
        PreparedStatement editStatement = conn.prepareStatement(editQuery);
        editStatement.setString(1, person.getName());
        editStatement.setString(2, person.getId());
        editStatement.executeUpdate();
    }
    
    

    public void deletePerson(String id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "DELETE FROM person WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public List<Person> getAllPeople() throws SQLException {
        List<Person> people = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM person";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                people.add(new Person(rs.getString("id"), rs.getString("name")));
            }
        }
        return people;
    }
}
