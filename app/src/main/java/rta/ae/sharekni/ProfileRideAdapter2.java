package rta.ae.sharekni;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rta.ae.sharekni.Arafa.DataModel.BestRouteDataModel;

/**
 * Created by Nezar Saleh on 11/9/2015.
 */
public class ProfileRideAdapter2 extends ArrayAdapter<BestRouteDataModel> {

    int resourse;
    Context context;
    BestRouteDataModel[] BestrouteArray;
    LayoutInflater layoutInflater;

    public ProfileRideAdapter2(Context context, int resource, BestRouteDataModel[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourse = resource;
        this.BestrouteArray = objects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ProfileRideAdapter2(Activity con, int top_rides_custom_row, List<BestRouteDataModel> arr) {
        super(con, top_rides_custom_row, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vh;
        if (v == null) {
            v = layoutInflater.inflate(resourse, parent, false);
            vh = new ViewHolder();
            vh.FromEm = (TextView) v.findViewById(R.id.em_from);
            vh.ToEm = (TextView) v.findViewById(R.id.em_to);
            vh.FromReg = (TextView) v.findViewById(R.id.reg_from);
            vh.ToReg = (TextView) v.findViewById(R.id.reg_to);
            vh.RouteEnName = (TextView) v.findViewById(R.id.driver_profile_RouteEnName);
             vh.StartFromTime= (TextView) v.findViewById(R.id.StartFromTime);
            //  vh.EndToTime_= (TextView) v.findViewById(R.id.EndToTime_);
            // vh.driver_profile_dayWeek= (TextView) v.findViewById(R.id.driver_profile_dayWeek);

            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }
        BestRouteDataModel bestRouteDataModel = BestrouteArray[position];
        vh.FromEm.setText(bestRouteDataModel.getFromEm());
        vh.ToEm.setText(bestRouteDataModel.getToEm());
        vh.FromReg.setText(bestRouteDataModel.getFromReg());
        vh.ToReg.setText(bestRouteDataModel.getToReg());
        vh.FromEmId = bestRouteDataModel.getFromEmId();
        vh.FromRegid = bestRouteDataModel.getFromRegid();
        vh.ToEmId = bestRouteDataModel.getToEmId();
        vh.ToRegId = bestRouteDataModel.getToRegId();


        StringBuffer res = new StringBuffer();

        String[] strArr = bestRouteDataModel.getRouteName().split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            if (stringArray.length != 0) {
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);
                res.append(str).append(" ");

            }

        }
        vh.RouteEnName.setText(res);
        vh.StartFromTime.setText(bestRouteDataModel.getStartFromTime());
        //   vh.EndToTime_.setText(bestRouteDataModel.getEndToTime_());
        //   vh.driver_profile_dayWeek.setText(bestRouteDataModel.getDriver_profile_dayWeek());
        return v;
    }


    static class ViewHolder {
        TextView FromEm;
        TextView ToEm;
        TextView FromReg;
        TextView ToReg;
        TextView RouteEnName;
           TextView StartFromTime;
        //  TextView EndToTime_;
        // TextView driver_profile_dayWeek;
        int FromEmId, ToEmId, FromRegid, ToRegId;

    }



}
