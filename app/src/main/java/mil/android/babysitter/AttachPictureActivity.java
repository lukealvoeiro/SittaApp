package mil.android.babysitter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mil.android.babysitter.data.User;

public class AttachPictureActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth = null;

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private boolean babysitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_picture);

        email = getIntent().getExtras().getString("Email");
        password = getIntent().getExtras().getString("Password");
        name = getIntent().getExtras().getString("Name");
        phoneNumber = getIntent().getExtras().getString("PhoneNumber");
        babysitter = getIntent().getExtras().getBoolean("Babysitter");

        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.tvFinish)
    public void finishRegistration(){
        showProgressDialog();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if(task.isSuccessful()) {
                    FirebaseUser fbUser = task.getResult().getUser();
                    fbUser.updateProfile(new UserProfileChangeRequest.Builder().
                            setDisplayName(usernameFromEmail(fbUser.getEmail())).build());

                    final String userId = fbUser.getUid();

                    String key = FirebaseDatabase.getInstance().getReference().child("user").push().getKey();
                    User newUser = new User(userId, name, email, phoneNumber, babysitter);

                    FirebaseDatabase.getInstance().getReference().child("user").child(key).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(AttachPictureActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(AttachPictureActivity.this,
                            task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}

