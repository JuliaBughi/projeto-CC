import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;

//perceber melhor se as classes maiores como bandwidth também têm de ter serializableName

class Device {
    @SerializedName("device_id")
    String device_id;
    DeviceMetrics device_metrics;
    LinkMetrics link_metrics;

    @Override
    public String toString() {
        return "Device{" +
                "device_id='" + device_id + '\'' +
                ", device_metrics=" + device_metrics +
                ", link_metrics=" + link_metrics +
                '}';
    }
}

class DeviceMetrics {
    @SerializedName("cpu_usage")
    boolean cpu_usage;
    @SerializedName("ram_usage")
    boolean ram_usage;
    @SerializedName("interface_stats")
    List<String> interface_stats;

    @Override
    public String toString() {
        return "DeviceMetrics{" +
                "cpu_usage=" + cpu_usage +
                ", ram_usage=" + ram_usage +
                ", interface_stats=" + interface_stats +
                '}';
    }
}

class LinkMetrics {
    Bandwidth bandwidth;
    Jitter jitter;
    PacketLoss packet_loss;
    Latency latency;
    @SerializedName("alertflow_conditions")
    AlertFlowConditions alertflow_conditions;

    @Override
    public String toString() {
        return "LinkMetrics{" +
                "bandwidth=" + bandwidth +
                ", jitter=" + jitter +
                ", packet_loss=" + packet_loss +
                ", latency=" + latency +
                ", alertflow_conditions=" + alertflow_conditions +
                '}';
    }
}

class Bandwidth {
    @SerializedName("tool")
    String tool;
    @SerializedName("role")
    String role;
    @SerializedName("server_address")
    String server_address;
    @SerializedName("duration")
    int duration;
    @SerializedName("transport_type")
    String transport_type;
    @SerializedName("frequency")
    int frequency;

    @Override
    public String toString() {
        return "Bandwidth{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}

class Jitter extends Bandwidth{

    @Override
    public String toString() {
        return "Jitter{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}

class PacketLoss extends Bandwidth{

    @Override
    public String toString() {
        return "PacketLoss{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }

}

class Latency {
    @SerializedName("tool")
    private String tool;
    @SerializedName("destination")
    private String destination;
    @SerializedName("packet_count")
    private int count;
    @SerializedName("frequency")
    private int frequency;

    @Override
    public String toString() {
        return "Latency{" +
                "tool='" + tool + '\'' +
                ", destination='" + destination + '\'' +
                ", count='" + count + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}

class AlertFlowConditions {
    @SerializedName("cpu_usage")
    private int cpu_usage;
    @SerializedName("ram_usage")
    private int ram_usage;
    @SerializedName("interface_stats")
    private int interface_stats;
    @SerializedName("packet_loss")
    private int packet_loss;
    @SerializedName("jitter")
    private int jitter;

    @Override
    public String toString() {
        return "AlertFlowConditions{" +
                "cpu_usage=" + cpu_usage +
                ", ram_usage=" + ram_usage +
                ", interface_stats=" + interface_stats +
                ", packet_loss=" + packet_loss +
                ", jitter=" + jitter +
                '}';
    }
}

class ListTasks{
    List<Task> tasks;
}