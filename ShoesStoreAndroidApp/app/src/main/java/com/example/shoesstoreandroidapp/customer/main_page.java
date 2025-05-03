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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class main_page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private shoes_item_adapter shoesAdapter;
    private List<shoesModel> shoesList;

    private RecyclerView categoryRecyclerView;
    private categoryAdapter categoryAdapter;

    private ImageButton toCart;
    private ImageButton homeActive;
    private ImageButton imgbtnNoti;
    private SearchView searchView;
    private ImageButton imgbtnUser;
    private ImageButton imgbtnOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        thamChieu();

        // 🟦 Setup RecyclerView cho danh mục
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // 🟦 Gọi API danh mục
        CategoryApi categoryApi = RetrofitClient.getRetrofit().create(CategoryApi.class);
        categoryApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryObjects = response.body();

                    // Tạo danh sách tên
                    List<String> categoryNames = new ArrayList<>();
                    for (Category c : categoryObjects) {
                        categoryNames.add(c.getName());
                    }

                    categoryAdapter = new categoryAdapter(main_page.this, categoryNames);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                } else {
                    Log.e("API_ERROR", "Phản hồi không thành công");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API Category: " + t.getMessage());
            }
        });

        homeActive.setImageResource(R.drawable.home_icon_active);

        // 🟦 Setup RecyclerView cho sản phẩm
        recyclerView = findViewById(R.id.shoes_item_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        shoesList = new ArrayList<>();
        shoesAdapter = new shoes_item_adapter(this, shoesList);
        recyclerView.setAdapter(shoesAdapter);

        // 🟦 Gọi API sản phẩm
        ProductApi apiService = RetrofitClient.getRetrofit().create(ProductApi.class);
        apiService.getProducts().enqueue(new Callback<ApiResponse<List<shoesModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<shoesModel>>> call, Response<ApiResponse<List<shoesModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<shoesModel> productList = response.body().getResult();
                    shoesList.clear();
                    shoesList.addAll(productList);
                    shoesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<shoesModel>>> call, Throwable t) {
                Toast.makeText(main_page.this, "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("API_ERROR", "Lỗi khi gọi API: " + t.getMessage());
            }
        });

        // 🟦 Click sự kiện
        toCart.setOnClickListener(v -> startActivity(new Intent(main_page.this, cart_page.class)));
        imgbtnNoti.setOnClickListener(v -> startActivity(new Intent(main_page.this, notificationActivity.class)));
        imgbtnUser.setOnClickListener(v -> startActivity(new Intent(main_page.this, UserProfileActivity.class)));
        imgbtnOrderHistory.setOnClickListener(v -> startActivity(new Intent(main_page.this, PurchaseHistory.class)));

        // 🟦 Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterShoes(newText);
                return true;
            }
        });
    }

    private void filterShoes(String query) {
        List<shoesModel> filteredList = new ArrayList<>();
        for (shoesModel item : shoesList) {
            if (item.getShoesName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        shoesAdapter.updateList(filteredList);
    }

    private void thamChieu() {
        toCart = findViewById(R.id.CartBtn);
        homeActive = findViewById(R.id.imgbtnHome);
        imgbtnNoti = findViewById(R.id.imgbtnNoti);
        searchView = findViewById(R.id.searchView);
        imgbtnUser = findViewById(R.id.imgbtnUser);
        imgbtnOrderHistory = findViewById(R.id.imgbtnOrderHistory);
    }
}
