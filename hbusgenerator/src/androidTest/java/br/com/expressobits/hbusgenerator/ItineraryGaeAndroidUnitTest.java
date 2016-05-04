package br.com.expressobits.hbusgenerator;

import android.content.Context;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.gae.ClearItinerariesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullItinerariesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushItinerariesEndpointsAsyncTask;

/**
 * Test of operations with class of Itinerary in Cloud of Google.
 * @author Rafael Correa
 * @since 08/02/16
 */
@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ItineraryGaeAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private static String TAG = "Itinerary Gae Test";
    PushItinerariesEndpointsAsyncTask pushItinerariesEndpointsAsyncTask;
    ClearItinerariesEndpointsAsyncTask clearItinerariesEndpointsAsyncTask;
    PullItinerariesEndpointsAsyncTask pullItinerariesEndpointsAsyncTask;

    Context context;
    private MainActivity mActivity;

    private City city;
    Itinerary maringa;
    Itinerary vilaSchirmer;

    public ItineraryGaeAndroidUnitTest() {
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
        city.setLocation(new GeoPt().setLatitude(-29.776722f).setLongitude(-54.1046164f));

        maringa = new Itinerary();
        maringa.setName("São josé");
        maringa.setWays(Arrays.asList("Centro > Bairro", "Bairro > Centro"));
        maringa.setCodes(Arrays.asList("53B", "155A"));

        vilaSchirmer = new Itinerary();
        vilaSchirmer.setName("Vila Schirmer");
        vilaSchirmer.setWays(Arrays.asList("Centro > Bairro", "Bairro > Centro"));
        vilaSchirmer.setCodes(Arrays.asList("53B", "155A"));
    }

    @Test
    public void testRemoveItineraries() throws ExecutionException, InterruptedException {

        if (context != null){
            clearItinerariesEndpointsAsyncTask = new ClearItinerariesEndpointsAsyncTask();
            clearItinerariesEndpointsAsyncTask.setContext(context);
            clearItinerariesEndpointsAsyncTask.setCityName(city.getName());
            clearItinerariesEndpointsAsyncTask.setCountry(city.getCountry());
            clearItinerariesEndpointsAsyncTask.execute(city).get();
        }else{
            Log.e(TAG, "Context is null!");
        }

        while (clearItinerariesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(0, (int) clearItinerariesEndpointsAsyncTask.get());

        if (context != null){
            pullItinerariesEndpointsAsyncTask = new PullItinerariesEndpointsAsyncTask();
            pullItinerariesEndpointsAsyncTask.setContext(context);
            pullItinerariesEndpointsAsyncTask.execute(city).get();
        }else{
            Log.e(TAG, "Context is null!");
        }
        while (pullItinerariesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(0, pullItinerariesEndpointsAsyncTask.get().size());


    }




    @Test
    public void testPullItineraries() throws ExecutionException, InterruptedException {


        if (context != null){
            pullItinerariesEndpointsAsyncTask = new PullItinerariesEndpointsAsyncTask();
            pullItinerariesEndpointsAsyncTask.setContext(context);
            pullItinerariesEndpointsAsyncTask.execute(city).get();
        }else{
            Log.e(TAG, "Context is null!");
        }
        while (pullItinerariesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        List<Itinerary> itTest = pullItinerariesEndpointsAsyncTask.get();
        assertEquals(2, itTest.size());
        assertEquals(maringa.getName(), itTest.get(0).getName());
        assertEquals(maringa.getWays(), itTest.get(0).getWays());

    }

    @Test
    public void testPushItineraries() throws ExecutionException, InterruptedException {

        if (context != null){
            pushItinerariesEndpointsAsyncTask = new PushItinerariesEndpointsAsyncTask();
            pushItinerariesEndpointsAsyncTask.setContext(context);
            pushItinerariesEndpointsAsyncTask.setCityName(city.getName());
            pushItinerariesEndpointsAsyncTask.setCountry(city.getCountry());
            pushItinerariesEndpointsAsyncTask.execute(maringa, vilaSchirmer).get();
        }else{
            Log.e(TAG, "Context is null!");
        }

        while (pushItinerariesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2, (int)pushItinerariesEndpointsAsyncTask.get());

        if (context != null){
            pullItinerariesEndpointsAsyncTask = new PullItinerariesEndpointsAsyncTask();
            pullItinerariesEndpointsAsyncTask.setContext(context);
            pullItinerariesEndpointsAsyncTask.execute(city).get();
        }else{
            Log.e(TAG, "Context is null!");
        }
        while (pullItinerariesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2, pullItinerariesEndpointsAsyncTask.get().size());
    }







}
