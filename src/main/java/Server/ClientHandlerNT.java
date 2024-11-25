package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import Packet.*;
import Task.Task;

public class ClientHandlerNT implements Runnable {
        private final NetTaskPacket helloPacket;
        private final NetTaskServer server;
        private final InetAddress clientAddress;
        private final int clientPort;

        private String device_id;


    public ClientHandlerNT(NetTaskPacket helloPacket, NetTaskServer server, InetAddress clientAddress, int clientPort) {
            this.helloPacket = helloPacket;
            this.server = server;
            this.clientAddress = clientAddress;
            this.clientPort = clientPort;
        }

    @Override
    public void run() {
        DatagramSocket responseSocket = null;
        try {
            responseSocket = new DatagramSocket();
            NTSender sender = new NTSender(responseSocket);

            device_id = helloPacket.getDevice_id();

            //podemos assumir que é logo a primeira ligação
            this.server.addDevice(clientAddress, helloPacket.getDevice_id()); //aqui não é preciso gerir concorrência?

            List<Task> tasksForDevice = Task.getTasksForDevice(device_id, server.getTaskList());

            if(tasksForDevice.isEmpty()){ // fechar a ligação se não há tasks para mandar
                sender.sendData("",clientAddress,clientPort,2, device_id, -1);

                System.out.println("No tasks to send to client "+ device_id);
                System.out.println("Closing connection...");
            }
            else{
                String tasks = Task.TasksToString(tasksForDevice,device_id);
                sender.sendData(tasks,clientAddress,clientPort,2,device_id,1);

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

