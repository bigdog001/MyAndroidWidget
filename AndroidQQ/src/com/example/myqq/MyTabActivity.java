package com.example.myqq;

import com.example.download.httpDownloader;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;


public class MyTabActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
     
        TabHost tabHost1=getTabHost();
        Intent intent1=new Intent();
        intent1.setClass(this, MyQQNews.class);
        TabHost.TabSpec spec1=tabHost1.newTabSpec("消息");
        Resources resources1=getResources();
        spec1.setIndicator("消息",resources1.getDrawable(R.drawable.actionbar_icon));
        spec1.setContent(intent1);
        tabHost1.addTab(spec1);
        
        TabHost tabHost2=getTabHost();
        Intent intent2=new Intent();
        intent2.setClass(this, MyQQContact.class);
        TabHost.TabSpec spec2=tabHost2.newTabSpec("联系人");
        Resources resources2=getResources();
        spec2.setIndicator("联系人", resources2.getDrawable(R.drawable.abc_ic_search));
        spec2.setContent(intent2);
        tabHost2.addTab(spec2);
        
        TabHost tabHost3=getTabHost();
        Intent intent3=new Intent();
        intent3.setClass(this, MyQQDynamic.class);
        TabHost.TabSpec spec3=tabHost2.newTabSpec("动态");
        Resources resources3=getResources();
        spec3.setIndicator("动态", resources3.getDrawable(R.drawable.app_panel_add_icon));
        spec3.setContent(intent3);
        tabHost2.addTab(spec3);

    }  
}
