package org.example.Packet;

import java.io.*;
import org.example.Task.*;

public class Packets {

    public class UDPPacket{
        private int nr_seq;
        private String device_id;
        private int ack; // 1 se for um ack
        private Task task; // ou se calhar pode ser ListTasks e manda logo todas as tasks juntas

        //vai ser preciso fazer os construtores

        public UDPPacket(UDPPacket p){
            this.nr_seq = p.nr_seq;
            this.device_id = p.device_id;
            this.ack = p.ack;
            this.task = p.task;
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

        public static byte[] UDPPacketToBytes(UDPPacket udpPacket) throws IOException {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(udpPacket);
            objStream.flush();
            return byteStream.toByteArray();
        }

        public static UDPPacket BytesToUDPPacket(byte[] data) throws IOException, ClassNotFoundException {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            ObjectInputStream objStream = new ObjectInputStream(byteStream);
            return (UDPPacket) objStream.readObject();
        }
    }

    public class TCPPacket {
        private int nr_seq;

        //tem que se criar uma classe para pacotes do alertflow(acho eu)

        //vai ser preciso fazer os construtores
    }
}
