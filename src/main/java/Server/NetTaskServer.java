package Server;

import java.net.*;
import java.util.*;
import java.io.*;

import Packet.NTReceiver;
import Packet.NetTaskPacket;
import Task.*;

public class NetTaskServer implements Runnable {
    private Map<InetAddress,String> mapDevices = new HashMap<>();  // par ip->device_id
    private final List<Task> taskList; //lista de tarefas carregadas do json
    private final int UDP_PORT = 9876;

    public NetTaskServer(String filepath) throws IOException {
        this.taskList = Task.jsonReader(filepath);
        System.out.println("Json file loaded");
    }

    public void addDevice(InetAddress ip, String device_id){
        this.mapDevices.put(ip, device_id);
    }

    public List<Task> getTaskList(){
        return this.taskList;
    }

    public void run() {
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(UDP_PORT);
            NTReceiver receiver = new NTReceiver(socket);

            while (true) {
                NetTaskPacket helloMessage = receiver.receive(1);

                System.out.println("Client " + helloMessage.getDevice_id() + " connected");

                new Thread(new ClientHandlerNT(helloMessage, this, receiver.getSenderAddress(), receiver.getSenderPort())).start();
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            if (socket != null && !socket.isClosed()) { //isto está certo?
                socket.close();
            }
        }
    }
}