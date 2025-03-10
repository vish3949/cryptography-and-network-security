import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(serverAddress, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server at " + serverAddress + ":" + port);

            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println("Server: " + serverResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            }).start();

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
        } catch (IOException e) {
            System.err.println("Error in client: " + e.getMessage());
        }
    }
}