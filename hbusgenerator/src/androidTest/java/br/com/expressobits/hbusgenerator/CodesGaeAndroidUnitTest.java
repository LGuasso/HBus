package br.com.expressobits.hbusgenerator;

import android.content.Context;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.backend.codeApi.model.Code;
import br.com.expressobits.hbus.gae.PullCodesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushCodesEndpointsAsyncTask;

/**
 * Test of operations with class of Itinerary in Cloud of Google.
 * @author Rafael Correa
 * @since 12/02/16
 */
@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(AndroidJUnit4.class)
@SmallTest
public class CodesGaeAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static String TAG = "Itinerary Gae Test";
    PushCodesEndpointsAsyncTask pushCodesEndpointsAsyncTask;
    //ClearItinerariesEndpointsAsyncTask clearItinerariesEndpointsAsyncTask;
    PullCodesEndpointsAsyncTask pullCodesEndpointsAsyncTask;

    Context context;
    private MainActivity mActivity;

    private City city;
    Code code155A;
    Code code110B;

    public CodesGaeAndroidUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        context = mActivity;

        city = new City();
        city.setName("Testâlandia");
        city.setCountry("RS");
        city.setLocation(new GeoPt().setLatitude(-109.776722f).setLongitude(-14.1046164f));

        code155A = new Code();
        code155A.setName("155A");
        code155A.setDescrition("Loucura");

        code110B = new Code();
        code110B.setName("110B");
        code110B.setDescrition("Rem");
    }

    @Test
    public void testCodes() throws ExecutionException, InterruptedException {
        if(context!=null){
            pushCodesEndpointsAsyncTask = new PushCodesEndpointsAsyncTask();
            pushCodesEndpointsAsyncTask.setContext(context);
            pushCodesEndpointsAsyncTask.setCountry(city.getCountry());
            pushCodesEndpointsAsyncTask.setCityName(city.getName());
            pushCodesEndpointsAsyncTask.execute(code155A,code110B).get();
        }

        while (pushCodesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2, (int) pushCodesEndpointsAsyncTask.get());


        pullCodesEndpointsAsyncTask = new PullCodesEndpointsAsyncTask();
        pullCodesEndpointsAsyncTask.setContext(context);
        List<Code> codes = pullCodesEndpointsAsyncTask.execute(city).get();
        while (pullCodesEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2,codes.size());
        assertEquals(code110B.getName(),codes.get(0).getName());
    }

}
