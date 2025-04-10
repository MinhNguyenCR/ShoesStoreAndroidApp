package com.example.shoesstoreandroidapp.customer;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesstoreandroidapp.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<cartModel> mList;
    private OnQuantityChangeListener quantityChangeListener;
    private OnItemDeleteListener itemDeleteListener;

    public void setOnDeletedChangeListener(OnItemDeleteListener itemDeleteListener) {
        this.itemDeleteListener = itemDeleteListener;
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public interface OnItemDeleteListener{
        void onItemDeleted(int position);
    }


    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    public CartAdapter(Context context, List<cartModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cartModel cartModel = mList.get(position);
        holder.tvName.setText(cartModel.getName());
        holder.tvPrice.setText(String.valueOf(cartModel.getPrice()));
        Glide.with(context).load(cartModel.getImage()).into(holder.imgvImg);
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvPrice;
        private ImageView imgvImg;
        private ImageButton btnMore;
        private ImageButton btnLess;
        public TextView quantity;
        public ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.productName);
            tvPrice = (TextView) itemView.findViewById(R.id.productPrice);
            quantity = (TextView) itemView.findViewById(R.id.productQuantity);
            imgvImg = (ImageView) itemView.findViewById(R.id.productImage);
            btnMore = (ImageButton) itemView.findViewById(R.id.increaseQuantityButton);
            btnLess = (ImageButton) itemView.findViewById(R.id.decreaseQuantityButton);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String quantityStr = quantity.getText().toString();
                    int CurrentQuantity = Integer.parseInt(quantityStr);
                    CurrentQuantity++;
                    quantity.setText(String.valueOf(CurrentQuantity));
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChanged(); // Thông báo thay đổi
                    }
                }
            });
            btnLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String quantityStr = quantity.getText().toString();
                    int CurrentQuantity = Integer.parseInt(quantityStr);
                    if (CurrentQuantity > 1) {
                        CurrentQuantity--;
                        quantity.setText(String.valueOf(CurrentQuantity));
                        if (quantityChangeListener != null) {
                            quantityChangeListener.onQuantityChanged(); // Thông báo thay đổi
                        }
                    } else
                        Toast.makeText(context, "Không được nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemDeleteListener != null){
                        itemDeleteListener.onItemDeleted(getAdapterPosition());
                    }
                }
            });
        }
    }
}