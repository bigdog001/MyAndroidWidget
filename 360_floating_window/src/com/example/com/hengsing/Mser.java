package com.example.com.hengsing;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Mser extends Service {  
    //����  
    //����ി������ ֻ��Ϊ����activity���button�� �ڿ���һ��service   
      
    @Override  
    public IBinder onBind(Intent intent) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public void onCreate() {  
  //����serviceʱһ�� ʵ����һ��TableShowView�����ҵ�������fun()��������ע�ᵽwindowManager��  
        super.onCreate();  
        new TableShowView(this).fun();  
    }  
  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        // TODO Auto-generated method stub  
        return super.onStartCommand(intent, flags, startId);  
    }  
}  