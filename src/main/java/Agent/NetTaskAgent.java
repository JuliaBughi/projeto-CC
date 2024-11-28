package Agent;

import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Packet.*;
import Task.*;

public class NetTaskAgent implements Runnable{
    private static InetAddress server_ip;
    private static String device_id;
    //private static InetAddress server_ip = InetAddress.getLoopbackAddress(); //isso supostamente é equivalente ao localhost, mas verificar
    private static int UDP_PORT = 9876;
    private static NTSender sender;
    private static NTReceiver receiver;
    private static int nr_seq=1;

    public NetTaskAgent(String server_ip, String device_id) throws UnknownHostException {
        this.server_ip = InetAddress.getByName(server_ip);
        this.device_id = device_id;
    }

    public void run(){
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket();
            sender = new NTSender(socket);
            receiver = new NTReceiver(socket);

            int nr_seq = 1;

            nr_seq = sender.sendData("",server_ip,UDP_PORT,nr_seq,device_id,0);
            System.out.println("Establishing connection to server...");
            // aqui foi enviado o registo

            NetTaskPacket answer = receiver.receive(nr_seq);
            nr_seq = answer.getNr_seq()+1;
            System.out.println("Confirmation of connection");



            if(answer.getType()==1){ // se há tasks para o cliente fazer
                System.out.println("Tasks received, starting execution...");
                String tasks = answer.getData();
                List<Task> l = Task.StringToTasks(tasks);
                this.ScheduleNTMetricCollect(answer, socket);
                this.ScheduleAFMetricCollect(answer,socket);
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

    public void ScheduleNTMetricCollect(NetTaskPacket packet, DatagramSocket socket){
        String tasks = packet.getData();
        List<Task> l = Task.StringToTasks(tasks);

        for(Task t : l) {
            if (!t.getBandwidth().startsWith("*")) { // quer dizer que tem de fazer a bandwidth
                String[] parts = t.getBandwidth().split(",");
                // aqui tem que se o iperf periodicamente
                // a minha dúvida é como é que se faz em relação ao nr de sequencia
                schedulerIperf(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),parts[4], Integer.parseInt(parts[5]), 2, socket);
            }

            if (!t.getJitter().startsWith("*")) { // fazer o iperf para sacar o jitter
                String[] parts = t.getJitter().split(",");
                schedulerIperf(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),parts[4], Integer.parseInt(parts[5]), 3, socket);
            }

            if (!t.getPacketLoss().startsWith("*")){ // fazer o iperf para sacar o jitter
                String[] parts = t.getPacketLoss().split(",");
                schedulerIperf(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),parts[4], Integer.parseInt(parts[5]), 4, socket);
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

    public void ScheduleAFMetricCollect(NetTaskPacket packet, DatagramSocket socket){

    }

    public static void schedulerIperf(String tool, String role, String server_address,
                                      int duration,String transport_type, int frequency, int type, DatagramSocket socket){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {

            double result = MetricCollector.runIperf(tool,role,server_address,duration,transport_type,type);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String res = Double.toString(result) + ';' + now.format(formatter);
            try {
                nr_seq = sender.sendData(res,server_ip,UDP_PORT,nr_seq,device_id,type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }, 0, frequency, TimeUnit.SECONDS);

    }

    public static void schedulerPing(String tool, String destination, int count, int frequency,DatagramSocket socket){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            double result = MetricCollector.runPing(tool,destination,count);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String res = Double.toString(result) + ';' + now.format(formatter);
            try {
                nr_seq = sender.sendData(res,server_ip,UDP_PORT,nr_seq,device_id,5);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }}, 0, frequency, TimeUnit.SECONDS);

    }

}






