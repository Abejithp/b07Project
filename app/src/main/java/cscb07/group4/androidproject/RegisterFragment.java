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
import cscb07.group4.androidproject.manager.AccountManager;
import cscb07.group4.androidproject.manager.AccountType;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_RegisterFragment_to_LoginFragment);
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String name = binding.fullnameField.getEditText().getText().toString();
                String email = binding.emailField.getEditText().getText().toString();
                String pwd = binding.passwordField.getEditText().getText().toString();

                binding.fullnameField.setError(null);
                binding.fullnameField.setErrorEnabled(false);
                binding.emailField.setError(null);
                binding.emailField.setErrorEnabled(false);
                binding.passwordField.setError(null);
                binding.passwordField.setErrorEnabled(false);

                // FirebaseAuth cannot take empty email/password
                if (TextUtils.isEmpty(name)) {
                    binding.fullnameField.setError("Please enter name");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    binding.emailField.setError("Please enter email");
                    return;
                }

                if (!email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
                    binding.emailField.setError("Invalid email");
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    binding.passwordField.setError("Please enter password");
                    return;
                }

                if (pwd.length() < 6) {
                    binding.passwordField.setError("Password must be at least 6 characters long");
                    return;
                }

                AccountManager.getInstance().register(email, pwd, name, AccountType.STUDENT, getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            NavHostFragment.findNavController(RegisterFragment.this)
                                    .navigate(R.id.action_RegisterFragment_to_EditCourseFragment);
                            Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Register Failed (" + task.getException().getMessage() + ")", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return root;
    }
}