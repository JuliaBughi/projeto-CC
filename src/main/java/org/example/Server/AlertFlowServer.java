package src.main.java.org.example.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AlertFlowServer {

    private void main(String[] args) throws IOException {
        ServerSocket socket = null;

        try{
            socket = new ServerSocket(6666);

            while (true) {
                Socket clientsSocket = socket.accept();
                //ler o conteúdo do alertflow
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientsSocket.getInputStream()));

                // podemos ter um print para confirmar as coisas
                String clientMessage = inFromClient.readLine();
                System.out.println("TCP Server received: " + clientMessage);

                // Responder com ACK
                OutputStream ack = clientsSocket.getOutputStream();
                //tem de se criar um paco te de ack para mandar como resposta

                //ack.write(pacote.getBytes());

                clientsSocket.close();
            }


        } catch(Exception e){
            e.printStackTrace();
        } finally{
            if (socket != null && !socket.isClosed()) {
                socket.close();
        }
    }


    }
}
