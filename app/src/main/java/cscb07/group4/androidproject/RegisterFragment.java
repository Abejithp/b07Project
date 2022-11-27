package cscb07.group4.androidproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import cscb07.group4.androidproject.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_RegisterFragment_to_LoginFragment);
            }
        });

        binding.btnAdminRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = binding.editTextTextEmailAddress.getText().toString();
                String pwd = binding.editTextTextPassword.getText().toString();

                // FirebaseAuth cannot take empty email/password
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                    return;
                }

                AuthManager.getInstance().register(email, pwd, AccountType.ADMIN, getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Registered Successfully As Admin", Toast.LENGTH_SHORT).show();
                        }else{
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getContext(), "Register Failed (" + task.getException().getMessage() + ")", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Register Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        binding.btnStudentRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = binding.editTextTextEmailAddress.getText().toString();
                String pwd = binding.editTextTextPassword.getText().toString();

                // FirebaseAuth cannot take empty email/password
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                    return;
                }

                AuthManager.getInstance().register(email, pwd, AccountType.STUDENT, getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Registered Successfully As Student", Toast.LENGTH_SHORT).show();
                        }else{
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getContext(), "Register Failed (" + task.getException().getMessage() + ")", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Register Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        return root;
    }
}