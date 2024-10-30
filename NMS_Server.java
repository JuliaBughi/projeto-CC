import java.io.*;
import java.net.*;

import Task.ListTasks;
import com.google.gson.Gson;

public class NMS_Server {


    //O que vem no pacote
    //porta de origem/destino, ip origem/destino, payload(é mandado para o parser)
    // Para o ack:
    // tem de se inverter a source e a destination

    // o socket do servidor so serve para receber
    // tem de se criar sockets para mandar as merdas e depois fechar


    private static ListTasks Jsonparser(String file_path) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(file_path);
            ListTasks listTasks = gson.fromJson(reader, ListTasks.class);
            return listTasks;
        } catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro: " + e.getMessage());
            return null;
        }
    }


}

