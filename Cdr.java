import java.util.Date;

public class Cdr {
    private Date timeStamp;
    private String msisdn;
    private String serviceId;
    private float[] buckets;
    private float[] counters;
    private String tarifarioId;
    private String operation;

    public Cdr(Date timeStamp, String msisdn, String service, float[] buckets2, float[] counters2, String tarifario, String operation) {
        this.timeStamp = timeStamp;
        this.msisdn = msisdn;
        this.serviceId = service;
        this.buckets = buckets2;
        this.counters = counters2;
        this.tarifarioId = tarifario;
        this.operation = operation;
    }

    // Getter and Setter methods for each attribute

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getServiceId() {
        return serviceId;
    }

    public float[] getBuckets() {
        return buckets;
    }

    public float[] getCounters() {
        return counters;
    }

    public String getTarifarioId() {
        return tarifarioId;
    }

    public String getOperation(){
        return operation;
    }

}
