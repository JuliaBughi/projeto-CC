import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Task {
    @SerializedName("task_id")
    String task_id;
    @SerializedName("frequency")
    int frequency;
    @SerializedName("devices")
    List<Device> devices;

    @Override
    public String toString() {
        return "Task{" +
                "task_id='" + task_id + '\'' +
                ", frequency=" + frequency +
                ", devices=" + devices +
                '}';
    }
}

class ListTasks{
    List<Task> tasks;

    @Override
    public String toString() {
        return "ListTasks{" +
                "tasks=" + tasks +
                '}';
    }

}

class Device {
    @SerializedName("device_id")
    String device_id;
    @SerializedName("device_metrics")
    DeviceMetrics device_metrics;
    @SerializedName("link_metrics")
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
    @SerializedName("bandwidth")
    Bandwidth bandwidth;
    @SerializedName("jitter")
    Jitter jitter;
    @SerializedName("packet_loss")
    PacketLoss packet_loss;
    @SerializedName("latency")
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

