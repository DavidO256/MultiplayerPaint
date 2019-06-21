package paint.net.task;

import paint.Utilities;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ConnectionTask implements Callable<String> {
    public static String SUCCESS_RESPONSE = "1";
    private Socket socket;
    private String name;
    private String pid;

    public ConnectionTask(Socket socket, String pid, String name) {
        this.socket = socket;
        this.name = name;
        this.pid = pid;
    }

    @Override
    public String call() throws IOException {
        Utilities.writeSocket(socket, pid + "\n" + name);
        String[] messages = Utilities.readSocket(socket, true);
        System.out.println(String.join(",", messages));
        return messages.length > 0 ? messages[0] : "";
    }

}
