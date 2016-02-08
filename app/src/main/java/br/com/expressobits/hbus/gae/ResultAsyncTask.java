package br.com.expressobits.hbus.gae;

import java.util.List;

/**
 * Created by rafael on 07/02/16.
 */
public interface ResultAsyncTask<Result> {

    public void finished(List<Result> results);
}
