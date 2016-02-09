package br.com.expressobits.hbusgenerator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.support.test.runner.*;
import android.util.Log;
import android.util.Pair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.gae.PullCitiesEndpointsAsyncTask;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Testa puxar cidades do datastore
 * @author Rafael on 05/02/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class PullCitiesAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private static String TAG = "PullCitiesAndroidUnitTest";
    Context context;
    private MainActivity mActivity;
    List<City> cities = new ArrayList<>();

    public PullCitiesAndroidUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        context = mActivity;


        if(context!=null){
            cities = new PullCitiesEndpointsAsyncTask().execute(
                    new Pair<Context, String>(context, "RS")
            ).get();
            for (City city:cities){
                Log.i(TAG,city.getName()+" "+city.getCountry()+" "+city.getLocation());
            }


        }else{
            Log.e(TAG,"Context is null!");
        }

    }



    @Test
    public void testSizePullCities() throws ExecutionException, InterruptedException {
        assertTrue(cities.size() == 2);
    }


    @Test
    public void testSequencePullCities() throws ExecutionException, InterruptedException {
        assertEquals(cities.get(0).getName(),"Canoas");
    }

}
