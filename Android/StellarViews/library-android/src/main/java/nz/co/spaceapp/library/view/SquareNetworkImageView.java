package nz.co.spaceapp.library.view;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Roberta on 11/04/2015.
 */
public class SquareNetworkImageView extends NetworkImageView {
    public SquareNetworkImageView(Context context) {
        super(context);
    }

    public SquareNetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
