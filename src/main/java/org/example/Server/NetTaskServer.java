package src.main.java.org.example.Server;

import java.net.*;
import java.util.*;

import org.example.Task.*;
import org.example.Packet.*;

//USADO APENAS COMO BASE, NÃO FUNCIONA NO CORE
public class NetTaskServer {
    /*
    public static void main(String[] args) {   ---- acho que isto não é preciso
        // Iniciar threads para UDP e TCP
        new Thread(UDPServer::startUDPServer).start();
    }*/

    private Map<InetAddress,String> mapDevices;  // par ip->device_id

    public void addDevice(InetAddress ip, String device_id){
        this.mapDevices.put(ip, device_id);
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