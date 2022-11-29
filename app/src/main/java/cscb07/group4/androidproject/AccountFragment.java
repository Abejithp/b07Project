package cscb07.group4.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import cscb07.group4.androidproject.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        update();

        binding.logoutButton.setOnClickListener(v -> {
            AccountManager.getInstance().logout();
            AccountFragment.this.update();
        });

        binding.becomeAdminButton.setOnClickListener(v -> {
            AccountManager.getInstance().becomeAdmin();
            AccountFragment.this.update();
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(AccountFragment.this)
                        .navigate(R.id.action_AccountFragment_to_RegisterFragment);
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(AccountFragment.this)
                        .navigate(R.id.action_AccountFragment_to_LoginFragment);
            }
        });

        return root;
    }

    public void update() {
        AccountManager auth = AccountManager.getInstance();
        if (AccountManager.getInstance().isLoggedIn()) {
            binding.nameView.setText(auth.getAccount().getFirstName() + " " + auth.getAccount().getLastName());
            binding.emailView.setText(auth.getAccount().getEmail());
            binding.accountTypeView.setText(getString(R.string.account_type, auth.getAccount().getType().toString()));
            binding.passwordView.setText(getString(R.string.password, auth.getAccount().getPwd()));

            binding.logoutButton.setVisibility(View.VISIBLE);

        } else {
            binding.nameView.setText(getString(R.string.not_logged_in));
            binding.emailView.setText(getString(R.string.na));
            binding.accountTypeView.setText(getString(R.string.account_type, getString(R.string.na)));
            binding.passwordView.setText(getString(R.string.password, getString(R.string.na)));

            binding.logoutButton.setVisibility(View.INVISIBLE);
        }
    }
}
