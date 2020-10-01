package com.imra.mytestwork.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.imra.mytestwork.R;
import com.imra.mytestwork.mvp.models.User;
import com.imra.mytestwork.mvp.presenters.MainPresenter;
import com.imra.mytestwork.mvp.presenters.RepositoriesPresenter;
import com.imra.mytestwork.mvp.views.MainInterface;
import com.imra.mytestwork.mvp.views.RepositoriesView;
import com.imra.mytestwork.ui.adapters.RecyclerAdapter;
import com.imra.mytestwork.ui.utils.ItemClickSupport;
import com.imra.mytestwork.ui.views.FrameSwipeRefreshLayout;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class UsersFragment extends MvpAppCompatFragment implements RepositoriesView, MainInterface {

    @InjectPresenter
    MainPresenter mMainPresenter;

    @InjectPresenter
    RepositoriesPresenter mRepPresenter;

    @BindView(R.id.activity_home_swipe_refresh_layout)
    FrameSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.activity_home_progress_bar_repositories)
    ProgressBar mRepositoriesProgressBar;
    @BindView(R.id.activity_home_list_view_repositories)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_home_image_view_no_repositories)
    ImageView mNoRepositoriesImageView;
    @BindView(R.id.activity_home_text_view_no_repositories)
    TextView mNoRepositoriesTextView;
    @BindView(R.id.activity_home_frame_layout_details)
    FrameLayout mDetailsFrameLayout;

    private AlertDialog mErrorDialog;

    private RecyclerAdapter mRecyclerAdapter;

    private Unbinder unbinder;

    @ProvidePresenter
    RepositoriesPresenter provideFragmentPresenter() {
        return new RepositoriesPresenter();
    }

    public static UsersFragment getInstance() {
        return new UsersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mRecyclerAdapter = new RecyclerAdapter(getMvpDelegate());
        mRecyclerAdapter.setHasStableIds(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext()));
        DividerItemDecoration divider = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(getActivity().getResources().getDrawable(R.drawable.drawable_divider_recycler_view)));
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            mMainPresenter.onRepositorySelection(position, mRecyclerAdapter.getItem(position));
        });
        mSwipeRefreshLayout.setListViewChild(mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mRepPresenter.loadRepositories(true, isConnected()));
    }

    private boolean isConnected () {
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = (cm != null) ? cm.getNetworkCapabilities(cm.getActiveNetwork()) : null;
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
        } else {
            NetworkInfo nInfo = (cm != null) ? cm.getActiveNetworkInfo() : null;
            return nInfo != null && nInfo.isConnected();
        }
    }

    @Override
    public void withoutInternetMessage() {
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                "Offline mode. Please check your internet connection",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onFirstAttach() {
        mRepPresenter.loadRepositories(false, isConnected());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStartLoading() {
        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onFinishLoading() {
        mSwipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void showRefreshing() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideRefreshing() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void showListProgress() {
        mRecyclerView.setVisibility(View.GONE);
        mNoRepositoriesImageView.setVisibility(View.GONE);
        mNoRepositoriesTextView.setVisibility(View.GONE);
        mRepositoriesProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListProgress() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mRepositoriesProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        mErrorDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()).getApplicationContext())
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setOnCancelListener(dialog -> mRepPresenter.onErrorCancel())
                .show();
    }

    @Override
    public void hideError() {
        if (mErrorDialog != null && mErrorDialog.isShowing()) {
            mErrorDialog.hide();
        }
    }

    @Override
    public void setRepositories(List<User> users) {
        if(users.isEmpty()) {
            mNoRepositoriesImageView.setVisibility(View.VISIBLE);
            mNoRepositoriesTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mNoRepositoriesImageView.setVisibility(View.GONE);
            mNoRepositoriesTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mRecyclerAdapter.setRepositories(users);
    }

    @Override
    public void addRepositories(List<User> users) {
        if(users.isEmpty()) {
            mNoRepositoriesImageView.setVisibility(View.VISIBLE);
            mNoRepositoriesTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mNoRepositoriesImageView.setVisibility(View.GONE);
            mNoRepositoriesTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mRecyclerAdapter.addRepositories(users);
    }

    @Override
    public void setSelection(int position) {
        mRecyclerAdapter.setSelection(position);
    }

    @Override
    public void showDetailsContainer(int position) {
        mDetailsFrameLayout.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(800)
                .playOn(mDetailsFrameLayout);
    }

    @Override
    public void showDetails(int position, User user) {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home_frame_layout_details, UserCardFragment.getInstance(position, user))
                .commit();
    }
}
