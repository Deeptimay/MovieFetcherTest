package com.example.myapplication;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myapplication.Utils.CommonUtils;
import com.example.myapplication.omdbApiRetrofitService.RetrofitLoader;
import com.example.myapplication.omdbApiRetrofitService.SearchService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FirstFragment extends Fragment implements LoaderManager.LoaderCallbacks<SearchService.ResultWithDetail> {

    private static final int LOADER_ID = 1;
    private static final String LOG_TAG = "FirstFragment";
    private Button mSearchButton;
    private EditText mSearchEditText;
    private RecyclerView mMovieListRecyclerView;
    private MovieRecyclerViewAdapter mMovieAdapter;
    private String mMovieTitle;
    private ProgressBar mProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        initView(view);
        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mSearchEditText = (EditText) findViewById(R.id.search_edittext);
//        // set action for pressing search button on keyboard
//        mSearchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_SEARCH
//                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
//                    startSearch();
//                    handled = true;
//                }
//                return handled;
//            }
//        });
//        mSearchButton = (Button) findViewById(R.id.search_button);
//        mMovieListRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startSearch();
//            }
//        });
//        mMovieAdapter = new MainActivity.MovieRecyclerViewAdapter(null);
//        mMovieListRecyclerView.setAdapter(mMovieAdapter);
//        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
//        StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
//        mMovieListRecyclerView.setItemAnimator(null);
//        // Attach the layout manager to the recycler view
//        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
//        getSupportLoaderManager().enableDebugLogging(true);
//        mProgressBar = (ProgressBar) findViewById(R.id.progress_spinner);
//    }

    private void initView(View view) {
        mSearchButton = (Button) view.findViewById(R.id.search_button);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_spinner);
        mSearchEditText = (EditText) view.findViewById(R.id.search_edittext);
        // set action for pressing search button on keyboard
        mSearchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    startSearch();
                    handled = true;
                }
                return handled;
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
        mMovieAdapter = new MovieRecyclerViewAdapter(null);
        mMovieListRecyclerView.setAdapter(mMovieAdapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
        mMovieListRecyclerView.setItemAnimator(null);
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        getActivity().getSupportLoaderManager().enableDebugLogging(true);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("mMovieTitle", mMovieTitle);
//        outState.putInt("progress_visibility", mProgressBar.getVisibility());
//    }

//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        // Always call the superclass so it can restore the view hierarchy
//        super.onRestoreInstanceState(savedInstanceState);
//        int progress_visibility = savedInstanceState.getInt("progress_visibility");
//        // if the progressBar was visible before orientation-change
//        if (progress_visibility == View.VISIBLE) {
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//        // init the loader, so that the onLoadFinished is called
//        mMovieTitle = savedInstanceState.getString("mMovieTitle");
//        if (mMovieTitle != null) {
//            Bundle args = new Bundle();
//            args.putString("movieTitle", mMovieTitle);
//            getSupportLoaderManager().initLoader(LOADER_ID, args, this);
//        }
//    }

    @Override
    public Loader<SearchService.ResultWithDetail> onCreateLoader(int id, Bundle args) {
        return new RetrofitLoader(FirstFragment.this.getActivity(), args.getString("movieTitle"));
    }

    @Override
    public void onLoadFinished(Loader<SearchService.ResultWithDetail> loader, SearchService.ResultWithDetail resultWithDetail) {
        mProgressBar.setVisibility(View.GONE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        if (resultWithDetail.getResponse().equals("True")) {
            mMovieAdapter.swapData(resultWithDetail.getMovieDetailList());
        } else {
            Snackbar.make(mMovieListRecyclerView,
                    getResources().getString(R.string.snackbar_title_not_found), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<SearchService.ResultWithDetail> loader) {
        mMovieAdapter.swapData(null);
    }

    private void startSearch() {
        if (CommonUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
            CommonUtils.hideSoftKeyboard(getActivity());
            String movieTitle = mSearchEditText.getText().toString().trim();
            if (!movieTitle.isEmpty()) {
                Bundle args = new Bundle();
                args.putString("movieTitle", movieTitle);
                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                mMovieTitle = movieTitle;
                mProgressBar.setVisibility(View.VISIBLE);
                mMovieListRecyclerView.setVisibility(View.GONE);
            } else
                Snackbar.make(mMovieListRecyclerView,
                        getResources().getString(R.string.snackbar_title_empty),
                        Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(mMovieListRecyclerView,
                    getResources().getString(R.string.network_not_available),
                    Snackbar.LENGTH_LONG).show();
        }
    }


//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
//    }

    public class MovieRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

        private List<SearchService.Detail> mValues;

        public MovieRecyclerViewAdapter(List<SearchService.Detail> items) {
            mValues = items;
        }


        @Override
        public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie, parent, false);
            return new MovieRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MovieRecyclerViewAdapter.ViewHolder holder, final int position) {

            final SearchService.Detail detail = mValues.get(position);
            final String title = detail.Title;
            final String imdbId = detail.imdbID;
            final String director = detail.Director;
            final String year = detail.Year;
            holder.mDirectorView.setText(director);
            holder.mTitleView.setText(title);
            holder.mYearView.setText(year);

            final String imageUrl;
            if (!detail.Poster.equals("N/A")) {
                imageUrl = detail.Poster;
            } else {
                // default image if there is no poster available
                imageUrl = getResources().getString(R.string.default_poster);
            }
            holder.mThumbImageView.layout(0, 0, 0, 0); // invalidate the width so that glide wont use that dimension
            Glide.with(FirstFragment.this).load(imageUrl).into(holder.mThumbImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(SecondFragment.MOVIE_DETAIL, detail);
                    bundle.putString(SecondFragment.IMAGE_URL, imageUrl);
                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);

//                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                    // Pass data object in the bundle and populate details activity.
//                    intent.putExtra(DetailActivity.MOVIE_DETAIL, detail);
//                    intent.putExtra(DetailActivity.IMAGE_URL, imageUrl);
//
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation(MainActivity.this,
//                                    holder.mThumbImageView, "poster");
//                    startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mValues == null) {
                return 0;
            }
            return mValues.size();
        }

        @Override
        public void onViewRecycled(MovieRecyclerViewAdapter.ViewHolder holder) {
            super.onViewRecycled(holder);
            Glide.with(holder.mThumbImageView.getContext()).clear(holder.mThumbImageView);
        }

        public void swapData(List<SearchService.Detail> items) {
            if (items != null) {
                mValues = items;
                notifyDataSetChanged();

            } else {
                mValues = null;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mYearView;
            public final TextView mDirectorView;
            public final ImageView mThumbImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.movie_title);
                mYearView = (TextView) view.findViewById(R.id.movie_year);
                mThumbImageView = (ImageView) view.findViewById(R.id.thumbnail);
                mDirectorView = (TextView) view.findViewById(R.id.movie_director);
            }
        }
    }
}