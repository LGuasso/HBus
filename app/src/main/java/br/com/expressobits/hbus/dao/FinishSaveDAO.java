package br.com.expressobits.hbus.dao;

/**
 * Created by rafael on 24/04/16.
 */
public interface FinishSaveDAO {
    void finish();
    void progressUpdate(Object type,int progress,int total);
}
