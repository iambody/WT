package io.vtown.WeiTangApp.comment.view;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-3 下午3:15:21
 * 
 */
public class CircleTextView extends View {

	private static final int DEFAULT_FILL_COLOR = Color.RED;// Color.TRANSPARENT;
	private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
	private static final int DEFAULT_TEXT_SIZE = 28;
	private static final int DEFAULT_TEXT_PADDING = 44;
	private String mTextString = "顶部";
	private int mTextColor = DEFAULT_TEXT_COLOR;
	private int mTextSize = DEFAULT_TEXT_SIZE;
	private int mTextPadding = DEFAULT_TEXT_PADDING;
	private int mCircleColor = DEFAULT_FILL_COLOR;
	private Paint mCirclePaint = new Paint();
	private Paint mTextPaint = new Paint();
	private float mDrawableRadius;

	private RectF mDrawableRect = new RectF();

	public CircleTextView(Context context) {
		super(context);
	}

	public CircleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CircleTextView, defStyle, 0);
		mTextColor = typedArray.getColor(R.styleable.CircleTextView_TextColor,
				DEFAULT_TEXT_COLOR);
		mTextSize = typedArray.getDimensionPixelSize(
				R.styleable.CircleTextView_TextSize, DEFAULT_TEXT_SIZE);
		// mTextPadding = typedArray.getDimensionPixelSize(
		// R.styleable.CircleTextView_TextPadding, DEFAULT_TEXT_PADDING);
		// mBorderColor = typedArray.getColor(
		// R.styleable.CircleTextView_BorderColor, DEFAULT_BORDER_COLOR);
		// mBorderWidth = typedArray.getDimensionPixelSize(
		// R.styleable.CircleTextView_BorderWidth, DEFAULT_BORDER_WIDTH);
		mTextPadding=typedArray.getDimensionPixelSize(
				R.styleable.CircleTextView_TextPadding, DEFAULT_TEXT_SIZE);
		mCircleColor = typedArray.getColor(
				R.styleable.CircleTextView_CircleColor, DEFAULT_FILL_COLOR);
		typedArray.recycle();
		init();
	}

	private void init() {
		setup();
	}

	private void setup() {

		if (getWidth() == 0 && getHeight() == 0) {
			return;
		}

		mCirclePaint.setStyle(Paint.Style.FILL);
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);

		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(mTextColor);
		mTextPaint.setAntiAlias(true);

		mDrawableRect.set(0, 0, getWidth(), getHeight());

		mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f,
				mDrawableRect.width() / 2.0f);

		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (StrUtils.isEmpty(mTextString))
			return;
		setup();
		canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f,
				mDrawableRadius, mCirclePaint);
		Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
		if (!StrUtils.isEmpty(mTextString))
			canvas.drawText(mTextString,
					getWidth() / 2 - mTextPaint.measureText(mTextString) / 2,
					getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2,
					mTextPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
		int WidthmeasureModle = MeasureSpec.getMode(widthMeasureSpec);
		int WidthmeasureSize = MeasureSpec.getSize(widthMeasureSpec);
		int HeightmeasureModle = MeasureSpec.getMode(heightMeasureSpec);
		int HeightmeasureSize = MeasureSpec.getSize(heightMeasureSpec);
		if (!StrUtils.isEmpty(mTextString)) {
			int TextMeasureSize = (int) mTextPaint.measureText(mTextString);
			TextMeasureSize += 2 * mTextPadding;
			if (WidthmeasureModle == MeasureSpec.AT_MOST
					&& HeightmeasureModle == MeasureSpec.AT_MOST) {
				if (TextMeasureSize > getMeasuredWidth()
						|| TextMeasureSize > getMeasuredHeight()) {
					setMeasuredDimension(TextMeasureSize, TextMeasureSize);
					return;
				}

			}
		}

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

	public String getmTextString() {
		return mTextString;
	}

	public void setmTextString(String mTextString) {
		this.mTextString = mTextString;
		setup();
	}

	public int getmTextColor() {

		return mTextColor;
	}

	public void setmTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
		setup();
	}

	public int getmTextSize() {
		return mTextSize;
	}

	public void setmTextSize(int mTextSize) {
		this.mTextSize = mTextSize;
		setup();
	}

	public int getmCircleColor() {
		return mCircleColor;
	}

	public void setmCircleColor(int mCircleColor) {
		this.mCircleColor = mCircleColor;
		setup();
	}

}
