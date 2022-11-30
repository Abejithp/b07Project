package cscb07.group4.androidproject.manager;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import cscb07.group4.androidproject.Account;
import cscb07.group4.androidproject.AccountChangeListener;

public final class AccountManager {

    private static final AccountManager INSTANCE = new AccountManager();

    private List<AccountChangeListener> listeners = new ArrayList<>();

    private Account account;

    public static AccountManager getInstance() {
        return INSTANCE;
    }

    /**
     * @return If the user is currently logged in.
     */
    public boolean isLoggedIn() {
        return this.account != null;
    }

    /**
     * Get info about the currently logged in user. See {@link #isLoggedIn()} to check if the
     * user is logged in first.
     *
     * @return The Account object containing the user's information.
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @return If the current user has admin permissions.
     */
    public boolean isAdmin() {
        return isLoggedIn() && this.account.getType() == AccountType.ADMIN;
    }

    public void login(String email, String pwd, Activity activity, OnCompleteListener<AuthResult> onComplete) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
            onComplete.onComplete(task);

            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();

                database.child("accounts").child(firebaseUser.getUid()).get().addOnCompleteListener(dataTask -> {
                    if (dataTask.isSuccessful()) {
                        AccountManager.this.setAccount(dataTask.getResult().getValue(Account.class));
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

            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();

                Account account = new Account();
                account.setIdToken(firebaseUser.getUid());
                account.setEmail(firebaseUser.getEmail());
                account.setPwd(pwd);
                account.setType(type);

                AccountManager.this.setAccount(account);

                database.child("accounts").child(firebaseUser.getUid()).setValue(account);
            }
        });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        setAccount(null);
    }

    // TODO Remove later...
    @Deprecated
    public void becomeAdmin() {
        Account account = new Account();
        account.setFirstName("Amongus");
        account.setLastName("Faded");
        account.setIdToken("XX_69_AMONGUS_420_XXFADED");
        account.setType(AccountType.ADMIN);
        account.setEmail("email@gmail.com");
        account.setPwd("password");
        this.setAccount(account);
    }

    public void registerListener(AccountChangeListener listener) {
        this.listeners.add(listener);
    }

    private void onAccountChange() {
        for (AccountChangeListener runnable : this.listeners) {
            runnable.onAccountChange();
        }
    }

    private void setAccount(Account account) {
        this.account = account;
        onAccountChange();
    }
}
