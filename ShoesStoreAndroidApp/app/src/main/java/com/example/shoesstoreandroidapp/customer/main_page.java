package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.shoesstoreandroidapp.customer.API.CategoryAPI;
import com.example.shoesstoreandroidapp.customer.API.ProductAPI;
import com.example.shoesstoreandroidapp.customer.Adapter.ListProductAdapter;
import com.example.shoesstoreandroidapp.customer.Model.ProductModel;
import com.example.shoesstoreandroidapp.customer.Response.ProductResponse;

import java.util.ArrayList;
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
    private RecyclerView categoryRecyclerView;
    private categoryAdapter categoryAdapter;
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

        // Setup RecyclerView cho danh mục
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Gọi API danh mục
        CategoryAPI categoryApi = RetrofitClient.getRetrofit().create(CategoryAPI.class);
        categoryApi.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryObjects = response.body();
                    List<String> categoryNames = new ArrayList<>();
                    // Thêm nút All đầu tiên để hiển thị tất cả các san phẩm
                    categoryNames.add("All");
                    for (Category c : categoryObjects) {
                        categoryNames.add(c.getName());
                    }
                    // Tạo adapter có listener khi click
                    categoryAdapter = new categoryAdapter(main_page.this, categoryNames, new OnCategoryClickListener() {
                        @Override
                        public void onCategoryClick(String category) {
                            // Không gọi lại API, dùng danh sách đã có
                            if (category.equals("All")) {
                                listProductAdapter.updateList(listProduct);
                            } else {
                                fetchProductsByCategory(category);
                            }
                        }
                    });

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

        // Hiên thị danh sách các sản phẩm
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


        // Click sự kiện
        toCart.setOnClickListener(v -> startActivity(new Intent(main_page.this, cart_page.class)));
        imgbtnNoti.setOnClickListener(v -> startActivity(new Intent(main_page.this, notificationActivity.class)));
        imgbtnUser.setOnClickListener(v -> startActivity(new Intent(main_page.this, UserProfileActivity.class)));
        imgbtnOrderHistory.setOnClickListener(v -> startActivity(new Intent(main_page.this, PurchaseHistory.class)));

        // Search
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
        List<ProductModel> filteredList = new ArrayList<>();
        for (ProductModel item : listProduct) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        listProductAdapter.updateList(filteredList);
    }

    // Gọi API lọc sản phẩm theo category
    private void fetchProductsByCategory(String category) {
        ProductAPI productAPI = RetrofitClient.getRetrofit().create(ProductAPI.class);
        Call<ProductResponse> call = productAPI.getProductsByCategoryName(category);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductModel> productList = response.body().getResult();
                    // Cập nhật lại sản phẩm trên UI
                    listProductAdapter.updateList(productList);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(main_page.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
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

