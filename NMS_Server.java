import java.io.*;
import java.net.*;
import com.google.gson.Gson;

public class NMS_Server {

    // Porta para UDP (NetTask)
    private static final int UDP_PORT = 9876;

    // Porta para TCP (AlertFlow)
    private static final int TCP_PORT = 6789;

    public static void main(String[] args) {
        // Iniciar threads para UDP e TCP
        new Thread(NMS_Server::startUDPServer).start();
        new Thread(NMS_Server::startTCPServer).start();
    }

    // Método para iniciar o servidor UDP (NetTask)
    private static void startUDPServer() {
        try{
            DatagramSocket udpServer = new DatagramSocket(UDP_PORT);
            byte[] receiveData = new byte[1024];

            while (true) {
                // ler o pacote mandado do cliente
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpServer.receive(receivePacket);

                // para cada pacote recebido arrancar uma thread para lidar com o cliente
                // clientHandler(buffer).run() seria algo deste genero

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("UDP Server received: " + message);

                // Responder com ACK
                String ack = "UDP ACK from server";
                byte[] sendData = ack.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                udpServer.send(sendPacket);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Método para iniciar o servidor TCP (AlertFlow)
    private static void startTCPServer() {
        try {
            ServerSocket tcpServer = new ServerSocket(TCP_PORT);  // o giga meteu que podia ser StreamSocket (ver as diferencas)
            while (true) {
                Socket clientsSocket = tcpServer.accept();
                //ler o conteúdo do alertflow
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientsSocket.getInputStream()));

                // podemos ter um print para confirmar as coisas
                String clientMessage = inFromClient.readLine();
                System.out.println("TCP Server received: " + clientMessage);

                // Responder com ACK
                OutputStream ack = clientsSocket.getOutputStream();
                //tem de se criar um paco te de ack para mandar como resposta

                //ack.write(pacote.getBytes());

                clientsSocket.close();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    //O que vem no pacote
    //porta de origem/destino, ip origem/destino, payload(é mandado para o parser)
    // Para o ack:
    // tem de se inverter a source e a destination

    // o socket do servidor so serve para receber
    // tem de se criar sockets para mandar as merdas e depois fechar


    private static ListTasks Jsonparser(String file_path) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file_path)) {
            ListTasks listTasks = gson.fromJson(reader, ListTasks.class);
            listTasks.tasks.forEach(task -> {
                System.out.println("Task ID: " + task.task_id);
                System.out.println("Frequency: " + task.frequency);
                task.devices.forEach(device -> {
                    System.out.println("Device ID: " + device.device_id);
                    System.out.println("CPU Usage Monitoring: " + device.device_metrics.cpu_usage);
                    // Continue extraindo outras propriedades conforme necessário
                });
            });

            return listTasks;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }


}

