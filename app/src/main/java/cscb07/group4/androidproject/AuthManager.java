package cscb07.group4.androidproject;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class AuthManager {

    private static final AuthManager INSTANCE = new AuthManager();

    private Account account;

    public void login(String email, String pwd, Activity activity, OnCompleteListener<AuthResult> onComplete) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
            onComplete.onComplete(task);

            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();

                database.child("accounts").child(firebaseUser.getUid()).get().addOnCompleteListener(dataTask -> {
                    if (dataTask.isSuccessful()) {
                        AuthManager.this.account = dataTask.getResult().getValue(Account.class);
                    }
                });
            }
        });
    }

    public void register(String email, String pwd, AccountType type, Activity activity, OnCompleteListener<AuthResult> onComplete) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
            onComplete.onComplete(task);

            if (task.isSuccessful()){
                FirebaseUser firebaseUser = auth.getCurrentUser();

                Account account = new Account();
                account.setIdToken(firebaseUser.getUid());
                account.setEmail(firebaseUser.getEmail());
                account.setPwd(pwd);
                account.setType(type);

                AuthManager.this.account = account;

                database.child("accounts").child(firebaseUser.getUid()).setValue(account);
            }
        });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        this.account = null;
    }

    public boolean isLoggedIn() {
        return this.account != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && this.account.getType() == AccountType.ADMIN;
    }

    public Account getAccount() {
        return account;
    }

    public static AuthManager getInstance() {
        return INSTANCE;
    }
}
