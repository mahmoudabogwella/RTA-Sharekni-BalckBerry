package rta.ae.sharekni.Arafa.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rta.ae.sharekni.Arafa.Classes.AppController;
import rta.ae.sharekni.Arafa.Classes.GetData;
import rta.ae.sharekni.Arafa.Classes.ImageDecoder;
import rta.ae.sharekni.Arafa.DataModel.BestDriverDataModel;
import rta.ae.sharekni.Arafa.DataModel.BestRouteDataModel;
import rta.ae.sharekni.Arafa.DataModelAdapter.ProfileRideAdapter;
import rta.ae.sharekni.LoginApproved;
import rta.ae.sharekni.R;
import rta.ae.sharekni.RideDetails.RideDetailsAsDriver;
import rta.ae.sharekni.RideDetails.RideDetailsAsPassenger;

public class DriverDetails extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    TextView TopName, NationalityEnName;
    ImageView profile_msg, profile_call;
    ListView lv_driver;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String URL_Photo = GetData.PhotoURL;
    String days;
    SharedPreferences myPrefs;
    int Passenger_ID;
    int Driver_ID;
    String AccountType;
    String ID;
    private Toolbar toolbar;
    String FirstName, SecondName, ThirdName, Full_Name;
    jsoning jsoning;
    TextView Driver_profile_Item_rate;
    String IsMobileVerified, IsPhotoVerified;
    ImageView GreenPointCar_im;
    TextView Green_Points_txt, Green_Km_txt, Green_Routes_txt, Green_Vehicles_txt, Green_co2_saving_txt;

    String Locale_Str;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (LoginApproved.getInstance() != null) {
                LoginApproved.getInstance().finish();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.driver_details);
        initToolbar();

        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();
        CircularImageView Photo = (CircularImageView) findViewById(R.id.profilephoto);

        lv_driver = (ListView) findViewById(R.id.user_rides);
        TopName = (TextView) findViewById(R.id.TopName);
        NationalityEnName = (TextView) findViewById(R.id.NationalityEnName);
        profile_msg = (ImageView) findViewById(R.id.profile_msg);
        profile_call = (ImageView) findViewById(R.id.profile_call);
        Driver_profile_Item_rate = (TextView) findViewById(R.id.Driver_profile_Item_rate);

        Green_Points_txt = (TextView) findViewById(R.id.Green_Points_txt);
        Green_Km_txt = (TextView) findViewById(R.id.Green_Km_txt);
        Green_Routes_txt = (TextView) findViewById(R.id.Green_Routes_txt);
        Green_Vehicles_txt = (TextView) findViewById(R.id.Green_Vehicles_txt);
        Green_co2_saving_txt = (TextView) findViewById(R.id.Green_co2_saving_txt);
        GreenPointCar_im = (ImageView) findViewById(R.id.GreenPointCar_im);


        Locale locale = Locale.getDefault();
        Locale_Str = locale.toString();

        Log.d("test locale", Locale_Str);


        if (!Locale_Str.contains("ar")) {


            GreenPointCar_im.setImageResource(R.drawable.greenpointcar);

        } else {

            GreenPointCar_im.setImageResource(R.drawable.greencarar);

        }


        myPrefs = this.getSharedPreferences("myPrefs", 0);
        Passenger_ID = Integer.parseInt(myPrefs.getString("account_id", "0"));
        AccountType = myPrefs.getString("account_type", null);

        GetData j = new GetData();
        Bundle in = getIntent().getExtras();
        Driver_ID = in.getInt("DriverID");
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading) + "...");
        pDialog.show();
        jsoning = new jsoning(lv_driver, pDialog, this);
        jsoning.execute();

        try {
            final JSONObject json = j.GetDriverById(Driver_ID);

            // TopName.setText(json.getString("FirstName") + " " + json.getString("MiddleName"));

            Full_Name = "";
            FirstName = json.getString("FirstName");
            if (FirstName.equals("")) {
                Full_Name += "";
            } else {
                FirstName = FirstName.substring(0, 1).toUpperCase() + FirstName.substring(1);
                Log.d("First Name", FirstName);
                Full_Name += FirstName;

            }


            SecondName = json.getString("MiddleName");
            if (SecondName.equals("")) {
                Full_Name += " ";
            } else {

                Log.d("Second name 1", SecondName);
                SecondName = SecondName.substring(0, 1).toUpperCase() + SecondName.substring(1);
                Log.d("Second name 2", SecondName);
                Full_Name += " ";
                Full_Name += SecondName;
            }

            ThirdName = json.getString("LastName");
            if (ThirdName.equals("")) {

                Full_Name += " ";
            } else {

                Log.d("Second name 1", ThirdName);
                ThirdName = ThirdName.substring(0, 1).toUpperCase() + ThirdName.substring(1);
                Log.d("Second name 2", ThirdName);
                Full_Name += " ";
                Full_Name += ThirdName;
            }


            //  Driver_profile_Item_rate.setText(json.getString("AccountRating"));


            TopName.setText(Full_Name);

            NationalityEnName.setText(json.getString(getString(R.string.nat_name2)));
            ImageDecoder im = new ImageDecoder();
            im.stringRequest(json.getString("PhotoPath"), Photo, DriverDetails.this);
//            Photo.setImageUrl(URL_Photo + json.getString("PhotoPath"), imageLoader);
            Driver_profile_Item_rate.setText(json.getString("AccountRating"));

            IsMobileVerified = json.getString("IsMobileVerified");
            IsPhotoVerified = json.getString("IsPhotoVerified");

            int x1 = json.getInt("GreenPoints");
            int x2 = json.getInt("TotalDistance");

            int x3 = json.getInt("CO2Saved");
            x3 = x3 / 1000;

            Green_co2_saving_txt.setText(String.valueOf(x3));
            Green_Points_txt.setText(String.valueOf(x1));
            Green_Km_txt.setText(String.valueOf(x2));


            if (json.getInt("DriverMyRidesCount") > 2) {

                Green_Routes_txt.setText("2");
            } else {
                Green_Routes_txt.setText(json.getString("DriverMyRidesCount"));
            }


            Green_Vehicles_txt.setText(json.getString("VehiclesCount"));

            // Green_co2_saving_txt.setText(json.getString("CO2Saved"));


            profile_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (IsMobileVerified.equals("false")) {
                        Toast.makeText(getBaseContext(), R.string.No_Phone_number, Toast.LENGTH_SHORT).show();

                    } else if (IsMobileVerified.equals("true")) {

                        if (ActivityCompat.checkSelfPermission(DriverDetails.this, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Request missing location permission.
                            ActivityCompat.requestPermissions(DriverDetails.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST_CALL_PHONE
                            );
                        } else {

                            Intent intent = null;
                            try {

                                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + json.getString("Mobile")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);


                        }


                    }
                }
            });

            profile_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (IsMobileVerified.equals("false")) {
                        Toast.makeText(getBaseContext(), R.string.No_Phone_number, Toast.LENGTH_SHORT).show();

                    } else if (IsMobileVerified.equals("true")) {

                        Intent intent = null;
                        try {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + json.getString("Mobile")));
                            intent.putExtra("sms_body", getString(R.string.hello_world) + json.getString("FirstName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);


                    }

                }
            });

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.mytext_appbar);
        textView.setText(R.string.driver_details);


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

    public class jsoning extends AsyncTask {

        ListView lv;
        Activity con;
        BestRouteDataModel[] driver;
        private ProgressDialog pDialog;
        private List<BestDriverDataModel> arr = new ArrayList<>();
        boolean exists = false;

        public jsoning(final ListView lv, ProgressDialog pDialog, final Activity con) {

            this.lv = lv;
            this.con = con;
            this.pDialog = pDialog;

        }

        @Override
        protected void onPostExecute(Object o) {
            if (exists) {
                ProfileRideAdapter arrayAdapter = new ProfileRideAdapter(DriverDetails.this, R.layout.driver_profile_rides, driver);
                lv.setAdapter(arrayAdapter);
                setListViewHeightBasedOnChildren(lv);
                lv.requestLayout();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        assert AccountType != null;
                        if (Passenger_ID != 0) {
                            if (Driver_ID != Passenger_ID) {
                                Intent in = new Intent(DriverDetails.this, RideDetailsAsPassenger.class);
                                in.putExtra("RouteID", driver[i].getID());
                                in.putExtra("PassengerID", Passenger_ID);
                                Log.d("Last 4", String.valueOf(Passenger_ID));
                                in.putExtra("DriverID", Driver_ID);
                                Log.d("inside intent", String.valueOf(Passenger_ID));
                                DriverDetails.this.startActivity(in);
                            } else {
                                Intent in = new Intent(DriverDetails.this, RideDetailsAsDriver.class);
                                in.putExtra("RouteID", driver[i].getID());
                                in.putExtra("RouteName", driver[i].getRouteName());
                                in.putExtra("PassengerID", Passenger_ID);
                                Log.d("Last 3", String.valueOf(Passenger_ID));
                                in.putExtra("DriverID", Driver_ID);
                                DriverDetails.this.startActivity(in);
                            }
                        } else {
                            Intent in = new Intent(DriverDetails.this, RideDetailsAsPassenger.class);
                            in.putExtra("RouteID", driver[i].getID());
                            in.putExtra("PassengerID", Passenger_ID);
                            Log.d("Last 2", String.valueOf(Passenger_ID));
                            in.putExtra("DriverID", Driver_ID);
                            DriverDetails.this.startActivity(in);
                        }
                    }
                });
            }
            hidePDialog();
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
                        new AlertDialog.Builder(DriverDetails.this)
                                .setTitle(getString(R.string.connection_problem))
                                .setMessage(getString(R.string.con_problem_message))
                                .setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        Intent in = getIntent();
                                        in.putExtra("DriverID", Driver_ID);
                                        startActivity(getIntent());
                                    }
                                })
                                .setNegativeButton(getString(R.string.goBack), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        Toast.makeText(DriverDetails.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                JSONArray response = null;
                try {
                    response = new GetData().GetDriverRides(Driver_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                driver = new BestRouteDataModel[response.length()];
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = response.getJSONObject(i);
                        BestRouteDataModel item = new BestRouteDataModel(Parcel.obtain());
                        days = "";
                        item.setID(json.getInt("ID"));
                        item.setFromEm(json.getString(getString(R.string.from_em_en_name)));
                        item.setFromReg(json.getString(getString(R.string.from_reg_en_name)));
                        item.setToEm(json.getString(getString(R.string.to_em_en_name)));
                        item.setToReg(json.getString(getString(R.string.to_reg_en_name)));
                        item.setRouteName(json.getString(getString(R.string.route_name)));
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
//                        Log.d("FromEmlv", json.getString(getString(R.string.from_em_name)));
//                        Log.d("FromReglv", json.getString(getString(R.string.from_reg_name)));
//                        Log.d("TomEmlv", json.getString(getString(R.string.to_em_name)));
//                        Log.d("ToReglv", json.getString(getString(R.string.to_reg_name)));

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
        getMenuInflater().inflate(R.menu.menu_view_profile, menu);
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


    @Override
    public void onBackPressed() {
        if (jsoning.getStatus() == AsyncTask.Status.RUNNING) {
            jsoning.cancel(true);
        }
        finish();
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.checkSelfPermission(DriverDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}