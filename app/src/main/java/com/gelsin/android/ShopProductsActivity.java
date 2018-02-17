package com.gelsin.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gelsin.android.adapter.ProductListAdapter;
import com.gelsin.android.item.ProductItem;
import com.gelsin.android.util.RecyclerTouchListener;
import com.gelsin.android.util.ResultHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShopProductsActivity extends AppCompatActivity {

    private final String TAG = "ShopProductsActivity";

    private Intent intent;
    private RecyclerView productList;
    private ArrayList<ProductItem> products;
    private ArrayList<String> shopping_list;
    private ProductListAdapter productListAdapter;
    private ProgressBar progressBar;
    private TextView productsAmount;
    private float amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_products);

        intent = getIntent();
        getSupportActionBar().setSubtitle(intent.getStringExtra("shop_name"));

        productList = findViewById(R.id.shop_products);
        progressBar = findViewById(R.id.shop_products_progress);
        productsAmount = findViewById(R.id.shop_products_amount);
        productsAmount.setText(getString(R.string.amount) + " " + String.valueOf(amount));

        products = new ArrayList<>();
        shopping_list = new ArrayList<>();
        GelsinActions.getShopProducts(intent.getStringExtra("shop_id"), new ResultHandler() {
            @Override
            public void handle(String result) {

                Gson gson = new Gson();
                products = gson.fromJson(result, new TypeToken<ArrayList<ProductItem>>(){}.getType());

                if(products.size() == 0) {
                    findViewById(R.id.shop_no_products).setVisibility(View.VISIBLE);
                } else {
                    productList.setLayoutManager(new LinearLayoutManager(ShopProductsActivity.this, LinearLayoutManager.VERTICAL, false));
                    productListAdapter = new ProductListAdapter(ShopProductsActivity.this, products);
                    productList.setAdapter(productListAdapter);

                    productList.addOnItemTouchListener(new RecyclerTouchListener(ShopProductsActivity.this, productList, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            shopping_list.add(products.get(position).get_id());
                            amount += products.get(position).getPrice();

                            productsAmount.setText(getString(R.string.amount) + " " + String.valueOf(amount));
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                }

                progressBar.setVisibility(View.GONE);

            }
        });


    }
    
    public void giveOrder(View view) {
        GelsinActions.giveAnOrder(shopping_list, new ResultHandler() {
            @Override
            public void handle(String result) {
                Toast.makeText(ShopProductsActivity.this, R.string.successful_order, Toast.LENGTH_SHORT);
                finish();
            }
        });
    }
}