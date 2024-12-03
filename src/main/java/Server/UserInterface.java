package Server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class UserInterface implements Runnable{

    Scanner sc = new Scanner(System.in);
    Menu menu;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void run(){
        try {
            main_menu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void main_menu() throws IOException {
        this.menu = new Menu(new String[]{
                "\nMENU\n",
                "Connection info",
                "Alertflow messages",
                "All metrics",
                "Metrics from a agent",
                "Metrics from a period of time"
        });
        menu.setHandler(1, this::ConnectionInfo);
        menu.setHandler(2, this::AlertFlowMessages);
        menu.setHandler(3, () -> menu_OrderOptions(1));
        menu.setHandler(4, this::MetricsAgent);
        menu.setHandler(5, () -> menu_OrderOptions(2));

        menu.execute();
    }

    public void ConnectionInfo() throws IOException {
        NMS_Server.ConnectionPrint();

        pressAnyKey();
        main_menu();
    }

    public void AlertFlowMessages() throws IOException {
        NMS_Server.AFPrint();

        pressAnyKey();
        main_menu();
    }

    public void menu_OrderOptions(int i) throws IOException {
        this.menu = new Menu(new String[]{
                "\nORDER OPTIONS\n",
                "Order by agent",
                "Order by date"
        });
        if(i==1){
            menu.setHandler(1, this:: allByAgent);
            menu.setHandler(2, this:: allByDate);
        }
        else{
            menu.setHandler(1, () -> menu_getDates(1, ""));
            menu.setHandler(2, () -> menu_getDates(2, ""));
        }

        menu.execute();
    }

    public void MetricsAgent() throws IOException {
        System.out.println("\nAgent id: ");
        String id = sc.nextLine();

        menu_MetricsAgent(id);
    }

    public void menu_MetricsAgent(String id) throws IOException {
        this.menu = new Menu(new String[]{
                "\nAGENT METRICS OPTIONS\n",
                "All metrics",
                "Metrics from a period of time"
        });
        menu.setHandler(1, () -> AgentAllMetrics(id));
        menu.setHandler(2, () -> menu_getDates(3, id));

        menu.execute();
    }

    public LocalDateTime getDate(){
        String d1;
        LocalDateTime date1;

        while(true){
            System.out.println("Enter date (format: yyyy-MM-dd HH:mm:ss): ");
            d1 = sc.nextLine();

            try {
                date1 = LocalDateTime.parse(d1,FORMATTER);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter date in format yyyy-MM-dd.");
            }
        }

        return date1;
    }

    public void menu_getDates(int i, String id) throws IOException {
        LocalDateTime date1;
        LocalDateTime date2;

        while(true){
            System.out.println("\nInitial date (format: yyyy-MM-dd HH:mm:ss): ");
            String d1 = sc.nextLine();

            try{
                date1 = LocalDateTime.parse(d1,FORMATTER);
                if(LocalDateTime.now().isBefore(date1))
                    System.out.println("Invalid date. Date is after actual date.");
                else
                    break;
            } catch(DateTimeParseException e){
                System.out.println("Invalid date format. Please enter date in format yyyy-MM-dd HH:mm:ss.");
            }

        }

        while(true){
            System.out.println("\nEnd date (format: yyyy-MM-dd HH:mm:ss) or write now (date now): ");
            String d2 = sc.nextLine();

            try{
                if(d2.equals("now")){
                    date2 = LocalDateTime.now();
                    break;
                }
                else{
                    date2 = LocalDateTime.parse(d2,FORMATTER);
                    if(LocalDateTime.now().isBefore(date2))
                        System.out.println("Invalid date. Date is after actual date.");
                    else
                        break;
                }

            } catch(DateTimeParseException e){
                System.out.println("Invalid date format. Please enter date in format yyyy-MM-dd HH:mm:ss.");
            }

        }

        if(date2.isBefore(date1)){
            System.out.println("End date is before start date. Enter a new end date.");
            date2 = getDate();
        }

        if(i==1)
            PeriodByAgent(date1,date2);
        else if(i==2)
            PeriodByDate(date1,date2);
        else
            AgentPeriod(id,date1,date2);
    }

    public void allByAgent() throws IOException {
        NMS_Server.printAllMetrics();

        pressAnyKey();
        main_menu();
    }

    public void allByDate() throws IOException {
        NMS_Server.printAllMetrics();

        pressAnyKey();
        main_menu();
    }

    public void AgentAllMetrics(String id) throws IOException {

        List<MetricNT> l = NMS_Server.getMetricsForDevice(id);

        if(!l.isEmpty()){
            System.out.println("Metrics Agent "+ id);
            for(MetricNT m : l)
                m.toString();
        }
        else{
            System.out.println("Agent id doesn´t exist or agent has not sent metrics yet");
        }

        pressAnyKey();
        main_menu();
    }

    public void AgentPeriod(String id, LocalDateTime d1, LocalDateTime d2) throws IOException {

        List<MetricNT> l = NMS_Server.getMetricsForDevice(id);

        for(MetricNT m : l){
            if((m.getDate().isAfter(d1) ||m.getDate().isEqual(d1)) && (m.getDate().isBefore(d2) ||m.getDate().isEqual(d2)))
                System.out.println(m.toString());
             else if(m.getDate().isAfter(d2))
                break;
        }

        pressAnyKey();
        main_menu();
    }

    public void PeriodByAgent(LocalDateTime d1, LocalDateTime d2) throws IOException {
        NMS_Server.printPeriodByAgent(d1,d2);

        pressAnyKey();
        main_menu();
    }

    public void PeriodByDate(LocalDateTime d1, LocalDateTime d2) throws IOException {
        NMS_Server.printPeriodByDate(d1,d2);

        pressAnyKey();
        main_menu();
    }



    public void pressAnyKey(){
        System.out.println("Press any key to continue");
        String x = sc.nextLine();
    }


}
