package mil.android.babysitter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.Iterator;
import java.util.List;

import mil.android.babysitter.adapter.CardViewAdapter;
import mil.android.babysitter.data.User;

public class MainActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    private User currUser;
    private CardViewAdapter cardViewAdapter;

    private List<User> listUsers;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


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

        populateListUsers();
    }


    public void populateListUsers() {

        FirebaseUser currFirebaseUser = mAuth.getCurrentUser();
        DatabaseReference currUserReference = databaseReference.child(currFirebaseUser.getUid());

        currUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);


                databaseReference.child("users").addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                                listUsers.clear();


                                while (items.hasNext()) {
                                    DataSnapshot item = items.next();

                                    String uid, name, email, phoneNumber;
                                    Boolean babysitter;
                                    uid = item.child("uid").getValue().toString();
                                    name = item.child("name").getValue().toString();
                                    email = item.child("email").getValue().toString();
                                    phoneNumber = item.child("phoneNumber").getValue().toString();
                                    babysitter = (Boolean) item.child("babysitter").getValue();
                                    User user = new User(uid, name, email, phoneNumber, babysitter);

                                    if (currUser.getUid() != uid && babysitter != currUser.isBabysitter()) {
                                        listUsers.add(user);
                                    }
                                }
                                databaseReference.child("users").removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
