package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;
import com.example.shoesstoreandroidapp.UserProfileActivity;
import com.example.shoesstoreandroidapp.customer.API.ProductAPI;
import com.example.shoesstoreandroidapp.customer.Adapter.ListProductAdapter;
import com.example.shoesstoreandroidapp.customer.Model.ProductModel;
import com.example.shoesstoreandroidapp.customer.Response.ProductResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class main_page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private shoes_item_adapter shoesAdapter;
    private ListProductAdapter listProductAdapter;
    private List<ProductModel> listProduct;
    private List<shoesModel> shoesList;
    private ImageButton toCart;
    private ImageButton homeActive;
    private ImageButton imgbtnNoti;
    private SearchView searchView;
    private ImageButton imgbtnUser;
    private ImageButton imgbtnOrderHistory;

    ProductAPI productAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        thamChieu();
        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        List<String> categories = Arrays.asList("All", "Nike", "Adidas", "New Balance", "Puma", "Reebok");

        categoryAdapter adapter = new categoryAdapter(this, categories);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(adapter);

        homeActive.setImageResource(R.drawable.home_icon_active);


        // Hiên thị danh sách các sản phẩm//shoes items
        recyclerView = findViewById(R.id.shoes_item_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        listProduct = new ArrayList<>();

        productAPI = RetrofitClient.getRetrofit().create(ProductAPI.class);
        productAPI.getProducts().enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listProduct.clear();
                        listProduct.addAll(response.body().getResult());
                        listProductAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {
                    Toast.makeText(main_page.this, "Load failed", Toast.LENGTH_SHORT).show();
                }
            });


        listProductAdapter = new ListProductAdapter(this, listProduct);
        recyclerView.setAdapter(listProductAdapter);


        toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, cart_page.class);
                startActivity(intent);
            }
        });
        imgbtnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, notificationActivity.class);
                startActivity(intent);
            }
        });
        imgbtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        imgbtnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page.this, PurchaseHistory.class);
                startActivity(intent);
            }
        });


        //search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission (optional)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterShoes(newText);
                return true;
            }
        });
    }
//    private void filterShoes(String query) {
//        List<shoesModel> filteredList = new ArrayList<>();
//        for (shoesModel item : shoesList) {
//            if (item.getShoesName().toLowerCase().contains(query.toLowerCase())) {
//                filteredList.add(item);
//            }
//        }
//        shoesAdapter.updateList(filteredList);
//    }

    private void filterShoes(String query) {
        List<ProductModel> filteredList = new ArrayList<>();
        for (ProductModel item : listProduct) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        listProductAdapter.updateList(filteredList);
    }
    public void thamChieu(){

        toCart = findViewById(R.id.CartBtn);
        homeActive = findViewById(R.id.imgbtnHome);
        imgbtnNoti = findViewById(R.id.imgbtnNoti);
        searchView = findViewById(R.id.searchView);
        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);
    }


}