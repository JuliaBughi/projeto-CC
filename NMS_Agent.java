package org.example;

import java.io.*;
import java.net.*;
public class NMS_Agent {

    // Endereço e portas do servidor
    private static final String SERVER_ADDRESS = "localhost"; // por ser localhost não tem de se meter o IP?
    private static final int UDP_PORT = 9876;
    private static final int TCP_PORT = 6666;

    public static void main(String[] args) throws Exception {
        // Iniciar comunicação UDP e TCP
        sendUDPMessage("Hello from UDP Agent");
        sendTCPMessage("Alert: Critical condition from TCP Agent");
    }

    // Método para enviar mensagem via UDP
    // passar como argumento o pacote que se quer enviar para o server
    private static void sendUDPMessage(String message) throws IOException{

        DatagramSocket udpSocket = new DatagramSocket();
        InetAddress ipAddress = InetAddress.getByName(SERVER_ADDRESS);
        // aqui têm que se fazer o getBytes do pacote
        byte[] sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, UDP_PORT);
        udpSocket.send(sendPacket);

        // Receber resposta do servidor
        byte[] receiveData = new byte[1024]; // ver melhor depois este 1024
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        udpSocket.receive(receivePacket);
        // fazer o getdata para dar um pacote
        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        // pode-se deixar esta mensagem para ter feedback
        System.out.println("UDP Agent received: " + response);

    }

    // Método para enviar mensagem via TCP
    // mais uma vez tem que se dar como argumento o pacote
    private static void sendTCPMessage(String message) throws IOException {

        Socket clientSocket = new Socket(SERVER_ADDRESS, TCP_PORT);
        OutputStream outToServer = clientSocket.getOutputStream();
        // esta linha de baixo tem que ser adaptada ao pacote
        outToServer.write((message + "\n").getBytes());

        // Receber resposta do servidor
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        // passar a resposta para pacote
        String response = inFromServer.readLine();
        // pode-se deixar esta mensagem para ter feedback
        System.out.println("TCP Agent received: " + response);

    }
}
