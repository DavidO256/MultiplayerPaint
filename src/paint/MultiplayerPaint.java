package paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import paint.ui.SetupController;

import java.io.IOException;

public class MultiplayerPaint extends Application {

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/setup.fxml"));
        try {
            Stage setupStage = new Stage();
            setupStage.setScene(new Scene(loader.load()));
            System.out.println(getClass().getResource("/resources/setup.css").getFile());
            setupStage.getScene().getStylesheets().add(getClass().getResource("/resources/setup.css").toExternalForm());
            SetupController controller = loader.getController();
            controller.initialize();
            setupStage.showAndWait();
            setupStage.close();
            if (controller.isClient())
                controller.createPlayer(stage).run();
            if (controller.isServer())
                controller.createHost(stage).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(MultiplayerPaint.class);
    }
}
