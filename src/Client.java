
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private static String[] arguments;
    private Map<String, String> messages = new HashMap<>();

    private Client() {}

    public static void main(String args[]) {
        arguments = args;
        if (args.length == 0) {
            System.out.println("You should send a message to the server");
            System.exit(1);
        } else new Client().run();
    }
    private void run() {
        try {
            if (arguments.length == 0) {
                throw new RuntimeException("You should send a message to the server");
            }
            String name = arguments[0];
            clientSocket = new Socket("localhost", 1234);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            messages.put("name", name);
            messages.put("message", "Hello, server");
            Gson gson = new Gson();
            String clientGson = gson.toJson(messages);
            out.writeObject(clientGson);
            out.flush();
            System.out.println((String) in.readObject());
        } catch (RuntimeException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ioe) {ioe.printStackTrace();}
        }
    }

}