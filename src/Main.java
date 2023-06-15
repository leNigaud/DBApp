public class Main {
    public static void main(String[] args) {
        DBAppView view = new DBAppView();
        PersonDAO dao = new PersonDAO();
        DBAppController controller = new DBAppController(view, dao);
        controller.refreshTable();
        view.getFrame().setVisible(true);
    }
}
