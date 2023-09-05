package rummy.realguide.playtowin.Utils.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class RegularEditText extends AppCompatEditText {
    public RegularEditText(Context context) {
        super(context);
        init();
    }

    public RegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            try {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/Regular.ttf"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
