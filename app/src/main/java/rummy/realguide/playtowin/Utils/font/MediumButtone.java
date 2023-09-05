package rummy.realguide.playtowin.Utils.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class MediumButtone extends AppCompatButton {

    public MediumButtone(Context context) {
        super(context);
        init();
    }

    public MediumButtone(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediumButtone(Context context, AttributeSet attrs, int defStyleAttr) {
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
