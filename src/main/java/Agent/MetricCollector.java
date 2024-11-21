package Agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetricCollector {
    public static double runIperf(String tool, String role, String server_address, int duration, String transport_type,String type) {
        String result1 = "";
        String result2 = "";
        try {
            ProcessBuilder builder;
            if(role.equals("c")) // se for cliente
                if(transport_type.equals("u"))
                    builder = new ProcessBuilder(tool, "-"+role, server_address, "-t", String.valueOf(duration), "-"+transport_type);
                else
                    builder = new ProcessBuilder(tool, "-"+role, server_address, "-t", String.valueOf(duration));
            else // se for server
                if(transport_type.equals("u"))
                    builder = new ProcessBuilder(tool, "-"+role, "-i", String.valueOf(duration), "-"+transport_type);
                else
                    builder = new ProcessBuilder(tool, "-"+role, "-i", String.valueOf(duration));

            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) { // é preciso as 2 ultimas linhas porque a ultima pode ser a dizer que houve pacotes out of order
                result1 = result2;
                result2 = line;
            }
            process.waitFor();

            if(type.equals("b")) // bandwidth
                return parseBandwidth(result1,result2);
            else if(type.equals("j")) //jitter
                return parseJitter(result1,result2);
            else // packet loss
                return parsePacketLoss(result1,result2);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public static double runPing(String tool, String destination, int count) {
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
        return parseLatency(result);

    }

    public static double parseBandwidth(String lastLine1, String lastLine2) {
        // Regex patterns for different iperf output formats
        String[] patterns = {
                // Pattern for iperf3 output
                ".*\\s(\\d+\\.\\d+)\\s*Mbits/sec.*",
                // Pattern for older iperf versions
                ".*\\s(\\d+\\.\\d+)\\s*Mbps.*"
        };

        // Check both lines, prioritizing the last line
        String[] linesToCheck = {lastLine2, lastLine1};

        for (String line : linesToCheck) {

            // Try each pattern
            for (String patternStr : patterns) {
                Pattern pattern = Pattern.compile(patternStr);
                Matcher matcher = pattern.matcher(line != null ? line : "");

                if (matcher.find()) {
                    try {
                        return Double.parseDouble(matcher.group(1));
                    } catch (NumberFormatException e) {
                        // If parsing fails, continue to next pattern
                        continue;
                    }
                }
            }
        }

        // Return -1 if no bandwidth found
        return -1;
    }

    public static double parseJitter(String lastLine1, String lastLine2) {
        String pattern = ".*\\s(\\d+\\.\\d+)\\s*ms.*";

        String[] linesToCheck = {lastLine2, lastLine1};

        for (String line : linesToCheck) {
            if (line != null && line.trim().endsWith("r")) {
                continue;
            }

            Pattern jitterPattern = Pattern.compile(pattern);
            Matcher matcher = jitterPattern.matcher(line != null ? line : "");

            if (matcher.find()) {
                try {
                    return Double.parseDouble(matcher.group(1));
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        return -1;
    }

    public static double parsePacketLoss(String lastLine1, String lastLine2) {
        // Pattern matches format "X/Y (Z%)"
        String pattern = ".*(\\d+)/(\\d+)\\s*\\((\\d+(\\.\\d+)?)%\\).*";

        String[] linesToCheck = {lastLine2, lastLine1};

        for (String line : linesToCheck) {
            if (line != null && line.trim().endsWith("r")) {
                continue;
            }

            Pattern lossPattern = Pattern.compile(pattern);
            Matcher matcher = lossPattern.matcher(line != null ? line : "");

            if (matcher.find()) {
                try {
                    // Extract the percentage directly from the parentheses
                    return Double.parseDouble(matcher.group(3)); // dá return da percentagem
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        return -1;
    }

    //ex da ultima linha do output: rtt min/avg/max/mdev = 16.556/17.230/17.900/0.477 ms
    public static double parseLatency(String lastLine) {
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

    // Este dá print da percentagem atual do uso de CPU
    // top -bn1 | grep "Cpu(s)" | awk '{print $2 + $4}'

    //Este dá a percentagem de ram atual com o '%', depois tem de se tirar isso
    //free -m | awk '/Mem:/ {printf "%.2f%%", $3/$2 * 100.0}'

    //não faço a mínima de como se faz o interface stats

    //não percebi bem como se fazem os outros depois - é baseados nos comandos do jitter e packet loss?



}
