package mil.android.babysitter;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
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

import butterknife.BindView;
import mil.android.babysitter.adapter.UserAdapter;
import mil.android.babysitter.data.User;

public class AcceptedUsersActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    List<User> acceptedUsers;
    private ProgressDialog progressDialog;

    @BindView(R.id.errorMsg)
    TextView errorMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_users);


        final RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        acceptedUsers = new ArrayList<User>();

        populateAcceptedUsers();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initUsers(recyclerView);
            }
        },300);
    }

    public void populateAcceptedUsers(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                child("match").child(MainActivity.CURR_USER.getUid());

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final User userToAdd = dataSnapshot.getValue(User.class);
                acceptedUsers.add(userToAdd);
                Log.d("USER_ADDED", "onChildAdded: " + userToAdd.getUid());
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

    public void initUsers(final RecyclerView recyclerView) {
        userAdapter = new UserAdapter(acceptedUsers, AcceptedUsersActivity.this);
        recyclerView.setAdapter(userAdapter);
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
