package rta.ae.sharekni;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import rta.ae.sharekni.Arafa.Activities.MostRides;
import rta.ae.sharekni.TakeATour.TakeATour;

public class QuickSearch extends AppCompatActivity implements View.OnClickListener {

    int From_Em_Id = -1;
    int From_Reg_Id = -1;
    int To_Em_Id;
    int To_Reg_Id;
    String IS_Smoking = "";

    String To_EmirateEnName, From_EmirateEnName, To_RegionEnName, From_RegionEnName;

    List<TreeMap<String, String>> Emirates_List = new ArrayList<>();
    List<TreeMap<String, String>> Regions_List = new ArrayList<>();
    List<TreeMap<String, String>> Regions_List2 = new ArrayList<>();

    Dialog MainDialog;
    AutoCompleteTextView txt_regions;
    Spinner spinner;
    RelativeLayout pickup_relative;
    SimpleAdapter EmAdapter;
    Button btn_submit_pickUp;
    String txt_PickUp = String.valueOf(R.string.start_point);
    JSONArray Regions = null;
    JSONArray Emirates = null;

    ImageView sweep_icon;

    TextView txt_Selecet_Start_Point;
    RelativeLayout dropOff_relative;

    TextView txt_Select_Dest;

    String txt_Drop_Off;

    RelativeLayout calendar_relative;
    static final int DILOG_ID = 0;

    int year_x, month_x, day_x;

    TextView txt_year;
    TextView txt_beforeCal;
    String full_date;
    int i = 0;
    private int hour;
    private int minute;
    int savefind = 0;

    static final int TIME_DIALOG_ID = 999;

    RelativeLayout time_relative;

    TextView txt_time_selected;
    TextView before_Time;
    SharedPreferences myPrefs;

    Button btn_search_page;
    int MyId;
    private Toolbar toolbar;

    Button quickSearch_pickUp;
    Button quickSearch_Dropoff;
    Button quick_Destination;
    RelativeLayout MostRides_Relative, MapLookUp_Relative, Advanced_Search_Relative_2;


    String From_EmirateEnName_str, From_RegionEnName_str, To_EmirateEnName_str, To_RegionEnName_str;
    int From_Em_Id_2 = -1, From_Reg_Id_2 = -1, To_Em_Id_2 = -1, To_Reg_Id_2 = -1;
    ImageView save_off, save_on;
    TextView save_search_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quick_search);

        txt_year = (TextView) findViewById(R.id.search_txt_yaer);
        txt_beforeCal = (TextView) findViewById(R.id.textview50);
        pickup_relative = (RelativeLayout) findViewById(R.id.pickup_relative);
        dropOff_relative = (RelativeLayout) findViewById(R.id.dropOff_relative);
        txt_Selecet_Start_Point = (TextView) findViewById(R.id.txt_Selecet_Start_Point);

        txt_Select_Dest = (TextView) findViewById(R.id.txt_Select_Dest);
        txt_time_selected = (TextView) findViewById(R.id.txt_time_selected);
        before_Time = (TextView) findViewById(R.id.textview51);
        btn_search_page = (Button) findViewById(R.id.btn_search_page);
        //   quickSearch_pickUp = (Button) findViewById(R.id.quickSearch_pickUp);
        //      quickSearch_Dropoff = (Button) findViewById(R.id.quickSearch_Dropoff);

        MostRides_Relative = (RelativeLayout) findViewById(R.id.search_top_rides_im);
        MapLookUp_Relative = (RelativeLayout) findViewById(R.id.map_look_up);
        Advanced_Search_Relative_2 = (RelativeLayout) findViewById(R.id.advanced_search);
        quick_Destination = (Button) findViewById(R.id.quick_Destination);

        save_off = (ImageView) findViewById(R.id.save_off);
        save_on = (ImageView) findViewById(R.id.save_on);
        save_search_txt = (TextView) findViewById(R.id.save_search_txt);

        i = 0;

        //  txt_PickUp = getString(R.string.start_point);

        try {
            if (TakeATour.getInstance() != null) {
                TakeATour.getInstance().finish();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            if (PickUpActivity.getInstance() != null) {

                Intent intent = getIntent();
                From_Em_Id_2 = intent.getIntExtra("From_Em_Id", 0);
                From_Reg_Id_2 = intent.getIntExtra("From_Reg_Id", 0);
                To_Em_Id_2 = intent.getIntExtra("To_Em_Id", 0);
                To_Reg_Id_2 = intent.getIntExtra("To_Reg_Id", 0);
//                Bundle b = intent.getBundleExtra("options");
//
//                savefind = b.getInt("savefind");
//                if (savefind == 1){
//                    save_off.setVisibility(View.INVISIBLE);
//                    save_on.setVisibility(View.VISIBLE);
//                    save_search_txt.setTextColor(Color.RED);
//                }
//                if (savefind == 0) {
//                    save_on.setVisibility(View.INVISIBLE);
//                    save_off.setVisibility(View.VISIBLE);
//                    save_search_txt.setTextColor(Color.GRAY);
//                }
//                if (!b.getString("time").equals("")){
//                    txt_time_select
// ed.setText(b.getString("time"));
//                    before_Time.setVisibility(View.INVISIBLE);
//                }
//                IS_Smoking = b.getString("IS_Smoking");
//                full_date = b.getString("full_date");
//                if (full_date != null){
//                    txt_beforeCal.setVisibility(View.INVISIBLE);
//                    txt_year.setText(full_date);
//                }

                txt_PickUp = "";
                From_EmirateEnName_str = intent.getStringExtra("From_EmirateEnName");
                From_RegionEnName_str = intent.getStringExtra("From_RegionEnName");
                To_EmirateEnName_str = intent.getStringExtra("To_EmirateEnName");
                To_RegionEnName_str = intent.getStringExtra("To_RegionEnName");
                i = 1;

                try {

                    if (From_EmirateEnName_str.equals("null") || From_EmirateEnName_str.equals("")) {
                        From_EmirateEnName_str = "";
                        From_EmirateEnName_str = getString(R.string.not_set);
                        txt_PickUp = "";
                        txt_PickUp += From_EmirateEnName_str;
                    } else {
                        txt_PickUp = "";
                        txt_PickUp += From_EmirateEnName_str;
                    }


                    if (From_RegionEnName_str.equals("null") || From_RegionEnName_str.equals(getString(R.string.not_set))) {
                        From_RegionEnName_str = "";
                        From_RegionEnName_str = getString(R.string.not_set);
                        txt_PickUp += ",";
                        txt_PickUp += From_RegionEnName_str;

                    } else {
                        txt_PickUp += ",";
                        txt_PickUp += From_RegionEnName_str;
                    }


                } catch (NullPointerException e) {

                    txt_PickUp = getString(R.string.start_point);
                }


                try {
                    if (To_EmirateEnName_str.equals("null") || To_EmirateEnName_str.equals("")) {
                        To_EmirateEnName_str = "";
                        To_EmirateEnName_str = getString(R.string.not_set);
                        txt_Drop_Off = "";
                        txt_Drop_Off += To_EmirateEnName_str;
                    } else {
                        txt_Drop_Off = "";
                        txt_Drop_Off += To_EmirateEnName_str;
                    }

                    if (To_RegionEnName_str.equals("null") || To_RegionEnName_str.equals(getString(R.string.not_set))) {
                        To_RegionEnName_str = "";
                        To_RegionEnName_str = getString(R.string.not_set);
                        txt_Drop_Off += ",";
                        txt_Drop_Off += To_RegionEnName_str;

                    } else {
                        txt_Drop_Off += ",";
                        txt_Drop_Off += To_RegionEnName_str;
                    }

                } catch (NullPointerException e) {

                    txt_Drop_Off = getString(R.string.end_point);
                }

                Log.d("From_Em_Id_2", String.valueOf(intent.getIntExtra("From_Em_Id", 0)));
                Log.d("From_Reg_Id_2", String.valueOf(intent.getIntExtra("From_Reg_Id", 0)));
                Log.d("To_Em_Id_2", String.valueOf(intent.getIntExtra("To_Em_Id", 0)));
                Log.d("To_Reg_Id_2", String.valueOf(intent.getIntExtra("To_Reg_Id", 0)));


                PickUpActivity.getInstance().finish();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnButtonClick();
        showTimeDialogOnButtonClick();


        initToolbar();
        myPrefs = this.getSharedPreferences("myPrefs", 0);
        MyId = Integer.parseInt(myPrefs.getString("account_id", "0"));


        save_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((From_Em_Id_2 != -1) && (From_Reg_Id_2 != -1) && (To_Em_Id_2 != -1) && (To_Reg_Id_2 != -1) && (From_Em_Id_2 != 0) && (From_Reg_Id_2 != 0) && (To_Em_Id_2 != 0) && (To_Reg_Id_2 != 0)) {
                    save_off.setVisibility(View.INVISIBLE);
                    save_on.setVisibility(View.VISIBLE);
                    save_search_txt.setTextColor(Color.RED);
                    savefind = 1;
                } else {
                    Toast.makeText(QuickSearch.this, R.string.saveSearch_Error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        save_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_on.setVisibility(View.INVISIBLE);
                save_off.setVisibility(View.VISIBLE);
                save_search_txt.setTextColor(Color.GRAY);
                savefind = 0;
            }
        });


        if (i == 0) {
            txt_Selecet_Start_Point.setText(getString(R.string.start_point));
            Log.d("pick 1", txt_PickUp);


        } else if (i == 1) {


            txt_Selecet_Start_Point.setText(txt_PickUp);

        }


        if (i == 0) {
            txt_Select_Dest.setText(getString(R.string.end_point));
//            Log.d("drop off 1 ", txt_Drop_Off);

        } else if (i == 1) {


            txt_Select_Dest.setText(txt_Drop_Off);

        }


        quick_Destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putInt("savefind", savefind);
                b.putString("IS_Smoking", IS_Smoking);
                b.putString("full_date", full_date);
                if (txt_time_selected.getText() != getString(R.string.start_time)) {
                    b.putString("time", txt_time_selected.getText().toString());
                }
                Intent intent = new Intent(getBaseContext(), PickUpActivity.class);
                intent.putExtra("FALG_SEARCH", 1);
                intent.putExtra("options", b);
                startActivity(intent);
                finish();
            }
        });


        MostRides_Relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getBaseContext(), MostRides.class);
                startActivity(intent1);
            }
        });


        MapLookUp_Relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);

            }
        });

        Advanced_Search_Relative_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Advanced_Search.class);
                startActivity(intent);

            }
        });


        String ret;
        try {
            InputStream inputStream = openFileInput("Emirates.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
                Emirates = new JSONArray(ret);
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //quickSearch_pickUp.setOnClickListener(this);
        //pickup_relative.setOnClickListener(this);
        //quickSearch_Dropoff.setOnClickListener(this);
        // dropOff_relative.setOnClickListener(this);

        btn_search_page.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (From_Em_Id_2 != -1 && From_Reg_Id_2 != -1 && From_Em_Id_2 != 0 && From_Reg_Id_2 != 0) {

                    Intent intent1 = new Intent(getBaseContext(), QuickSearchResults.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent1.putExtra("From_Em_Id", From_Em_Id_2);
                    intent1.putExtra("To_Em_Id", To_Em_Id_2);
                    intent1.putExtra("From_Reg_Id", From_Reg_Id_2);
                    intent1.putExtra("To_Reg_Id", To_Reg_Id_2);
                    intent1.putExtra("From_EmirateEnName", From_EmirateEnName_str);
                    intent1.putExtra("From_RegionEnName", From_RegionEnName_str);
                    intent1.putExtra("To_EmirateEnName", To_EmirateEnName_str);
                    intent1.putExtra("To_RegionEnName", To_RegionEnName_str);
                    intent1.putExtra("SaveFind", savefind);
                    intent1.putExtra("Smokers", IS_Smoking);
                    intent1.putExtra("MapKey", "QSearch");
                    intent1.putExtra("InviteType", "");
                    Log.d("Test", From_EmirateEnName + From_RegionEnName + To_EmirateEnName + To_RegionEnName);
                    startActivity(intent1);
                    i = 1;
                } else {
                    Toast.makeText(QuickSearch.this, R.string.select_start_point, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }  // on create



    /*
    private class getTo extends AsyncTask {
        ProgressDialog pDialog;
        boolean exists = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QSearch.this);
            pDialog.setMessage("Loading" + "...");
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (exists) {
                if (pDialog != null) {
                    pDialog.dismiss();
                    pDialog = null;
                }

                EmAdapter = new SimpleAdapter(QSearch.this, Emirates_List
                        , R.layout.dialog_pick_emirate_lv_row
                        , new String[]{"EmirateId", "EmirateEnName"}
                        , new int[]{R.id.row_id_search, R.id.row_name_search});

                MainDialog = new Dialog(QSearch.this);
                MainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MainDialog.setContentView(R.layout.main_search_dialog);
                TextView Lang_Dialog_txt_id = (TextView) MainDialog.findViewById(R.id.Lang_Dialog_txt_id);
                Lang_Dialog_txt_id.setText("Drop Off");
                btn_submit_pickUp = (Button) MainDialog.findViewById(R.id.btn_submit_puckup);
                sweep_icon = (ImageView) MainDialog.findViewById(R.id.sweep_icon);

//                MainDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                txt_regions = (AutoCompleteTextView) MainDialog.findViewById(R.id.mainDialog_Regions_auto);
                spinner = (Spinner) MainDialog.findViewById(R.id.Emirates_spinner);
                spinner.setAdapter(EmAdapter);

                MainDialog.show();


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt_Drop_Off = "";
                        TextView txt_em_name = (TextView) view.findViewById(R.id.row_name_search);
                        TextView txt_em_id = (TextView) view.findViewById(R.id.row_id_search);
                        To_Em_Id = Integer.parseInt(txt_em_id.getText().toString());
                        To_EmirateEnName = txt_em_name.getText().toString();

                        txt_Drop_Off += txt_em_name.getText().toString();
                        txt_Drop_Off += ", ";
//                        if (To_Em_Id == 2) {
                            String ret;
                            try {
                                InputStream inputStream = openFileInput("Regions"+To_Em_Id+".txt");
                                if (inputStream != null) {
                                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                    String receiveString;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    while ((receiveString = bufferedReader.readLine()) != null) {
                                        stringBuilder.append(receiveString);
                                    }
                                    inputStream.close();
                                    ret = stringBuilder.toString();
                                    Regions = new JSONArray(ret);
                                }
                            } catch (FileNotFoundException e) {
                                Log.e("login activity", "File not found: " + e.toString());
                            } catch (IOException e) {
                                Log.e("login activity", "Can not read file: " + e.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                        }
                        Log.d("id of lang", "" + To_Em_Id);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                txt_regions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetData getData = new GetData();
                        JSONArray jsonArray;
                        try {
                            if (Regions == null) {
                                jsonArray = getData.GetRegionsByEmiratesID(To_Em_Id);
                            } else {
                                jsonArray = Regions;
                            }
                            Regions_List.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                TreeMap<String, String> valuePairs = new TreeMap<>();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                valuePairs.put("ID", jsonObject.getString("ID"));
                                valuePairs.put("RegionEnName", jsonObject.getString("RegionEnName"));
                                Regions_List2.add(valuePairs);
                            }
                            Regions = null;
                            Log.d("test Regions search ", Regions_List2.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final SimpleAdapter RegAdapter = new SimpleAdapter(QSearch.this, Regions_List2
                                , R.layout.dialog_pick_regions_lv_row
                                , new String[]{"ID", "RegionEnName"}
                                , new int[]{R.id.row_id_search, R.id.row_name_search});

                        txt_regions.setAdapter(RegAdapter);
                        txt_regions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView txt_reg_name = (TextView) view.findViewById(R.id.row_name_search);
                                TextView txt_reg_id = (TextView) view.findViewById(R.id.row_id_search);
                                To_Reg_Id = Integer.parseInt(txt_reg_id.getText().toString());
                                To_RegionEnName = txt_reg_name.getText().toString();
                                txt_regions.setText(txt_reg_name.getText().toString());
                                txt_Drop_Off += txt_reg_name.getText().toString();
                            }
                        });
                    }
                });


                sweep_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        txt_regions.setText("");
                        txt_regions.setHint("Enter Region");

                    }
                });


                btn_submit_pickUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_Select_Dest.setText(txt_Drop_Off);
                        MainDialog.dismiss();
                    }
                });
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
                        new AlertDialog.Builder(QSearch.this)
                                .setTitle("Connection Problem!")
                                .setMessage("Make sure you have internet connection")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intentToBeNewRoot = new Intent(QSearch.this, QSearch.class);
                                        ComponentName cn = intentToBeNewRoot.getComponent();
                                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                                        startActivity(mainIntent);
                                    }
                                })
                                .setNegativeButton("Exit!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        Toast.makeText(QSearch.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                Emirates_List.clear();
                try {
                    JSONArray j;
                    if (Emirates == null) {
                        j = new GetData().GetEmitares();
                    } else {
                        j = Emirates;
                    }
                    for (int i = 0; i < j.length(); i++) {
                        TreeMap<String, String> valuePairs = new TreeMap<>();
                        JSONObject jsonObject = j.getJSONObject(i);
                        valuePairs.put("EmirateId", jsonObject.getString("EmirateId"));
                        valuePairs.put("EmirateEnName", jsonObject.getString("EmirateEnName"));
                        Emirates_List.add(valuePairs);
                    }
                    Log.d("test Emirates ", Emirates_List.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    */


    /*
    private class getFrom extends AsyncTask {
        ProgressDialog pDialog;
        boolean exists = false;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(QSearch.this);
            pDialog.setMessage("Loading" + "...");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }


            EmAdapter = new SimpleAdapter(QSearch.this, Emirates_List
                    , R.layout.dialog_pick_emirate_lv_row
                    , new String[]{"EmirateId", "EmirateEnName"}
                    , new int[]{R.id.row_id_search, R.id.row_name_search});

            MainDialog = new Dialog(QSearch.this);
            MainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            MainDialog.setContentView(R.layout.main_search_dialog);
            btn_submit_pickUp = (Button) MainDialog.findViewById(R.id.btn_submit_puckup);
            TextView Lang_Dialog_txt_id= (TextView) MainDialog.findViewById(R.id.Lang_Dialog_txt_id);
            sweep_icon= (ImageView) MainDialog.findViewById(R.id.sweep_icon);


            Lang_Dialog_txt_id.setText("Pick Up");
            txt_regions = (AutoCompleteTextView) MainDialog.findViewById(R.id.mainDialog_Regions_auto);
            spinner = (Spinner) MainDialog.findViewById(R.id.Emirates_spinner);
            spinner.setAdapter(EmAdapter);
            MainDialog.show();


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    txt_PickUp = "";
                    TextView txt_em_name = (TextView) view.findViewById(R.id.row_name_search);
                    TextView txt_em_id = (TextView) view.findViewById(R.id.row_id_search);
                    From_Em_Id = Integer.parseInt(txt_em_id.getText().toString());
                    From_EmirateEnName = txt_em_name.getText().toString();

                    txt_PickUp += txt_em_name.getText().toString();
                    txt_PickUp += ", ";
//                    if (From_Em_Id == 2) {
                        String ret;
                        try {
                            InputStream inputStream = openFileInput("Regions"+From_Em_Id+".txt");
                            if (inputStream != null) {
                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String receiveString;
                                StringBuilder stringBuilder = new StringBuilder();
                                while ((receiveString = bufferedReader.readLine()) != null) {
                                    stringBuilder.append(receiveString);
                                }
                                inputStream.close();
                                ret = stringBuilder.toString();
                                Regions = new JSONArray(ret);
                            }
                        } catch (FileNotFoundException e) {
                            Log.e("login activity", "File not found: " + e.toString());
                        } catch (IOException e) {
                            Log.e("login activity", "Can not read file: " + e.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                    }
                    Log.d("id of lang", "" + From_Em_Id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            txt_regions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetData getData = new GetData();
                    JSONArray jsonArray;
                    try {
                        if (Regions == null) {
                            jsonArray = getData.GetRegionsByEmiratesID(From_Em_Id);
                        } else {
                            jsonArray = Regions;
                        }
                        Regions_List.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TreeMap<String, String> valuePairs = new TreeMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            valuePairs.put("ID", jsonObject.getString("ID"));
                            valuePairs.put("RegionEnName", jsonObject.getString("RegionEnName"));
                            Regions_List.add(valuePairs);
                        }
                        Regions = null;
                        Log.d("test Regions search ", Regions_List.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final SimpleAdapter RegAdapter = new SimpleAdapter(QSearch.this, Regions_List
                            , R.layout.dialog_pick_regions_lv_row
                            , new String[]{"ID", "RegionEnName"}
                            , new int[]{R.id.row_id_search, R.id.row_name_search});

                    txt_regions.setAdapter(RegAdapter);
                    txt_regions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView txt_reg_name = (TextView) view.findViewById(R.id.row_name_search);
                            TextView txt_reg_id = (TextView) view.findViewById(R.id.row_id_search);
                            From_Reg_Id = Integer.parseInt(txt_reg_id.getText().toString());
                            From_RegionEnName = txt_reg_name.getText().toString();
                            txt_regions.setText(txt_reg_name.getText().toString());
                            txt_PickUp += txt_reg_name.getText().toString();
                        }
                    });
                }
            });



            sweep_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    txt_regions.setText("");
                    txt_regions.setHint("Enter Region");

                }
            });




            btn_submit_pickUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt_Selecet_Start_Point.setText(txt_PickUp);
                    MainDialog.dismiss();
                }
            });
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
                        new AlertDialog.Builder(QSearch.this)
                                .setTitle("Connection Problem!")
                                .setMessage("Make sure you have internet connection")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intentToBeNewRoot = new Intent(QSearch.this, QSearch.class);
                                        ComponentName cn = intentToBeNewRoot.getComponent();
                                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                                        startActivity(mainIntent);
                                    }
                                })
                                .setNegativeButton("Exit!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                        Toast.makeText(QSearch.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                Emirates_List.clear();
                try {
                    JSONArray j;
                    if (Emirates == null) {
                        j = new GetData().GetEmitares();
                    } else {
                        j = Emirates;
                    }
                    for (int i = 0; i < j.length(); i++) {
                        TreeMap<String, String> valuePairs = new TreeMap<>();
                        JSONObject jsonObject = j.getJSONObject(i);
                        valuePairs.put("EmirateId", jsonObject.getString("EmirateId"));
                        valuePairs.put("EmirateEnName", jsonObject.getString("EmirateEnName"));
                        Emirates_List.add(valuePairs);
                    }
                    Log.d("test Emirates ", Emirates_List.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    */

    Calendar cal = Calendar.getInstance();
    DatePicker d;

    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == DILOG_ID) {
            DatePickerDialog datePickerDialog = (DatePickerDialog) dialog;
            // Get the current date
            datePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        } else if (id == TIME_DIALOG_ID) {

            TimePickerDialog timePickerDialog = (TimePickerDialog) dialog;
            timePickerDialog.updateTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DILOG_ID) {
            DatePickerDialog dp = new DatePickerDialog(this, dPickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            d = dp.getDatePicker();
            d.updateDate(year_x, month_x, day_x);
//           d.setMaxDate(cal.getTimeInMillis());
            d.setMinDate(cal.getTimeInMillis());
            return dp;
        }
        if (id == TIME_DIALOG_ID) {
            return new TimePickerDialog(this,
                    timePickerListener, hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dPickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;
            txt_beforeCal.setVisibility(View.INVISIBLE);
            String year_string = String.valueOf(year_x);
            String month_string = String.valueOf(month_x);
            String day_string = String.valueOf(day_x);
            full_date = day_string + "/" + month_string + "/" + year_string;
            txt_year.setText(full_date);
            Log.d("Calendar test", full_date);
        }
    };


    public void showDialogOnButtonClick() {
        calendar_relative = (RelativeLayout) findViewById(R.id.calendar_relative);
        calendar_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DILOG_ID);
            }
        });

    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;
                    before_Time.setVisibility(View.INVISIBLE);
                    // set current time into textview
                    txt_time_selected.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void showTimeDialogOnButtonClick() {
        time_relative = (RelativeLayout) findViewById(R.id.time_relative);
        time_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);

        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.mytext_appbar);
        textView.setText(R.string.quick_search);
//        toolbar.setElevation(10);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Locale locale = Locale.getDefault();
            String Locale_Str2 = locale.toString();
            if (!Locale_Str2.contains("ar")) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            } else {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_forward);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }


    @Override
    public void onClick(View v) {

//        if (v == pickup_relative || v == quickSearch_pickUp) {
//            new getFrom().execute();
//        }
//        if (v == dropOff_relative || v == quickSearch_Dropoff) {
//            new getTo().execute();
//        }
    }


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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", "This is my message to be reloaded");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


}
