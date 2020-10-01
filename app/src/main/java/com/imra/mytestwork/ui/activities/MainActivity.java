package com.imra.mytestwork.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.FrameLayout;;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imra.mytestwork.R;
import com.imra.mytestwork.ui.fragments.AboutFragment;
import com.imra.mytestwork.ui.fragments.UsersFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import moxy.MvpAppCompatActivity;

public class MainActivity extends MvpAppCompatActivity{

    @BindView(R.id.fl_content)
    FrameLayout mContentFragmentLayout;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    private Unbinder unbinder;

    Disposable disposable;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_users :
                    loadFragment(UsersFragment.getInstance());
                    return true;
                case R.id.navigation_about :
                    loadFragment(AboutFragment.getInstance());
                    return true;
            }
            return false;
        });
        mBottomNavigationView.setSelectedItemId(R.id.navigation_users);
    }

    private void loadFragment (Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fl_content,fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(disposable != null) disposable.dispose();
    }
}
