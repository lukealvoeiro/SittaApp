package mil.android.babysitter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import mil.android.babysitter.R;
import mil.android.babysitter.data.User;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profileImg;
        public TextView tvName;
        public TextView tvPhoneNumber;
        public ImageView ivBio;
        public TextView tvBio;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImg);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            ivBio = itemView.findViewById(R.id.ivBio);
            tvBio = itemView.findViewById(R.id.tvBio);
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
        viewHolder.tvPhoneNumber.append(user.getPhoneNumber());
        viewHolder.tvBio.append(user.getBio());
        viewHolder.ivBio.setImageResource(R.drawable.ic_info);

        Glide.with(context).load(user.getImageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.profileImg) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                viewHolder.profileImg.setImageDrawable(circularBitmapDrawable);
            }
        });

        viewHolder.ivBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tvBio.setVisibility(View.VISIBLE);
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

    public List<User> getUsersList() {
        return usersList;
    }

}
