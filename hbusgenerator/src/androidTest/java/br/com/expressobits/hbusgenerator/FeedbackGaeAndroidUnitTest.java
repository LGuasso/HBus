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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.feedbackApi.FeedbackApi;
import br.com.expressobits.hbus.backend.feedbackApi.model.Feedback;
import br.com.expressobits.hbus.gae.ClearCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PullCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushCitiesEndpointsAsyncTask;
import br.com.expressobits.hbus.gae.PushFeedbackEndpointsAsyncTask;

/**
 * @author Rafael
 * @since 12/02/16
 */
@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(AndroidJUnit4.class)
@SmallTest
public class FeedbackGaeAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static String TAG = "Feedback Gae Test";
    PushFeedbackEndpointsAsyncTask pushFeedbackEndpointsAsyncTask;

    Context context;
    private MainActivity mActivity;

    public List<Feedback> feedbacks;


    public FeedbackGaeAndroidUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        context = mActivity;

        feedbacks = new ArrayList<>();
        Feedback feedback = new Feedback();
        feedback.setEmail("Rafinha2108@gmail.com");
        feedback.setMessage("Teste de feedbacks adksodjiaoshdiajsdonasudonjas asdjasoidujiashdj saujdijasjh");
        feedback.setType(0);
        feedbacks.add(feedback);

    }

    @Test
    public void testPush() throws ExecutionException, InterruptedException {
        if(context!=null){
            pushFeedbackEndpointsAsyncTask = new PushFeedbackEndpointsAsyncTask();
            pushFeedbackEndpointsAsyncTask.setContext(context);
            pushFeedbackEndpointsAsyncTask.execute(feedbacks.get(0)).get();
        }

        while (pushFeedbackEndpointsAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)){

        }
        assertEquals(2, (int) pushFeedbackEndpointsAsyncTask.get());

    }

}
