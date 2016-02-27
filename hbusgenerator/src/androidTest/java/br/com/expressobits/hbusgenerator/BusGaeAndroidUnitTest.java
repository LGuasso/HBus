package br.com.expressobits.hbusgenerator;

import android.content.Context;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.util.Pair;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.gae.PullAllBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushBusEndpointsAsyncTask;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * @author Rafael
 * @since 12/02/16
 */
@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(AndroidJUnit4.class)
@SmallTest
public class BusGaeAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static String TAG = "Itinerary Gae Test";
    PushBusEndpointsAsyncTask pushBusEndpointsAsyncTask;
    //ClearBusEndpointsAsyncTask clearItinerariesEndpointsAsyncTask;
    PullBusEndpointsAsyncTask pullBusEndpointsAsyncTask;

    PullAllBusEndpointsAsyncTask pullAllBusEndpointsAsyncTask;

    Context context;
    private MainActivity mActivity;

    private City city;
    private Itinerary itinerary;

    Bus bus1204;
    Bus bus2203;

    public BusGaeAndroidUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        context = mActivity;

        city = new City();
        city.setName("Santa Maria");
        city.setCountry("RS");
        city.setLocation(new GeoPt().setLatitude(-109.776722f).setLongitude(-14.1046164f));

        itinerary = new Itinerary();
        itinerary.setName("Orio");
        itinerary.setCodes(Arrays.asList("110B", "155A"));
        itinerary.setWays(Arrays.asList("Centro > Bairro", "Bairro > Centro"));

        bus1204 = new Bus();
        bus1204.setTime("12:04");
        bus1204.setCode("155A");
        bus1204.setWay("Centro > Bairro");
        bus1204.setTypeday("useful");

        bus2203 = new Bus();
        bus2203.setTime("22:03");
        bus2203.setCode("110B");
        bus2203.setWay("Bairro > Centro");
        bus2203.setTypeday("useful");
    }

    //@Test
    public void testBuses() throws ExecutionException, InterruptedException {
        if(context!=null){
            pushBusEndpointsAsyncTask = new PushBusEndpointsAsyncTask();
            pushBusEndpointsAsyncTask.setContext(context);
            pushBusEndpointsAsyncTask.setCountry(city.getCountry());
            pushBusEndpointsAsyncTask.setCityName(city.getName());
            pushBusEndpointsAsyncTask.setItineraryName(itinerary.getName());
            pushBusEndpointsAsyncTask.execute(bus1204,bus2203).get();
        }

        while (pushBusEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2, (int) pushBusEndpointsAsyncTask.get());


        pullBusEndpointsAsyncTask = new PullBusEndpointsAsyncTask();
        pullBusEndpointsAsyncTask.setContext(context);
        List<Bus> buses = pullBusEndpointsAsyncTask.execute(new Pair<>(city,itinerary)).get();
        while (pullBusEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2,buses.size());
        assertEquals(bus1204.getTime(),buses.get(0).getTime());
    }

    @Test
    public void testBusesAll() throws ExecutionException, InterruptedException {
        Long timeStart = System.currentTimeMillis();
        pullAllBusEndpointsAsyncTask = new PullAllBusEndpointsAsyncTask();
        pullAllBusEndpointsAsyncTask.setContext(context);
        List<Bus> buses = pullAllBusEndpointsAsyncTask.execute(new Pair<>(city,itinerary)).get();
        while (pullAllBusEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        for(Bus bus:buses){
            Log.d(TAG,bus.getTime()+" - "+bus.getCode());
        }
        Long timeElapse = System.currentTimeMillis() - timeStart;
        assertTrue(timeElapse<1000l);
        Log.d(TAG,"time elapse "+ HoursUtils.longTimetoString(timeElapse));
    }
}
