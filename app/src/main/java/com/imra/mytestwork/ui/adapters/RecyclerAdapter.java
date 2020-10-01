package com.imra.mytestwork.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.imra.mytestwork.R;
import com.imra.mytestwork.mvp.models.User;
import com.imra.mytestwork.mvp.presenters.UserPresenter;
import com.imra.mytestwork.mvp.views.UserView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import moxy.MvpDelegate;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class RecyclerAdapter extends MvpBaseRecyclerAdapter<RecyclerAdapter.ListViewHolder> {

    private static final int REPOSITORY_VIEW_TYPE = 0;
    private static final int PROGRESS_VIEW_TYPE = 1;
    private int mSelection = -1;
    private List<User> mUsers;

    public RecyclerAdapter(MvpDelegate<?> parentDelegate) {
        super(parentDelegate, String.valueOf(0));
        setHasStableIds(true);
        mUsers = new ArrayList<>();
    }

    public void setRepositories(@NonNull List<User> users) {
        mUsers = new ArrayList<>(users);
        notifyDataSetChanged();
    }

    public void addRepositories (@NonNull List<User> users) {
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public void setSelection(int selection) {
        mSelection = selection;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == mUsers.size() ? PROGRESS_VIEW_TYPE : REPOSITORY_VIEW_TYPE;
    }

    public User getItem (int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        if(!mUsers.isEmpty()) {
            User a = mUsers.get(position);
            holder.bind(a,position);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ListViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements UserView {

        @InjectPresenter
        UserPresenter mUserPresenter;

        private User mUser;
        private int mPosition;

        @BindView(R.id.first_name_item)
        TextView firstName;

        @BindView(R.id.last_name_item)
        TextView lastName;

        private MvpDelegate mMvpDelegate;

        @ProvidePresenter
        UserPresenter provideRepositoryPresenter() {
            return new UserPresenter(mPosition, mUser);
        }

        private View view;

        ListViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);
        }

        void bind (User user, int position) {
            mPosition = position;

            if (getMvpDelegate() != null) {
                getMvpDelegate().onSaveInstanceState();
                getMvpDelegate().onDetach();
                getMvpDelegate().onDestroyView();
                mMvpDelegate = null;
            }
            mUser = user;
            getMvpDelegate().onCreate();
            getMvpDelegate().onAttach();
        }

        @Override
        public void showRepository(int position, User user) {
            firstName.setText(mUser.getFirstName());
            lastName.setText(mUser.getLastName());
        }

        MvpDelegate getMvpDelegate() {
            if (mUser == null) {
                return null;
            }

            if (mMvpDelegate == null) {
                mMvpDelegate = new MvpDelegate<>(this);
                mMvpDelegate.setParentDelegate(RecyclerAdapter.this.getMvpDelegate(), String.valueOf(mPosition));
            }
            return mMvpDelegate;
        }
    }
}
