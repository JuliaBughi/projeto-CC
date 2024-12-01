package Agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AlertFlowAgent implements Runnable{

    private String server_ip;
    private int server_port = 6666;
    private String device_id;

    private BufferedReader in;
    private PrintWriter out;

    public AlertFlowAgent(String server_ip, String device_id){
        this.server_ip = server_ip;
        this.device_id = device_id;
    }

    public void run() {
        try {

            Socket socket = new Socket(server_ip, server_port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            out.println(device_id);
            out.flush();

            String response = in.readLine();
            System.out.println("AF: "+response);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
