package rta.ae.sharekni;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Locale;

import rta.ae.sharekni.Arafa.Activities.DriverDetails;
import rta.ae.sharekni.RideDetails.RideDetailsAsDriver;
import rta.ae.sharekni.Arafa.Classes.GetData;
import rta.ae.sharekni.Arafa.Classes.VolleySingleton;
import rta.ae.sharekni.Arafa.DataModel.BestRouteDataModel;


public class HistoryNew extends AppCompatActivity {

    String url = GetData.DOMAIN + "GetDriverDetailsByAccountId?AccountId=";
    String url2 = GetData.DOMAIN + "Passenger_MyApprovedRides?AccountId=";

    ListView Passenger_Approved_Rides_Lv;

    String days;

    Toolbar toolbar;
    int Driver_ID;

    ListView user_ride_created;

    SharedPreferences myPrefs;
    int FLAG_DRIVER_CREATED;
    int FLAG_DRIVER_JOINED;
    Boolean CREATED = false;
    Boolean JOINED = false;

    String AccountType;
    Activity c;

    rideJson rideJson;
    rideJson2 rideJson2;
    rideJson3 rideJson3;

    RelativeLayout history_created_rides_realtive;
    RelativeLayout history_joined_rides_realtive;
    TextView driver_profile_RouteEnName;
    TextView driver_profile_RouteEnName2;


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RadioGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (rideJson.getStatus() == AsyncTask.Status.RUNNING) {
            rideJson.cancel(true);
        }
        if (rideJson2.getStatus() == AsyncTask.Status.RUNNING) {
            rideJson2.cancel(true);
        }
        if (rideJson3.getStatus() == AsyncTask.Status.RUNNING) {
            rideJson3.cancel(true);
        }
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_new);

        user_ride_created = (ListView) findViewById(R.id.user_ride_created);
        Passenger_Approved_Rides_Lv = (ListView) findViewById(R.id.Passenger_Approved_Rides_Lv);
        history_created_rides_realtive = (RelativeLayout) findViewById(R.id.history_created_rides_realtive);
        driver_profile_RouteEnName = (TextView) findViewById(R.id.driver_profile_RouteEnName);
        history_joined_rides_realtive = (RelativeLayout) findViewById(R.id.history_joined_rides_realtive);
        driver_profile_RouteEnName2 = (TextView) findViewById(R.id.driver_profile_RouteEnName2);


        initToolbar();
        myPrefs = this.getSharedPreferences("myPrefs", 0);
        String ID = myPrefs.getString("account_id", null);
//        Bundle in = getIntent().getExtras();
//        Log.d("Intent Id :", String.valueOf(in.getInt("DriverID")));
        Driver_ID = Integer.parseInt(ID);
        AccountType = myPrefs.getString("account_type", null);
        Log.d("Driver Id", String.valueOf(Driver_ID));
        Log.d("Type", AccountType);


        rideJson = new rideJson();
        rideJson2 = new rideJson2();
        rideJson3 = new rideJson3();


        c = this;

        if (AccountType.equals("false")) {

            rideJson.execute();
            rideJson2.execute();


        } else if (AccountType.equals("true")) {
            user_ride_created.setVisibility(View.GONE);
            driver_profile_RouteEnName.setVisibility(View.GONE);
            history_created_rides_realtive.setVisibility(View.GONE);
            rideJson3.execute();

        }


        Log.d("create flag 1", String.valueOf(FLAG_DRIVER_CREATED));
        Log.d("join flag 1", String.valueOf(FLAG_DRIVER_JOINED));


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.mytext_appbar);
        textView.setText(getString(R.string.history));
//        toolbar.setElevation(10);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Locale locale = Locale.getDefault();
            String Locale_Str2 = locale.toString();
            if (Locale_Str2.contains("en")) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            } else {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_forward);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private class rideJson extends AsyncTask {
        boolean exists = false;
        ProgressDialog pDialog;


        @Override
        protected void onPostExecute(Object o) {
            CREATED = true;
            hidePDialog();
            super.onPostExecute(o);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HistoryNew.this);
            pDialog.setMessage(getString(R.string.loading) + "...");
            pDialog.show();
            super.onPreExecute();
        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
                Socket sock = new Socket();
                int timeoutMs = 2000;   // 2 seconds
                sock.connect(sockaddr, timeoutMs);
                exists = true;
            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(HistoryNew.this)
                                .setTitle(getString(R.string.connection_problem))
                                .setMessage(getString(R.string.con_problem_message))
                                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(getIntent());
                                    }
                                })
                                .setNegativeButton(getString(R.string.goBack), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        Toast.makeText(HistoryNew.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + Driver_ID,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response = response.replaceAll("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                                response = response.replaceAll("<string xmlns=\"http://Sharekni-MobAndroid-Data.org/\">", "");
                                response = response.replaceAll("</string>", "");
                                // Display the first 500 characters of the response string.
                                String data = response.substring(40);
                                Log.d("url", url + Driver_ID);
                                try {
                                    JSONArray jArray = new JSONArray(data);
                                    final BestRouteDataModel[] driver = new BestRouteDataModel[jArray.length()];
                                    JSONObject json;
                                    if (jArray.length() == 0) {
                                        Log.d("Error 3 ", "Error3");

                                        FLAG_DRIVER_CREATED = 1;

                                        user_ride_created.setVisibility(View.GONE);
                                        driver_profile_RouteEnName.setVisibility(View.GONE);
                                        history_created_rides_realtive.setVisibility(View.GONE);

//                                        final Dialog dialog = new Dialog(c);
//                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        dialog.setContentView(R.layout.noroutesdialog);
//                                        Button btn = (Button) dialog.findViewById(R.id.noroute_id);
//                                        TextView Text_3 = (TextView) dialog.findViewById(R.id.Text_3);
//                                        dialog.show();
//                                        Text_3.setText("There is no Rides Created yet");
//
//                                        btn.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                dialog.dismiss();
//                                                c.finish();
//                                            }
//                                        });

                                    }


                                    for (int i = 0; i < jArray.length(); i++) {
                                        try {
                                            BestRouteDataModel item = new BestRouteDataModel(Parcel.obtain());
                                            days = "";
                                            json = jArray.getJSONObject(i);
                                            item.setID(json.getInt("ID"));
                                            item.setFromEm(json.getString(getString(R.string.from_em_en_name)));
                                            item.setFromReg(json.getString(getString(R.string.from_reg_en_name)));
                                            item.setToEm(json.getString(getString(R.string.to_em_en_name)));
                                            item.setToReg(json.getString(getString(R.string.to_reg_en_name)));
                                            item.setRouteName(json.getString(getString(R.string.route_name)));
//                                            item.setStartFromTime(json.getString("StartFromTime"));
//                                            item.setEndToTime_(json.getString("EndToTime_"));

                                            if (json.getString("Saturday").equals("true")) {
                                                days += getString(R.string.sat);
                                            }
                                            if (json.getString("Sunday").equals("true")) {
                                                days += getString(R.string.sun);

                                            }
                                            if (json.getString("Monday").equals("true")) {
                                                days += getString(R.string.mon);

                                            }
                                            if (json.getString("Tuesday").equals("true")) {
                                                days += getString(R.string.thu);
                                            }
                                            if (json.getString("Wednesday").equals("true")) {
                                                days += getString(R.string.wed);
                                            }
                                            if (json.getString("Thursday").equals("true")) {
                                                days += getString(R.string.thu);

                                            }
                                            if (json.getString("Friday").equals("true")) {
                                                days += getString(R.string.fri);
                                            }


                                            item.setDriver_profile_dayWeek(days);
                                            days = "";

                                            driver[i] = item;
                                            Log.d("ID", String.valueOf(json.getInt("ID")));
                                            Log.d("FromEmlv", json.getString("FromEmirateEnName"));
                                            Log.d("FromReglv", json.getString("FromRegionEnName"));
                                            Log.d("TomEmlv", json.getString("ToEmirateEnName"));
                                            Log.d("ToReglv", json.getString("ToRegionEnName"));


                                            user_ride_created.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    Intent in = new Intent(HistoryNew.this, RideDetailsAsDriver.class);
                                                    in.putExtra("RouteID", driver[i].getID());
                                                    in.putExtra("DriverID", Driver_ID);
                                                    HistoryNew.this.startActivity(in);


                                                }

                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("Error 1 ", e.toString());
                                        }
                                    }

                                    HistoryNewAdapter arrayAdapter = new HistoryNewAdapter(HistoryNew.this, R.layout.history_created_joined_rides_list_item, driver);
                                    user_ride_created.setAdapter(arrayAdapter);
                                    setListViewHeightBasedOnChildren(user_ride_created);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("Error 2 ", e.toString());
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                VolleySingleton.getInstance(HistoryNew.this).addToRequestQueue(stringRequest);
            }
            return null;
        }
    }


    private class rideJson2 extends AsyncTask {
        ProgressDialog pDialog;

        @Override
        protected void onPostExecute(Object o) {
            JOINED = true;
            Log.d("create flag", String.valueOf(FLAG_DRIVER_CREATED));
            Log.d("join flag", String.valueOf(FLAG_DRIVER_JOINED));
            hidePDialog();


            if (FLAG_DRIVER_JOINED == 1 && FLAG_DRIVER_CREATED == 1) {

                CREATED = false;
                JOINED = false;

                final Dialog dialog = new Dialog(c);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.noroutesdialog);
                Button btn = (Button) dialog.findViewById(R.id.noroute_id);
                TextView Text_3 = (TextView) dialog.findViewById(R.id.Text_3);
                dialog.show();
                Text_3.setText(R.string.No_History);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        c.finish();
                    }
                });

            }

            super.onPostExecute(o);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HistoryNew.this);
            pDialog.setMessage(getString(R.string.loading) + "...");
            pDialog.show();
            super.onPreExecute();
        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            boolean exists = false;
            try {
                SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
                Socket sock = new Socket();
                int timeoutMs = 2000;   // 2 seconds
                sock.connect(sockaddr, timeoutMs);
                exists = true;
            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(HistoryNew.this)
                                .setTitle(getString(R.string.connection_problem))
                                .setMessage(getString(R.string.con_problem_message))
                                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(getIntent());
                                    }
                                })
                                .setNegativeButton(getString(R.string.goBack), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        Toast.makeText(HistoryNew.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                final GetData GD = new GetData();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url2 + Driver_ID,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response = response.replaceAll("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                                response = response.replaceAll("<string xmlns=\"http://Sharekni-MobAndroid-Data.org/\">", "");
                                response = response.replaceAll("</string>", "");
                                // Display the first 500 characters of the response string.
                                String data = response.substring(40);
                                Log.d("url", url + Driver_ID);
                                try {
                                    JSONArray jArray = new JSONArray(data);
                                    final BestRouteDataModel[] passenger = new BestRouteDataModel[jArray.length()];
                                    JSONObject json;


                                    if (jArray.length() == 0) {
                                        Log.d("Error 3 ", "Error3");


                                        FLAG_DRIVER_JOINED = 1;
                                        Passenger_Approved_Rides_Lv.setVisibility(View.INVISIBLE);
                                        history_joined_rides_realtive.setVisibility(View.INVISIBLE);
                                        driver_profile_RouteEnName2.setVisibility(View.INVISIBLE);


                                        if (FLAG_DRIVER_JOINED == 1 && FLAG_DRIVER_CREATED == 1) {

                                            CREATED = false;
                                            JOINED = false;

                                            final Dialog dialog = new Dialog(c);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setContentView(R.layout.noroutesdialog);
                                            Button btn = (Button) dialog.findViewById(R.id.noroute_id);
                                            TextView Text_3 = (TextView) dialog.findViewById(R.id.Text_3);
                                            dialog.show();
                                            Text_3.setText(R.string.No_History);

                                            btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    c.finish();
                                                }
                                            });

                                        }

//                                        final Dialog dialog = new Dialog(c);
//                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        dialog.setContentView(R.layout.noroutesdialog);
//                                        Button btn = (Button) dialog.findViewById(R.id.noroute_id);
//                                        TextView Text_3 = (TextView) dialog.findViewById(R.id.Text_3);
//                                        dialog.show();
//                                        Text_3.setText("There is no Rides joined");
//
//                                        btn.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                dialog.dismiss();
//                                                c.finish();
//                                            }
//                                        });

                                    }


                                    for (int i = 0; i < jArray.length(); i++) {
                                        try {
                                            BestRouteDataModel item = new BestRouteDataModel(Parcel.obtain());
                                            days = "";
                                            json = jArray.getJSONObject(i);
                                            int Route_ID = (json.getInt("RouteID"));
                                            int Driver_Account = (json.getInt("Account"));
                                            String Route_Name = (json.getString("Name_en"));
                                            item.setRoutePassengerId(json.getInt("RoutePassengerId"));
                                            Log.d("Route id", String.valueOf(Route_ID));
                                            Log.d("Driver_account", String.valueOf(Driver_Account));
                                            Log.d("Route Name", Route_Name);

                                            JSONObject jsonObject = GD.GetRouteById(Route_ID);
                                            String Routename2 = jsonObject.getString("RouteEnName");
                                            Log.d("Route name 2 ", Routename2);

                                            item.setFromEm(jsonObject.getString(getString(R.string.from_em_en_name)));
                                            item.setFromReg(jsonObject.getString(getString(R.string.from_reg_en_name)));
                                            item.setToEm(jsonObject.getString(getString(R.string.to_em_en_name)));
                                            item.setToReg(jsonObject.getString(getString(R.string.to_reg_en_name)));
                                            item.setRouteName(jsonObject.getString(getString(R.string.route_name)));
                                            //          item.setStartFromTime(jsonObject.getString("StartFromTime"));
                                            //        item.setEndToTime_(jsonObject.getString("EndToTime_"));

                                            item.setDriver_ID(Driver_Account);

                                            passenger[i] = item;

                                            Passenger_Approved_Rides_Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent intent = new Intent(HistoryNew.this, DriverDetails.class);
                                                    intent.putExtra("DriverID", passenger[position].getDriver_ID());
                                                    HistoryNew.this.startActivity(intent);
                                                }
                                            });


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    HistoryNewAdapter arrayAdapter = new HistoryNewAdapter(HistoryNew.this, R.layout.history_created_joined_rides_list_item, passenger);
                                    Passenger_Approved_Rides_Lv.setAdapter(arrayAdapter);
                                    setListViewHeightBasedOnChildren(Passenger_Approved_Rides_Lv);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error : ", error.toString());
                        //Ride.setText("That didn't work! : " + error.toString());
                    }
                });
                VolleySingleton.getInstance(HistoryNew.this).addToRequestQueue(stringRequest);
            }
            return null;
        }  //  do in background


    } //  Ride Json


    private class rideJson3 extends AsyncTask {
        ProgressDialog pDialog;

        @Override
        protected void onPostExecute(Object o) {
            hidePDialog();
            super.onPostExecute(o);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HistoryNew.this);
            pDialog.setMessage(getString(R.string.loading) + "...");
            pDialog.show();
            super.onPreExecute();
        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            boolean exists = false;
            try {
                SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
                Socket sock = new Socket();
                int timeoutMs = 2000;   // 2 seconds
                sock.connect(sockaddr, timeoutMs);
                exists = true;
            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(HistoryNew.this)
                                .setTitle(getString(R.string.connection_problem))
                                .setMessage(getString(R.string.con_problem_message))
                                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(getIntent());
                                    }
                                })
                                .setNegativeButton(getString(R.string.goBack), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        Toast.makeText(HistoryNew.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                final GetData GD = new GetData();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url2 + Driver_ID,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response = response.replaceAll("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                                response = response.replaceAll("<string xmlns=\"http://Sharekni-MobAndroid-Data.org/\">", "");
                                response = response.replaceAll("</string>", "");
                                // Display the first 500 characters of the response string.
                                String data = response.substring(40);
                                Log.d("url", url2 + Driver_ID);
                                try {
                                    JSONArray jArray = new JSONArray(data);
                                    final BestRouteDataModel[] passenger = new BestRouteDataModel[jArray.length()];
                                    JSONObject json;


                                    if (jArray.length() == 0) {
                                        Log.d("Error 3 ", "Error3");

                                        final Dialog dialog = new Dialog(c);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.noroutesdialog);
                                        Button btn = (Button) dialog.findViewById(R.id.noroute_id);
                                        TextView Text_3 = (TextView) dialog.findViewById(R.id.Text_3);
                                        dialog.show();
                                        Text_3.setText(R.string.No_History);

                                        btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                c.finish();
                                            }
                                        });

                                    }


                                    for (int i = 0; i < jArray.length(); i++) {
                                        try {
                                            BestRouteDataModel item = new BestRouteDataModel(Parcel.obtain());
                                            days = "";
                                            json = jArray.getJSONObject(i);
                                            int Route_ID = (json.getInt("RouteID"));
                                            int Driver_Account = (json.getInt("Account"));
                                            String Route_Name = (json.getString("Name_en"));
                                            item.setRoutePassengerId(json.getInt("RoutePassengerId"));
                                            Log.d("Route id", String.valueOf(Route_ID));
                                            Log.d("Driver_account", String.valueOf(Driver_Account));
                                            Log.d("Route Name", Route_Name);

                                            JSONObject jsonObject = GD.GetRouteById(Route_ID);
                                            String Routename2 = jsonObject.getString("RouteEnName");
                                            Log.d("Route name 2 ", Routename2);

                                            item.setFromEm(jsonObject.getString(getString(R.string.from_em_en_name)));
                                            item.setFromReg(jsonObject.getString(getString(R.string.from_reg_en_name)));
                                            item.setToEm(jsonObject.getString(getString(R.string.to_em_en_name)));
                                            item.setToReg(jsonObject.getString(getString(R.string.to_reg_en_name)));
                                            item.setRouteName(jsonObject.getString(getString(R.string.route_name)));
                                            //          item.setStartFromTime(jsonObject.getString("StartFromTime"));
                                            //        item.setEndToTime_(jsonObject.getString("EndToTime_"));

                                            item.setDriver_ID(Driver_Account);

                                            passenger[i] = item;

                                            Passenger_Approved_Rides_Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent intent = new Intent(HistoryNew.this, DriverDetails.class);
                                                    intent.putExtra("DriverID", passenger[position].getDriver_ID());
                                                    HistoryNew.this.startActivity(intent);
                                                }
                                            });


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    HistoryNewAdapter arrayAdapter = new HistoryNewAdapter(HistoryNew.this, R.layout.history_created_joined_rides_list_item, passenger);
                                    Passenger_Approved_Rides_Lv.setAdapter(arrayAdapter);
                                    setListViewHeightBasedOnChildren(Passenger_Approved_Rides_Lv);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error : ", error.toString());
                        //Ride.setText("That didn't work! : " + error.toString());
                    }
                });
                VolleySingleton.getInstance(HistoryNew.this).addToRequestQueue(stringRequest);
            }
            return null;
        }  //  do in background


    } //  Ride Json


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_create_car_pool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
