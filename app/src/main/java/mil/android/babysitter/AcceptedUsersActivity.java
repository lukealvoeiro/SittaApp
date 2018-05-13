package mil.android.babysitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mil.android.babysitter.adapter.UserAdapter;
import mil.android.babysitter.data.User;
import mil.android.babysitter.touch.ProfileTouchHelperCallback;

public class AcceptedUsersActivity extends AppCompatActivity {

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_users);


        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initUsers(recyclerView);
    }

    public void initUsers(final RecyclerView recyclerView) {
        final List<User> acceptedUsers = MainActivity.CURR_USER.getAcceptedUsers();
        TextView tvNamesAccepted = findViewById(R.id.tvNamesAccepted);

        /*try {
            String names = "";
            for (int i = 0; i < acceptedUsers.size(); i++) {
                names = names + acceptedUsers.get(i).getName() + " ";
            }
            tvNamesAccepted.setText(names);
        }
        catch (Exception e) {
            Toast.makeText(AcceptedUsersActivity.this, "Yikes, accepted user list is empty. Size: "
                    + Integer.toString(acceptedUsers.size()) + "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        */

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
