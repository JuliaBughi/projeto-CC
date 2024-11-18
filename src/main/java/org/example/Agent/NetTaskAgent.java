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
    private String server_ip;
    private int server_socket;

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

            NetTaskPacket packet = new NetTaskPacket(1,device_id,0,null);
            String message = NetTaskPacket.NetTaskPacketToString(packet);
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, server_socket);
            socket.send(sendPacket);
            // aqui foi enviado o registo

            //supostamente temos que receber aqui a primeira resposta
            byte[] receivePacket = new byte[1024];
            DatagramPacket serverResponse = new DatagramPacket(receivePacket, receivePacket.length);
            socket.receive(serverResponse);



            String response = new String(serverResponse.getData(),0,serverResponse.getLength());
            NetTaskPacket serverMessage = NetTaskPacket.StringToNetTaskPacket(response);

            this.ScheduleMetricCollect(serverMessage, socket, serverAddress);

            /*
            while(true){
                //Receive response from server
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                String aux = new String(receivePacket.getData(),0, receivePacket.getLength());
                NetTaskPacket received = NetTaskPacket.StringToNetTaskPacket(aux);
            }
            */

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

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            String iperfMetrics = MetricCollector.runIperf(server_address);
            String pingMetrics = MetricCollector.runPing(server_address);
            // Enviar métricas para o servidor
            String metricsMessage = "iperf:" + iperfMetrics + ";ping:" + pingMetrics;
            byte[] metricsData = metricsMessage.getBytes();
            DatagramPacket metricsPacket = new DatagramPacket(metricsData, metricsData.length, server_address, server_socket);
            try {
                socket.send(metricsPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }}, 0, 30, TimeUnit.SECONDS); // Coletar métricas a cada 30 segundos
    }
}






