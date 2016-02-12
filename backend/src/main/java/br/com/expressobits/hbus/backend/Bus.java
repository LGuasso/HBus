package br.com.expressobits.hbus.backend;

/**
 * @author Rafael
 * @since 12/02/16
 */
public class Bus implements Comparable<Bus>{

    private String time;

    private String way;

    private String typeday;

    private String code;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getTypeday() {
        return typeday;
    }

    public void setTypeday(String typeday) {
        this.typeday = typeday;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int compareTo(Bus another) {

        int hourThis = Integer.parseInt(this.time.split(":")[0]);
        int hourAnother = Integer.parseInt(another.time.split(":")[0]);
        int minuteThis = Integer.parseInt(this.time.split(":")[1]);
        int minuteAnother = Integer.parseInt(another.time.split(":")[1]);

        if(hourThis>hourAnother){
            return 1;
        }else if(hourThis<hourAnother){
            return -1;
        }else{
            if(minuteThis>minuteAnother){
                return 1;
            }else if(minuteThis<minuteAnother){
                return -1;
            }else{
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        return getTime()+" - "+getCode()+" - "
                +" - "+getTypeday()+" - "+getWay();
    }
}
