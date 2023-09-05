package rummy.realguide.playtowin.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import rummy.realguide.playtowin.MainActivity;
import rummy.realguide.playtowin.Model.CategoryModel;
import rummy.realguide.playtowin.R;
import rummy.realguide.playtowin.Utils.Utility;

import java.util.ArrayList;

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.MyViewHolder> {
    Activity activity;
    ArrayList<CategoryModel> homeData;

    public HomeDataAdapter(Activity activity, ArrayList<CategoryModel> homeData) {
        this.activity=activity;
        this.homeData=homeData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_data,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.probrLoder.setVisibility(View.VISIBLE);

            if (homeData.get(position).getImage()!=null)
            {
                Glide.with(activity)
                        .load(homeData.get(position).getImage())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(holder.ivBanner);
            }

            if (homeData.get(position).getTitle()!=null)
            {
                holder.txtBanner.setVisibility(View.VISIBLE);
                holder.txtBanner.setText(homeData.get(position).getTitle());
            }
            else
            {
                holder.txtBanner.setVisibility(View.GONE);
            }

            holder.cardBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.Redirect(activity,homeData.get(position));
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return homeData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardBanner;
        ProgressBar probrLoder;
        ImageView ivBanner;
        TextView txtBanner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBanner=itemView.findViewById(R.id.cardBanner);
            probrLoder=itemView.findViewById(R.id.probrLoder);
            ivBanner=itemView.findViewById(R.id.ivBanner);
            txtBanner=itemView.findViewById(R.id.txtBanner);
        }
    }
}
