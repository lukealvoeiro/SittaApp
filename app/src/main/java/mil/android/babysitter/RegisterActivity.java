package mil.android.babysitter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mil.android.babysitter.data.User;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etRegisterEmail)
    EditText etRegisterEmail;

    @BindView(R.id.etRegisterName)
    EditText etRegisterName;

    @BindView(R.id.etRegisterPhoneNumber)
    EditText etRegisterPhoneNumber;

    @BindView(R.id.etRegisterPassword)
    EditText etRegisterPassword;

    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnNext)
    public void goToRoleSelector() {
        boolean completed = false;
        if (!isFormValid()) {
            return;
        }

        Intent intent = new Intent(RegisterActivity.this, ChooseRoleActivity.class);

        Bundle extras = new Bundle();
        extras.putString("Email", etRegisterEmail.getText().toString());
        extras.putString("Password", etRegisterPassword.getText().toString());
        extras.putString("Name", etRegisterName.getText().toString());
        extras.putString("PhoneNumber", etRegisterPhoneNumber.getText().toString());
        intent.putExtras(extras);
        startActivity(intent);
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(etRegisterEmail.getText())) {
            etRegisterEmail.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(etRegisterName.getText())) {
            etRegisterName.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(etRegisterPhoneNumber.getText())) {
            etRegisterPhoneNumber.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(etRegisterPassword.getText())) {
            etRegisterPassword.setError("Required");
            return false;
        }

        return true;
    }
}