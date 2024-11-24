package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandlerAF implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    ClientHandlerAF(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            //nao envia nada, so espera o recebimento das metricas, acho que o out nao é necessário (?)
            //ou tambem é por aqui que ele depois manda fechar?

            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
