package com.xidian.mktime.module;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.mktime.R;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.bean.Billing;
import com.xidian.mktime.bean.StudyStatistics;
import com.xidian.mktime.utils.TokenUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @描述 我的积分
 */
public class TokenActivity extends BaseActivity{

    RecyclerView recyclerView;
    private List<Billing> billingList = new ArrayList<Billing>();

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
        return R.layout.activity_token;
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
        setToolbarTitle("我的积分");
        billingList = DataSupport.order("milliseconds desc").find(Billing.class);
        TokenAdapter adapter = new TokenAdapter(billingList);
        recyclerView.setAdapter(adapter);

        mTvSumNum.setText(Integer.toString(TokenUtil.getTodayTokenSum()));
        mTvSumTime.setText(Integer.toString(TokenUtil.getTokenSum()));

    }


    @Override
    protected void initAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
