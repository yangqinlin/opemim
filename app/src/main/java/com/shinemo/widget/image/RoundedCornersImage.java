package com.shinemo.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shinemo.imdemo.R;

public class RoundedCornersImage extends SimpleDraweeView {

	private int mCornerRadius;

	private static final Xfermode MASK_XFERMODE = new PorterDuffXfermode(
			PorterDuff.Mode.DST_IN);
//	private Bitmap mMaskBitmap;
	private Paint mPaint;
	private Canvas mCanvas;
	private Paint mImagePaint;

	public RoundedCornersImage(Context context) {
		this(context, null);
	}

	public RoundedCornersImage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundedCornersImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.RoundedCornersImage);
		mCornerRadius = array.getDimensionPixelSize(0, 0);
		array.recycle();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setFilterBitmap(false);
		mPaint.setXfermode(MASK_XFERMODE);
		mPaint.setAntiAlias(true);  
		mCanvas = new Canvas();
		mImagePaint = new Paint();
		mImagePaint.setColor(Color.BLACK);
	}

	public Bitmap createMask() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(bitmap);
		if (mCornerRadius == 0) {
			mCanvas.drawRoundRect(
					new RectF(0.0F, 0.0F, getWidth(), getHeight()),
					getWidth() / 2, getHeight() / 2, mImagePaint);
		} else {
			mCanvas.drawRoundRect(
					new RectF(0.0F, 0.0F, getWidth(), getHeight()),
					mCornerRadius, mCornerRadius, mImagePaint);
		}
		return bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			Drawable drawable = getDrawable();
			if (drawable != null) {
				int saveCount = canvas.saveLayer(0.0F, 0.0F, getWidth(),
						getHeight(), null, 31);
				drawable.setBounds(0, 0, getWidth(), getHeight());
				drawable.draw(canvas);
//				if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
//					mMaskBitmap = createMask();
//				}
//				if (mMaskBitmap != null) {
					canvas.drawBitmap(createMask(), 0.0F, 0.0F, mPaint);
//				}
				canvas.restoreToCount(saveCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
