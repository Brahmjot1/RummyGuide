package rummy.realguide.playtowin.Utils.recyclerviewpager;

import android.content.Context;

public class PagerModel {

    String img;
    String title;
    Context context;

    public PagerModel(Context context){
        this.context=context;
    }



    public PagerModel(String img, String title, Context context) {
        this.img = img;
        this.title = title;
    }
    public PagerModel(String img, String title) {
        this.img = img;
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
