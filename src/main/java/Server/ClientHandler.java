package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import Packet.*;

import Packet.*;
import Task.Task;

public class ClientHandler implements Runnable {
        private final NetTaskPacket receivePacket;
        private final NetTaskServer server;
        private InetAddress clientAddress;
        private int clientPort;


    public ClientHandler(NetTaskPacket receivePacket, NetTaskServer server, InetAddress clientAddress, int clientPort) {
            this.receivePacket = receivePacket;
            this.server = server;
            this.clientAddress = clientAddress;
            this.clientPort = clientPort;
        }

    @Override
    public void run() {
        DatagramSocket responseSocket = null;
        try {
            responseSocket = new DatagramSocket();


            //podemos assumir que é logo a primeira ligação
            this.server.addDevice(clientAddress, receivePacket.getDevice_id());

            List<Task> tasksForDevice = Task.getTasksForDevice(receivePacket.getDevice_id(), server.getTaskList());

            if(tasksForDevice.isEmpty()){
                // se não houver tasks para mandar fecha-se a ligação?
                // provavelmente tinha de mandar um pacote ao cliente a dizer para fechar a ligação
                //mete-se o ack=2 para o cliente saber que é para terminar a ligação
                NetTaskPacket newPacket = new NetTaskPacket(2, receivePacket.getDevice_id(), 1, tasksForDevice);
                String serverResponse = NetTaskPacket.NetTaskPacketToString(newPacket);
                byte[] sendData = serverResponse.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                responseSocket.send(sendPacket);

                System.out.println("No tasks to send to client "+ receivePacket.getDevice_id());
                System.out.println("Closing connection...");
            }
            else{

                int tentativas = 0;
                int maxTentativas = 5;
                boolean ackRecebido = false;

                //ciclo para retransmissão do pacote
                while(tentativas < maxTentativas && !ackRecebido){

                    try{
                        //envio do pacote
                        NetTaskPacket newPacket = new NetTaskPacket(1, receivePacket.getDevice_id(), 1, tasksForDevice);
                        String serverResponse = NetTaskPacket.NetTaskPacketToString(newPacket);
                        byte[] sendData = serverResponse.getBytes();

                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                        responseSocket.send(sendPacket);

                        System.out.println("Tasks sent to client "+ receivePacket.getDevice_id());
                        for(Task t : tasksForDevice)
                            System.out.println("Task "+t.getTask_id());

                        responseSocket.setSoTimeout(5000);
                        byte[] buffer = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                        responseSocket.receive(receivePacket);

                        String ackResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        NetTaskPacket receivedPacket = NetTaskPacket.StringToNetTaskPacket(ackResponse);

                        if(receivedPacket.getAck() == 1){
                            ackRecebido = true;
                        }
                    } catch (SocketTimeoutException e) {
                        tentativas++;
                    }

                }



                //ciclo para coleta das métricas
                while(true){

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

