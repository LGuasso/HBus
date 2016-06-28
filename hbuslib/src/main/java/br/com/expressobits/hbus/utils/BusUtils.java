package br.com.expressobits.hbus.utils;

import java.util.List;

import br.com.expressobits.hbus.model.Bus;

/**
 * Created by rafael on 07/06/16.
 */
public class BusUtils {

    public static Bus getNextBusforList(List<Bus> buses){
        //TODO Create metodo separado
        Bus nowBus = new Bus();
        Bus nextBus;
        nowBus.setTime(HoursUtils.getNowTimeinString());
        if(buses.size() > 0) {
            nextBus = buses.get(0);
            for (int i = 0; i < buses.size(); i++) {
                nextBus = buses.get(i);
                if (HoursUtils.compareTo(nowBus,nextBus) <= 0) {
                    nextBus = buses.get(i);
                    return nextBus;
                } else {

                }
            }
            return nextBus;
        }else{
            //TODO create default pattern not found bus
            nowBus.setTime("--:--");
            return nowBus;
        }
    }
}
