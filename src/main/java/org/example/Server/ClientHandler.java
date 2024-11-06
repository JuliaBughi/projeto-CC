package src.main.java.org.example.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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

                //Extract client message
                Packets.UDPPacket clientMessage = Packets.UDPPacket.BytesToUDPPacket(receivePacket.getData());

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                if(clientMessage.getAck()==0 && clientMessage.getTask()==null){ //quer dizer que é a ligação do cliente ao servidor
                    this.server.addDevice(clientAddress, clientMessage.getDevice_id());

                    //mandar ack e tarefas
                }
                else{ // é um ack (não sei o que é suposto fazer com o ack)

                }
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

