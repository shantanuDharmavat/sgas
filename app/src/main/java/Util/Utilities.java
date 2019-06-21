package Util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Created by Abhijit on 29-12-2016.
 */

public class Utilities {
    int mYear;
    int mMonth;
    int mDay;
    boolean isOkayClicked = false;

    public LatLng getLatLng(Location mCurrentLocation) {
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        Log.e("lat long values", "" + latLng);
        return latLng;
    }

    public static void setListViewHeightBasedOnChildren(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }

    public String dateUtil() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        StringBuilder builder = new StringBuilder();
        builder.append(mYear + "-");
        if ((mMonth +1) < 10)
            builder.append("0"+(mMonth + 1)+ "-");
        else
            builder.append((mMonth + 1)+ "-");
        builder.append(mDay);

        return builder.toString();
    }

    public void datePopUp(final Context context1, final EditText txtDate) {
        Context context = context1;

        final EditText date = txtDate;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month;
                String day;
                if ((monthOfYear + 1) < 10)
                    month = "0" + (monthOfYear + 1);
                else
                    month = "" + (monthOfYear + 1);

                if ((dayOfMonth + 1) < 10)
                    day = "0" + (dayOfMonth);
                else
                    day = "" + (dayOfMonth);

                date.setText(year + "-" + (month) + "-" + day);
            }
        }, mYear, mMonth, mDay);
        dpd.show();

    }
}
