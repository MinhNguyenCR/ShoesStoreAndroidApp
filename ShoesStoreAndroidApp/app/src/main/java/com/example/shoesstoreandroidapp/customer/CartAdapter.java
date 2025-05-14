package com.example.shoesstoreandroidapp.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.shoesstoreandroidapp.customer.API.CartAPI;
import com.example.shoesstoreandroidapp.customer.Model.CartItemModel;
import com.example.shoesstoreandroidapp.customer.Response.BooleanResponse;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartItemModel> mList;
    private OnQuantityChangeListener quantityChangeListener;
    private OnItemDeleteListener itemDeleteListener;
    private CartAPI cartAPI;

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

    public CartAdapter(Context context, List<CartItemModel> mList) {
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
        CartItemModel cartModel = mList.get(position);
        holder.tvName.setText(cartModel.getProductName());
        holder.tvSize.setText(String.valueOf(cartModel.getSize()));
        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText(formatter.format(cartModel.getTotal_price()) );
        holder.quantity.setText(String.valueOf(cartModel.getQuantity()));
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
        private TextView tvSize;
        private TextView tvPrice;
        private ImageView imgvImg;
        private ImageButton btnMore;
        private ImageButton btnLess;
        public TextView quantity;
        public ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.productName);
            tvSize = (TextView) itemView.findViewById(R.id.productSize);
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

                    mList.get(getAdapterPosition()).setQuantity(CurrentQuantity);

                    quantity.setText(String.valueOf(CurrentQuantity));
                    if (quantityChangeListener != null) {
                        quantityChangeListener.onQuantityChanged();
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

                        mList.get(getAdapterPosition()).setQuantity(CurrentQuantity);

                        quantity.setText(String.valueOf(CurrentQuantity));
                        if (quantityChangeListener != null) {
                            quantityChangeListener.onQuantityChanged();
                        }
                    } else
                        Toast.makeText(context, "Không được nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    CartItemModel item = mList.get(position);
                                    Long cartItemId = item.getId();

                                    cartAPI = RetrofitClient.getRetrofit().create(CartAPI.class);
                                    cartAPI.deleteCartItemFromCart(cartItemId).enqueue(new Callback<BooleanResponse>() {
                                        @Override
                                        public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                                            if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1000) {
                                                Toast.makeText(v.getContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                                if (itemDeleteListener != null) {
                                                    // xóa giỏ hàng ra khỏi giao diện
                                                    itemDeleteListener.onItemDeleted(position);
                                                }
                                            } else {
                                                Toast.makeText(v.getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BooleanResponse> call, Throwable t) {
                                            Toast.makeText(v.getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });


        }
    }
}