package com.example.com.hengsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {  
    //一个有只有一个按钮的activity  
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
        //按钮被点击  
       this.startService(new Intent(this,Mser.class));  
// new TableShowView(this).fun(); 如果只是在activity中启动   
// 当activity跑去后台的时候[暂停态，或者销毁态] 我们设置的显示到桌面的view也会消失  
// 所以这里采用的是启动一个服务，服务中创建我们需要显示到table上的view，并将其注册到windowManager上    
        this.finish();  
    }  
}  