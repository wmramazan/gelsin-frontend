package com.gelsin.android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gelsin.android.adapter.OrderListAdapter;
import com.gelsin.android.item.OrderItem;
import com.gelsin.android.util.RecyclerTouchListener;
import com.gelsin.android.util.ResultHandler;

import java.util.ArrayList;

/**
 * Created by wmramazan on 17.02.2018.
 */

public class OrdersFragment extends Fragment {

    private View view;
    private Context context;
    private RecyclerView orderList;
    private ArrayList<OrderItem> orders;
    private OrderListAdapter orderListAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_item, container, false);
        context = getContext();

        orderList = view.findViewById(R.id.itemList);
        progressBar = view.findViewById(R.id.itemList_progress);

        orders = new ArrayList<>();
        //orders.add(new OrderItem("test", "test", "test", 12.21, 123.12));
        progressBar.setVisibility(View.GONE);

        GelsinActions.getCustomerOrders(new ResultHandler() {
            @Override
            public void handle(String result) {

            }
        });

        if(orders.size() == 0) {
            TextView noContent_title = view.findViewById(R.id.itemList_noContent_title);
            TextView noContent_text = view.findViewById(R.id.itemList_noContent_text);
            noContent_title.setText(R.string.no_orders_title);
            noContent_text.setText(R.string.no_orders_text);
            view.findViewById(R.id.itemList_noContent).setVisibility(View.VISIBLE);
        } else {
            orderList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            orderListAdapter = new OrderListAdapter(context, orders);
            orderList.setAdapter(orderListAdapter);
        }

        orderList.addOnItemTouchListener(new RecyclerTouchListener(context, orderList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

}
