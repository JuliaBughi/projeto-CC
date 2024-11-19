package org.example.Agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AlertFlowAgent implements Runnable{
    private String server_ip;
    private int server_socket;

    AlertFlowAgent(String server_ip, int server_socket){
        this.server_ip = server_ip;
        this.server_socket = server_socket;
    }

    public void run() {
        try {
            Socket socket = new Socket(server_ip, server_socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());


            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
