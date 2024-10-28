import java.io.*;
import java.net.*;
public class NMS_Agent {

    // Endereço e portas do servidor
    private static final String SERVER_ADDRESS = "localhost";
    private static final int UDP_PORT = 9876;
    private static final int TCP_PORT = 6789;

    public static void main(String[] args) throws Exception {
        // Iniciar comunicação UDP e TCP
        sendUDPMessage("Hello from UDP Agent");
        sendTCPMessage("Alert: Critical condition from TCP Agent");
    }

    // Método para enviar mensagem via UDP
    private static void sendUDPMessage(String message) {
        try (DatagramSocket udpSocket = new DatagramSocket()) {
            InetAddress ipAddress = InetAddress.getByName(SERVER_ADDRESS);
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, UDP_PORT);
            udpSocket.send(sendPacket);

            // Receber resposta do servidor
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpSocket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("UDP Agent received: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para enviar mensagem via TCP
    private static void sendTCPMessage(String message) {
        try (Socket tcpSocket = new Socket(SERVER_ADDRESS, TCP_PORT)) {
            OutputStream outToServer = tcpSocket.getOutputStream();
            outToServer.write((message + "\n").getBytes());

            // Receber resposta do servidor
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            String response = inFromServer.readLine();
            System.out.println("TCP Agent received: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
