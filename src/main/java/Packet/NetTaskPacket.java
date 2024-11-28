package Packet;

import Task.Task;

import java.util.List;

public class NetTaskPacket {
    private int nr_seq;
    private String device_id;
    private int ack; // 1 se for um ack
    private int isLast; // caso o pacote tenah de ser partido isto serve para saber se é o ultimo pedaço
    private int type; //flag para saber se manda tasks, se manda metricas,...
    // type -1 - terminar ligação
    // type 0 - olá do cliente ao server
    // type 1 - mandar tasks
    // type 2 - bandwidth
    // type 3 - jitter
    // type 4 - packetLoss
    // type 5 - latency
    // type 6 - metricas (cpu,ram,...)
    private String data; // ou se calhar pode ser ListTasks e manda logo todas as tasks juntas

    public NetTaskPacket(){
    }

    public NetTaskPacket(int nr_seq, String device_id, int ack,int isLast, int type, String data){
        this.nr_seq = nr_seq;
        this.device_id = device_id;
        this.ack = ack;
        this.isLast = isLast;
        this.type = type;
        this.data = data;
    }

    public NetTaskPacket(int nr_seq, String device_id, int type){ //ack
        this.nr_seq = nr_seq;
        this.device_id = device_id;
        this.ack = 1;
        this.isLast = 1;
        this.type = type;
        this.data = "";
    }

    public void setNr_seq(int nr_seq){
        this.nr_seq = nr_seq;
    }

    public void setDevice_id(String device_id){
        this.device_id = device_id;
    }

    public void setAck(int ack){
        this.ack = ack;
    }

    public void setIsLast(int isLast){
        this.isLast = isLast;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setData(String data){
        this.data = data;
    }

    public int getNr_seq(){
        return this.nr_seq;
    }

    public String getDevice_id(){
        return this.device_id;
    }

    public int getAck(){
        return this.ack;
    }

    public int getIsLast(){
        return this.isLast;
    }
    public int getType(){
        return this.type;
    }

    public String getData(){
        return this.data;
    }

    public static String NetTaskPacketToString(NetTaskPacket packet){

        return String.format("%d,%s,%d,%d,%d,%s",packet.nr_seq,packet.device_id,packet.ack,packet.isLast,packet.type,packet.data);
    }

    public static NetTaskPacket StringToNetTaskPacket(String message){
        String[] parts = message.split(",");
        NetTaskPacket packet = new NetTaskPacket();

        packet.nr_seq = Integer.parseInt(parts[0]);
        packet.device_id = parts[1];
        packet.ack = Integer.parseInt(parts[2]);
        packet.isLast = Integer.parseInt(parts[3]);
        packet.type = Integer.parseInt(parts[4]);
        if(packet.type==0 || packet.ack==1)
            packet.data="";
        else
            packet.data = parts[5];

        return packet;
    }

}
