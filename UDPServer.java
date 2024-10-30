import java.io.*;
import java.net.*;
import com.google.gson.Gson;

//USADO APENAS COMO BASE, NÃO FUNCIONA NO CORE
public class UDPServer {

    public static void main(String[] args) {
        // Iniciar threads para UDP e TCP
        new Thread(UDPServer::startUDPServer).start();
    }


    private static void startUDPServer() {
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(9876);
            System.out.println("UDP Server running and waiting for clients");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                socket.receive(receivePacket);

                //inicia uma nova thread para lidar com a mensagem recebida
                new Thread(new ClientHandler(receivePacket)).start();
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

    public ClientHandler(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }


    @Override
    public void run() {
        DatagramSocket responseSocket = null;
        try {
            //Create a new socket for sending the response
            responseSocket = new DatagramSocket();

            //Extract client message
            String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Client received: " + clientMessage);
            String[] parts = clientMessage.split(",");
            if (parts.length ==  3) {
                String userId = parts[0];
                String timestamp = parts[1];
                String message = parts[2];

                //Display the message

                System.out.println("User: " + userId + ", Timestamp: " + timestamp + ", Message: " + message);

                //Generate a response
                String responseMessage = "Server received your message: " + message;
                byte[] sendData = responseMessage.getBytes();

                //Send response to client using the new socket
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                responseSocket.send(sendPacket);
            } else {
                System.out.println("Server received an invalid message");
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