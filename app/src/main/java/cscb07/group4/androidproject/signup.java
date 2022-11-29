package cscb07.group4.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class signup extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    TextInputLayout fullname_var, username_var, email_var, phonenum_var, password_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullname_var = findViewById(R.id.fullname_field);
        username_var = findViewById(R.id.username_field);
        email_var = findViewById(R.id.email_field);
        password_var = findViewById(R.id.password_field);
    }

    public void loginbuttonclick(View view) {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        finish();

    }

    public void registerbuttonclick(View view) {
        String fullname_ = fullname_var.getEditText().getText().toString();
        String username_ = username_var.getEditText().getText().toString();
        String email_ = Objects.requireNonNull(email_var.getEditText()).getText().toString();
        String password_ = password_var.getEditText().getText().toString();

        if (!fullname_.isEmpty()) {
            fullname_var.setError(null);
            fullname_var.setErrorEnabled(false);
            if (!username_.isEmpty()) {
                username_var.setError(null);
                username_var.setErrorEnabled(false);
                if (!email_.isEmpty()) {
                    email_var.setError(null);
                    email_var.setErrorEnabled(false);
                    if (!password_.isEmpty()) {
                        password_var.setError(null);
                        password_var.setErrorEnabled(false);
                        if (email_.matches( "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" ) ){

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            reference = firebaseDatabase.getReference( "accounts");

                            String fullname_s = fullname_var.getEditText().getText().toString();
                            String username_s = username_var.getEditText().getText().toString();
                            String email_s = Objects.requireNonNull(email_var.getEditText()).getText().toString();
                            String password_s = password_var.getEditText().getText().toString();


                            storingdata storingdatas = new storingdata(fullname_s, username_s, email_s, password_s);

                            reference.child(username_s).setValue(storingdatas);
                            Toast.makeText(getApplicationContext(), "Registered sucessfully" ,Toast.LENGTH_SHORT ).show();

                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
                            finish();

                        }else{
                            email_var.setError("Invalid email");
                        }
                    } else {
                        password_var.setError("please enter password");
                    }
                } else {
                    email_var.setError("please enter the email");
                }
            } else {
                phonenum_var.setError("please enter the phone number");
            }
        } else {
            fullname_var.setError("please enter the full name");
        }
    }
}