package com.xidian.focustime.module;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xidian.focustime.R;
import com.xidian.focustime.bean.Wallpaper;

import java.util.List;

import skin.support.SkinCompatManager;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder>{

    private List<Wallpaper> mWallpaperList;

    private String currWallpaperName=SkinCompatManager.getInstance().getCurSkinName();

    static class ViewHolder extends RecyclerView.ViewHolder {
        View wallpaperView;
        ImageView ivChoose, ivBackground;

        public ViewHolder(View view) {
            super(view);
            wallpaperView = view;
            ivChoose = (ImageView) view.findViewById(R.id.ivChoose );
            ivBackground = (ImageView) view.findViewById(R.id.ivBackground );
        }
    }

    public WallpaperAdapter(List<Wallpaper> wallpaperList) {
        mWallpaperList = wallpaperList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallpaper, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.wallpaperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Wallpaper wallpaper = mWallpaperList.get(position);
                // 后缀加载
                if("default".equals(wallpaper.getName())){
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                }else{
                    SkinCompatManager.getInstance().loadSkin(wallpaper.getName(), SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                }
                currWallpaperName=wallpaper.getName();
                notifyDataSetChanged();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wallpaper wallpaper = mWallpaperList.get(position);
        if(currWallpaperName.equals(wallpaper.getName())){
            holder.ivChoose.setVisibility(View.VISIBLE);
        }else {
            holder.ivChoose.setVisibility(View.GONE);
        }
        holder.ivBackground.setImageResource(wallpaper.getResId());

    }

    @Override
    public int getItemCount() {
        return mWallpaperList.size();
    }

}