package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;

import java.util.ArrayList;
import java.util.List;

public class cart_page extends AppCompatActivity {
    RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<cartModel> cartList;
    private TextView totalMoney;
    private TextView itemCount;
    private Button goToCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        recyclerView = findViewById(R.id.cartItemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartList = new ArrayList<>();
        // Thêm dữ liệu mẫu vào giỏ hàng
        cartList.add(new cartModel("Nike Air Max", 12000.0, R.drawable.img_2));
        cartList.add(new cartModel("Adidas Ultraboost", 15000.0, R.drawable.img_3));
        cartList.add(new cartModel("Puma RS-X", 9000.0, R.drawable.img_1));
        cartList.add(new cartModel("Nike Air Max", 12000.0, R.drawable.img_2));
        cartList.add(new cartModel("Adidas Ultraboost", 150.0, R.drawable.img_3));
        cartList.add(new cartModel("Puma RS-X", 90.0, R.drawable.img_1));


        totalMoney = findViewById(R.id.totalMoney);
        itemCount = findViewById(R.id.cartItemCount);
        itemCount.setText(String.valueOf(cartList.size()));

        cartAdapter = new CartAdapter(this, cartList);
        cartAdapter.setOnQuantityChangeListener(new CartAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged() {
                CalcTotalMoney();
            }
        });
        cartAdapter.setOnDeletedChangeListener(new CartAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDeleted(int position) {
                cartList.remove(position);
                cartAdapter.notifyItemRemoved(position);
                CalcTotalMoney();
            }
        });

        goToCheckOut = findViewById(R.id.makePaymentButton);
        goToCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CartItem> itemsToSend = new ArrayList<>();
                for(int i=0; i<cartList.size(); i++){
                    cartModel item = cartList.get(i);
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                    int quantity = 1;
                    if (viewHolder instanceof CartAdapter.ViewHolder) {
                        try {
                            quantity = Integer.parseInt(((CartAdapter.ViewHolder) viewHolder).quantity.getText().toString());
                        } catch (NumberFormatException e) {
                            quantity = 1;
                        }
                    }
                    itemsToSend.add(new CartItem(item, quantity));
                }
                double total = Double.valueOf(totalMoney.getText().toString());

                Intent intent = new Intent(cart_page.this, checkout_page.class);
                intent.putExtra("cartItems", itemsToSend);
                intent.putExtra("totalMoney", total);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(cartAdapter);

        CalcTotalMoney();
    }

    public void CalcTotalMoney() {
        double total = 0;
        for (cartModel item : cartList) {
            total += item.getPrice() * getQuantityCartItem(item);
        }
        totalMoney.setText(String.valueOf(total));
        itemCount.setText(String.valueOf(cartList.size()));
    }

    public int getQuantityCartItem(cartModel item) {
        int position = cartList.indexOf(item);
        if (position != -1) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder instanceof CartAdapter.ViewHolder) {
                return Integer.parseInt(((CartAdapter.ViewHolder) viewHolder).quantity.getText().toString());
            }
        }
        return 1;
    }
}