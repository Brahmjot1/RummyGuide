package rummy.realguide.playtowin.Utils.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class SemiBoldButton extends AppCompatButton {
    public SemiBoldButton(Context context) {
        super(context);
        init();
    }

    public SemiBoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SemiBoldButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            try {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/Medium.ttf"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
