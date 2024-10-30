import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import java.util.*;
import Packet.*;

//USADO APENAS COMO BASE, NÃO FUNCIONA NO CORE
public class UDPServer {
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


class ClientHandler implements Runnable {
    private DatagramPacket receivePacket;
    private UDPServer server;

    public ClientHandler(DatagramPacket receivePacket, UDPServer server) {
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

// ao iniciar o cliente também temos de por o seu ip, o ip do server, socket do server e o seu nome ("r1")
// o nome do cliente tem de ser mandado para o server para ele depois saber para onde manda as tarefas
// fazer um hashmap com nome-ip para o server saber mandar as tarefas

// ao iniciar o server temos de lhe passar o ip