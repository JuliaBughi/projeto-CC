package Packet;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NTSender {
    private DatagramSocket socket;
    private InetAddress receiverAddress;
    private int receiverPort;
    private String deviceId;
    private int type;
    private static final int TIMEOUT = 1000; // milliseconds
    private static final int MAX_RETRIES = 5;

    public NTSender(DatagramSocket socket) throws SocketException {
        this.socket = socket;
        socket.setSoTimeout(TIMEOUT);
    }

    public int sendData(String data, InetAddress receiverAddress, int receiverPort,int nr_seq, String deviceId, int type) throws IOException {
        this.receiverAddress = receiverAddress;
        this.receiverPort = receiverPort;
        this.deviceId = deviceId;
        this.type=type;


        int offset = 0;
        int chunkSize = 1024;
        int last=0;

        for (int i = 0; i < data.length(); i += chunkSize) {
            String chunk = data.substring(i, Math.min(data.length(), i + chunkSize));
            boolean isLast = (i + chunkSize >= data.length());
            if(isLast) last=1;

            NetTaskPacket packet = new NetTaskPacket(nr_seq,deviceId,0,last,type,data);
            String packetStr = NetTaskPacket.NetTaskPacketToString(packet);

            if (!sendWithRetry(packetStr, nr_seq)) {
                throw new IOException("Failed to send packet after maximum retries");
            }

            nr_seq++;
        }

        return nr_seq+1; // dá return do novo numero de sequencia a ser usado no procimo pacote
    }

    private boolean sendWithRetry(String packetStr, int expectedSequence) throws IOException {
        int retries = 0;
        byte[] packetBytes = packetStr.getBytes(StandardCharsets.UTF_8);

        while (retries < MAX_RETRIES) {
            DatagramPacket datagramPacket = new DatagramPacket(packetBytes, packetBytes.length, receiverAddress, receiverPort);

            socket.send(datagramPacket);

            try {
                byte[] ackBuffer = new byte[1024];
                DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
                socket.receive(ackPacket);

                String ackStr = new String(ackPacket.getData(), 0, ackPacket.getLength(),
                        StandardCharsets.UTF_8);
                NetTaskPacket packet = NetTaskPacket.StringToNetTaskPacket(ackStr);

                if (packet.getAck()==1 && packet.getNr_seq() == expectedSequence) {
                    return true; // recebeu o ack
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout, retrying... (" + (retries + 1) + "/" +
                        MAX_RETRIES + ")");
                retries++;
            }
        }

        return false; // tentou 5 vezes e não deu
    }
}