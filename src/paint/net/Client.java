package paint.net;

import paint.Utilities;
import paint.net.task.ConnectionTask;

import java.io.IOException;
import java.net.Socket;
import java.util.Stack;

public class Client {
    public static final long CONNECTION_TIME_LIMIT = 5000;
    private Stack<String> packets;
    private Thread packetThread;
    private Socket socket;
    private String uuid;
    private String name;

    public Client(String ip, String port, String name) throws IOException {
        this(ip, Integer.parseInt(port), name);
    }

    public Client(String ip, int port, String name) throws IOException {
        this.name = name;
        socket = new Socket(ip, port);
        uuid = establishConnection();
        packets = new Stack<String>();
        packetThread = new Thread(this::updatePackets);
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    private void updatePackets() {
        while(socket.isConnected()) {
            try {
                String[] incoming = Utilities.readSocket(socket, true);
                if (incoming.length > 0) {
                    synchronized (this) {
                        System.out.println(incoming);
                        for (String p : incoming)
                            packets.push(p);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String establishConnection() throws IOException {
        String[] messages = Utilities.readSocket(socket, CONNECTION_TIME_LIMIT);
        if(messages.length == 0) {
            socket.close();
            throw new IOException("No response.");
        } else {
            Utilities.writeSocket(socket, name);
            return messages[0];
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void start() {
        packetThread.start();
    }

    public void close() {
        packetThread.interrupt();
        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
