import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Server {
    private static final int PORT = 54321;
    private static final int DELAY = 1000;
    private ServerSocket serverSocket;
    private ObjectOutputStream out;

    private Server() {}

    public static void main(String args[]) {
        while (true) new Server().run();
    }

    private void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            Socket connection = serverSocket.accept();
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            String message = (String) in.readObject();
            List<String> stringList = Pattern.compile("[\",:}{]")
                    .splitAsStream(message.replaceAll("[\"\\r\\n,:}{]", ""))
                    .collect(Collectors.toList());
            String[] words = stringList.toString().split("\\s");
            String newMessage = "{\n\"message\": \"" + words[3] + ", " + words[1] + "\"\n}";
            Thread.sleep(DELAY);
            System.out.println(newMessage);
            sendMessage(newMessage);
        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {serverSocket.close();}
            catch (IOException ioe) {ioe.printStackTrace();}
        }
    }

    private void sendMessage(String serverMessage) {
        try {
            out.writeObject(serverMessage);
            out.flush();
        } catch (IOException ioException) {ioException.printStackTrace();}
    }

}
