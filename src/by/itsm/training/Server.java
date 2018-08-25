package by.itsm.training;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Server {
    private static final int PORT = 54321;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            while (true) {
                Socket connection = server.accept();
                new Thread(new ServerThread(connection)).start();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

class ServerThread implements Runnable {
    private static final int DELAY = 1000;
    private ObjectOutputStream out;
    private Socket socket;

    ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String message = (String) in.readObject();
            List<String> stringList = Pattern.compile("[\",:}{]")
                    .splitAsStream(message.replaceAll("[\"\\r\\n,:}{]", ""))
                    .collect(Collectors.toList());
            String[] words = stringList.toString().split("\\s");
            String name = words[1];
            String newMessage = "{\n\"message\": \"Hello, " + name + "\"\n}";
            Thread.sleep(DELAY);
            System.out.println(newMessage);
            sendMessage(newMessage);
        } catch (IOException | InterruptedException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                socket.close();
//                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace();
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

//using Gson:
            /*
            String clientGson = (String) in.readObject();
            Gson serverGson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> messages = serverGson.fromJson(clientGson, type);
            String name = messages.get("name");
            String message = messages.get("message");
            String newMessage = message.replaceFirst("server", name);
            */