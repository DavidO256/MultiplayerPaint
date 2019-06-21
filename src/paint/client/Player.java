package paint.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import paint.net.Client;
import paint.ui.PlayerController;

import java.io.IOException;

public class Player {
    private PlayerController controller;
    private Client client;
    private String name;
    private Stage stage;

    public Player(Client client, Stage stage, String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/player.fxml"));
        stage.setScene(new Scene(loader.load()));
        controller = loader.getController();
        this.client = client;
        this.stage = stage;
        this.name = name;
    }

    public void run() {
        stage.show();
    }
}
