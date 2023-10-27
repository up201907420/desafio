import java.util.Calendar;
import java.util.Date;

public class BillingAccount {
    private String msisdn;
    private float bucketA;
    private float bucketB;
    private float bucketC;
    private int counterA;
    private int counterB;
    private int counterC;
    private int counterD;

    //construtor
    public BillingAccount(String msisdn, int bucketA, int bucketB, int buckerC, int counterA, int counterB, int counterC, int counterD) {
        this.msisdn = msisdn;
        this.bucketA = bucketA;
        this.bucketB = bucketB;
        this.bucketC = buckerC;
        this.counterA = counterA;
        this.counterB = counterB;
        this.counterC = counterC;
        this.counterD = counterD;
    }

    public String getMsisdn(){
        return msisdn;
    }
    //processo responsavel pela requisição de pagamento
    public boolean chargingRequest(int ID, Date timeStamp, String service, boolean roaming, String msisdn, int rsu){
        charge(ID, timeStamp, service,roaming,rsu);
        return true;
    }

    // verificar legibilidade da tarifa
    public Cdr charge(int ID, Date timestamp, String service,boolean roaming, int rsu) {
        float price;
        //Caso o serviço seja A no chargingRequest
        if(service.equals("A")){

            if(isWeekDay(timestamp) && counterA<=100){
                price = Alpha1(timestamp, roaming,rsu);
                //Descontar no bucket A
                if(!roaming){
                    return paymentA("Aplha1",ID,rsu,timestamp,service,price);
                }
                //descontar no bucket B
                else if(roaming && (counterB > 5)){
                    return paymentB("Aplha1",ID,rsu,timestamp,service, price);
                }
                //descontar no bucket C
                else {
                    return paymentC("Aplha1",ID,rsu,timestamp,service, price);
                }                
            }
            else if(roaming == false && bucketB>10){
                price = Alpha2(timestamp,rsu);
                return paymentB("Aplha2",ID,rsu,timestamp,service, price);
            }
            else if(roaming == true && bucketC>10){
                price = Alpha3(timestamp,rsu);
                return paymentC("Aplha3",ID,rsu,timestamp,service, price);
            }
            else{
                System.out.println(" Não Elegivel");
                return null;
            }
        }

        //Caso o serviço seja B no chargingRequest
        else if(service.equals("B")){
            if(isWeekDay(timestamp) || (!isWeekDay(timestamp) && isNightHour(timestamp))){
                price = Beta1(timestamp,roaming,rsu);
                if(!roaming){
                    return paymentA("Beta1",ID,rsu,timestamp,service,price);
                }
                //descontar no bucket B
                else if(roaming && (counterB > 5)){
                    return paymentB("Beta1",ID,rsu,timestamp,service, price);
                }
                //descontar no bucket C
                else {
                    return paymentC("Beta1",ID,rsu,timestamp,service, price);
                }      
            }
            else if(roaming == false && bucketB>10){
                price = Beta2(timestamp,rsu);
                return paymentB("Beta2",ID,rsu,timestamp,service, price);
            }
            else if(roaming == true && bucketC>10){
                price = Beta3(timestamp,rsu);
                return paymentC("Beta3",ID,rsu,timestamp,service,price);
            }
            else{
                System.out.println(" Não Elegivel");
                return null;
            }
        }
        return null;
    }
    
    //pagamento e mensagem de sucesso/erro + registo da transação
    private Cdr paymentA(String tarifario, int ID, int rsu, Date timestamp, String service, float price) {
        if(bucketA-price >= 0){
            bucketA = bucketA - price;
            System.out.println("Request ID: " + ID + "\nResult: OK\n" + "Granted Service Units: " + rsu);
        }else{       
            float unitPrice = price/rsu;
            float gsu = bucketA/unitPrice;
            bucketA = bucketA - unitPrice*gsu;
            System.out.println("Request ID: " + ID + "\nResult: CreditLimitReached \n" + "Granted Service Units: " + gsu);         
        }
        float[] buckets = new float[]{bucketA, bucketB, bucketC};
        float[] counters = new float[]{counterA, counterB, counterC,counterD};
        return new Cdr(timestamp, msisdn, service,buckets, counters, tarifario, "Charging Reply");
    }

    private Cdr paymentB(String tarifario, int ID, int rsu, Date timestamp, String service, float price) {
        if(bucketB-price >= 0){
            bucketB = bucketB - price;
            System.out.println("Request ID: " + ID + "\nResult: OK\n" + "Granted Service Units: " + rsu);
        }else{       
            float unitPrice = price/rsu;
            float gsu = bucketB/unitPrice;
            bucketB = bucketB - unitPrice*gsu;
            System.out.println("Request ID: " + ID + "\nResult: CreditLimitReached \n" + "Granted Service Units: " + gsu);         
        }
        float[] buckets = new float[]{bucketB, bucketB, bucketC};
        float[] counters = new float[]{counterA, counterB, counterC,counterD};
        return new Cdr(timestamp, msisdn, service,buckets, counters, tarifario, "Charging Reply");
    }

    private Cdr paymentC(String tarifario, int ID, int rsu, Date timestamp, String service, float price) {
        if(bucketC-price >= 0){
            bucketC = bucketC - price;
            System.out.println("Request ID: " + ID + "\nResult: OK\n" + "Granted Service Units: " + rsu);
        }else{       
            float unitPrice = price/rsu;
            float gsu = bucketC/unitPrice;
            bucketC = bucketC - unitPrice*gsu;
            System.out.println("Request ID: " + ID + "\nResult: CreditLimitReached \n" + "Granted Service Units: " + gsu);         
        }
        float[] buckets = new float[]{bucketC, bucketB, bucketC};
        float[] counters = new float[]{counterA, counterB, counterC,counterD};
        return new Cdr(timestamp, msisdn, service,buckets, counters, tarifario, "Charging Reply");
    }



    //funcoes que calculam o valor quando é a tarifa em causa
    private float Beta3(Date timestamp, int rsu) {
        float desconto = (float) 0.0;
        if(counterB>10){
            desconto = (float) 0.02*rsu;
        }else if(bucketB>15) desconto = (float) 0.005*rsu;

        if(!isWeekDay(timestamp)){
            return (float) ((0.025*rsu)-desconto);
        }
        else{
            return (float) ((0.1*rsu)-desconto);
        }
    }

    private float Beta2(Date timestamp, int rsu) {
        float desconto = (float) 0.0;
        if(counterB>10){
            desconto = (float) 0.02*rsu;
        }else if(bucketB>15) desconto = (float) 0.005*rsu;

        if(isNightHour(timestamp)){
            return (float) ((0.025*rsu)-desconto);
        }
        else{
            return (float) ((0.05*rsu)-desconto);
        }
    }

    private float Beta1(Date timestamp, boolean roaming, int rsu) {
        float desconto = (float) 0.0;
        if(counterA>10){
            desconto = (float) 0.025*rsu;
        }else if(bucketC>50) desconto = (float) 0.010*rsu;

        if(roaming){
            return (float) ((0.2*rsu)-desconto);
        }
        else if(isNightHour(timestamp)){
            return (float) ((0.05*rsu)-desconto);
        }
        else{
            return (float) ((0.1*rsu)-desconto);
        }
    }

    private float Alpha3(Date timestamp, int rsu) {
        float desconto = (float) 0.0;
        if(counterB>10){
            desconto = (float) 0.2*rsu;
        }else if(bucketB>15) desconto = (float) 0.05*rsu;

        if(!isWeekDay(timestamp)){
            return (float) ((0.25*rsu)-desconto);
        }
        else{
            return rsu-desconto;
        }


    }

    private float Alpha2(Date timestamp, int rsu) {
        float desconto = (float) 0.0;
        if(counterB>10){
            desconto = (float) 0.2*rsu;
        }else if(bucketB>15) desconto = (float) 0.05*rsu;

        if(isNightHour(timestamp)){
            return (float) ((0.25*rsu)-desconto);
        }
        else{
            return (float) ((0.25*rsu)-desconto);
        }

    }

    private float Alpha1(Date timestamp, boolean roaming, int rsu) {
        float desconto = (float) 0.0;
        if(counterA>10){
            desconto = (float) 0.25*rsu;
        }else if(bucketC>50) desconto = (float) 0.10*rsu;
        if(roaming){
            return rsu*2-desconto;
        }
        else if(isNightHour(timestamp)){
            return (float) (0.5*rsu)-desconto;
        }
        else{
            return rsu-desconto;
        }

    }



    //verificar se o dia presente na timestamp é dia da semana
    public static boolean isWeekDay(final Date d){
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        int day = cal.get(Calendar.DAY_OF_WEEK);
        return !(day == Calendar.SATURDAY || day == Calendar.SUNDAY);
    }

    //verificar se a hora presente na timestamp é considerado de noite
    public static boolean isNightHour(Date timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Define the night hour range (for example, 22:00 to 06:00)
        int nightStartHour = 22;
        int nightEndHour = 6;

        // Check if the hour is within the night range
        return (hour >= nightStartHour) || (hour < nightEndHour);
    }

}
