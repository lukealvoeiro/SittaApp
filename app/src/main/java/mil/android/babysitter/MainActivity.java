package mil.android.babysitter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import mil.android.babysitter.adapter.CardViewAdapter;
import mil.android.babysitter.adapter.UserAdapter;
import mil.android.babysitter.data.User;
import mil.android.babysitter.view.TinderCard;

public class MainActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    public static User CURR_USER;

    private List<User> listUsers;

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

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(mContext, "Listusers "+listUsers.size(), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < listUsers.size(); i++) {
                            if(listUsers.get(i).getUid().equals(uidCurr)){
                                CURR_USER = listUsers.get(i);
                                listUsers.remove(CURR_USER);
                            }
                        }
                        pruneList();
                    }
                }, 1000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final User userToAdd = dataSnapshot.getValue(User.class);

                final DatabaseReference innerRef = ref.child(userToAdd.getUid()).child("match");
                innerRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String key = dataSnapshot.getKey();
                        boolean val = (boolean) dataSnapshot.getValue();

                        userToAdd.addMatchedUsers(key, val);
                        Toast.makeText(MainActivity.this, "MATCH", Toast.LENGTH_SHORT).show();
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

                listUsers.add(userToAdd);

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

    public void pruneList(){
        Map<String, Boolean> pruner = CURR_USER.getMatchedUsers();
        for (int i = 0; i < listUsers.size(); i++) {
            String idToCompare = listUsers.get(i).getUid();
            boolean sitterOrNah = listUsers.get(i).isBabysitter();

            Boolean value = pruner.get(idToCompare);
            if (value == null) {
                if (sitterOrNah != CURR_USER.isBabysitter()){
                    mSwipeView.addView(new TinderCard(mContext, listUsers.get(i), mSwipeView));
                }
            }
        }
    }
}