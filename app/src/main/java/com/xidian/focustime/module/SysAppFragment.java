package com.xidian.focustime.module;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xidian.focustime.R;
import com.xidian.focustime.adapter.MainAdapter;
import com.xidian.focustime.base.BaseFragment;
import com.xidian.focustime.bean.App;

import java.util.ArrayList;
import java.util.List;


public class SysAppFragment extends BaseFragment {

    public static SysAppFragment newInstance(List<App> list) {
        SysAppFragment sysAppFragment = new SysAppFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) list);
        sysAppFragment.setArguments(bundle);
        return sysAppFragment;
    }

    private RecyclerView mRecyclerView;
    private List<App> data, list;
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
            if (info.isSysApp()) {
                list.add(info);
            }
        }
        mMainAdapter.setApps(list);
    }
}
