package rta.ae.sharekni.MainNavigationDrawerFragment;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import rta.ae.sharekni.Arafa.Activities.BestDrivers;
import rta.ae.sharekni.Arafa.Activities.MostRides;
import rta.ae.sharekni.DriverAlertsForRequest;
import rta.ae.sharekni.EditProfile;
import rta.ae.sharekni.FAQ;
import rta.ae.sharekni.StartScreen.StartScreenActivity;
import rta.ae.sharekni.QuickSearch;
import rta.ae.sharekni.R;


public class NavigationDrawerFragment extends Fragment {


    Dialog list_dialog;
    ListView mListView;
    List<TreeMap<String, String>> Lang_List = new ArrayList<>();
    private int Language_ID;


    public static CircularImageView circularImageView;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mrecyclerView;

    public static RelativeLayout navy_Change_lang;
    String Locale_Str;


    String Navy_Photo_Path;

    private ActionBarDrawerToggle mdrawerToggle;
    private DrawerLayout mdrawerLayout;


    private View containerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    public static TextView Convert_txt_id;
    public static ImageView menuicon12;
    ImageView LanguageMenuIcon;
    public static RelativeLayout navy_My_vehicles;
    RelativeLayout happy_meter_relative, navy_homePage, navy_TopRides, navy_BestDrivers, navy_SearchOptions, navy_MyProfile, navy_Logout, navy_Edit_Profile;
    public static TextView tv_name_home, nat_home;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        navy_TopRides = (RelativeLayout) layout.findViewById(R.id.navy_TopRides);
        navy_BestDrivers = (RelativeLayout) layout.findViewById(R.id.navy_BestDrivers);
        navy_SearchOptions = (RelativeLayout) layout.findViewById(R.id.navy_SearchOptions);
        navy_MyProfile = (RelativeLayout) layout.findViewById(R.id.navy_MyProfile);
        navy_Logout = (RelativeLayout) layout.findViewById(R.id.navy_Logout);
        tv_name_home = (TextView) layout.findViewById(R.id.tv_name_home);
        nat_home = (TextView) layout.findViewById(R.id.nat_home);
        navy_Edit_Profile = (RelativeLayout) layout.findViewById(R.id.navy_Edit_Profile);
        navy_Change_lang = (RelativeLayout) layout.findViewById(R.id.navy_Change_lang);
        happy_meter_relative = (RelativeLayout) layout.findViewById(R.id.happy_meter_relative);


        navy_My_vehicles = (RelativeLayout) layout.findViewById(R.id.navy_My_vehicles);
        menuicon12 = (ImageView) layout.findViewById(R.id.menuicon12);
        Convert_txt_id = (TextView) layout.findViewById(R.id.Convert_txt_id);
        LanguageMenuIcon = (ImageView) layout.findViewById(R.id.menuicon20);


        happy_meter_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FAQ.class);
                startActivity(intent);
            }
        });


        navy_TopRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MostRides.class);
                startActivity(intent);
            }
        });


        navy_BestDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BestDrivers.class);
                startActivity(intent);
            }
        });


        navy_SearchOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QuickSearch.class);
                startActivity(intent);
            }
        });


        navy_MyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), DriverAlertsForRequest.class);
                startActivity(intent);
            }
        });

        navy_Edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });


        navy_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences myPrefs = getContext().getSharedPreferences("myPrefs", 0);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("account_id", null);
                editor.putString("account_type", "");
                editor.commit();
                Intent in = new Intent(getContext(), StartScreenActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);
            }


        });


        circularImageView = (CircularImageView) layout.findViewById(R.id.navy);
        circularImageView.setBorderWidth(5);
        circularImageView.setSelectorStrokeWidth(5);
        circularImageView.addShadow();


        navy_Change_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });


        return layout;

    } // on create view


    //  1. First create setUp void   with 3 parameters
    // Fragment id   +  DrawerLayout  + Toolbar
    //call the two methods onDrawerOpened   + onDrawerClosed
    // at the onDrawerOpened we check if the user knows about our drawer if not  : equal it to true
    // and then save this variable to the void saveToPreference
    //finally call invalidateOptionsMenu from getActivity  ...


    // the onDrawerSlide void is used to measure the slide offset
    // in other words as i drag the navigation drawer as the toolbar get fade away but not to be completely invisible
    // we use the setAlpha method


    // after the 3  methods OnDrawer(OPened  -  Closed  - Slide)  we check the 2 variable mUserlearned + mSavedInstance
    // if  not open the drawer with the container view variable which has the fragment id passed through the setUp Void first parameter


    // at the onCreate method we checck the Preference through the readFromPreferrencce void


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mdrawerLayout = drawerLayout;
        mdrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_colse) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


                getActivity().invalidateOptionsMenu();

            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.d("Nezar", "offset" + slideOffset);
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }

            }
        };


        mdrawerLayout.setDrawerListener(mdrawerToggle);

        mdrawerLayout.post(new Runnable() {
            @Override
            public void run() {

                mdrawerToggle.syncState();
            }
        });

    } //  set up

    private void showAlertDialog() {

        Lang_List.clear();
        TreeMap<String, String> valuePairs = new TreeMap<>();
        valuePairs.put("LanguageId", "1");
        valuePairs.put("LanguageEnName", getActivity().getString(R.string.language_arabic));
        Lang_List.add(valuePairs);
        TreeMap<String, String> valuePairs2 = new TreeMap<>();
        valuePairs2.put("LanguageId", "2");
        valuePairs2.put("LanguageEnName", getActivity().getString(R.string.language_english));
        Lang_List.add(valuePairs2);
        TreeMap<String, String> valuePairs3 = new TreeMap<>();
        valuePairs3.put("LanguageId", "3");
        valuePairs3.put("LanguageEnName", getActivity().getString(R.string.language_chinese));
        Lang_List.add(valuePairs3);
        TreeMap<String, String> valuePairs4 = new TreeMap<>();
        valuePairs4.put("LanguageId", "4");
        valuePairs4.put("LanguageEnName", getActivity().getString(R.string.language_filipino));
        Lang_List.add(valuePairs4);
        TreeMap<String, String> valuePairs5 = new TreeMap<>();
        valuePairs5.put("LanguageId", "5");
        valuePairs5.put("LanguageEnName", getActivity().getString(R.string.language_urdu));
        Lang_List.add(valuePairs5);

        Locale locale = Locale.getDefault();
        Locale_Str = locale.toString();
        if (Locale_Str.contains("en")) {
            Lang_List.remove(1);
        } else if (Locale_Str.contains("ar")) {
            Lang_List.remove(0);
        } else if (Locale_Str.contains("zh")) {
            Lang_List.remove(2);
        } else if (Locale_Str.contains("fil")) {
            Lang_List.remove(3);
        }else if (Locale_Str.contains("ur")) {
            Lang_List.remove(4);
        }


        final SimpleAdapter adapter2 = new SimpleAdapter(getActivity(), Lang_List
                , R.layout.autocomplete_row
                , new String[]{"LanguageId", "LanguageEnName"}
                , new int[]{R.id.row_id, R.id.row_name});


        list_dialog = new Dialog(getActivity());
        list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        list_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        list_dialog.setContentView(R.layout.language_dialog);
        mListView = (ListView) list_dialog.findViewById(R.id.lang_dialog_lv_id);
        mListView.setAdapter(adapter2);

        list_dialog.show();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt_lang_name = (TextView) view.findViewById(R.id.row_name);
                TextView txt_lang_id = (TextView) view.findViewById(R.id.row_id);
                Language_ID = Integer.parseInt(txt_lang_id.getText().toString());
                Log.d("id of lang", "" + Language_ID);
                switch (Language_ID) {
                    case 1:
                        Locale locale2 = new Locale("ar");
                        Locale.setDefault(locale2);
                        Configuration config2 = new Configuration();
                        config2.locale = locale2;
                        getActivity().getApplicationContext().getResources().updateConfiguration(config2, null);
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                        break;
                    case 2:
                        Locale locale3 = new Locale("en");
                        Locale.setDefault(locale3);
                        Configuration config3 = new Configuration();
                        config3.locale = locale3;
                        getActivity().getApplicationContext().getResources().updateConfiguration(config3, null);
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                        break;
                    case 3:
                        Locale locale4 = new Locale("zh");
                        Locale.setDefault(locale4);
                        Configuration config4 = new Configuration();
                        config4.locale = locale4;
                        getActivity().getApplicationContext().getResources().updateConfiguration(config4, null);
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                        break;
                    case 4:
                        Locale locale5 = new Locale("fil");
                        Locale.setDefault(locale5);
                        Configuration config5 = new Configuration();
                        config5.locale = locale5;
                        getActivity().getApplicationContext().getResources().updateConfiguration(config5, null);
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                        break;
                    case 5:
                        Locale locale6 = new Locale("ur");
                        Locale.setDefault(locale6);
                        Configuration config6 = new Configuration();
                        config6.locale = locale6;
                        getActivity().getApplicationContext().getResources().updateConfiguration(config6, null);
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                        break;
                    default:
                        break;
                }
                list_dialog.dismiss();
            }
        });


    }


}
