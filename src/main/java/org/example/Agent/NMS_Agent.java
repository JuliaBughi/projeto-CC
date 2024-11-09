package org.example.Agent;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class NMS_Agent {

    // Endereço e portas do servidor
    private static final String SERVER_ADDRESS = "localhost";
    private static final int UDP_PORT = 9876;
    private static final int TCP_PORT = 6666;

    public void main(String[] args) throws Exception {
        // Iniciar comunicação UDP e TCP
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter client Ip: ");
        String client_ip = scanner.nextLine();

        System.out.println("Enter device id: ");
        String device_id = scanner.nextLine();

        System.out.println("Enter server Ip: ");
        String server_ip = scanner.nextLine();

        System.out.println("Enter server socket: ");
        int server_socket = scanner.nextInt();

        NetTaskAgent netTaskAgent = new NetTaskAgent(client_ip,device_id,server_ip,server_socket);

        new Thread(netTaskAgent).start();
        //depois deve ser preciso fazer outra par ao AlertFlow
    }




}
