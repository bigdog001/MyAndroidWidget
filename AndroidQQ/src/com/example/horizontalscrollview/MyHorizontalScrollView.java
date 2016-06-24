package com.example.horizontalscrollview;

import com.nineoldandroids.view.ViewHelper;

import android.R.integer;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MyHorizontalScrollView extends HorizontalScrollView{
	//父容器
	private LinearLayout mLayout;
	//menu
	private ViewGroup mMenuView;
	//内容
	private ViewGroup mMainView;
	//屏幕的宽度
	private int mScreenWidth;
	//menu右边距
	private int mMenuRightPadding=50;
	//menu的宽度
	private int mMenuWidth;


	private boolean flag;
	//调用2个参数的构造函数，用来初始化一些数据
	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm=(WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		DisplayMetrics outMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		/**
		 * dip: device independent pixels(设备独立像素). 不同设备有不同的显示效果,
		 * 这个和设备硬件有关，一般我们为了支持WVGA、HVGA和QVGA 推荐使用这个，不依赖像素。 
		   px: pixels(像素). 不同设备显示效果相同，一般我们HVGA代表320x480像素，这个用的比较多。 
		   pt: point，是一个标准的长度单位，1pt＝1/72英寸，用于印刷业，非常简单易用； 
		   sp: scaled pixels(放大像素). 主要用于字体显示best for textsize，根据 google 的建议，TextView 的字号最好使用 sp 做单位，
		 * 
		 * */

		mScreenWidth=outMetrics.widthPixels;
		//设置menu的右边边距
		mMenuRightPadding=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());

	}
	//用来测量自己的宽和高还有子view的宽和高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(!flag){
			//取得linerlayout
			mLayout=(LinearLayout) getChildAt(0);	

			mMenuView=(ViewGroup) mLayout.getChildAt(0);
			mMainView=(ViewGroup) mLayout.getChildAt(1);

			mMenuWidth=mMenuView.getLayoutParams().width=mScreenWidth-mMenuRightPadding;
			mMainView.getLayoutParams().width=mScreenWidth;
			flag=true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
	//指定怎么显示布局
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		super.onLayout(changed, l, t, r, b);
		if(changed){
			//这个没有动画效果的隐藏
			this.scrollTo(mMenuWidth, 0);
		}
	}
	//通过设置偏移量，将menu隐藏
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			//就是多出来的区域的x（隐藏的区域的X）
			int scrollX=getScrollX();
			if(scrollX>mMenuWidth/2){
				//有动画效果的隐藏
				this.smoothScrollTo(mMenuWidth, 0);
			}else{
				this.smoothScrollTo(0, 0);
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 内容界面有差别
	 * 菜单偏移量有差别
	 * 菜单有缩放
	 * **/
	//滚动条发生改变时调用     l的初始值是宽度（ScrollX的值）
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);
		//scale 的值1~0；l getScrollX
		float scale = l * 1.0f / mMenuWidth;
		
		float rightScale = 0.7f + 0.3f * scale;
		float leftScale = 1.0f - scale * 0.3f;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);
		
		//属性动画 1.传一个view对象，2.传偏移量
		ViewHelper.setTranslationX(mMenuView, mMenuWidth * scale * 0.8f);

		//设置缩放的位置
		ViewHelper.setPivotX(mMainView, 0);
		ViewHelper.setPivotY(mMainView, mMainView.getHeight() / 2);
		
		ViewHelper.setScaleX(mMenuView, leftScale);
		ViewHelper.setScaleY(mMenuView, leftScale);
		//设置透明度
		ViewHelper.setAlpha(mMenuView, leftAlpha);
		
		ViewHelper.setScaleX(mMainView, rightScale);
		ViewHelper.setScaleY(mMainView, rightScale);
	}
}
