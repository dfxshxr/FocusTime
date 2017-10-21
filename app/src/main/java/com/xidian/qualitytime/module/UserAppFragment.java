package com.xidian.qualitytime.module;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xidian.qualitytime.R;
import com.xidian.qualitytime.adapter.MainAdapter;
import com.xidian.qualitytime.base.BaseFragment;
import com.xidian.qualitytime.bean.App;

import java.util.ArrayList;
import java.util.List;

public class UserAppFragment extends BaseFragment {

    public static UserAppFragment newInstance(List<App> list) {
        UserAppFragment userAppFragment = new UserAppFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) list);
        userAppFragment.setArguments(bundle);
        return userAppFragment;
    }

    private RecyclerView mRecyclerView;
    private List<App> data,list;
    private MainAdapter mMainAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_app_list;
    }

    @Override
    protected void init(View rootView) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        data = getArguments().getParcelableArrayList("data");
        mMainAdapter = new MainAdapter(getContext());
        mRecyclerView.setAdapter(mMainAdapter);
        list = new ArrayList<>();
        for (App info : data) {
            if (!info.isSysApp()) {
                list.add(info);
            }
        }
        mMainAdapter.setApps(list);
    }
}
