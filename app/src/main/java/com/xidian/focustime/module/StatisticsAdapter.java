package com.xidian.focustime.module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.xidian.focustime.LockApplication;
import com.xidian.focustime.R;
import com.xidian.focustime.bean.Statistics;
import com.xidian.focustime.utils.DataUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder>{

    private List<Statistics> mStatisticsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View statisticsView;
        TextView tvEnd,tvStatus,tvSetting,tvPlay,tvThisDetail,tvDate;

        public ViewHolder(View view) {
            super(view);
            statisticsView = view;
            tvEnd = (TextView) view.findViewById(R.id.tvEnd);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvSetting = (TextView) view.findViewById(R.id.tvSetting);
            tvPlay = (TextView) view.findViewById(R.id.tvPlay);
            tvThisDetail=(TextView) view.findViewById(R.id.tvThisDetail);
            tvDate=(TextView) view.findViewById(R.id.tvDate);
        }
    }

    public StatisticsAdapter(List<Statistics> statisticsList) {
        mStatisticsList = statisticsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today_statistics, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.statisticsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Statistics statistics = mStatisticsList.get(position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Statistics statistics = mStatisticsList.get(position);

        SimpleDateFormat dateFm = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFm2 = new SimpleDateFormat("YYYY年MM月dd日E");

        String time = dateFm.format(new Date(statistics.getEndMilliseconds()));
        holder.tvEnd.setText(time);
        String date = dateFm2.format(new Date(statistics.getEndMilliseconds()));
        holder.tvDate.setText(date);

        if (position != 0) {
            Statistics dataBefore = mStatisticsList.get(position - 1);
            if (date.equals(dateFm2.format(new Date(dataBefore.getEndMilliseconds())))){
                holder.tvDate.setVisibility(View.GONE);

            }
        }

        holder.tvSetting.setText(DataUtil.formatTime(statistics.getSettingMilliseconds()));
        if(statistics.getThisMilliseconds()-statistics.getSettingMilliseconds()>=0)
        {
            holder.tvStatus.setText("成功");
            holder.tvStatus.setTextColor(LockApplication.getContext().getResources().getColor(R.color.red6));
        } else {
            holder.tvStatus.setText("");
            holder.tvStatus.setTextColor(LockApplication.getContext().getResources().getColor(R.color.gray0));
        }

        holder.tvThisDetail.setText(DataUtil.formatTime(statistics.getThisMilliseconds()));

        if(statistics.getPlayMilliseconds()>0){
            holder.tvPlay.setText("休息"+DataUtil.timeParse2Minutes(statistics.getPlayMilliseconds()));
        }else {
            holder.tvPlay.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return mStatisticsList.size();
    }

}