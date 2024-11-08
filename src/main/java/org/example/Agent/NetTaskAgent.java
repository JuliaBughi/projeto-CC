package org.example.Agent;

import java.net.*;
import java.util.Scanner;
import org.example.Packet.*;

public class NetTaskAgent {

    private String client_ip;
    private String device_id;
    private String server_ip;
    private int server_socket;

    public void main(String args){
        DatagramSocket socket = null;
        try{
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter client Ip: ");
            this.client_ip = scanner.nextLine();

            System.out.println("Enter device id: ");
            this.device_id = scanner.nextLine();

            System.out.println("Enter server Ip: ");
            this.server_ip = scanner.nextLine();

            System.out.println("Enter server socket: ");
            this.server_socket = scanner.nextInt();

            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(server_ip); //o stor falou sobre colocar também o 10.0.0...

            //envio do registo para o servidor ver como é que se tem de mandar o ack 0 de registo para o sv
            NetTaskPacket packet = new NetTaskPacket(1,device_id,0,null);

            String message = NetTaskPacket.NetTaskPacketToString(packet);
            DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), serverAddress, server_socket);
            socket.send(sendPacket);
            // aqui foi enviado o registo

            while(true){
                /*
                //Send message to server
                byte[] sendData = clientMessage.getBytes();
                //aqui tem de preparar o pacote para mandar
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, server_socket);

                //Receive response from server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                //Print the server's response
                Packets.UDPPacket serverResponse = Packets.UDPPacket.BytesToUDPPacket(receivePacket.getData());

                //tem de se fazer a situação do ack
                */

            }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if(socket != null && socket.isClosed()){}
        }
    }
}
