package Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Util.OnItemClickListener;
import model.IncidentModel;
import shantanu.testmap.R;

/**
 * Created by Shantanu on 14-02-2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.IncidentModelHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private List<IncidentModel> mDataset;
    private OnItemClickListener onItemClickListener;

    public static class IncidentModelHolder extends RecyclerView.ViewHolder {
        TextView textViewIncidentId;
        TextView textViewDateTime;
        TextView textViewAddress;

        public IncidentModelHolder(View itemView) {
            super(itemView);
            textViewIncidentId = (TextView) itemView.findViewById(R.id.TextViewIncidentId);
            textViewDateTime = (TextView) itemView.findViewById(R.id.TextViewIncidentDate);
            textViewAddress = (TextView) itemView.findViewById(R.id.TextViewIncidentAddress);
            Log.i(LOG_TAG, "Adding Listener");
        }
    }

    public MyRecyclerViewAdapter(List<IncidentModel> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public IncidentModelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incident_list_row, parent, false);

        IncidentModelHolder IncidentModelHolder = new IncidentModelHolder(view);
        return IncidentModelHolder;
    }

    @Override
    public void onBindViewHolder(IncidentModelHolder holder, int position) {
        holder.textViewIncidentId.setText(mDataset.get(position).getIncident_id());
        holder.textViewDateTime.setText(mDataset.get(position).getDate_time());
        holder.textViewAddress.setText(mDataset.get(position).getAddress_of_incident());

        final IncidentModel model = mDataset.get(position);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(model);
            }
        };

        holder.textViewIncidentId.setOnClickListener(listener);
        holder.textViewAddress.setOnClickListener(listener);
        holder.textViewDateTime.setOnClickListener(listener);
    }

    public void addItem(IncidentModel dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}