package org.example.Agent;

import java.net.*;
import java.util.Scanner;
import org.example.Packet.*;

public class NetTaskAgent implements Runnable{

    private String client_ip;
    private String device_id;
    private String server_ip;
    private int server_socket;

    public NetTaskAgent(String client_ip, String device_id, String server_ip,int server_socket){
        this.client_ip = client_ip;
        this.device_id = device_id;
        this.server_ip = server_ip;
        this.server_socket = server_socket;
    }


    public void run(){
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(server_ip); //o stor falou sobre colocar também o 10.0.0...

            //envio do registo para o servidor ver como é que se tem de mandar o ack 0 de registo para o sv
            NetTaskPacket packet = new NetTaskPacket(1,device_id,0,null);
            String message = NetTaskPacket.NetTaskPacketToString(packet);
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, server_socket);
            socket.send(sendPacket);
            // aqui foi enviado o registo

            while(true){
                //Receive response from server
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                String aux = new String(receivePacket.getData(),0, receivePacket.getLength());
                NetTaskPacket received = NetTaskPacket.StringToNetTaskPacket(aux);



            }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if(socket != null && socket.isClosed()){}
        }
    }
}
