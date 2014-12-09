package com.grizzly.apf.Dao.Ormlite;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.grizzly.apf.Ormlite.Model.BaseModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by Fco on 12/1/14.
 */
public class AsyncDao<T extends BaseModel, C, O extends OrmLiteSqliteOpenHelper>
        extends AsyncTask<Void, Void, Boolean> {

    private AdvancedDao<T, C, O> dao;
    private AfterOperation<T> afterDao = null;

    public AsyncDao(Class<T> entityClass, Class<C> idClass, Class<O> schemaClass){
        dao = new AdvancedDao<>(entityClass, idClass, schemaClass);
    }

    public AsyncDao(AdvancedDao<T, C, O> dao){
        this.dao = dao;
    }

    public void setAfterOperation(AfterOperation<T> afterOperation){
        afterDao = afterOperation;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /*TODO: Code for preparation.
        if(activity != null){
            if(waitingMessage == null || waitingMessage.trim().equalsIgnoreCase("")){
                waitingMessage = activity.getString(R.string.waiting_message);
            }
            pd = new ProgressDialog(activity);
            pd.setTitle(activity.getString(R.string.loading_data));
            pd.setMessage(waitingMessage);
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }*/

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        if(afterDao!=null){
            afterDao.doAfterDao(dao.getSource());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        /*TODO: Code for post execution.
        if(pd != null) {
            pd.dismiss();
        }

        this.result = result.booleanValue();

        if(result){
            if(taskCompletion != null){
                taskCompletion.onTaskCompleted(jsonResponseEntity);
            }
        }
        else{
            if(taskFailure != null){
                try {
                    taskFailure.onTaskFailed(jsonResponseEntityClass.newInstance(), failure);
                } catch (InstantiationException e) {
                    //e.printStackTrace();
                } catch (IllegalAccessException e) {
                    ////e.printStackTrace();
                }
            }
        }*/
    }
}
