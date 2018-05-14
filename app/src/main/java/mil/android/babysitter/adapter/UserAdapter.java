package mil.android.babysitter.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import mil.android.babysitter.MainActivity;
import mil.android.babysitter.R;
import mil.android.babysitter.UserChatActivity;
import mil.android.babysitter.data.User;
import mil.android.babysitter.touch.ProfileTouchHelperAdapter;

/**
 * Created by madina on 5/13/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements ProfileTouchHelperAdapter {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImg;
        public TextView tvName;
        public TextView tvBiography;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.profileImg);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvBiography = (TextView) itemView.findViewById(R.id.tvBiography);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }

    private List<User> usersList;
    private Context context;
    private int lastPosition = -1;

    public UserAdapter(List<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final User user = usersList.get(position);

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvBiography.setText(user.getBio());
        //DO NOT FORGET TO SET PROFILE IMAGE!

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemDismiss(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserChatActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onItemDismiss(int position) {
        final User userToDelete = usersList.get(position);
        usersList.remove(userToDelete);
        notifyItemRemoved(position);
        //MainActivity.CURR_USER.removeAcceptedUser(userToDelete);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(usersList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(usersList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
}
