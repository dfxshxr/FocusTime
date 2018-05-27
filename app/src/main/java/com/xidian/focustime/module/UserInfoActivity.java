package com.xidian.focustime.module;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.xidian.focustime.R;
import com.xidian.focustime.base.BaseActivity;
import com.xidian.focustime.bean.Statistics;
import com.xidian.focustime.utils.DataUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @描述 我的个人信息
 */
public class UserInfoActivity extends BaseActivity{

    RecyclerView recyclerView;
    private List<Statistics> statisticsList = new ArrayList<Statistics>();

    @BindView(R.id.appBar)
    protected AppBarLayout mAppBar;
    @BindView(R.id.ivToolbarHistory)
    ImageView mIvHistory;
    @BindView(R.id.tvSumNum)
    TextView mTvSumNum;
    @BindView(R.id.tvSumTime)
    TextView mTvSumTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_today_statistics;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
      //  mIvHistory.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        setToolbarTitle("今日统计");
        long todayStart=DataUtil.getStartTimeOfDay(System.currentTimeMillis());
        statisticsList= DataSupport.order("endMilliseconds desc").find(Statistics.class);
        StatisticsAdapter adapter = new StatisticsAdapter(statisticsList);
        recyclerView.setAdapter(adapter);

        long sumTime=0;
        int sumSum=0;
        for(int    i=0;    i<statisticsList.size();    i++)    {
            Statistics statistics =    statisticsList.get(i);

            if(statistics.getThisMilliseconds()>=0){
                sumTime=sumTime+statistics.getThisMilliseconds();
            }
            if(statistics.getThisMilliseconds()-statistics.getSettingMilliseconds()>=0){
                sumSum=sumSum+1;
            }
        }
        mTvSumNum.setText(Integer.toString(sumSum));
        mTvSumTime.setText(DataUtil.formatTime(sumTime));

    }


    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
