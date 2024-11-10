package org.example.Agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MetricCollector {
    public static String runIperf(String serverIp) {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder("iperf", "-c", serverIp, "-t", "10"); // Cliente `iperf` conectando ao servidor
            // aqui em vez do servidor implementar uma forma de mudar o serverIP para conectar tambem aos outros agentes e etc
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

    public static String runPing(String serverIp) {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder("ping", "-c", "4", serverIp); // Pingar 4 vezes
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
}
