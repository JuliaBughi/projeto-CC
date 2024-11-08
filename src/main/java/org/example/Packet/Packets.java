package org.example.Packet;

import java.io.*;
import java.util.List;

import org.example.Task.*;

public class Packets {

    public static class NetTaskPacket{
        private int nr_seq;
        private String device_id;
        private int ack; // 1 se for um ack
        private List<Task> tasks; // ou se calhar pode ser ListTasks e manda logo todas as tasks juntas

        //vai ser preciso fazer os construtores
        public NetTaskPacket(){}

        public NetTaskPacket(int nr_seq, String device_id, int ack,List<Task> tasks){
            this.nr_seq = nr_seq;
            this.device_id = device_id;
            this.ack = ack;
            this.tasks = tasks;
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

        public List<Task> getTasks(){
            return this.tasks;
        }

        public static String NetTaskPacketToString(NetTaskPacket packet){
            if(packet.ack==1 && packet.nr_seq>1){ // se for ack e não for o primeiro pacote, não precisa de ter device_id nem tasks
                return String.format("%d,%d",packet.nr_seq,packet.ack);
            }

            return String.format("%d,%s,%d,%s",packet.nr_seq,packet.device_id,
                    packet.ack,Task.TasksToString(packet.tasks,packet.device_id));
        }

        public static NetTaskPacket StringToNetTaskPacket(String message){
            String[] parts = message.split(",");
            NetTaskPacket packet = new NetTaskPacket();

            if (parts.length == 2){
                packet.nr_seq = Integer.parseInt(parts[0]);
                packet.ack = Integer.parseInt(parts[1]);
            }

            packet.nr_seq = Integer.parseInt(parts[0]);
            packet.device_id = parts[1];
            packet.ack = Integer.parseInt(parts[2]);
            packet.tasks = Task.StringToTasks(parts[3]);

            return packet;
        }

    }

    public static class Packet {
        private int nr_seq;

        //tem que se criar uma classe para pacotes do alertflow(acho eu)

        //vai ser preciso fazer os construtores
    }
}
