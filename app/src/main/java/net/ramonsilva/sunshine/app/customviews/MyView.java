package net.ramonsilva.sunshine.app.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ramonsilva on 29/01/16.
 */
public class MyView extends View {

    public MyView(Context context){
        super(context);
    }

    public MyView(Context context, AttributeSet attr){
        super(context, attr);
    }

    public MyView(Context context, AttributeSet attr, int defaultStyle){
        super(context,attr, defaultStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
