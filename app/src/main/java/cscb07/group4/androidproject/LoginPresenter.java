package cscb07.group4.androidproject;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import cscb07.group4.androidproject.manager.AccountManager;

public class LoginPresenter {

    private LoginFragment view;
    private AccountManager model;

    public LoginPresenter(LoginFragment view, AccountManager model) {
        this.view = view;
        this.model = model;
    }

    public boolean checkEmail() {
        String email = view.getEmail();

        if (TextUtils.isEmpty(email)) {
            view.setEmailError("Please enter email");
            return false;

        } else if (!email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            view.setEmailError("Invalid email");
            return false;
        }
        return true;
    }

    public boolean checkPassword() {
        String password = view.getPassword();

        if (TextUtils.isEmpty(password)) {
            view.setPasswordError("Please enter password");
            return false;
        }
        return true;
    }

    public void login(OnCompleteListener<AuthResult> onComplete) {
        model.login(view.getEmail(), view.getPassword(), view.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    view.setPasswordError("Incorrect email/password");
                }
                onComplete.onComplete(task);
            }
        });
    }
}
