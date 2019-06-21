package paint.net;

import paint.Utilities;
import paint.client.Player;
import paint.net.task.ConnectionTask;
import paint.net.task.RequestTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class Server {
    public static final String ASSIGNMENT_PREFIX = "A>",
        JOIN_MESSAGE = "JOIN", PLAYER_LIST_MESSAGE = "PLAYERS";
    private Map<String, Socket> connections;
    private Map<String, String> players;
    private ExecutorService service;
    private Thread connectionThread;
    private ServerSocket server;
    private String name;

    public Server(String port, String name) throws IOException {
        this(Integer.parseInt(port), name);
    }

    public Server(int port, String name) throws IOException {
        this.name = name;
        connections = new HashMap<String, Socket>();
        players = new HashMap<String, String>();
        connectionThread = new Thread(this::listenConnections);
        service = Executors.newSingleThreadExecutor();
        server = new ServerSocket(port);
    }

    private void listenConnections() {
        while(!server.isClosed()) {
            try {
                String pid = UUID.randomUUID().toString();
                Socket socket = server.accept();
                String player = service.submit(new ConnectionTask(socket, pid, name)).get(5, TimeUnit.SECONDS);
                if(!player.isEmpty()) {
                    broadcast(formatAssignment(JOIN_MESSAGE, pid, player));
                    //Utilities.writeSocket(socket, formatAssignment(PLAYER_LIST_MESSAGE, players.toString()));
                    synchronized (this) {
                        players.put(pid, player);
                        connections.put(pid, socket);
                    }
                }
            } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void broadcast(String message) {

    }

    private String formatAssignment(String key, String ... values) {
        return ASSIGNMENT_PREFIX + key + ":" + String.join(",", values);

    }

    public void start() throws IOException {
        connectionThread.start();
    }

    public void close() {
        service.shutdown();
        connectionThread.interrupt();
    }

    public boolean isOpen() {
        return !server.isClosed();
    }
}
