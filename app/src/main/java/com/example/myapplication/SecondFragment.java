package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.omdbApiRetrofitService.SearchService;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class SecondFragment extends Fragment {

    public static final String MOVIE_DETAIL = "movie_detail";
    public static final String IMAGE_URL = "image_url";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_second, container, false);
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        final SearchService.Detail detail = getArguments().getParcelable(MOVIE_DETAIL);
        final String imageUrl =  getArguments().getString(IMAGE_URL);
        Glide.with(this).load(imageUrl).into( (ImageView) view.findViewById(R.id.main_backdrop));

        // set title for the appbar
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(detail.Title);

        ((TextView) view.findViewById(R.id.grid_title)).setText(detail.Title);
        ((TextView) view.findViewById(R.id.grid_writers)).setText(detail.Writer);
        ((TextView) view.findViewById(R.id.grid_actors)).setText(detail.Actors);
        ((TextView) view.findViewById(R.id.grid_director)).setText(detail.Director);
        ((TextView) view.findViewById(R.id.grid_genre)).setText(detail.Genre);
        ((TextView) view.findViewById(R.id.grid_released)).setText(detail.Released);
        ((TextView) view.findViewById(R.id.grid_plot)).setText(detail.Plot);
        ((TextView) view.findViewById(R.id.grid_runtime)).setText(detail.Runtime);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }
}