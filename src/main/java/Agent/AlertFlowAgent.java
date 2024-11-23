package Agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AlertFlowAgent implements Runnable{

    public void run() {
        try {
            String server_ip = "localhost";
            int server_socket = 6666;
            Socket socket = new Socket(server_ip, server_socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
