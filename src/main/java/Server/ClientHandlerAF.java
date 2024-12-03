package Server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandlerAF implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ClientHandlerAF(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            String line = in.readLine();
            LocalDateTime date = LocalDateTime.now();
            String s = new String(date.format(FORMATTER) +" -  Client "+ line + " connected ");
            NMS_Server.AFAdd(s);

            out.println("connected to server");
            out.flush();

            String message;
            while ((message = in.readLine()) != null) {
                date = LocalDateTime.now();
                s = new String(date.format(FORMATTER) +" - " +message);
                NMS_Server.AFAdd(s);
            }

            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
