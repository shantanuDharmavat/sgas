package Util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import shantanu.testmap.R;

/**
 * Created by Abhijit on 13-12-2016.
 */

public class MapPopUp_ListAdaptor extends ArrayAdapter<String>
{

    private final Activity context;
    private final String[] str_key;
    private final String[]  str_value;

    public MapPopUp_ListAdaptor(Activity context, String[] title, String[] desc)
    {
        super(context, R.layout.activity_geojson2view_single_row, title);
        this.context = context;
        this.str_key = title;
        this.str_value = desc;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.map_pop_up_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        //txtTitle.setMinWidth(px);
        txtTitle.setText(str_key[position]);

        TextView desc = (TextView) rowView.findViewById(R.id.txt2);
        desc.setText(str_value[position]);

        return rowView;

    }


}