package com.mnunez.pagertest.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mnunez.pagertest.R;
import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mnunez on 5/30/17.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> mUsers;
    private Context mContext;
    private NavigationDetailsListener mNavigationDetailsListener;

    public UsersAdapter(Context context) {
        mUsers = AppInfoSingleton.getInstance().getmFilteredUsers();
        mContext = context;
        mNavigationDetailsListener = (NavigationDetailsListener) context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        holder.name.setText(mUsers.get(position).getName());
        holder.role.setText(mUsers.get(position).getRole().getDescription());
        holder.status.setText(mUsers.get(position).getStatus());
        Picasso.with(mContext).load(mUsers.get(position).getAvatar()).resize(60, 60).centerCrop().placeholder(R.drawable.ic_user_placeholder).into(holder.avatar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigationDetailsListener.goToDetails(mUsers.get(holder.getAdapterPosition()), mContext.getString(R.string.user_details_header), false);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView avatar;
        TextView name, role, status;


        UserViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.name);
            role = (TextView) itemView.findViewById(R.id.role);
            status = (TextView) itemView.findViewById(R.id.status);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }

    public interface NavigationDetailsListener {
        void goToDetails(User user, String text, boolean isUserLogged);
    }
}
