package com.example.com.hengsing;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Mser extends Service {  
    //服务  
    //这个类纯蛋疼用 只是为了在activity点击button后 在开启一个service   
      
    @Override  
    public IBinder onBind(Intent intent) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public void onCreate() {  
  //创建service时一个 实例化一个TableShowView对象并且调用他的fun()方法把它注册到windowManager上  
        super.onCreate();  
        new TableShowView(this).fun();  
    }  
  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        // TODO Auto-generated method stub  
        return super.onStartCommand(intent, flags, startId);  
    }  
}  