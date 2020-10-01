package com.imra.mytestwork.ui.views;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Date: 04.08.2019
 * Time: 14:33
 *
 * @author IMRA027
 */
public class FrameSwipeRefreshLayout extends SwipeRefreshLayout {

    private RecyclerView mListViewChild;

    public FrameSwipeRefreshLayout(Context context) {
        super(context);
    }

    public FrameSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListViewChild(RecyclerView listViewChild) {
        mListViewChild = listViewChild;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mListViewChild != null && mListViewChild.getVisibility() == VISIBLE) {
            return mListViewChild.canScrollVertically(-1);
        }
        return super.canChildScrollUp();
    }
}