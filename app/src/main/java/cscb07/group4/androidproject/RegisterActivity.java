package cscb07.group4.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText etEmail, etPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegisterAdmin, btnRegisterStu, btnLogin;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPwd = findViewById(R.id.editTextTextPassword);
        btnRegisterAdmin = findViewById(R.id.btn_admin_register);
        btnRegisterStu = findViewById(R.id.btn_student_register);

        btnLogin = findViewById(R.id.btn_signIn);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        btnRegisterAdmin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = etEmail.getText().toString();
                String pwd = etPwd.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            Admin admin = new Admin();
                            admin.setIdToken(firebaseUser.getUid());
                            admin.setEmail(firebaseUser.getEmail());
                            admin.setPwd(pwd);

                            mDatabaseRef.child("AdminAccount").child(firebaseUser.getUid()).setValue(admin);
                            Toast.makeText(RegisterActivity.this, "Registered Successfully As Admin", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnRegisterStu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = etEmail.getText().toString();
                String pwd = etPwd.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            Student student = new Student();
                            student.setIdToken(firebaseUser.getUid());
                            student.setEmail(firebaseUser.getEmail());
                            student.setPwd(pwd);

                            mDatabaseRef.child("StudentAccount").child(firebaseUser.getUid()).setValue(student);
                            Toast.makeText(RegisterActivity.this, "Registered Successfully As Student", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}