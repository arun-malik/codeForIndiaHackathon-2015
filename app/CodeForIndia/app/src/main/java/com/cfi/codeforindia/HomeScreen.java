package com.cfi.codeforindia;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/*import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;*/
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HomeScreen extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        LatLng sjsu = new LatLng(37.3253, -122.0713);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(sjsu).title("Your emergency contact"));

        LatLng emer1 = new LatLng(37.3050, -122.1300);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(emer1).title("Rakshak Volunteer 1"));

        LatLng emer2 = new LatLng(37.3400, -122.0900);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(emer2).title("Rakshak Volunteer 2"));

        LatLng emer3 = new LatLng(37.30978796, -122.05541486);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(emer3).title("Rakshak Volunteer 3"));

        LatLng emer4 = new LatLng(37.31978796, -122.02541486);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(emer4).title("Rakshak Volunteer 4"));

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));
            System.out.println("my loc: " + location.getLatitude()+", "+location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(11)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sjsu,5));
    }

    public void emergencyButton(View view) {
        Intent i = new Intent(this, EmergencyContact.class);
        startActivity(i);
    }

    public void helpButton(View view) {
        Toast.makeText(getApplicationContext(), "Distress signal is send to Emergency contact and SOS volunteers around you. Help is on its way!!!",
                Toast.LENGTH_LONG).show();

      /*  OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://ominiscient.azure-mobile.net/tables/user")
                    .build();
            try {

                Response response = client.newCall(request).execute();
                System.out.println("get: " + response.body().string());
            } catch (Exception e){
                System.out.println("Exception: "+e);
            }*/

        LiefernAsyncTask asyncTask = new LiefernAsyncTask();
        asyncTask.execute(new Object());

        /*HttpPatch httpPatch = new HttpPatch("https://ominiscient.azure-mobile.net/tables/user/A2A82A4D-D967-401C-A2E8-D3131002B4AC");


        JSONObject obj=new JSONObject();
        try {
            obj.put("sensorlocation", "37.39978796,-122.05541486");
            obj.put("isdistress", "true");
        }catch (Exception e){

        }

        try {
            httpPatch.setEntity(new StringEntity(obj.toString()));
            httpPatch.setHeader("Content-type", "application/json");
            httpPatch.getEntity();
        } catch (Exception e){

        }*/


        //loginResult.parseJSON(WebServiceHelper.executeRequest(httpPost, 0));

    }


    /**
     * Base AsyncTask which executes all web request in the background
     */
    public static class LiefernAsyncTask extends AsyncTask<Object, Object, Object> {
        private static final String TAG = "LiefernAsyncTask";


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


        /**
         * Displays progress dialog
         */
        @Override
        protected void onPreExecute()  {
            taskInProgress = true;
            error = false;
            //showProgressDialog();
        }

        /**
         * Processes calling activity's processService() in background thread.
         * Checks for error occurred during processing request
         */
        @Override
        protected Object doInBackground(Object... params) {
            try  {

                //webServiceModel = parentActivity.processService();
                //error = webServiceModel.isError();
                //message = webServiceModel.getMessage();

                OkHttpClient client = new OkHttpClient();
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, "{\n" +
                        " \"id\": \"A2A82A4D-D967-401C-A2E8-D3131002B4AC\",\n" +
                        " \"name\": \"prachi\",\n" +
                        " \"mobile\": 4088020568,\n" +
                        " \"emailid\": \"shahprachi25@gmail.com\",\n" +
                        " \"password\": \"thisistestpassword\",\n" +
                        " \"sensorlocation\": \"37.400241, -122.055163\",\n" +
                        " \"applocation\": null,\n" +
                        " \"isvolunteer\": null,\n" +
                        " \"isdistress\": null,\n" +
                        " \"emergencymobile\": 4088346060,\n" +
                        " \"emergencyname\": \"Arun Malik\",\n" +
                        " \"emergencyemail\": \"malik.mgm@gmail.com\"\n" +
                        "}");
                Request request = new Request.Builder()
                        .url("https://ominiscient.azure-mobile.net/tables/user/A2A82A4D-D967-401C-A2E8-D3131002B4AC")
                        .patch(body)
                        .build();
                Response response = client.newCall(request).execute();
                System.out.println("patch: " +response.body().string());

                /*Request request = new Request.Builder()
                        .url("https://ominiscient.azure-mobile.net/tables/user")
                        .build();


                    Response response = client.newCall(request).execute();
                    System.out.println("get: " + response.body().string());*/


            } catch (Exception e)  {

                e.printStackTrace();
                error = true;
                message = e.getMessage();

                try {

                   // error = webServiceModel.isError();
                    //message = webServiceModel.getMessage();

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
                   // Log.d(TAG, "Waiting parent activity paused");
                    try {
                        obj.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null; //webServiceModel;
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
                    //parentActivity.notifyWebResponse(webServiceModel);
                } else {
                   // parentActivity.notifyWebResponseError(webServiceModel);
                    //parentActivity.showErrorDialog(message);
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
        }


    }

}
