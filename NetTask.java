import java.net.*;

public class NetTask {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private int windowSize = 5; // Pode ajustar conforme necessário

    public NetTask(InetAddress address, int port) throws SocketException {
        this.socket = new DatagramSocket();
        this.address = address;
        this.port = port;
        socket.setSoTimeout(1000); // Configura um timeout
    }

    public void sendPacket(Packet packet) {
        // Implementação para enviar pacote com retransmissão e controle de fluxo
    }

    public Packet receivePacket() {
        // Implementação para receber pacotes e ACKs
    }

    // Outros métodos para controle de fluxo, etc.
}
