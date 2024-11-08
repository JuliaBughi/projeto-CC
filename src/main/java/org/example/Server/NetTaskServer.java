package org.example.Server;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

import org.example.Task.*;
import org.example.Packet.*;

//USADO APENAS COMO BASE, NÃO FUNCIONA NO CORE
public class NetTaskServer {
    private Map<InetAddress,String> mapDevices = new HashMap<>();  // par ip->device_id
    private List<Task> taskList = new ArrayList<>(); //lista de tarefas carregadas do json

    public NetTaskServer(String filepath) throws IOException {
        //carregar tarefas ao iniciar servidor - não sei bem o que ficou decidido com o stor
        this.taskList = Task.jsonReader(filepath);
    }

    public void addDevice(InetAddress ip, String device_id){
        this.mapDevices.put(ip, device_id);

    }

    public List<Task> getTaskList(){
        return this.taskList; // ver depois se é preciso fazer um clone da lista
    }

    public String getDeviceByIp(InetAddress ip){
        return this.mapDevices.get(ip);
    }

    private void main(String[] args) {
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(9876);
            System.out.println("UDP Server running and waiting for clients");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                socket.receive(receivePacket);

                new Thread(new ClientHandler(receivePacket, this)).start();
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