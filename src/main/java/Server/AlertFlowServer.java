package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandlerAF implements Runnable{

    Socket socket;

    ClientHandlerAF(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String clientMessage = in.readLine();

            out.flush();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


public class AlertFlowServer implements Runnable {

    public void run(){
        ServerSocket ss = null;

        try{
            ss = new ServerSocket(6666);

            while (true) {
                Socket socket = ss.accept();
                new Thread(new ClientHandlerAF(socket)).start();
            }


        } catch(Exception e){
            e.printStackTrace();
        }
    }


}
