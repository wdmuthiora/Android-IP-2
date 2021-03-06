package com.moringaschool.android_ip_1.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moringaschool.android_ip_1.Models.FilmEndPoint.DetailApiResponse;
import com.moringaschool.android_ip_1.Network.OnDetailsApiListener;
import com.moringaschool.android_ip_1.Network.RequestManager;
import com.moringaschool.android_ip_1.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    RequestManager manager;
    ProgressDialog dialog; //android popup thingy

    @BindView (R.id.tvMovieName) TextView tvMovieName;
    @BindView (R.id.tvMovieReleaseYear) TextView tvMovieReleaseYear;
    @BindView (R.id.tvMovieRating) TextView tvMovieRating;
    @BindView (R.id.tvMovieDescription) TextView tvMovieDescription;
    @BindView(R.id.ivMoviePoster) ImageView ivMoviePoster;
    @BindView(R.id.tvMovieLength) TextView tvMovieLength;
    @BindView(R.id.btnWatchTrailer) Button btnWatchTrailer;
    @BindView(R.id.btnCast) Button btnCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Toast.makeText(DetailsActivity.this, "You're going to have to be a little patient", Toast.LENGTH_SHORT).show();

        manager=new RequestManager(this); //Call the api

        String movie_id = getIntent().getStringExtra("movie_id"); //retrieve movie id from the MovieSearchActivity. NB. Call api after fetching movie_id.

        manager.searchMovieDetails(listener, movie_id); //use the received id

        dialog = new ProgressDialog(this);
        dialog.setTitle("I'm on it..."); //show this while loading. Be sure to dismiss() after.
        dialog.show();

    }

    private OnDetailsApiListener listener = new OnDetailsApiListener() {

        @Override
        public void onResponse(DetailApiResponse response) {

            dialog.dismiss();

            if(response.equals(null)){

                Toast.makeText(DetailsActivity.this, "You caught us. No data available.", Toast.LENGTH_SHORT).show();
                return; // to get out of this condition.
            }

            showResults(response);
        }

        @Override
        public void onError(String message) {

            dialog.dismiss();
            Toast.makeText(DetailsActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();

        }

    };

    private void showResults(DetailApiResponse response) { //map incoming data from api to elements.

        tvMovieName.setText(response.getTitle());
        tvMovieReleaseYear.setText("Year: "+response.getYear());
        tvMovieRating.setText("Rating: "+response.getRating());
        tvMovieLength.setText("Length: " +response.getLength());
        Picasso.get().load(response.getPoster()).into(ivMoviePoster); //Add try-catch for when the api does not return a valid poster url
        tvMovieDescription.setText(response.getPlot());
        btnWatchTrailer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(response.getTrailer().getLink().toString()));
                startActivity(webIntent);

            }

        });

    }

}