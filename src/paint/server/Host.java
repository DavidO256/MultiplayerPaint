package paint.server;

import javafx.stage.Stage;
import paint.net.Server;

import java.io.IOException;

public class Host {
    private Server server;

    public Host(Server server, Stage stage) {
        this.server = server;
    }

    public void run() throws IOException {
        server.start();
    }
}
