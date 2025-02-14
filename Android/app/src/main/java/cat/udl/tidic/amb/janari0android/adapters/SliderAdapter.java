package cat.udl.tidic.amb.janari0android.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

import cat.udl.tidic.amb.janari0android.ProductSale;
import cat.udl.tidic.amb.janari0android.R;

public class SliderAdapter extends RecyclerView.Adapter <SliderAdapter.SliderViewHolder>{

    private static final String TAG = "bakedbeans";
    private final ArrayList<ProductSale> sliderItems;
    private final Context context;
    OnItemClickListener listener;


    public void setOnItemClickListener(SliderAdapter.OnItemClickListener onItemClickListener) {
        listener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onClickProduct(int position);
    }
    public SliderAdapter(ArrayList<ProductSale> sliderItems, Context context) {
        this.sliderItems = sliderItems;
        this.context = context;
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.row_new_slide_product,
                        parent,
                        false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        // if there is no image, put a default one
        if(sliderItems.get(position).getProduct().getPhotos().size()==0)
            holder.imageView.setImageResource(R.drawable.__2_burger_free_download_png);
        else {
            // Loading image with firebase
            Glide.with(context)
                    .load(sliderItems.get(position).getProduct().getPhotos().get(0))
                    .fitCenter()
                    .into(holder.imageView);
        }
        holder.setName(sliderItems.get(position));
        holder.setPrice(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }


    class SliderViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView product_name, product_price;

        SliderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.name_product);
            product_price = itemView.findViewById(R.id.price_product);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        Log.d(TAG, "onClick: yes");
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClickProduct(position);
                            Log.d(TAG, "onClick: yes");
                        }
                    }
                }
            });
        }
        void setName(ProductSale setDataSliderProducts){
            product_name.setText(setDataSliderProducts.getProduct().getName());
        }
        void setPrice(ProductSale productSale){
            if(!productSale.getPrice().equals("Free"))
                product_price.setText(productSale.getPrice() + "€");
            else
                product_price.setText(productSale.getPrice());
        }
    }
}
