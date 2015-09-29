

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Base AsyncTask which executes all web request in the background
 */
public class LiefernAsyncTask extends AsyncTask<Object, Object, Object>  {
    private static final String TAG = "LiefernAsyncTask";

    /** Activity context */
    private LiefernBaseActivity parentActivity;

    /** Flag to check error occurred or not */
    private boolean error = false;

    /** Message field in server response */
    private String message;



    /** Flag to handle Pause/Resume event of activity which has started this task  */
    private static boolean parentActivityPaused = false;

    /** To notify others whether task is in progress or not*/
    private static boolean taskInProgress = false;

    /** Dialog to notify user */
    private static ProgressDialog progressDialog;

    /** To acquire lock */
    private static final Object obj = new Object();

    /**
     *
     * @param oParent Valid context of calling activity
     */
    public LiefernAsyncTask(LiefernBaseActivity oParent) {
        this.parentActivity = oParent;
    }

    /**
     * Displays progress dialog
     */
    @Override
    protected void onPreExecute()  {
        taskInProgress = true;
        error = false;
        webServiceModel = null;
        showProgressDialog();
    }

    /**
     * Processes calling activity's processService() in background thread.
     * Checks for error occurred during processing request
     */
    @Override
    protected Object doInBackground(Object... params) {
        try  {

            webServiceModel = parentActivity.processService();
            error = webServiceModel.isError();
            message = webServiceModel.getMessage();

        } catch (Exception e)  {

            e.printStackTrace();
            error = true;
            message = e.getMessage();

            try {

                error = webServiceModel.isError();
                message = webServiceModel.getMessage();

            } catch (Exception e2) {}

        } catch (Error e) {
            error = true;
        }

        if(error && (message == null || message.trim().length() == 0 )) {
            message = "Unable to execute";
        }

        // If calling activity is getting paused, then wait till it resumes
        synchronized (obj) {

            if(parentActivityPaused) {
                Log.d(TAG, "Waiting parent activity paused");
                try {
                    obj.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return webServiceModel;
    }

    public void doPause() {
        if(taskInProgress)
            parentActivityPaused = true;
    }

    /**
     * If calling activity is resumed and task is in progress then notify waiting threads.
     */
    public void doResume() {

        if(taskInProgress) {
            synchronized (obj) {
                obj.notifyAll();
            }
        }
        parentActivityPaused = false;
    }

    /**
     * Notify response to caller, in case of error displays error dialog
     */
    @Override
    protected void onPostExecute(Object result) {

        synchronized (obj) {

            if(!error) {
                parentActivity.notifyWebResponse(webServiceModel);
            } else {
                parentActivity.notifyWebResponseError(webServiceModel);
                parentActivity.showErrorDialog(message);
            }

            doCleanUp();

            taskInProgress = false;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        doCleanUp();
    }

    public boolean isInProgress() {
        return taskInProgress;
    }

    /**
     * Clean up steps which you want to perform when task completes/cancelled.
     */
    private void doCleanUp() {
        taskInProgress = false;
        dismissDialog();
    }

    private void showProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new CustomDialog(parentActivity);
            progressDialog.setTitle(R.string.please_wait);
            progressDialog.setMessage(parentActivity.getString(R.string.loading));
            progressDialog.setCancelable(false);
        }
        if(progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}
