package com.moringaschool.android_ip_1.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moringaschool.android_ip_1.Adapter.HomeRecyclerAdapter;
import com.moringaschool.android_ip_1.Models.SearchEndPoint.SearchApiResponse;
import com.moringaschool.android_ip_1.Network.OnSearchApiListener;
import com.moringaschool.android_ip_1.Network.RequestManager;
import com.moringaschool.android_ip_1.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieSearchActivity extends AppCompatActivity implements OnMovieClickListener {

    @BindView(R.id.tvProfileGreeting) TextView tvProfileGreeting;
    @BindView(R.id.svSearchView) SearchView svSearchView;
    @BindView(R.id.rvRecyclerView) RecyclerView rvRecyclerView;
    @BindView(R.id.cvResultsDisplay) CardView cvResultsDisplay;

    HomeRecyclerAdapter adapter;

    RequestManager manager;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String inputUserName = intent.getStringExtra("inputUserName");

        tvProfileGreeting.setText("Welcome back, Kiongoss "+inputUserName);

        dialog=new ProgressDialog(this);

        manager=new RequestManager(this); //Network

        svSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {  //set listener to the search field.

            @Override
            public boolean onQueryTextSubmit(String query) { //pass this query to Request manager

                Toast.makeText(MovieSearchActivity.this, "What did the Doctor say to the midget in the waiting room? ", Toast.LENGTH_LONG).show();

                dialog.setTitle("Wait for it..."); //show this while waiting. Be sure to dismiss() after
                dialog.show();

                manager.searchMovies(listener,query);  //call api, and search for this "query"

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

    }

    private final OnSearchApiListener listener=new OnSearchApiListener() { //listen out for network results from api

        @Override
        public void onResponse(SearchApiResponse response) {

            dialog.dismiss();

            if(response==null){ //If there are no results.

                Toast.makeText(MovieSearchActivity.this, "You caught us. No data available", Toast.LENGTH_SHORT).show();

                return; //to get out of this.

            }

            showResult(response);
            showRecyclerView();

        }

        @Override
        public void onError(String message) {

            dialog.dismiss();
            Toast.makeText(MovieSearchActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();

        }

    };

    private void showResult(SearchApiResponse response) {

        rvRecyclerView.setHasFixedSize(true); //Do not adapt size to accommodate different size results
        rvRecyclerView.setLayoutManager(new GridLayoutManager(MovieSearchActivity.this, 1)); //show 3 items per row
        adapter=new HomeRecyclerAdapter(this, response.getTitles(), this);
        rvRecyclerView.setAdapter(adapter);
        
    }

    @Override
    public void onMovieClicked(String id) {

        startActivity(new Intent(MovieSearchActivity.this, DetailsActivity.class).putExtra("movie_id", id)); //pass id to the DetailsActivity

    }

    private void showRecyclerView() {
        cvResultsDisplay.setVisibility(View.VISIBLE);
    }

}