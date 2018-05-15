package mil.android.babysitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mil.android.babysitter.adapter.UserAdapter;
import mil.android.babysitter.data.User;
import mil.android.babysitter.touch.ProfileTouchHelperCallback;

public class AcceptedUsersActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    Map<String, Boolean> valueMap;
    Set<Map.Entry<String, Boolean>> setMatches;
    List<User> acceptedUsers;
    List<String> matchedIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_users);


        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        valueMap = new HashMap<String, Boolean>();
        matchedIds = new ArrayList<String>();

        initUsers();

        //initValueMap();
        //initMatchedIds();
        //populateAcceptedUsers();
        //initUsers(recyclerView);
    }


    public void initUsers() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                child("user");



        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                User user = dataSnapshot.getValue(User.class);
                Log.d("tag", user.toString());
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

    public void initValueMap() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                child("user").child(MainActivity.CURR_USER.getUid()).child("match");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                boolean val = (boolean) dataSnapshot.getValue();
                Log.d("VALUE_MAP", "onDataChange: "+ key + " " + val);
                valueMap.put(key, val);
                Log.d("VALUE_MAP2", "onChildAdded: "+ valueMap.get(key));
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

    public void initMatchedIds() {
        setMatches = valueMap.entrySet();
        for (final Map.Entry<String, Boolean> entry: setMatches) {
            final DatabaseReference queryRef = FirebaseDatabase.getInstance().getReference().
                    child("user").child(entry.getKey()).child("match").child(entry.getKey());


            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    boolean match = (boolean) dataSnapshot.getValue();
                    if(match == true){
                        matchedIds.add(dataSnapshot.getKey());
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

    public void populateAcceptedUsers() {
        for (int i = 0; i < matchedIds.size(); i++) {
            String curr = matchedIds.get(i);

            DatabaseReference matchingQueryRef = FirebaseDatabase.getInstance().getReference().
                    child("user").child(curr);
            matchingQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final User userToAdd = dataSnapshot.getValue(User.class);
                    acceptedUsers.add(userToAdd);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void initUsers(final RecyclerView recyclerView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userAdapter = new UserAdapter(acceptedUsers, AcceptedUsersActivity.this);
                recyclerView.setAdapter(userAdapter);

                ItemTouchHelper.Callback callback = new ProfileTouchHelperCallback(userAdapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(recyclerView);
            }
        });
    }
}
