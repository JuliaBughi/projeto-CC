package Agent;

import java.net.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Packet.*;
import Task.*;

public class NetTaskAgent implements Runnable{
    private String client_ip;
    private String device_id;
    private final InetAddress server_ip = InetAddress.getLoopbackAddress(); //isso supostamente é equivalente ao localhost, mas verificar
    private final int UDP_PORT = 9876;

    public NetTaskAgent(String client_ip, String device_id){
        this.client_ip = client_ip;
        this.device_id = device_id;
    }

    public void run(){
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket();
            NTSender sender = new NTSender(socket);
            NTReceiver receiver = new NTReceiver(socket);

            int nr_seq = 1;

            nr_seq = sender.sendData("",server_ip,UDP_PORT,nr_seq,device_id,0);
            System.out.println("Establishing connection to server...");
            // aqui foi enviado o registo

            NetTaskPacket answer = receiver.receive(1);
            System.out.println("Confirmation of connection");



            if(answer.getType()==1){ // se há tasks para o cliente fazer
                System.out.println("Tasks received, starting execution...");
                this.ScheduleMetricCollect(answer, socket, server_ip);
                // também tem que se fazer aqui a coleta das alertflow conditions
            }

            // se type == -1 terminar ligação porque não há tasks para ele

        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if(socket != null && !socket.isClosed()){
                System.out.println("No tasks received, closing connection...");
                socket.close();
            }
        }
    }

    public void ScheduleMetricCollect(NetTaskPacket packet, DatagramSocket socket, InetAddress server_address){
        String tasks = packet.getData();
        List<Task> l = Task.StringToTasks(tasks);

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
                //socket.send(metricsPacket);
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
                //socket.send(metricsPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }}, 0, frequency, TimeUnit.SECONDS); // Coletar métricas a cada 30 segundos

    }

}






