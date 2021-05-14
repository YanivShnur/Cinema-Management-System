
package il.ac.haifa.cs.sweng.cms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static Movie selectedFilmTitle ;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("ViewMovies"), 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Movie getSelectedFilmTitle() {
        return selectedFilmTitle;
    }

    public static void setSelectedFilmTitle(Movie selectedFilmTitle) {
        App.selectedFilmTitle = selectedFilmTitle;
    }

    public static void main(String[] args) {
        DB.main(args);
        launch();
    }

}
