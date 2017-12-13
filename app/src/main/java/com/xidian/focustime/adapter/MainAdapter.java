package com.xidian.focustime.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xidian.focustime.R;
import com.xidian.focustime.bean.App;
import com.xidian.focustime.db.AppManager;

import java.util.ArrayList;
import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<App> mApps = new ArrayList<>();
    private Context mContext;
    private PackageManager packageManager;
    private AppManager mAppManager;

    public MainAdapter(Context mContext) {
        this.mContext = mContext;
        packageManager = mContext.getPackageManager();
        mAppManager = new AppManager(mContext);
    }

    public void setApps(List<App> Apps) {
        mApps.clear();
        mApps.addAll(Apps);
        notifyDataSetChanged();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        final App App = mApps.get(position);
        initData(holder.mAppName, holder.mSwitchCompat, holder.mAppIcon, App);
        holder.mSwitchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mSwitchCompat.setClickable(false);
                changeItemLockStatus(holder.mSwitchCompat, App, position);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(TextView AppName, CheckBox switchCompat, ImageView mAppIcon, App app) {

        if(app.isRecommendApp()&&app.isLocked()){
            AppName.setText(packageManager.getApplicationLabel(app.getAppInfo())+" (推荐加入白名单)");
        }else{
            AppName.setText(packageManager.getApplicationLabel(app.getAppInfo()));
        }
        switchCompat.setChecked(app.isLocked());
        ApplicationInfo appInfo = app.getAppInfo();
        mAppIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo));
    }

    public synchronized void changeItemLockStatus(CheckBox checkBox, App app, int position) {

        if (checkBox.isChecked()) {
            try {

                app.setLocked(true);
                mAppManager.lockApp(app.getPackageName());
                //notifyItemChanged(position);
                //mApps.remove(app);
                //AppList.getUnlockedAppList().remove(app);
                //AppList.addLockedApp(app);
                //notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
            catch (Exception e)
            {  e.printStackTrace();}

        } else {
            try {

                app.setLocked(false);
                mAppManager.unlockApp(app.getPackageName());
                //notifyItemChanged(position);
                //mApps.remove(app);
                //AppList.getLockedAppList().remove(app);
                //AppList.addUnlockedApp(app);
                //notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
            catch (Exception e)
            {  e.printStackTrace();}
        }

    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAppIcon;
        private TextView mAppName;
        private CheckBox mSwitchCompat;

        public MainViewHolder(View itemView) {
            super(itemView);
            mAppIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            mAppName = (TextView) itemView.findViewById(R.id.app_name);
            mSwitchCompat = (CheckBox) itemView.findViewById(R.id.switch_compat);
        }
    }

}
