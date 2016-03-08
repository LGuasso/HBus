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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.expressobits.hbus.backend.busApi.model.Bus;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.gae.ClearCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullAllBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushBusEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * @author Rafael
 * @since 12/02/16
 */
@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(AndroidJUnit4.class)
@SmallTest
public class CityGaeAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static String TAG = "Itinerary Gae Test";
    PushCitiesEndpointsAsyncTask pushCitiesEndpointsAsyncTask;
    ClearCitiesEndpointsAsyncTask clearCitiesEndpointsAsyncTask;
    PullCitiesEndpointsAsyncTask pullCitiesEndpointsAsyncTask;

    Context context;
    private MainActivity mActivity;

    public List<City> cities;


    public CityGaeAndroidUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        context = mActivity;

    }

    //@Test
    public void testBuses() throws ExecutionException, InterruptedException {
        if(context!=null){
            pushCitiesEndpointsAsyncTask = new PushCitiesEndpointsAsyncTask();
            pushCitiesEndpointsAsyncTask.setContext(context);
            //pushCitiesEndpointsAsyncTask.setCountry(city.getCountry());
            //pushCitiesEndpointsAsyncTask.execute().get();
        }

        while (pushCitiesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2, (int) pushCitiesEndpointsAsyncTask.get());


        pullCitiesEndpointsAsyncTask = new PullCitiesEndpointsAsyncTask();
        pullCitiesEndpointsAsyncTask.setContext(context);
        pullCitiesEndpointsAsyncTask.execute("RS").get();
        while (pullCitiesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2,cities.size());
        //assertEquals(bus1204.getTime(),buses.get(0).getTime());
    }

     @Test
    public void testClearCities() throws ExecutionException, InterruptedException {
        if(context!=null){
            clearCitiesEndpointsAsyncTask = new ClearCitiesEndpointsAsyncTask();
            clearCitiesEndpointsAsyncTask.setContext(context);
            clearCitiesEndpointsAsyncTask.execute("RS").get();
            while (clearCitiesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

            }

            pullCitiesEndpointsAsyncTask = new PullCitiesEndpointsAsyncTask();
            pullCitiesEndpointsAsyncTask.setContext(context);
            cities = pullCitiesEndpointsAsyncTask.execute("RS").get();
            while (pullCitiesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

            }
            assertEquals(0,cities.size());
        }


    }
}
