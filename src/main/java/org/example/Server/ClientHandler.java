package org.example.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import org.example.Packet.*;

import org.example.Packet.*;
import org.example.Task.Task;

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
                responseSocket = new DatagramSocket();

                String packet = new String(receivePacket.getData(),0,receivePacket.getLength());
                NetTaskPacket clientMessage = NetTaskPacket.StringToNetTaskPacket(packet);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                //podemos assumir que é logo a primeira ligação
                this.server.addDevice(clientAddress, clientMessage.getDevice_id());

                List<Task> tasksForDevice = Task.getTasksForDevice(clientMessage.getDevice_id(), server.getTaskList());

                if(tasksForDevice.isEmpty()){
                    // se não houver tasks para mandar fecha-se a ligação?
                    // provavelmente tinha de mandar um pacote ao cliente a dizer para fechar a ligação
                    //mete-se o ack=2 para o cliente saber que é para terminar a ligação
                    NetTaskPacket newPacket = new NetTaskPacket(2, clientMessage.getDevice_id(), 1, tasksForDevice);
                    String serverResponse = NetTaskPacket.NetTaskPacketToString(newPacket);
                    byte[] sendData = serverResponse.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    responseSocket.send(sendPacket);
                }
                else{ // aqui o device_id já não importa porque ele já foi registado no map do server
                    NetTaskPacket newPacket = new NetTaskPacket(1, clientMessage.getDevice_id(), 1, tasksForDevice);
                    String serverResponse = NetTaskPacket.NetTaskPacketToString(newPacket);
                    byte[] sendData = serverResponse.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    responseSocket.send(sendPacket);

                    while(true){
                        // ver se recebe um ack(do cliente a dizer que recebeu as tarefas)
                        // ou se são dados enviados pelo cliente
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (responseSocket != null && !responseSocket.isClosed()) {
                    responseSocket.close();
                }
            }
        }
    }

