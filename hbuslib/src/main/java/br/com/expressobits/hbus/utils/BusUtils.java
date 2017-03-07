package br.com.expressobits.hbus.utils;

import java.util.Calendar;
import java.util.List;

import br.com.expressobits.hbus.model.Bus;

/**
 * @author Rafael Correa
 * @since 07/06/16
 */
public class BusUtils {

    public static Bus getNextBusforList(List<Bus> buses){
        Bus nowBus = new Bus();
        Bus nextBus;
        //nowBus.setTime(HoursUtils.getNowTimeinString());
        nowBus.setTime(Calendar.getInstance().getTimeInMillis());

        if(buses.size() > 0) {
            nextBus = buses.get(0);
            for (int i = 0; i < buses.size(); i++) {
                nextBus = buses.get(i);
                if (nowBus.compareTo(nextBus) <= 0) {
                    nextBus = buses.get(i);
                    return nextBus;
                } else {

                }
            }
            return nextBus;
        }else{
            nowBus.setTime(0L);
            return nowBus;
       }
    }
}
