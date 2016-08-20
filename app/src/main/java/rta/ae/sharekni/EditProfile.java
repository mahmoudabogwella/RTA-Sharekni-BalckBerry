package rta.ae.sharekni;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import rta.ae.sharekni.Arafa.Classes.GetData;


public class EditProfile extends AppCompatActivity {


    static final int DILOG_ID = 0;
    public volatile boolean parsingComplete = true;
    int MyID;
    RelativeLayout btn_datepicker_id;
    Button btn_save;
    Button btn_upload_image;
    EditText edit_fname;
    EditText edit_lname;
    EditText edit_reg_mob;
    TextView malefemale_txt, femalemale_txt;
    int Language_ID;
    int year_x, month_x, day_x;
    int Nationality_ID;
    char i = 'M';
    ImageView malefemale, femalemale;
    TextView txt_lang;
    AutoCompleteTextView txt_country;
    String full_date;
    String uploadedImage;
    Toolbar toolbar;
    TextView txt_year;
    TextView txt_dayOfWeek;
    TextView txt_comma;
    TextView txt_beforeCal;
    Dialog list_dialog;
    ListView mListView;
    Button btn_Edit_Cancel;
    Boolean mobile_no_ver = false;
    String Locale_Str;


    LinearLayout Edit_Profile_Nat_Linear;

    List<TreeMap<String, String>> Lang_List = new ArrayList<>();
    List<TreeMap<String, String>> Country_List = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener dPickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;
            txt_beforeCal.setVisibility(View.INVISIBLE);
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year_x);
            cal.set(Calendar.MONTH, month_x);
            cal.set(Calendar.DAY_OF_MONTH, day_x + 4);
            if (year < 2015 - 18) {
                Date date = cal.getTime();
                String dayOfWeek = simpledateformat.format(date);
                String year_string = String.valueOf(year_x);
                String month_string = String.valueOf(month_x);
                String day_string = String.valueOf(day_x);
                full_date = day_string + "/" + month_string + "/" + year_string;
                txt_year.setText(full_date);
                txt_comma.setVisibility(View.VISIBLE);
                txt_dayOfWeek.setText(dayOfWeek);
                Log.d("Calendar test", full_date);
            } else {
                txt_dayOfWeek.setText(R.string.must_be_more_18);
                Toast.makeText(EditProfile.this, R.string.too_young, Toast.LENGTH_SHORT).show();
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static String createSoapHeader() {
        String soapHeader;
        soapHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + ">";
        return soapHeader;
    }

    public static byte[] getReqData(String data) {
        StringBuilder requestData = new StringBuilder();
        requestData.append(createSoapHeader());
        requestData.append("<soap:Body>" + "<UploadImage" + " xmlns=\"http://Sharekni-MobAndroid-Data.org/\">" + "<ImageContent>").append(data).append("</ImageContent>\n").append("<imageExtenstion>jpg</imageExtenstion>").append("</UploadImage> </soap:Body> </soap:Envelope>");
        Log.d("reqData: ", requestData.toString());
        return requestData.toString().trim().getBytes();
    }

    private static String convertStreamToString(InputStream is)
            throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_test);
        initToolbar();
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        MyID = Integer.parseInt(myPrefs.getString("account_id", null));
        Log.d("inside Edit", String.valueOf(MyID));

        edit_fname = (EditText) findViewById(R.id.edit_reg_fname);
        edit_lname = (EditText) findViewById(R.id.edit_reg_lname);
        txt_beforeCal = (TextView) findViewById(R.id.txt_beforeCal);
        btn_save = (Button) findViewById(R.id.btn_Edit_id);
        malefemale_txt = (TextView) findViewById(R.id.malefemale_txt_edit);
        femalemale_txt = (TextView) findViewById(R.id.femalemale_txt_edit);
        malefemale = (ImageView) findViewById(R.id.malefemale_edit);
        femalemale = (ImageView) findViewById(R.id.femalemale_edit);
        btn_upload_image = (Button) findViewById(R.id.btnUploadPhotoEdt);
        txt_country = (AutoCompleteTextView) findViewById(R.id.autocompletecountry_id);
        txt_lang = (TextView) findViewById(R.id.autocomplete_lang_id);
        // txt_year = (TextView) findViewById(R.id.txt_year);
        //  txt_beforeCal = (TextView) findViewById(R.id.txt_beforeCal);
        //  txt_comma = (TextView) findViewById(R.id.Register_comma_cal);
        //  txt_dayOfWeek = (TextView) findViewById(R.id.txt_dayOfWeek);
        btn_Edit_Cancel = (Button) findViewById(R.id.btn_Edit_Cancel);
        edit_reg_mob = (EditText) findViewById(R.id.edit_reg_mob);

        //       Edit_Profile_Nat_Linear = (LinearLayout) findViewById(R.id.Edit_Profile_Nat_Linear);


//        txt_country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    txt_country.setHint(R.string.password);
//                    if (txt_country != null) {
//                        if (txt_country.length() <= 4) {
//                            Edit_Profile_Nat_Linear.setBackgroundResource(R.drawable.user_register_border_error);
//                            Toast.makeText(RegisterNewTest.this, getString(R.string.short_pass), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } else {
//                    Password_Linear.setBackgroundResource(R.drawable.user_register_border);
//                }
//                if (hasFocus) {
//                    txt_country.setHint("");
//                }
//            }
//        });


//        cal.add(Calendar.YEAR, -18);

//        year_x = cal.get(Calendar.YEAR);
//        month_x = cal.get(Calendar.MONTH);
//        day_x = cal.get(Calendar.DAY_OF_MONTH);

        //  showDialogOnButtonClick();

        try {
            JSONObject j = new GetData().GetDriverById(MyID);
            String Firstname = (j.getString("FirstName"));
            Firstname = Firstname.substring(0, 1).toUpperCase() + Firstname.substring(1);
            edit_fname.setText(Firstname);
            edit_fname.setTextColor(getResources().getColor(R.color.primaryColor));
            String LastName = (j.getString("LastName"));
            LastName = LastName.substring(0, 1).toUpperCase() + LastName.substring(1);
            edit_lname.setText(LastName);
            edit_lname.setTextColor(getResources().getColor(R.color.primaryColor));
            edit_reg_mob.setText(j.getString("Mobile").substring(4));
            edit_reg_mob.setTextColor(getResources().getColor(R.color.primaryColor));
            full_date = j.getString("BirthDate");

            //            txt_year.setText(full_date);
            //
            //            txt_beforeCal.setVisibility(View.INVISIBLE);
            //
            if (j.getInt("NationalityId") != 0) {
                txt_country.setText(j.getString(getString(R.string.nat_name2)));
            }

            txt_country.setTextColor(getResources().getColor(R.color.primaryColor));

            Nationality_ID = j.getInt("NationalityId");

            if (!getString(R.string.pref_lang2).equals("null")) {
                txt_lang.setText(j.getString(getString(R.string.pref_lang2)));
            }
            txt_lang.setTextColor(getResources().getColor(R.color.primaryColor));

            Language_ID = j.getInt("PrefferedLanguage");

            if (j.getString("IsPhotoVerified").toLowerCase().equals("true") || j.getString("IsPhotoVerified").toLowerCase().equals("false")) {
                uploadedImage = j.getString("PhotoPath");
            } else {
                uploadedImage = "";
            }


            Locale locale = Locale.getDefault();
            Locale_Str = locale.toString();
            Log.d("Home locale", Locale_Str);


            if (!Locale_Str.contains("ar")) {

                if (j.getString("GenderEn").equals("Male")) {
                    femalemale.setVisibility(View.INVISIBLE);
                    malefemale.setVisibility(View.VISIBLE);
                    malefemale_txt.setTextColor(Color.RED);
                    femalemale_txt.setTextColor(Color.GRAY);
                    i = 'M';
                    Log.d("Account_Gender_1", String.valueOf(i));
                } else {
                    malefemale.setVisibility(View.INVISIBLE);
                    femalemale.setVisibility(View.VISIBLE);
                    malefemale_txt.setTextColor(Color.GRAY);
                    femalemale_txt.setTextColor(Color.RED);
                    i = 'F';
                    Log.d("Account_Gender_1", String.valueOf(i));
                }

            } else {

                if (j.getString("GenderEn").equals("Male")) {
                    femalemale.setVisibility(View.VISIBLE);
                    malefemale.setVisibility(View.INVISIBLE);
                    malefemale_txt.setTextColor(Color.RED);
                    femalemale_txt.setTextColor(Color.GRAY);
                    i = 'M';
                    Log.d("Account_Gender_1", String.valueOf(i));
                } else {
                    malefemale.setVisibility(View.VISIBLE);
                    femalemale.setVisibility(View.INVISIBLE);
                    malefemale_txt.setTextColor(Color.GRAY);
                    femalemale_txt.setTextColor(Color.RED);
                    i = 'F';
                    Log.d("Account_Gender_1", String.valueOf(i));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        edit_reg_mob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    edit_reg_mob.setHint(getString(R.string.REg_Mobile));
                    if (edit_reg_mob != null) {
                        if (edit_reg_mob.length() < 9) {
                            Toast.makeText(EditProfile.this, getString(R.string.short_mobile), Toast.LENGTH_SHORT).show();
//                            MobileNumber_Linear.setBackgroundResource(R.drawable.user_register_border_error);
                        } else {
//                            MobileNumber_Linear.setBackgroundResource(R.drawable.user_register_border);
                        }
                    } else {
//                        MobileNumber_Linear.setBackgroundResource(R.drawable.user_register_border);
                    }
                }
                if (hasFocus) {
                    edit_reg_mob.setHint("");
                }
            }
        });

        btn_Edit_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_library), getString(R.string.cancel)};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EditProfile.this);
                builder.setTitle(getString(R.string.add_photo));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals(getString(R.string.take_photo))) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        } else if (items[item].equals(getString(R.string.choose_from_library))) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), 1337);
                        } else if (items[item].equals(getString(R.string.cancel))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        txt_country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                Boolean result = false;
                if (!hasFocus) {
                    txt_country.setHint(getString(R.string.nationality));
                    if (Country_List.size() != 0 && txt_country.getText() != null && !txt_country.getText().toString().equals(getString(R.string.Reg_Nat))) {
                        for (int i = 0; i <= 193; i++) {
                            String a = Country_List.get(i).get("NationalityEnName");
                            String b = txt_country.getText().toString();
                            if (a.equals(b)) {
                                result = true;
                            }
                        }
                    }
                    if (!result) {
                        Toast.makeText(EditProfile.this, getString(R.string.unknown_country), Toast.LENGTH_SHORT).show();

                    }


                }

                if (hasFocus) {
                    txt_country.setHint("");
                }
            }
        });

        txt_lang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                Boolean result = false;
                try {
                    if (!hasFocus) {
                        if (Lang_List.size() != 0 && txt_lang.getText() != null && !txt_lang.getText().toString().equals(getString(R.string.Reg_PrefLang))) {
                            for (int i = 0; i <= Lang_List.size(); i++) {
                                String a = Lang_List.get(i).get("NationalityEnName");
                                String b = txt_lang.getText().toString();
                                if (a.equals(b)) {
                                    result = true;
                                }
                            }
                        }
                        if (!result) {
                            Toast.makeText(EditProfile.this, getString(R.string.unknown_language), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(EditProfile.this, getString(R.string.unknown_language), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_fname.getText().toString().equals("") && !edit_fname.getText().toString().equals(getString(R.string.Reg_FirstN)) && !edit_lname.getText().toString().equals("") && !edit_lname.getText().toString().equals(getString(R.string.Reg_LastN)) && !edit_reg_mob.getText().toString().equals("") && !edit_reg_mob.getText().toString().equals(getString(R.string.REg_Mobile)) && !txt_lang.getText().toString().equals("") && !txt_lang.getText().toString().equals(getString(R.string.Reg_PrefLang)) && full_date != null) {
                    ArrayList codes = new ArrayList();
                    codes.add("50");
                    codes.add("52");
                    codes.add("55");
                    codes.add("56");
                    String code = edit_reg_mob.getText().toString().substring(0, 2);
                    if (!codes.contains(code)) {
                        Toast.makeText(EditProfile.this, getString(R.string.short_mobile), Toast.LENGTH_SHORT).show();
                    } else {
                        String Fname = edit_fname.getText().toString();
                        String Lname = edit_lname.getText().toString();
                        String mobile = edit_reg_mob.getText().toString();
//                        String country = txt_country.getText().toString();
//                        String lang = txt_lang.getText().toString();
//                        if (i == 'M'){
//                            femalemale.setVisibility(View.INVISIBLE);
//                            malefemale.setVisibility(View.VISIBLE);
//                            malefemale_txt.setTextColor(Color.RED);
//                            femalemale_txt.setTextColor(Color.GRAY);
//                            Log.d("Account_Gender", String.valueOf(i));
//                        }else {
//                            malefemale.setVisibility(View.INVISIBLE);
//                            femalemale.setVisibility(View.VISIBLE);
//                            malefemale_txt.setTextColor(Color.GRAY);
//                            femalemale_txt.setTextColor(Color.RED);
//                            Log.d("Account_Gender", String.valueOf(i));
//                        }
                        char gender = i;
                        String birthdate = "";
                        //String photoname = "testing.jpg";
                        Log.d("Account_Gender_Submit", String.valueOf(gender));
                        String x = String.valueOf(Language_ID);
                        String y = String.valueOf(Nationality_ID);
                        GetData gd = new GetData();
                        gd.EditProfileForm(MyID, Fname, Lname, mobile, String.valueOf(gender), birthdate, y, x, uploadedImage, EditProfile.this);
                    }
                } else {
                    Toast.makeText(EditProfile.this, getString(R.string.fill_all_error), Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (!Locale_Str.contains("ar")) {

            Log.d("Change Gender", "English Language");
            malefemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    malefemale.setVisibility(View.INVISIBLE);
                    femalemale.setVisibility(View.VISIBLE);
                    malefemale_txt.setTextColor(Color.GRAY);
                    femalemale_txt.setTextColor(Color.RED);
                    i = 'F';
                    Log.d("Account_Gender_En ", String.valueOf(i));


                }
            });


            femalemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    femalemale.setVisibility(View.INVISIBLE);
                    malefemale.setVisibility(View.VISIBLE);
                    malefemale_txt.setTextColor(Color.RED);
                    femalemale_txt.setTextColor(Color.GRAY);
                    i = 'M';
                    Log.d("Account_Gender_En ", String.valueOf(i));
                }
            });


            femalemale_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    malefemale_txt.setTextColor(Color.GRAY);
                    femalemale_txt.setTextColor(Color.RED);
                    malefemale.setVisibility(View.INVISIBLE);
                    femalemale.setVisibility(View.VISIBLE);
                    i = 'F';
                    Log.d("Account_Gender_En ", String.valueOf(i));

                }
            });


            malefemale_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = 'M';
                    malefemale_txt.setTextColor(Color.RED);
                    femalemale_txt.setTextColor(Color.GRAY);
                    malefemale.setVisibility(View.VISIBLE);
                    femalemale.setVisibility(View.INVISIBLE);
                    Log.d("Account_Gender_En ", String.valueOf(i));
                }
            });
        } else {
            Log.d("Change Gender", "Arabic Language");

            malefemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    malefemale.setVisibility(View.INVISIBLE);
                    femalemale.setVisibility(View.VISIBLE);
                    malefemale_txt.setTextColor(Color.RED);
                    femalemale_txt.setTextColor(Color.GRAY);
                    i = 'M';
                    Log.d("Account_Gender_Ar ", String.valueOf(i));


                }
            });


            femalemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    femalemale.setVisibility(View.INVISIBLE);
                    malefemale.setVisibility(View.VISIBLE);
                    malefemale_txt.setTextColor(Color.GRAY);
                    femalemale_txt.setTextColor(Color.RED);
                    i = 'F';
                    Log.d("Account_Gender_Ar ", String.valueOf(i));
                }
            });


            femalemale_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    malefemale_txt.setTextColor(Color.GRAY);
                    femalemale_txt.setTextColor(Color.RED);
                    malefemale.setVisibility(View.VISIBLE);
                    femalemale.setVisibility(View.INVISIBLE);
                    i = 'F';
                    Log.d("Account_Gender_Ar ", String.valueOf(i));

                }
            });


            malefemale_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = 'M';
                    malefemale_txt.setTextColor(Color.RED);
                    femalemale_txt.setTextColor(Color.GRAY);

                    malefemale.setVisibility(View.INVISIBLE);
                    femalemale.setVisibility(View.VISIBLE);
                    Log.d("Account_Gender_Ar ", String.valueOf(i));
                }
            });
        }


        // get Languages
        new lang().execute();

        // get nationals
        new nat().execute();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    Calendar cal = Calendar.getInstance();
    DatePicker d;

    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == DILOG_ID) {
            DatePickerDialog datePickerDialog = (DatePickerDialog) dialog;
            // Get the current date
            datePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DILOG_ID) {
            DatePickerDialog dp = new DatePickerDialog(this, dPickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            d = dp.getDatePicker();
            d.updateDate(year_x, month_x, day_x);
            d.setMaxDate(cal.getTimeInMillis());
            return dp;
        }
        return null;
    }

//    public void showDialogOnButtonClick() {
//        btn_datepicker_id = (RelativeLayout) findViewById(R.id.datepicker_id);
//        btn_datepicker_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(DILOG_ID);
//            }
//        });
//    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("UploadImageResult")) {
                            uploadedImage = text;
                            uploadedImage = uploadedImage.replace("\"", "");
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();


                int width = thumbnail.getWidth();
                int height = thumbnail.getHeight();

                float bitmapRatio = (float) width / (float) height;
                if (bitmapRatio > 0) {
                    width = 600;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = 600;
                    width = (int) (height * bitmapRatio);
                }
                Bitmap im = Bitmap.createScaledBitmap(thumbnail, width, height, true);


                im.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                new ImageUpload().execute(encoded);
//                ivImage.setImageBitmap(thumbnail);
            } else if (requestCode == 1337) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, 150, 150, false);
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                new ImageUpload().execute(encoded);
//                ivImage.setImageBitmap(bm);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        TextView textView = (TextView) toolbar.findViewById(R.id.mytext_appbar);
        textView.setText(R.string.edit_profile);
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
            }            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditProfileTest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://rta.ae.sharekni/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditProfileTest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://rta.ae.sharekni/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class lang extends AsyncTask {

        @Override
        protected void onPostExecute(Object o) {
            final SimpleAdapter adapter2 = new SimpleAdapter(EditProfile.this, Lang_List
                    , R.layout.autocomplete_row
                    , new String[]{"LanguageId", "LanguageEnName"}
                    , new int[]{R.id.row_id, R.id.row_name});

            list_dialog = new Dialog(EditProfile.this);
            list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            list_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            list_dialog.setContentView(R.layout.language_dialog);
            mListView = (ListView) list_dialog.findViewById(R.id.lang_dialog_lv_id);
            mListView.setAdapter(adapter2);
            txt_lang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list_dialog.show();
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView txt_lang_name = (TextView) view.findViewById(R.id.row_name);
                            TextView txt_lang_id = (TextView) view.findViewById(R.id.row_id);
                            Language_ID = Integer.parseInt(txt_lang_id.getText().toString());
                            txt_lang.setText(txt_lang_name.getText().toString());
                            Log.d("id of lang", "" + Language_ID);
                            list_dialog.dismiss();
                        }
                    });
                }
            });
        }

        @Override
        protected Object doInBackground(Object[] params) {
            boolean exists = false;
            try {
                SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
                Socket sock = new Socket();
                int timeoutMs = 20000;   // 2 seconds
                sock.connect(sockaddr, timeoutMs);
                exists = true;
            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(EditProfile.this)
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
                        Toast.makeText(EditProfile.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                try {
                    JSONArray j = new GetData().GetPrefLanguage();
                    for (int i = 0; i < j.length(); i++) {
                        TreeMap<String, String> valuePairs = new TreeMap<>();
                        JSONObject jsonObject = j.getJSONObject(i);
                        valuePairs.put("LanguageId", jsonObject.getString("LanguageId"));
                        valuePairs.put("LanguageEnName", jsonObject.getString(getString(R.string.lang_name)));
                        Lang_List.add(valuePairs);
                    }
                    Log.d("Language :", Lang_List.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Language 2 :", Lang_List.toString());
            }
            return null;
        }
    }

    private class nat extends AsyncTask {
        boolean exists = false;

        @Override
        protected void onPostExecute(Object o) {
            if (exists) {
                SimpleAdapter adapterCountry = new SimpleAdapter(EditProfile.this, Country_List
                        , R.layout.autocomplete_row
                        , new String[]{"ID", "NationalityEnName"}
                        , new int[]{R.id.row_id, R.id.row_name});
                txt_country.setAdapter(adapterCountry);

                txt_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView txt_lang_name = (TextView) view.findViewById(R.id.row_name);
                        TextView txt_lang_id = (TextView) view.findViewById(R.id.row_id);
                        Nationality_ID = Integer.parseInt(txt_lang_id.getText().toString());
                        txt_country.setText(txt_lang_name.getText().toString());
                        Log.d("id of lang", "" + Nationality_ID);
                    }
                });
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
                Socket sock = new Socket();
                int timeoutMs = 10000;   // 2 seconds
                sock.connect(sockaddr, timeoutMs);
                exists = true;
            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(EditProfile.this)
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
                        Toast.makeText(EditProfile.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (exists) {
                try {
                    JSONArray j = new GetData().GetNationalities();
                    for (int i = 0; i < j.length(); i++) {
                        TreeMap<String, String> valuePairs = new TreeMap<>();
                        JSONObject jsonObject = j.getJSONObject(i);
                        valuePairs.put("ID", jsonObject.getString("ID"));
                        valuePairs.put("NationalityEnName", jsonObject.getString(getString(R.string.nat_name2)));
                        Country_List.add(valuePairs);
                    }
                    Log.d("Nat :", Country_List.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Nat 2 :", Country_List.toString());
            }
            return null;
        }
    }

    private class ImageUpload extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(EditProfile.this);
            pDialog.setMessage(getString(R.string.loading) + "...");
            pDialog.show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            hidePDialog();
            super.onPostExecute(s);
        }

        private void hidePDialog() {
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }


        @Override
        protected String doInBackground(String... params) {
            callSOAPWebService(params[0]);
            return null;
        }

        private boolean callSOAPWebService(String data) {
            OutputStream out = null;
            int respCode;
            boolean isSuccess = false;
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(GetData.NonOpDomain);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                do {
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "keep-alive");
                    httpURLConnection.setRequestProperty("Content-Type", "text/xml");
                    httpURLConnection.setRequestProperty("SendChunked", "True");
                    httpURLConnection.setRequestProperty("UseCookieContainer", "True");
                    HttpURLConnection.setFollowRedirects(false);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(true);
                    httpURLConnection.setRequestProperty("Content-length", getReqData(data).length + "");
                    httpURLConnection.setReadTimeout(100 * 1000);
                    // httpURLConnection.setConnectTimeout(10 * 1000);
                    httpURLConnection.connect();
                    out = httpURLConnection.getOutputStream();
                    if (out != null) {
                        out.write(getReqData(data));
                        out.flush();
                    }
                    respCode = httpURLConnection.getResponseCode();
                    Log.e("respCode", ":" + respCode);
                } while (respCode == -1);

                // If it works fine
                if (respCode == 200) {
                    try {
                        InputStream responce = httpURLConnection.getInputStream();
                        String str = convertStreamToString(responce);
                        System.out.println(".....data....." + str);
                        InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
                        XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                        XmlPullParser myparser = xmlFactoryObject.newPullParser();
                        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        myparser.setInput(is, null);
                        parseXMLAndStoreIt(myparser);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    isSuccess = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out = null;
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                    httpURLConnection = null;
                }
            }
            return isSuccess;
        }
    }

}
