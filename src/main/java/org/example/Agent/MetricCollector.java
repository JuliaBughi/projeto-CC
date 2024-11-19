package org.example.Agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MetricCollector {
    public static String runIperf(String tool, String role, String server_adress, int duration, String transport_type ) {
        String result = "";
        try {
            ProcessBuilder builder;
            if(role.equals("c"))
                if(transport_type.equals("u"))
                    builder = new ProcessBuilder(tool, "-"+role, server_adress, "-t", String.valueOf(duration), "-"+transport_type);
                else
                    builder = new ProcessBuilder(tool, "-"+role, server_adress, "-t", String.valueOf(duration));
            else
                builder = new ProcessBuilder(tool, "-"+role); // se for server

            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String runPing(String tool, String destination, int count) {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(tool, "-c", String.valueOf(count), destination);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result = line; // assim só guarda a última linha que é a que importa
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    //ex da ultima linha do output: rtt min/avg/max/mdev = 16.556/17.230/17.900/0.477 ms
    public static double extractPingLatency(String lastLine) {
        double averageLatency = -1;
        try {
            if (lastLine.contains("rtt")) {
                String[] parts = lastLine.split("=");
                if (parts.length > 1) {
                    String[] metrics = parts[1].trim().split("/");
                    if (metrics.length > 1) {
                        averageLatency = Double.parseDouble(metrics[1]); // O segundo valor é o average
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return averageLatency;
    }

    // perceber como é que se garante que o iperf é feito em simultaneo no servidor e no cliente
    // perceber como se sacam as metricas com o iperf (acho que só dá se for do lado do servidor)

}
