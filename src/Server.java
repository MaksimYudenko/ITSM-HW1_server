import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Server() {
    }

    public static void main(String args[]) {
        while (true) new Server().run();
    }

    private void run() {
        try {
            serverSocket = new ServerSocket(1234);
            Socket connection = serverSocket.accept();
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            String clientGson = (String) in.readObject();
            Gson serverGson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> messages = serverGson.fromJson(clientGson, type);
            String name = messages.get("name");
            String message = messages.get("message");
            String newMessage = message.replaceFirst("server", name);
            Thread.sleep(1000);
            sendMessage(newMessage);
            System.out.println(newMessage);
        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                serverSocket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void sendMessage(String serverMessage) {
        try {
            out.writeObject(serverMessage);
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}