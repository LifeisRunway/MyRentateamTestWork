package com.imra.mytestwork.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import moxy.MvpDelegate;

public abstract class MvpBaseRecyclerAdapter<L extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerAdapter.ListViewHolder> {

    private MvpDelegate<? extends MvpBaseRecyclerAdapter> mMvpDelegate;
    private MvpDelegate<?> mParentDelegate;
    private String mChildId;

    public MvpBaseRecyclerAdapter (MvpDelegate<?> parentDelegate, String childId) {
        mParentDelegate = parentDelegate;
        mChildId = childId;

        getMvpDelegate().onCreate();
    }

    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
            mMvpDelegate.setParentDelegate(mParentDelegate, mChildId);

        }
        return mMvpDelegate;
    }

}
