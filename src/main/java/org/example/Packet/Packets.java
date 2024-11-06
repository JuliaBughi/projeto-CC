package src.main.java.org.example.Packet;

import java.io.*;
import src.main.java.org.example.Task.*;

public class Packets {

    public static class NetTaskPacket{
        private int nr_seq;
        private String device_id;
        private int ack; // 1 se for um ack
        private Task task; // ou se calhar pode ser ListTasks e manda logo todas as tasks juntas

        //vai ser preciso fazer os construtores

        public NetTaskPacket(int nr_seq, String device_id, int ack,Task task){
            this.nr_seq = nr_seq;
            this.device_id = device_id;
            this.ack = ack;
            this.task = task;
        }

        //Construtor de cópia
        public NetTaskPacket(NetTaskPacket p){
            this.nr_seq = p.nr_seq;
            this.device_id = p.getDevice_id();
            this.ack = p.getAck();
            this.task = p.getTask();
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

        public Task getTask(){
            return this.task;
        }

    }

    public static class Packet {
        private int nr_seq;

        //tem que se criar uma classe para pacotes do alertflow(acho eu)

        //vai ser preciso fazer os construtores
    }
}
