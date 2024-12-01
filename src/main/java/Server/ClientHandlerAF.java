package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandlerAF implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    ClientHandlerAF(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            String line = in.readLine();
            System.out.println("AF: client "+ line + " connected ");

            out.println("connected to server");
            out.flush();

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("AF "+message);
            }

            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
