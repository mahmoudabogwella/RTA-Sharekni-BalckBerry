package rta.ae.sharekni;

import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Locale;

import rta.ae.sharekni.Arafa.Activities.DriverDetails;
import rta.ae.sharekni.Arafa.Classes.GetData;
import rta.ae.sharekni.Arafa.DataModel.BestRouteDataModel;
import rta.ae.sharekni.Arafa.DataModel.BestRouteDataModelDetails;
import rta.ae.sharekni.Arafa.DataModelAdapter.BestRouteDataModelAdapterDetails;


public class MostRidesDetails extends AppCompatActivity {

    Toolbar toolbar;
    ListView lv;
    int FromEmirateId, ToEmirateId, FromRegionId, ToRegionId;

    SharedPreferences myPrefs;
    BestRouteDataModel data;

    TextView txt_FromEm;
    TextView txt_FromReg;
    TextView txt_ToEm;
    TextView txt_ToReg;
    back back;

    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_rides_details);
        lv = (ListView) findViewById(R.id.lv_most_rides_details);
        txt_FromEm = (TextView) findViewById(R.id.From_Emirate_best);
        txt_FromReg = (TextView) findViewById(R.id.From_Reg_best);
        txt_ToEm = (TextView) findViewById(R.id.To_Emirate_best);
        txt_ToReg = (TextView) findViewById(R.id.To_Reg_best);

        initToolbar();

        Bundle in = getIntent().getExtras();
        data = (BestRouteDataModel) in.getParcelableArrayList("Data");

        myPrefs = this.getSharedPreferences("myPrefs", 0);
        ID = myPrefs.getString("account_id", null);


        assert data != null;
        Log.d("Emirate name from", data.getFromEm());
        Log.d("Emirate name to", data.getToEm());
        Log.d("REg nameto ", data.getToReg());
        Log.d("REg From", data.getFromReg());

        try {
            assert data != null;
            FromEmirateId = data.getFromEmId();
            FromRegionId = data.getFromRegid();

            ToEmirateId = data.getToEmId();
            ToRegionId = data.getToRegId();

            Log.d("test most Em From: ", String.valueOf(FromEmirateId));
            Log.d("test most Reg From: ", String.valueOf(FromRegionId));
            Log.d("test most  To Emirate: ", String.valueOf(ToEmirateId));
            Log.d("test most : To Region ", String.valueOf(ToRegionId));


        } catch (NullPointerException e) {
            Toast.makeText(MostRidesDetails.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        back = new back();

        back.execute();

        txt_FromEm.setText(data.getFromEm());
        txt_ToEm.setText(data.getFromReg());
        txt_FromReg.setText(data.getToReg());
        txt_ToReg.setText(data.getToEm());
    }

    private class back extends AsyncTask {
        JSONArray jsonArray;
        boolean exists = false;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MostRidesDetails.this);
            pDialog.setMessage(getString(R.string.loading) + "...");
            pDialog.setIndeterminate(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
        protected void onPostExecute(Object o) {
            String days = "";
            if (jsonArray.length() != 0 || jsonArray != null) {

                final BestRouteDataModelDetails[] driver = new BestRouteDataModelDetails[jsonArray.length()];
                final ArrayList<BestRouteDataModelDetails> ar = new ArrayList<>();

                try {
                    JSONObject json;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            final BestRouteDataModelDetails item = new BestRouteDataModelDetails(Parcel.obtain());
                            json = jsonArray.getJSONObject(i);
                            item.setFromEmId(json.getInt("FromEmirateId"));
                            item.setFromRegid(json.getInt("FromRegionId"));
                            item.setToEmId(json.getInt("ToEmirateId"));
                            item.setToRegId(json.getInt("ToRegionId"));
                            item.setDriverName(json.getString("DriverName"));
                            item.setNationality_en(json.getString(getString(R.string.nat_name)));
                            item.setSDG_Route_Start_FromTime(json.getString("StartTime"));
                            item.setDriverMobile(json.getString("DriverMobile"));
                            item.setDriverRating(json.getString("Rating"));
                            item.setDriverId(json.getInt("AccountId"));
                            item.setRouteId(json.getInt("RouteId"));
                            item.setPhotoURl(json.getString("DriverPhoto"));
                            // Testing Line
                            item.setLastSeen(json.getString("LastSeen"));
                            if (!json.getString("DriverPhoto").equals("NoImage.png")) {
                                GetData gd = new GetData();
                                item.setDriverPhoto(gd.GetImage(json.getString("DriverPhoto").replaceAll(" ", "")));
                            }
                            days = "";

                            int x1 = json.getInt("GreenPoints");
                            int x3 = json.getInt("CO2Saved");
                            x3 = x3 / 1000;

                            item.setGreenPoints(String.valueOf(x1));
                            item.setGreenCo2Saving(String.valueOf(x3));

                            //  item.setGreenPoints(json.getString("GreenPoints"));
                            //  item.setGreenCo2Saving(json.getString("CO2Saved"));

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
                                days += getString(R.string.tue);
                            }
                            if (json.getString("Wendenday").equals("true")) {
                                days += getString(R.string.wed);
                            }
                            if (json.getString("Thrursday").equals("true")) {
                                days += getString(R.string.thu);

                            }
                            if (json.getString("Friday").equals("true")) {
                                days += getString(R.string.fri);
                            }
                            if (!days.equals("")) {
                                item.setSDG_RouteDays(days.substring(1));
                            }
                            days = "";

                            driver[i] = item;
                            BestRouteDataModelAdapterDetails arrayAdapter = new BestRouteDataModelAdapterDetails(MostRidesDetails.this, R.layout.quick_search_list_item_2, driver);
                            lv.setAdapter(arrayAdapter);
                            ar.add(i, driver[i]);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent in = new Intent(MostRidesDetails.this, DriverDetails.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    in.putExtra("DriverID", driver[i].getDriverId());
                                    in.putExtra("PassengerID", ID);
                                    in.putExtra("RouteID", driver[i].getRouteId());
                                    MostRidesDetails.this.startActivity(in);
                                }

                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                hidePDialog();
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
                        new AlertDialog.Builder(MostRidesDetails.this)
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
                        Toast.makeText(MostRidesDetails.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                GetData j = new GetData();

                if (ID == null) {
                    String url = GetData.DOMAIN + "GetMostDesiredRideDetails?AccountID=" + 0 + "&FromEmirateID=" + FromEmirateId + "&FromRegionID=" + FromRegionId + "&ToEmirateID=" + ToEmirateId + "&ToRegionID=" + ToRegionId;
                    Log.d("Url", url);
                    try {
                        jsonArray = j.MostRidesDetails(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String url = GetData.DOMAIN + "GetMostDesiredRideDetails?AccountID=" + ID + "&FromEmirateID=" + FromEmirateId + "&FromRegionID=" + FromRegionId + "&ToEmirateID=" + ToEmirateId + "&ToRegionID=" + ToRegionId;
                    Log.d("Url", url);
                    try {
                        jsonArray = j.MostRidesDetails(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_most_rides_details, menu);
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
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.mytext_appbar);
        textView.setText(R.string.ride_details);
//        toolbar.setElevation(10);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Locale locale = Locale.getDefault();
            String Locale_Str2 = locale.toString();
            if (!Locale_Str2.contains("ar")&&!Locale_Str2.contains("ur")) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            } else {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_forward);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (back.getStatus() == AsyncTask.Status.RUNNING) {
            back.cancel(true);
        }
        finish();

    }
}
