package dam.interfaces.uf2act3_agenda;

import dam.interfaces.uf2act3_agenda.controller.DialogUserEditController;
import dam.interfaces.uf2act3_agenda.controller.RootController;
import dam.interfaces.uf2act3_agenda.controller.UserMenuController;
import dam.interfaces.uf2act3_agenda.model.Person;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * Main class of the application.
 *
 * @author jkutkut
 */
public class MainApp extends Application {
    // ********** Constants **********
    public static final String APP_NAME = "Contacts";
    private static final String LOGO = "file:src/main/resources/dam/interfaces/uf2act3_agenda/images/logo.png";
    private static final String APP_XML = "view/app.fxml";
    private static final String USER_MENU_XML = "view/userMenu.fxml";
    private static final String MENU_XML = "view/dialog/userEdit.fxml";

    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 500;

    // ********** Attributes **********
    private ObservableList<Person> personData;

    // ********** UI **********
    private Stage primaryStage;
    private BorderPane rootLayout;
    private AnchorPane userMenu;

    public MainApp() {
        initComponents();
    }

    /**
     * Initializes components needed by the application.
     *
     * Note: The debug logic will be removed in future versions when persistence is implemented.
     */
    private void initComponents() {
        personData = FXCollections.observableArrayList();
        // TODO DEBUG
        personData.add(new Person("Hans", "Muster", "Musterstrasse 1", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Ruth", "Mueller", "Musterstrasse 2", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Heinz", "Kurz", "Musterstrasse 3", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Cornelia", "Meier", "Musterstrasse 4", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Werner", "Meyer", "Musterstrasse 5", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Lydia", "Kunz", "Musterstrasse 6", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Anna", "Best", "Musterstrasse 7", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Stefan", "Meier", "Musterstrasse 8", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Martin", "Mueller", "Musterstrasse 9", 12345, "Musterstadt", 2000, 1, 1));
    }

    // ********** App Methods **********

    /**
     * Starts the window of the application.
     * @param stage The stage to be shown.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        stage.setTitle(APP_NAME);
        primaryStage.getIcons().add(new Image(LOGO));
        loadLayouts();
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setScene(new Scene(rootLayout));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    // ********** Menu logic **********

    /**
     * Loads all the layouts needed for the basic application.
     * @throws IOException If the FXML file cannot be loaded.
     */
    private void loadLayouts() throws IOException {
        // App body
        FXMLLoader appLoader = new FXMLLoader(MainApp.class.getResource(APP_XML));
        rootLayout = appLoader.load();
        RootController appController = appLoader.getController();
        appController.setMainApp(this);

        // User menu
        FXMLLoader userMenuLoader = new FXMLLoader(MainApp.class.getResource(USER_MENU_XML));
        userMenu = userMenuLoader.load();
        UserMenuController controller = userMenuLoader.getController();
        controller.setMainApp(this);

        // Default menu
        rootLayout.setCenter(userMenu);
    }

    // ********** Getters **********
    public Window getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    // ********** UX Methods **********

    /**
     * Opens a dialog window to edit (or create) a person.
     * @param person The person to be edited (or created if empty).
     * @return True if the form was submitted successfully. False otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource(MENU_XML)
            );

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            DialogUserEditController userEdit = loader.getController();
            userEdit.setDialogStage(dialogStage);
            userEdit.setPerson(person);
            userEdit.showAndWait();
            return userEdit.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Standard method to show a message dialog to the user.
     *
     * @param window The window to be shown on top of.
     * @param type   The type of the dialog.
     * @param title  The title of the dialog.
     * @param header The header of the dialog.
     * @param msg    The content of the dialog.
     * @return The button pressed by the user.
     */
    public static Optional<ButtonType> showAlert(Window window, Alert.AlertType type, String title, String header, String msg) {
        Alert alert = new Alert(type);
        alert.initOwner(window);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        return alert.showAndWait();
    }

    /**
     * Standard method to show a message dialog to the user.
     * @param type The type of the dialog.
     * @param title The title of the dialog.
     * @param header The header of the dialog.
     * @param content The content of the dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        showAlert(primaryStage, type, title, header, content);
    }

    public void info(String title, String header, String content) {
        showAlert(Alert.AlertType.INFORMATION, title, header, content);
    }

    /**
     * Show an error mesaage to the user.
     * @param title The title of the dialog.
     * @param header The header of the dialog.
     * @param msg The content of the dialog.
     */
    public void warn(String title, String header, String msg) {
        showAlert(Alert.AlertType.WARNING, title, header, msg);
    }

    /**
     * Show an information mesaage to the user.
     * @param title The title of the dialog.
     * @param header The header of the dialog.
     * @param msg The content of the dialog.
     */
    public void error(String title, String header, String msg) {
        showAlert(Alert.AlertType.ERROR, title, header, msg);
    }

    /**
     * Confirm something with the user.
     * @param title The title of the dialog.
     * @param header The header of the dialog.
     * @param msg The content of the dialog.
     * @return True if the user confirmed. False otherwise.
     */
    public boolean confirm(String title, String header, String msg) {
        Optional<ButtonType> result = showAlert(primaryStage, Alert.AlertType.CONFIRMATION, title, header, msg);
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // ********** Preferences **********
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) { // TODO integrate with the logic
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle(String.format(
                "%s - %s",
                APP_NAME,
                file.getName()
            ));
        } else {
            prefs.remove("filePath");
            primaryStage.setTitle(APP_NAME);
        }
    }

    // ********** File IO **********
    public void loadPeople(File f) {
        // TODO remove debug values from initcomponents
        // TODO read file
        // TODO open last session on init?
        personData.add(new Person("Hans", "Muster", "Musterstrasse 1", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Ruth", "Mueller", "Musterstrasse 2", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Heinz", "Kurz", "Musterstrasse 3", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Cornelia", "Meier", "Musterstrasse 4", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Werner", "Meyer", "Musterstrasse 5", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Lydia", "Kunz", "Musterstrasse 6", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Anna", "Best", "Musterstrasse 7", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Stefan", "Meier", "Musterstrasse 8", 12345, "Musterstadt", 2000, 1, 1));
        personData.add(new Person("Martin", "Mueller", "Musterstrasse 9", 12345, "Musterstadt", 2000, 1, 1));
    }

    public void savePeople(File f) {
        // TODO write file
    }
}