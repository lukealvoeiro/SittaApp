package mil.android.babysitter.view;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import mil.android.babysitter.MainActivity;
import mil.android.babysitter.R;
import mil.android.babysitter.data.User;

import static mil.android.babysitter.MainActivity.CURR_USER;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameTxt)
    private TextView nameTxt;

    private User mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;


    public TinderCard(Context context, User profile, SwipePlaceHolderView swipeView) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameTxt.setText(mProfile.getName());
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("REJECTED", CURR_USER.getUid());
        Log.d("REJECTED", mProfile.getName());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("user").child(CURR_USER.getUid()).child("match").child(mProfile.getUid()).setValue(false);

        CURR_USER.addMatchedUsers(mProfile.getUid(), false);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("ACCEPTED", CURR_USER.getUid());
        Log.d("ACCEPTED", mProfile.getName());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("user").child(CURR_USER.getUid()).child("match").child(mProfile.getUid()).setValue(true);

        Map<String, Boolean> checkerMap = mProfile.getMatchedUsers();
        Boolean matchQuestion = checkerMap.get(CURR_USER.getUid());
        if(matchQuestion){
            ref.child("match").child(CURR_USER.getUid()).child(mProfile.getUid()).setValue(mProfile);
            ref.child("match").child(mProfile.getUid()).child(CURR_USER.getUid()).setValue(CURR_USER);
        }

        CURR_USER.addMatchedUsers(mProfile.getUid(), true);
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}