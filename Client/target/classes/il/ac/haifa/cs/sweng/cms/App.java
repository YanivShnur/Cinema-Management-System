package il.ac.haifa.cs.sweng.cms;

import il.ac.haifa.cs.sweng.cms.common.entities.Cinema;
import il.ac.haifa.cs.sweng.cms.common.entities.PurpleBadge;
import il.ac.haifa.cs.sweng.cms.common.entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static OCSFClient ocsfClient;
    private static String host = "localhost";
    private static Integer port = 8080;
    // FIXME: Temporarily select the user type manually
    // FIXME: "Customer" or "Employee"
    private static int userPermission = 0; // Default - Customer
    private static String userType = "Employee";
    private static String firstName = "David";
    private static String username = "david_1990";
    private static String pass = "123";
    private static User user;
    private static PurpleBadge pb;
    private static Stage stage;


    /**
     * main method that load new scene to stage
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/UserLogin.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/PurchaseCancel.fxml"));
        Parent root = (Parent)loader.load();

        scene = new Scene(root, 640, 480);//new scene to load
        this.stage.setScene(scene);//set scene
//        stage.setMaximized(true); //set max size available
        this.stage.setTitle("Cinema");
        this.stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
        this.stage.show();//show stage

    }

    public static int connectToServer() throws IOException {
        String hostToConnect = getHost();
        Integer portToConnect = getPort();

        ocsfClient = new OCSFClient(hostToConnect, portToConnect);
        // status:
        //  0 - Already connected
        //  1 - Succeed
        // else - Failure
        int status = ocsfClient.openConnection();
        return status;
    }

    public static void disconnect() throws IOException {
        ocsfClient.closeConnection();
    }


    static public Integer getPort() {
        return port;
    }

    static public void setPort(int p) {
        port = p;
    }

    static public String getHost() {
        return host;
    }

    static public void setHost(String h) {
        host = h;
    }

    static public String getUserType() {
        return userType;
    }

    static public void setUser(String user) {
        username = user;
    }

    static public void setPass(String password) {
        pass = password;
    }

    static public String getPass() { return pass; }

    public static String getName() {
        return firstName;
    }

    /**
     * set new FXML to to show on screen
     * @param fxml
     * @throws IOException
     */
    static void setRoot(String fxml) throws  IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxmls/"+fxml));//FXML to load
        Parent root = (Parent)loader.load(); //load FXML to root object
        scene.setRoot(root);//set new root to scene
    }

    /**
     * main method calls start method to start rolling
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Sets the calling controller and returns the OCSFClient instance.
     * @return OCSFClient instance.
     */
    protected static OCSFClient getOcsfClient(Initializable controller) {
        ocsfClient.setController(controller);
        return ocsfClient;
    }

    public static int getUserPermission() {
        return userPermission;
    }

    static public void setUserPermission(int permission) {
        userPermission = permission;
    }

    public static String getUserName() {
        return username;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User usr) {
        user = usr;
    }

	/**
	 */
	public static void getPb() {
		ocsfClient.getPurpleBadge();
		
	}

	/**
	 * @param pb the pb to set
	 */
	public static void setPb(PurpleBadge pb) {
		ocsfClient.updatePurpleBadge(pb);
	}

	protected static Stage getStage() { return stage; }

}