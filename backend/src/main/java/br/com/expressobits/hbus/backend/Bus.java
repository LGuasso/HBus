package br.com.expressobits.hbus.backend;

/**
 * @author Rafael
 * @since 12/02/16
 */
public class Bus{

    private String id;

    private String time;

    private String way;

    private String typeday;

    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
    public String toString() {
        return getTime()+" - "+getCode()+" - "
                +" - "+getTypeday()+" - "+getWay();
    }
}
