package br.com.expressobits.hbusgenerator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.gae.PushCitiesEndpointsAsyncTask;

/**
 * @author Rafael
 * @since 07/02/16
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class PushCitiesAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static String TAG = "PullCitiesAndroidUnitTest";
    PushCitiesEndpointsAsyncTask pushCitiesEndpointsAsyncTask;

    Context context;
    private MainActivity mActivity;

    public PushCitiesAndroidUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        context = mActivity;




        if(context!=null){
            pushCitiesEndpointsAsyncTask = new PushCitiesEndpointsAsyncTask();
            pushCitiesEndpointsAsyncTask.setContext(context);

            City alegrete = new City();
            alegrete.setName("Alegrete");
            alegrete.setCountry("RS");
            alegrete.setLocation(new GeoPt().setLatitude(-29.7913059f).setLongitude(-55.8162755f));

            City portoAlegre = new City();
            portoAlegre.setName("Porto Alegre");
            portoAlegre.setCountry("RS");
            portoAlegre.setLocation(new GeoPt().setLatitude(-30.1021474f).setLongitude(-51.2960047f));

            City cruzAlta = new City();
            cruzAlta.setName("Cruz Alta");
            cruzAlta.setCountry("RS");
            cruzAlta.setLocation(new GeoPt().setLatitude(-28.6415466f).setLongitude(-53.6268689f));

            City santaMaria = new City();
            santaMaria.setName("Santa Maria");
            santaMaria.setCountry("RS");
            santaMaria.setLocation(new GeoPt().setLatitude(-29.776722f).setLongitude(-54.1046164f));
            pushCitiesEndpointsAsyncTask.execute(alegrete, portoAlegre, cruzAlta, santaMaria).get();
        }else{
            Log.e(TAG,"Context is null!");
        }

    }



    @Test
    public void testSizePullCities() throws ExecutionException, InterruptedException {


        assertEquals(4,(int)pushCitiesEndpointsAsyncTask.get());
    }

}
