package com.sudarsono.user.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sudarsono.user.R;
import com.sudarsono.user.dao.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sudarsono
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> items;

    public UserAdapter() {
        this.items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        User item = items.get(position);
        holder.tvUsername.setText(item.getUsername());
        Picasso.get()
                .load(item.getAvatarUrl())
                .placeholder(R.drawable.ic_baseline_account_circle)
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<User> items) {
        int startIndex = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(startIndex, items.size());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;

        private ImageView ivAvatar;

        ViewHolder(View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }

    }
}
