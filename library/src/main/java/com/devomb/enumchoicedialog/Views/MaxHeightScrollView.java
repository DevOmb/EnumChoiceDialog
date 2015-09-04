package com.devomb.enumchoicedialog.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.devomb.enumchoicedialog.R;

/**
 * Created by Ombrax on 4/09/2015.
 */
public class MaxHeightScrollView extends ScrollView {

    //region variable
    private int maxHeight;
    //endregion

    //region constructor
    public MaxHeightScrollView(Context context) {
        super(context);
        init(null, 0);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }
    //endregion

    //region setup
    private void init(AttributeSet set, int defStyleAttr){
        TypedArray attributes = getContext().obtainStyledAttributes(set, R.styleable.MaxHeightScrollView, defStyleAttr, 0);
        maxHeight = attributes.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, 150);
        attributes.recycle();
    }
    //endregion

    //region measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    //endregion
}
