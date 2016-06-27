package com.example.com.hengsing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class TableShowView extends View {
	// ���������ʾ�����̳�TextView����дondraw������
	// ����һ���̲߳��ϵĵ���ondraw����ȥ��������д�ļ̳���TextView������
	// �������д�˸�������view��= =��������ص�

	Context						c;
	WindowManager				mWM;		// WindowManager
	WindowManager.LayoutParams	mWMParams;	// WindowManager����
	View						win;
	int tag = 0;
	int oldOffsetX;
	int oldOffsetY;

	public TableShowView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		c = context;
	}

	public void fun() {
		// ��������view WindowManager����
		mWM = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		win = LayoutInflater.from(c).inflate(R.layout.ctrl_window, null);
		win.setBackgroundColor(Color.TRANSPARENT);
		// ��������������һ�������ļ�
		
		win.setOnTouchListener(new OnTouchListener() {
			// ��������
			float	lastX, lastY;

			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();

				float x = event.getX();
				float y = event.getY();
				
				if(tag == 0){
				   oldOffsetX= mWMParams.x; // ƫ����
				   oldOffsetY = mWMParams.y; // ƫ����
				}
				
			    
				if (action == MotionEvent.ACTION_DOWN) {
					lastX = x;
					lastY = y;

				}
				else if (action == MotionEvent.ACTION_MOVE) {
					mWMParams.x += (int) (x - lastX); // ƫ����
					mWMParams.y += (int) (y - lastY); // ƫ����
					
					tag = 1;
					mWM.updateViewLayout(win, mWMParams);
				}

				else if (action ==  MotionEvent.ACTION_UP){
					int newOffsetX = mWMParams.x;
					int newOffsetY = mWMParams.y;					
					if(oldOffsetX == newOffsetX && oldOffsetY == newOffsetY){
						Toast.makeText(c, "��㵽���ˡ����ۣ�������", 1).show();
					}else {
						tag = 0;
					}
				}
				return true;
			}
		});
		


		WindowManager wm = mWM;
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		mWMParams = wmParams;
		wmParams.type = 2003; // type�ǹؼ��������2002��ʾϵͳ�����ڣ���Ҳ��������2003��
		wmParams.flags = 40;// �����������ɿ�

		wmParams.width = 50;
		wmParams.height = 50;
		wmParams.format = -3; // ͸��

		wm.addView(win, wmParams);// ������ص� ��WindowManager�ж���ղ����õ�ֵ
									// ֻ��addview�������ʾ��ҳ����ȥ��
		// ע�ᵽWindowManager win��Ҫ�ղ���������layout��wmParams�Ǹղ����õ�WindowManager������
		// Ч���ǽ�winע�ᵽWindowManager�в������Ĳ�����wmParams�����ö�

		
		

	}

}
