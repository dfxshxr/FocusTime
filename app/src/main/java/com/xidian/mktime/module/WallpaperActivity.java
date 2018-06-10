package com.xidian.mktime.module;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.WindowManager;
import android.widget.TextView;

import com.xidian.mktime.R;
import com.xidian.mktime.base.BaseActivity;
import com.xidian.mktime.bean.Wallpaper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A login screen that offers login via email/password.
 */
public class WallpaperActivity extends BaseActivity{

    RecyclerView recyclerView;
    private List<Wallpaper> wallpaperList = new ArrayList<Wallpaper>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallpaper;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //setToolbarTitle("选择壁纸");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void initData() {
        setToolbarTitle("壁纸选择");
        wallpaperList.add(new Wallpaper("default",R.drawable.skin0));
        wallpaperList.add(new Wallpaper("skin1",R.drawable.skin1));
        wallpaperList.add(new Wallpaper("skin2",R.drawable.skin2));
        wallpaperList.add(new Wallpaper("skin3",R.drawable.skin3));
        wallpaperList.add(new Wallpaper("skin4",R.drawable.skin4));
        WallpaperAdapter adapter = new WallpaperAdapter(wallpaperList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void initAction() {
    }



}

