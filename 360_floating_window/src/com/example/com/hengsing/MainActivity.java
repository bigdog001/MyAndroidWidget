package com.example.com.hengsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {  
    //һ����ֻ��һ����ť��activity  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				funClick(null);
			}
		});
        
    }  
    public void funClick(View v){  
        //��ť�����  
       this.startService(new Intent(this,Mser.class));  
// new TableShowView(this).fun(); ���ֻ����activity������   
// ��activity��ȥ��̨��ʱ��[��̬ͣ����������̬] �������õ���ʾ�������viewҲ����ʧ  
// ����������õ�������һ�����񣬷����д���������Ҫ��ʾ��table�ϵ�view��������ע�ᵽwindowManager��    
        this.finish();  
    }  
}  