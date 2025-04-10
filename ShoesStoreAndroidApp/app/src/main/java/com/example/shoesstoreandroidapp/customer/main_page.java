package com.example.shoesstoreandroidapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesstoreandroidapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main_page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private shoes_item_adapter shoesAdapter;
    private List<shoesModel> shoesList;
    private ImageButton toCart;
    private ImageButton homeActive;
    private ImageButton imgbtnNoti;
    private SearchView searchView;

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


        //shoes items
        recyclerView = findViewById(R.id.shoes_item_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shoesList = new ArrayList<>();
        // Add sample data
        shoesList.add(new shoesModel("Nike k-swiss", 185.0, 4.4, R.drawable.img));
        shoesList.add(new shoesModel("New balance", 185.0, 4.4, R.drawable.img_1));
        shoesList.add(new shoesModel("Puma Caven", 185.0, 4.4, R.drawable.img_2));
        shoesList.add(new shoesModel("Nike Sneaker", 185.0, 4.4, R.drawable.img_3));
        shoesList.add(new shoesModel("Nike k-swiss", 185.0, 4.4, R.drawable.img));
        shoesList.add(new shoesModel("New balance", 185.0, 4.4, R.drawable.img_1));
        shoesList.add(new shoesModel("Puma Caven", 185.0, 4.4, R.drawable.img_2));
        shoesList.add(new shoesModel("Nike Sneaker", 185.0, 4.4, R.drawable.img_3));
        shoesList.add(new shoesModel("Nike k-swiss", 185.0, 4.4, R.drawable.img));
        shoesList.add(new shoesModel("New balance", 185.0, 4.4, R.drawable.img_1));
        shoesList.add(new shoesModel("Puma Caven", 185.0, 4.4, R.drawable.img_2));
        shoesList.add(new shoesModel("Nike Sneaker", 185.0, 4.4, R.drawable.img_3));

        shoesAdapter = new shoes_item_adapter(this, shoesList);
        recyclerView.setAdapter(shoesAdapter);


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
    private void filterShoes(String query) {
        List<shoesModel> filteredList = new ArrayList<>();
        for (shoesModel item : shoesList) {
            if (item.getShoesName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        shoesAdapter.updateList(filteredList);
    }
    public void thamChieu(){

        toCart = findViewById(R.id.CartBtn);
        homeActive = findViewById(R.id.imgbtnHome);
        imgbtnNoti = findViewById(R.id.imgbtnNoti);
        searchView = findViewById(R.id.searchView);
    }
}