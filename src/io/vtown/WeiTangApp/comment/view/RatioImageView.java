package io.vtown.WeiTangApp.comment.view;


import io.vtown.WeiTangApp.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 
 * 自定义控件      按照比例来决定布局高度 ，根据图片的宽高去决定控件的宽高
 * @author yihuihua
 *
 */
public class RatioImageView extends ImageView {

	private float mRatio;

	public RatioImageView(Context context) {
		super(context);
		
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取属性，当自定义属性时, 系统会自动生成属性相关id, 此id通过R.styleable来引用
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
		//RatioLayout_ratio在R文件中，自动生成的,此方法可拿到比例属性
		//mRatio = typedArray.getFloat(R.styleable.RatioImageView_ratio, -1);
		//回收此对象，提高性能
		typedArray.recycle();
	}

	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int RootWidth = MeasureSpec.getSize(widthMeasureSpec);//获取控件宽度
		int RootWidthMode = MeasureSpec.getMode(widthMeasureSpec);//获取控件宽度模式
		int RootHeight = MeasureSpec.getSize(heightMeasureSpec);//获取控件高度
		int RootHeightMode = MeasureSpec.getMode(heightMeasureSpec);//获取控件高度模式
		
		//在宽度模式为固定模式，高度模式不是固定模式，比例大于0的情况下，去测量控件
		if(RootWidthMode == MeasureSpec.EXACTLY && RootHeightMode != MeasureSpec.EXACTLY && mRatio > 0){
			
			//获取图片宽度   图片宽度 = 控件宽度-左边距-右边距
			int imageWidth = RootWidth-getPaddingLeft()-getPaddingRight();
			//通过图片宽度计算图片高度   图片高度 = 宽度/比例值
			int imageHeight = (int) (imageWidth/mRatio+0.5f);
			//根据图片的高度去获取控件的高度   控件高度 = 图片高度+上边距+下边距
			RootHeight = imageHeight+getPaddingBottom()+getPaddingTop();
			// 根据最新的高度来重新生成heightMeasureSpec(高度模式是确定模式),因为图片的高度确定了，所以控件的高度也确定了
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(RootHeight, MeasureSpec.EXACTLY);
		}
		//按照最新的高度测量控件
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	
	/**
	 * 设置比例
	 * @param ratio
	 */
	public void setRatio(float ratio){
		this.mRatio = ratio;
	}

	

}
