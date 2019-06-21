package paint;

import java.io.IOException;
import java.net.Socket;

public class Utilities {

    public static void writeSocket(Socket socket, String message) throws IOException {
        socket.getOutputStream().write(message.getBytes());
    }

    public static String[] readSocket(Socket socket, long timeLimit) throws IOException {
        return readSocket(socket, true, timeLimit);
    }

    public static String[] readSocket(Socket socket, boolean block) throws IOException {
        return readSocket(socket, block, -1);
    }

    public static String[] readSocket(Socket socket, boolean block, long timeLimit) throws IOException {
        if(block) {
            long start = System.currentTimeMillis();
            while (socket.getInputStream().available() == 0) {
                if(timeLimit > 0 && start + timeLimit <= System.currentTimeMillis())
                    return new String[0];
            }
        }
        byte[] raw = new byte[socket.getInputStream().available()];
        socket.getInputStream().read(raw);
        return new String(raw).split("\n");
    }
}
