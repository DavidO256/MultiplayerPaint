package paint.ui;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import paint.client.Player;
import paint.net.Client;
import paint.net.Server;
import paint.server.Host;

import java.io.IOException;

public class SetupController {
    @FXML
    public TextField clientIP, clientPort, serverPort, clientName, serverName;
    private Client client = null;
    private Server server = null;

    private TextFormatter<String> makeFormatter(String regex) {
        return new TextFormatter<String>(change -> {
                change.setText(change.getText().replaceAll(regex, ""));
                return change;
        });
    }

    public void initialize() {
        clientPort.setTextFormatter(makeFormatter("\\D"));
        serverPort.setTextFormatter(makeFormatter("\\D"));
        clientName.setTextFormatter(makeFormatter("[^a-z|^A-Z|^0-9]"));
        serverName.setTextFormatter(makeFormatter("[^a-z|^A-Z|^0-9]"));
        clientIP.setTextFormatter(makeFormatter("[^a-z|^A-Z|^0-9|^\\.]"));
    }

    private boolean checkEmpty(TextField ... fields) {
        boolean valid = true;
        for(TextField f : fields) {
            if (f.getText().isEmpty()) {
                f.getStyleClass().add("error");
                valid = false;
            } else
                f.getStyleClass().remove("error");
        }
        return valid;
    }

    private boolean checkClient() {
        return checkEmpty(clientIP, clientPort, clientName);
    }

    private boolean checkServer() {
        return checkEmpty(serverPort, serverName);
    }

    public void startClient() {
        if(checkClient()) {
            try {
                Client c = new Client(clientIP.getText(), clientPort.getText(), clientName.getText());
                if (c.isConnected()) {
                    client = c;
                    clientName.getParent().getScene().getWindow().hide();
                }
            } catch (IOException e) {
                e.printStackTrace();
                clientIP.getStyleClass().add("error");
                clientPort.getStyleClass().add("error");
            }
        }
    }

    public void startServer() {
        if(checkServer()) {
            try {
                Server s = new Server(serverPort.getText(), serverName.getText());
                if (s.isOpen()) {
                    server = s;
                    serverName.getParent().getScene().getWindow().hide();
                }
            } catch(IOException e) {
                e.printStackTrace();
                serverPort.getStyleClass().add("error");
            }
        }
    }

    public Player createPlayer(Stage stage) {
        return new Player(client, stage, clientName.getText());
    }

    public Host createHost(Stage stage) {
        return new Host(server, stage);
    }

    public boolean isServer() {
        return server != null;
    }

    public boolean isClient() {
        return client != null;
    }
}
