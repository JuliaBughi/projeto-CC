public class Packet {

    class UDPPacket{ // supostamente o ip e porta de origem já são intrinsecos do datagramPacket
        private int id;
        private int dest_port;
        private int dest_ip;
        private Task task; // ou se calhar pode ser ListTasks e manda logo todas as tasks juntas
    }

    class TCPPacket {
        private int id;
        private int origin_port;
        private int dest_port;
        private int origin_ip;
        private int dest_ip;
    }
}
