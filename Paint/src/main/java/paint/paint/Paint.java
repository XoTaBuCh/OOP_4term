
package paint.paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Paint extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Paint.class.getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMaxHeight(555);
        stage.setMaxWidth(1000);
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        launch(args);

    }
}
