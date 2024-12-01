package Agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class AlertFlowAgent implements Runnable{

    private String server_ip;
    private int server_port = 6666;
    private String device_id;
    private static BlockingQueue<String> AFQueue;
    private BufferedReader in;
    private PrintWriter out;

    public AlertFlowAgent(String server_ip, String device_id, BlockingQueue<String> queue){
        this.server_ip = server_ip;
        this.device_id = device_id;
        this.AFQueue = queue;
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

            try {
                while (true) {
                    String line = AFQueue.take();
                    System.out.println("AF: "+line);
                    out.println(line);
                    out.flush();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
