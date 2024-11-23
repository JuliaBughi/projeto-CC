package Server;

import java.net.*;
import java.util.*;
import java.io.*;

import Packet.NetTaskPacket;
import Task.*;

public class NetTaskServer implements Runnable{
    private Map<InetAddress,String> mapDevices = new HashMap<>();  // par ip->device_id
    private final List<Task> taskList; //lista de tarefas carregadas do json
    private final int UDP_PORT = 9876;

    public NetTaskServer(String filepath) throws IOException {
        this.taskList = Task.jsonReader(filepath);
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
            System.out.println("UDP Server running and waiting for clients");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                String packet = new String(receivePacket.getData(),0,receivePacket.getLength());
                NetTaskPacket clientMessage = NetTaskPacket.StringToNetTaskPacket(packet);

                System.out.println("Client " + clientMessage.getDevice_id() + " connected");

                new Thread(new ClientHandler(clientMessage, this, receivePacket.getAddress(), receivePacket.getPort())).start();
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}

// ao iniciar o cliente também temos de por o seu ip, o ip do server, socket do server e o seu nome ("r1")
// o nome do cliente tem de ser mandado para o server para ele depois saber para onde manda as tarefas
// fazer um hashmap com nome-ip para o server saber mandar as tarefas

// ao iniciar o server temos de lhe passar o ip