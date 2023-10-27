import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CdrManager {
    private List<Cdr> cdrList;
    private List<BillingAccount> billingList;

    public CdrManager() {
        cdrList = new ArrayList<>();
        billingList = new ArrayList<>();
    }

    private void addCdr(Cdr cdr) {
        cdrList.add(cdr);
    }

    public void addBilling(BillingAccount account) {
        billingList.add(account);
    }

    public void createBilling(String msisdn, int bucketA, int bucketB, int buckerC, int counterA, int counterB, int counterC, int counterD){
        addBilling(new BillingAccount(msisdn, bucketA, bucketB, buckerC, counterA, counterB, counterC, counterD));
    }


    public boolean chargingRequest(int ID, Date timeStamp, String service, boolean roaming, String msisdn, int rsu){
        BillingAccount x = find(msisdn);
        Cdr record = x.charge(ID, timeStamp, service,roaming,rsu);
        addCdr(record);
        return true;
    }

    // encontrar o msisdn respetivo para poder ser possível operar sobre a Billing account
    private BillingAccount find(String msisdn){
        for (BillingAccount account : billingList) {
            if (account.getMsisdn().equals(msisdn)) {
                return account;
            }
        }
        return null;
    }

    public List<Cdr> fetchCdrsByIdAndOrderByTimestamp(String msisdn) {
        List<Cdr> filteredCdrs = cdrList.stream()
                .filter(cdr -> cdr.getMsisdn().equals(msisdn))
                .sorted(Comparator.comparing(Cdr::getTimeStamp).reversed())
                .collect(Collectors.toList());
    
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
        for (Cdr cdr : filteredCdrs) {
            System.out.println("Timestamp: " + dateFormat.format(cdr.getTimeStamp()));
            System.out.println("MSISDN: " + cdr.getMsisdn());
            System.out.println("Service ID: " + cdr.getServiceId());
            System.out.println("Buckets: " + Arrays.toString(cdr.getBuckets()));
            System.out.println("Counters: " + Arrays.toString(cdr.getCounters()));
            System.out.println("Tarifario ID: " + cdr.getTarifarioId());
            System.out.println("Operation: " + cdr.getOperation());
            System.err.println("-----------------------------");
        }
    
        return filteredCdrs;
    }
    
}
