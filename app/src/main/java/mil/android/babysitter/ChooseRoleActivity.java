package mil.android.babysitter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mil.android.babysitter.data.User;

public class ChooseRoleActivity extends AppCompatActivity {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        email = getIntent().getExtras().getString("Email");
        password = getIntent().getExtras().getString("Password");
        name = getIntent().getExtras().getString("Name");
        phoneNumber = getIntent().getExtras().getString("PhoneNumber");
        bio = getIntent().getExtras().getString("Bio");

        ButterKnife.bind(this);
    }

    @OnClick(R.id.babysitterSelector)
    public void setUserAsBabysitter(){

        Intent intent = new Intent(ChooseRoleActivity.this, AttachPictureActivity.class);

        Bundle extras = new Bundle();
        extras.putString("Email", email);
        extras.putString("Password", password);
        extras.putString("Name", name);
        extras.putString("PhoneNumber", phoneNumber);
        extras.putBoolean("Babysitter", true);
        extras.putString("Bio", bio);

        intent.putExtras(extras);
        startActivity(intent);
    }

    @OnClick(R.id.parentSelector)
    public void setUserAsParent(){
        Intent intent = new Intent(ChooseRoleActivity.this, AttachPictureActivity.class);

        Bundle extras = new Bundle();
        extras.putString("Email", email);
        extras.putString("Password", password);
        extras.putString("Name", name);
        extras.putString("PhoneNumber", phoneNumber);
        extras.putBoolean("Babysitter", false);
        extras.putString("Bio", bio);

        intent.putExtras(extras);
        startActivity(intent);
    }

}
