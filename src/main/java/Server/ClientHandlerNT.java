package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import Packet.*;
import Task.Task;

public class ClientHandlerNT implements Runnable {

        private final NetTaskPacket helloPacket;
        private final InetAddress clientAddress;
        private final int clientPort;
        private String device_id;
        private int nr_seq=2;
        private NTSender sender;
        private NTReceiver receiver;

    public ClientHandlerNT(NetTaskPacket helloPacket, InetAddress clientAddress, int clientPort) {
            this.helloPacket = helloPacket;
            this.clientAddress = clientAddress;
            this.clientPort = clientPort;
        }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            sender = new NTSender(socket);
            receiver = new NTReceiver(socket);

            device_id = helloPacket.getDevice_id();

            NetTaskServer.addDevice(clientAddress, helloPacket.getDevice_id());
            System.out.println("Received hello packet from "+device_id);
            List<Task> tasksForDevice = Task.getTasksForDevice(device_id, NetTaskServer.getTaskList());


            if(tasksForDevice.isEmpty()){ // fechar a ligação se não há tasks para mandar
                sender.sendData("",clientAddress,clientPort,nr_seq, device_id, -1);
                System.out.println("No tasks to send to client "+ device_id);
                System.out.println("Closing connection...");
            }
            else{
                String tasks = Task.TasksToString(tasksForDevice,device_id);
                nr_seq = sender.sendData(tasks,clientAddress,clientPort,nr_seq,device_id,1);
                System.out.println("Tasks sent to client "+ device_id);
                System.out.println("Waiting for metrics from "+ device_id);


                while(true){
                    socket.setSoTimeout(20000);
                    NetTaskPacket packet = receiver.receive(nr_seq);
                    NMS_Server.addMetric(packet.getDevice_id(), new MetricNT(packet.getDevice_id(),packet.getType(),packet.getData()));
                    System.out.println("Metric from "+device_id + ": "+packet.getData() + " type: "+packet.getType());
                    nr_seq = packet.getNr_seq() + 1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(socket != null && !socket.isClosed()){
                System.out.println("No tasks received, closing connection...");
                socket.close();
            }
        }
    }
}

