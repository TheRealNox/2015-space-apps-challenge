package nz.co.spaceapp.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Roberta on 10/04/2015.
 */
public class DropShadowImageView extends ImageView {

        private Rect mRect;
        private Paint mPaint;

        public DropShadowImageView(Context context)
        {
            super(context);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setShadowLayer(2f, 1f, 1f, Color.BLACK);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            Rect r = mRect;
            Paint paint = mPaint;

            canvas.drawRect(r, paint);
            super.onDraw(canvas);
        }

        @Override
        protected void onMeasure(int w, int h)
        {
            super.onMeasure(w,h);
            int mH, mW;
            mW = getSuggestedMinimumWidth() < getMeasuredWidth()? getMeasuredWidth() : getSuggestedMinimumWidth();
            mH = getSuggestedMinimumHeight() < getMeasuredHeight()? getMeasuredHeight() : getSuggestedMinimumHeight();
            setMeasuredDimension(mW + 5, mH + 5);
        }
}
