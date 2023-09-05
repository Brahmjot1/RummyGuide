package rummy.realguide.playtowin.Utils.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MediumTextView extends AppCompatTextView {

    public MediumTextView(Context context) {
        super(context);
        init();
    }

    public MediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        if (isInEditMode()) {

        } else {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Medium.ttf");
            setTypeface(tf);
        }
    }
}
