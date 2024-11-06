package src.main.java.org.example.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import src.main.java.org.example.Packet.*;
import src.main.java.org.example.Task.Task;

public class ClientHandler implements Runnable {
        private final DatagramPacket receivePacket;
        private final NetTaskServer server;

        public ClientHandler(DatagramPacket receivePacket, NetTaskServer server) {
            this.receivePacket = receivePacket;
            this.server = server;
        }

        @Override
        public void run() {
            DatagramSocket responseSocket = null;
            try {
                //Create a new socket for sending the response
                responseSocket = new DatagramSocket();

                //Extract client message  ver porque agora não tem o BytesToUDPPacket
                Packets.NetTaskPacket clientMessage = Packets.NetTaskPacket.BytesToUDPPacket(receivePacket.getData());

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                if(clientMessage.getAck()==0 && clientMessage.getTask()==null){ //quer dizer que é a ligação do cliente ao servidor
                    this.server.addDevice(clientAddress, clientMessage.getDevice_id());

                    List<Task> tasksForDevice = server.getTasksForDevice(clientMessage.getDevice_id());

                    //envia task para client
                    if (taskForDevice != null) {
                        byte[] sendData = taskForDevice.toString().getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                        responseSocket.send(sendPacket);
                    }
                }
                else if (clientMessage.getAck() == 1) {
                    System.out.println("ACK recebido do cliente: " + clientMessage.getDevice_id());
                }
                //else {  é um ack (não sei o que é suposto fazer com o ack)

                //}
                //Generate a response

            /* Send response to client using the new socket

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            responseSocket.send(sendPacket);
            */
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (responseSocket != null && !responseSocket.isClosed()) {
                    responseSocket.close();
                }
            }
        }
    }

