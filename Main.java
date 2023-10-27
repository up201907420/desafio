import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Example usage
        int counter = 1;
        CdrManager manager = new CdrManager();

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Add one day to the date
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Convert the Calendar back to a Date
        Date newDate = calendar.getTime();



        manager.createBilling("933333333", 17, 15, 15, 0, 0, 0, 2);
        System.out.println("Chargin Request on course with the following strucutre:\n" +"Request ID: "+ counter+ "|Timestamp: " +currentDate+ "|Service: A|Roaming : False|Msisdn: 933333333|RSU: 15");
        System.out.println("Response:");
        manager.chargingRequest(counter, currentDate, "A", false, "933333333", 15);
        System.out.println();
        System.out.println("Chargin Request on course with the following strucutre:\n" +"Request ID: "+ (counter++) + "|Timestamp: " +currentDate+ "|Service: A|Roaming : False|Msisdn: 933333333|RSU: 3");
        System.out.println("Response:");
        manager.chargingRequest(counter, newDate, "A", false, "933333333", 3);
        System.out.println();
        manager.createBilling("964444444", 7, 15, 15, 0, 0, 0, 2);
        System.out.println("Chargin Request on course with the following strucutre:\n" +"Request ID: "+ counter+ "|Timestamp: " +currentDate+ "|Service: A|Roaming : False|Msisdn: 964444444|RSU: 15");
        System.out.println("Response:");
        manager.chargingRequest(counter++, currentDate, "A", false, "964444444", 15);
        System.out.println();
        System.out.println("List of transaction of the msisdn 933333333");
        manager.fetchCdrsByIdAndOrderByTimestamp("933333333");
    }
}
