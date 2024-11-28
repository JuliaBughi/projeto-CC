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

        private int nr_seq=2;


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
            NTReceiver receiver = new NTReceiver(responseSocket);

            device_id = helloPacket.getDevice_id();

            //podemos assumir que é logo a primeira ligação
            this.server.addDevice(clientAddress, helloPacket.getDevice_id()); //aqui não é preciso gerir concorrência?
            System.out.println("Received hello packet from "+device_id);
            List<Task> tasksForDevice = Task.getTasksForDevice(device_id, server.getTaskList());
            System.out.println(tasksForDevice.size());
            for(Task t: tasksForDevice)
                System.out.println(t.toString());


            if(tasksForDevice.isEmpty()){ // fechar a ligação se não há tasks para mandar
                sender.sendData("",clientAddress,clientPort,nr_seq, device_id, -1);

                System.out.println("No tasks to send to client "+ device_id);
                System.out.println("Closing connection...");
            }
            else{
                String tasks = Task.TasksToString(tasksForDevice,device_id);
                System.out.println(tasks);
                nr_seq = sender.sendData(tasks,clientAddress,clientPort,nr_seq,device_id,1);
                System.out.println(nr_seq);
                System.out.println("Tasks sent to client "+ device_id);

                //ciclo para coleta das métricas
                while(true){
                    NetTaskPacket packet = receiver.receive(nr_seq); //pacotes com metricas
                    nr_seq = packet.getNr_seq()+1;
                    // perceber como se vão guardar as metricas

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

