package com.xidian.focustime.module;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xidian.focustime.R;
import com.xidian.focustime.utils.UtilBitmap;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView background=(ImageView)findViewById(R.id.background);

        UtilBitmap bitmap = new UtilBitmap();
     //   bitmap.blurImageView(this,  background, 1,this.getResources().getColor(R.color.colorWhite_t8));


    }
}
