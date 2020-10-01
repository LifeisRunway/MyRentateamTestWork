package com.imra.mytestwork.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.imra.mytestwork.R;
import com.imra.mytestwork.di.modules.GlideApp;
import com.imra.mytestwork.mvp.models.User;
import com.imra.mytestwork.mvp.presenters.UserPresenter;
import com.imra.mytestwork.mvp.views.UserView;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class UserCardFragment extends MvpAppCompatFragment implements UserView {

    public static final String ARGS_USER = "argsUser";
    public static final String ARGS_POSITION = "argsPosition";

    @InjectPresenter
    UserPresenter mFragmentPresenter;

    @BindView(R.id.avatar)
    ImageView mImageView;

    @BindView(R.id.last_name)
    TextView mLastName;

    @BindView(R.id.first_name)
    TextView mFirstName;

    @BindView(R.id.email)
    TextView mEmail;

    @BindView(R.id.btnClose)
    ImageView mImageButton;

    private Unbinder unbinder;

    private User mUser;
    private int mPosition;

    Disposable disposable;

    private int duration = 500;
    private View mVG;

    @ProvidePresenter
    UserPresenter provideFragmentPresenter() {
        assert getArguments() != null;
        mUser = (User) getArguments().get(ARGS_USER);
        mPosition = (int) getArguments().get(ARGS_POSITION);
        return new UserPresenter(mPosition, mUser);
    }

    public static UserCardFragment getInstance(int position, User user) {
        UserCardFragment userCardFragment = new UserCardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_USER, user);
        args.putInt(ARGS_POSITION, position);
        userCardFragment.setArguments(args);
        return userCardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment, container, false);
        if(getActivity() != null) {
            mVG = (View) container;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        if(mUser != null) {
            if(mUser.getAvatar() != null) {
                if(mImageView.getVisibility() == View.GONE) {
                    mImageView.setVisibility(View.VISIBLE);
                }
                GlideApp
                        .with(view)
                        .asBitmap()
                        .load(mUser.getAvatar())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .fitCenter()
                        .into(mImageView);
            } else {
                mImageView.setVisibility(View.GONE);
            }
        }

        mImageButton.setOnClickListener(v -> {
            YoYo.with(Techniques.FadeOut)
                    .duration(duration)
                    .playOn(mVG);

            disposable = Observable.just(mVG)
                    .subscribeOn(Schedulers.io())
                    .delay(duration, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mVG -> mVG.setVisibility(View.GONE));
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(disposable != null) disposable.dispose();
    }

    @Override
    public void showRepository(int position, User user) {
        mFirstName.setText(user.getFirstName());
        mLastName.setText(user.getLastName());
        mEmail.setText(user.getEmail());
    }

}
