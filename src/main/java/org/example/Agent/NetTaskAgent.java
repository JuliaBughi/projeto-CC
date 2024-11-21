package org.example.Agent;

import java.net.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.example.Packet.*;
import org.example.Task.*;

public class NetTaskAgent implements Runnable{
    private String client_ip;
    private String device_id;
    private static String server_ip;
    private static int server_socket;
    private int nr_seq = 1;

    public NetTaskAgent(String client_ip, String device_id, String server_ip,int server_socket){
        this.client_ip = client_ip;
        this.device_id = device_id;
        this.server_ip = server_ip;
        this.server_socket = server_socket;
    }

    public void run(){
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(server_ip);

            NetTaskPacket packet = new NetTaskPacket(nr_seq,device_id,0,null);
            String message = NetTaskPacket.NetTaskPacketToString(packet);
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, server_socket);
            socket.send(sendPacket);
            // aqui foi enviado o registo

            //receber aqui a primeira resposta
            byte[] receivePacket = new byte[1024];
            DatagramPacket serverResponse = new DatagramPacket(receivePacket, receivePacket.length);
            socket.receive(serverResponse);
            String response = new String(serverResponse.getData(),0,serverResponse.getLength());
            NetTaskPacket serverMessage = NetTaskPacket.StringToNetTaskPacket(response);

            if(serverMessage.getAck()==1){ // se há tasks para o cliente fazer
                this.ScheduleMetricCollect(serverMessage, socket, serverAddress);
                // também tem que se fazer aqui a coleta das alertflow conditions
            }
            // se ack == 2 terminar ligação porque não há tasks para ele

        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        }
    }

    public void ScheduleMetricCollect(NetTaskPacket packet, DatagramSocket socket, InetAddress server_address){
        List<Task> l = packet.getTasks();

        for(Task t : l) {
            if (!t.getBandwidth().startsWith("*")) { // quer dizer que tem de fazer a bandwidth
                String[] parts = t.getBandwidth().split(",");
                // aqui tem que se o iperf periodicamente
                // a minha dúvida é como é que se faz em relação ao nr de sequencia
                schedulerIperf(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),parts[4], Integer.parseInt(parts[5]), "b", socket);
            }

            if (!t.getJitter().startsWith("*")) { // fazer o iperf para sacar o jitter
                String[] parts = t.getJitter().split(",");
                schedulerIperf(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),parts[4], Integer.parseInt(parts[5]), "j", socket);
            }

            if (!t.getPacketLoss().startsWith("*")){ // fazer o iperf para sacar o jitter
                String[] parts = t.getPacketLoss().split(",");
                schedulerIperf(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),parts[4], Integer.parseInt(parts[5]), "p", socket);
            }

            if (!t.getLatency().startsWith("*")){ // fazer o ping para sacar a latency
                String[] parts = t.getLatency().split(",");
                schedulerPing(parts[0],parts[1],Integer.parseInt(parts[2]),Integer.parseInt(parts[3]), socket);
            }

        }

        // para o iperf convém fazer x retrays até desistir e dar mandar fail
        // ou então dar um certo tempo para ele ter a certeza que não dá
        // no json só pedir jitter e packet loss quando for -u, bandwidth pode-se pedir sempre
        // convém que a frequencia do server seja mais pequena do que a do cliente
        // fazer -i (frequencia servidor) no server e -t no cliente (duração do teste=frequencia cliente)

    }

    public static void schedulerIperf(String tool, String role, String server_address, int duration,String transport_type, int frequency, String type, DatagramSocket socket){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {

            double result = MetricCollector.runIperf(tool,role,server_address,duration,transport_type,type);
           // depois tem que se diferenciar se é bandwidth, jitter ou packetLoss

            // perceber como se vão mandar as merdas no pacote (se calhar tem que se alterar o pacote)

            //byte[] metricsData = metricsMessage.getBytes();
            //DatagramPacket metricsPacket = new DatagramPacket(metricsData, metricsData.length, server_ip, server_socket);
            try {
                socket.send(metricsPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }}, 0, frequency, TimeUnit.SECONDS); // Coletar métricas a cada 30 segundos

    }

    public static void schedulerPing(String tool, String destination, int count, int frequency,DatagramSocket socket){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            double latency = MetricCollector.runPing(tool,destination,count);
            // perceber como se vão mandar as merdas no pacote (se calhar tem que se alterar o pacote)

            //byte[] metricsData = metricsMessage.getBytes();
            //DatagramPacket metricsPacket = new DatagramPacket(metricsData, metricsData.length, server_ip, server_socket);
            try {
                socket.send(metricsPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }}, 0, frequency, TimeUnit.SECONDS); // Coletar métricas a cada 30 segundos

    }

}






