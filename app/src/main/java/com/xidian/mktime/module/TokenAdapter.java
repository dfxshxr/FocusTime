package com.xidian.mktime.module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xidian.mktime.LockApplication;
import com.xidian.mktime.R;
import com.xidian.mktime.bean.Billing;
import com.xidian.mktime.utils.DataUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.ViewHolder>{

    private List<Billing> mBillingList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View statisticsView;
        TextView tvEnd,tvNote,tvToken,tvDate;

        public ViewHolder(View view) {
            super(view);
            statisticsView = view;
            tvEnd = (TextView) view.findViewById(R.id.tvEnd);
            tvNote = (TextView) view.findViewById(R.id.tvNote);
            tvToken = (TextView) view.findViewById(R.id.tvToken);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
        }
    }

    public TokenAdapter(List<Billing> studyStatisticsList) {
        mBillingList = studyStatisticsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.statisticsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Billing billing = mBillingList.get(position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Billing billing = mBillingList.get(position);

        SimpleDateFormat dateFm = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFm2 = new SimpleDateFormat("yyyy年MM月dd日E");

        String time = dateFm.format(new Date(billing.getMilliseconds()));
        holder.tvEnd.setText(time);
        String date = dateFm2.format(new Date(billing.getMilliseconds()));
        holder.tvDate.setText(date);

        if (position != 0) {
            Billing dataBefore = mBillingList.get(position - 1);
            if (date.equals(dateFm2.format(new Date(dataBefore.getMilliseconds())))){
                holder.tvDate.setVisibility(View.GONE);

            }
        }

        holder.tvNote.setText(billing.getNote());
        if(billing.getToken()>=0)
        {
            holder.tvToken.setText("+"+billing.getToken());
            holder.tvToken.setTextColor(LockApplication.getContext().getResources().getColor(R.color.red6));
        } else {
            holder.tvToken.setText(""+(billing.getToken()));
            holder.tvToken.setTextColor(LockApplication.getContext().getResources().getColor(R.color.gray0));
        }


    }

    @Override
    public int getItemCount() {
        return mBillingList.size();
    }

}