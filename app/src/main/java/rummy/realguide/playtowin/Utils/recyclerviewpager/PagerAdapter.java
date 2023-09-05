package rummy.realguide.playtowin.Utils.recyclerviewpager;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import rummy.realguide.playtowin.R;

import java.util.List;


public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.ViewHolder> {

    List<PagerModel> list;
    Context context;
    OnItemClickListener mOnItemClickListener;
    int posAttached = 0;

    public PagerAdapter(Context context, List<PagerModel> categorie) {
        this.list = categorie;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_slider, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.probrLoder.setVisibility(View.VISIBLE);
            if (list.get(position).getImg().contains(".gif")) {
                Glide.with(context)
                        .load(list.get(position).getImg())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(holder.imageView);
            } else {
                Picasso.get().load(list.get(position).getImg()).into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.probrLoder.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.probrLoder.setVisibility(View.GONE);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        posAttached = holder.getAdapterPosition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();


    }

    public int getPosAttached() {
        return posAttached;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar probrLoder;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgBanner);
            probrLoder = itemView.findViewById(R.id.probrLoder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnclickItemListener(OnItemClickListener onclickItemListener) {
        this.mOnItemClickListener = onclickItemListener;
    }
}
