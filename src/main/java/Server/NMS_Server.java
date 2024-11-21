package Server;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class NMS_Server {


    public static boolean jsonFileExists(String filepath) {
        File file = new File(filepath);

        // Check if the file exists and is a file (not a directory)
        return file.exists() && file.isFile();
    }


    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Json file path: ");
        String path = scanner.nextLine();

        while(true){
            if(jsonFileExists(path))
                break;
            else{
                System.out.println("Invalid path. Try again:");
                path = scanner.nextLine();
            }

        }

        //não sei bem se é preciso dar-lhe metricas como no cliente
        NetTaskServer netTaskServer = new NetTaskServer(path);
        AlertFlowServer alertFlowServer = new AlertFlowServer();

        new Thread(netTaskServer).start();
        new Thread(alertFlowServer).start();
        // aqui tem de ter outra thread para a interface com o gestor de redes
        // tem que se criar uma maneira de guardar as metricas mandadas pelos clientes para depois serem consultadas pelo gestor
        // as metricas têm de ter uma data associada para depois e conseguir pedir as metricas dos ultimos x minutos/horas
        // definir também o numero de pontos que se apresenta por unidade de tempo (ex: 20 por hora)
    }
}
