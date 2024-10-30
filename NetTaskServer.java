import java.net.*;
import java.io.*;

public class NetTaskServer implements Runnable {
    private static final int SERVER_PORT = 9876;
    private DatagramSocket socket;

    public NetTaskServer() throws SocketException {
        this.socket = new DatagramSocket(SERVER_PORT); // Criar o socket UDP
    }

    @Override
    public void run() {
        byte[] receiveData = new byte[1024];

        System.out.println("NetTask Server (UDP) is listening on port " + SERVER_PORT);

        while (true) {
            try {
                // Receber pacote de um NMS_Agent
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket); // Aguardar um pacote UDP

                // Extrair a mensagem recebida
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + message);

                // Separar a mensagem recebida para obter cliente, tarefa, número de sequência e tipo de mensagem
                String[] messageParts = message.split("\\|");
                String clientId = messageParts[0];
                String taskId = messageParts[1];
                String sequenceNumber = messageParts[2];
                String messageType = messageParts[3];

                // Confirmar se é uma mensagem de MÉTRICA
                if (messageType.equals("MÉTRICA")) {
                    System.out.println("Metrics received from " + clientId + " for " + taskId + " (Seq: " + sequenceNumber + ")");

                    // Enviar ACK de volta com o número de sequência recebido
                    String ackMessage = clientId + "|" + taskId + "|" + sequenceNumber + "|ACK";
                    byte[] ackData = ackMessage.getBytes();

                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();

                    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, clientAddress, clientPort);
                    socket.send(ackPacket); // Enviar o ACK para o cliente
                    System.out.println("Sent ACK for sequence: " + sequenceNumber);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Iniciar o servidor NetTask (UDP)
            Thread netTaskServerThread = new Thread(new NetTaskServer());
            netTaskServerThread.start(); // Rodar o servidor UDP em uma nova thread
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
