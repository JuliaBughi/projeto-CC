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
                result += line + "\n";
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    // não consigo perceber como se saca o jitter e
}
