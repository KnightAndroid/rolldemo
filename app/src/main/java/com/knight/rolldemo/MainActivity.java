package com.knight.rolldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private ScrollView scrollView;
    private ImageView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTransparentStatusBar();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initView();
        myHandler.postDelayed(ScrollRunnable,10);
    }


    /**
     *
     * 把状态栏设为透明
     */
    protected void setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     *
     * 绑定ui
     */
    private void initView(){
        scrollView = findViewById(R.id.scroll);
        map = findViewById(R.id.map);
        Bitmap bitmap = readBitMap(this,R.drawable.scrollmap);
        map.setImageBitmap(bitmap);
    }



    private Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bitmap = BitmapFactory.decodeStream(is,null,opt);

        try {
            is.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;

    }


    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int off = map.getMeasuredHeight() - scrollView.getHeight();// 判断高度,ScrollView的最大高度，就是屏幕的高度
                    if(off > 0){

                            scrollView.scrollBy(0, 2);
                            if(scrollView.getScrollY() == off){
                                sendEmptyMessageDelayed(1, 25);
                            } else {
                                sendEmptyMessageDelayed(0, 25);
                            }


                    }else{
                        sendEmptyMessageDelayed(1, 25);
                    }
                    break;
                case 1:
                    int off1 = map.getMeasuredHeight() - scrollView.getHeight();// 判断高度,ScrollView的最大高度，就是屏幕的高度
                    if (off1 > 0) {
                        scrollView.scrollBy(0, -2);
                        if (scrollView.getScrollY() == 0) {

                                sendEmptyMessageDelayed(0, 25);

                        } else {
                            sendEmptyMessageDelayed(1, 25);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };
    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
            myHandler.sendEmptyMessage(0);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacks(ScrollRunnable);
    }

}