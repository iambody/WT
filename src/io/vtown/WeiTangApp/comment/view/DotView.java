package io.vtown.WeiTangApp.comment.view;

import io.vtown.WeiTangApp.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-17 上午11:29:21 自定义的小圆点
 * 
 */
public class DotView extends View {
	private static final int DEFAULT_DOT_COLOR = Color.GREEN;
	private static final int DEFAULT_DOT_SIZE = 10;

	/**
	 * 默认圆点的半径
	 */
	private float DotRadius = DEFAULT_DOT_SIZE;

	/**
	 * 默认圆点的颜色
	 */
	private int DotColor = DEFAULT_DOT_COLOR;
	/**
	 * 画布
	 */
	private RectF mDrawableRect = new RectF();

	public DotView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);

	}

	public DotView(Context arg0) {
		super(arg0);

	}

	public DotView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
				R.styleable.DotView, defStyle, 0);
		DotRadius = typedArray.getDimensionPixelSize(
				R.styleable.DotView_Dotradius, DEFAULT_DOT_SIZE);
		DotColor = typedArray.getColor(R.styleable.DotView_Dotcolor,
				DEFAULT_DOT_COLOR);
		typedArray.recycle();
		IBase();
	}

	private void IBase() {

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		// mDrawableRect.set(0, 0, DotRadius * 2, DotRadius * 2);
		//
		// DotRadius = Math.min(mDrawableRect.height() / 2.0f,
		// mDrawableRect.width() / 2.0f);
		Paint mPaint = new Paint();
		// 设置画笔颜色
		mPaint.setColor(DotColor);
		mPaint.setAntiAlias(true);
		// 设置填充
		mPaint.setStyle(Style.FILL);
		canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2,
				mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}

	public void SetDotColor(int MyColr) {
		this.DotColor = MyColr;
		invalidate();
	}
}
