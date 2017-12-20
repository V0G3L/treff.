package org.pispeb.treff_client.lists;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Lukas on 12/19/2017.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListViewHolder> {
    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendListViewHolder(null);
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
