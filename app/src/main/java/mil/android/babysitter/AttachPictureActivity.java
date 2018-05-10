package mil.android.babysitter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mil.android.babysitter.data.User;

public class AttachPictureActivity extends AppCompatActivity {

    public static final String blankUserUrl = "https://i.imgur.com/lNziRwJ.jpg";

    private boolean imgChanged;
    public static final int PICK_IMAGE = 1;

    @BindView(R.id.imgAttach)
    ImageView imgAttach;

    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;
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

        imgChanged = false;

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @OnClick(R.id.btnAttach)
    void attachClick(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedimg = data.getData();
            try {
                imgAttach.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                imgChanged = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void finishRegistration(final String imageUrl){
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

                    //String key = FirebaseDatabase.getInstance().getReference().child("user").push().getKey();
                    User newUser = new User(userId, name, email, phoneNumber, babysitter);

                    newUser.setImageUrl(imageUrl);

                    FirebaseDatabase.getInstance().getReference().child("user").child(userId).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    @OnClick(R.id.tvFinish)
    void sendClick() {
        if (!imgChanged) {
            try {
                finishRegistration(blankUserUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                uploadUserWithImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void uploadUserWithImage() throws Exception {
        imgAttach.setDrawingCacheEnabled(true);
        imgAttach.buildDrawingCache();
        Bitmap bitmap = imgAttach.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInBytes = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")+".jpg";
        StorageReference newImageRef = storageRef.child(newImage);
        StorageReference newImageImagesRef = storageRef.child("images/"+newImage);
        newImageRef.getName().equals(newImageImagesRef.getName());    // true
        newImageRef.getPath().equals(newImageImagesRef.getPath());    // false

        UploadTask uploadTask = newImageImagesRef.putBytes(imageInBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(AttachPictureActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String imgUrlToRegister = taskSnapshot.getDownloadUrl().toString();
                finishRegistration(imgUrlToRegister);
            }
        });
    }
}

