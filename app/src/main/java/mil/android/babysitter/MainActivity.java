package mil.android.babysitter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mil.android.babysitter.adapter.CardViewAdapter;
import mil.android.babysitter.adapter.UserAdapter;
import mil.android.babysitter.data.User;
import mil.android.babysitter.view.TinderCard;

public class MainActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    public static User CURR_USER;

    private List<User> listUsers;
    private UserAdapter userAdapter;

    private String uidCurr;
    private boolean userFound;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Populating", "We must get here");

        listUsers = new ArrayList<User>();
        mAuth = FirebaseAuth.getInstance();

        uidCurr = mAuth.getCurrentUser().getUid();
        userFound = false;

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        populateListUsers();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


        findViewById(R.id.matchesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AcceptedUsersActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
    }

    public void populateListUsers() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User userToAdd = dataSnapshot.getValue(User.class);

                Log.d("User-properties", userToAdd.getEmail());
                Log.d("User-properties", String.valueOf(userToAdd.isBabysitter()));
                Log.d("User-properties", "");

                if (!userFound) {
                    if (!userToAdd.getUid().equals(uidCurr)) {
                        listUsers.add(userToAdd);
                        mSwipeView.addView(new TinderCard(mContext, userToAdd, mSwipeView));
                    } else {
                        CURR_USER = userToAdd;
                        userFound = true;
                    }
                } else {
                    if (!CURR_USER.getUid().equals(uidCurr)) {
                        if (CURR_USER.isBabysitter() != userToAdd.isBabysitter()) {
                            listUsers.add(userToAdd);
                            mSwipeView.addView(new TinderCard(mContext, userToAdd, mSwipeView));
                        }
                    } else {
                        CURR_USER = userToAdd;
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
