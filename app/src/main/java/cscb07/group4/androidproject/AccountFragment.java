package cscb07.group4.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import cscb07.group4.androidproject.databinding.FragmentAccountBinding;
import cscb07.group4.androidproject.manager.AccountManager;

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
            NavHostFragment.findNavController(AccountFragment.this)
                    .navigate(R.id.action_AccountFragment_to_WelcomeFragment);
        });

        return root;
    }

    public void update() {
        AccountManager auth = AccountManager.getInstance();
        if (AccountManager.getInstance().isLoggedIn()) {
            binding.nameView.setText(auth.getAccount().getName());
            binding.emailView.setText(auth.getAccount().getEmail());
            binding.accountTypeView.setText(getString(R.string.account_type, auth.getAccount().getType().toString()));

            binding.logoutButton.setVisibility(View.VISIBLE);

        } else {
            binding.nameView.setText(getString(R.string.not_logged_in));
            binding.emailView.setText(getString(R.string.na));
            binding.accountTypeView.setText(getString(R.string.account_type, getString(R.string.na)));

            binding.logoutButton.setVisibility(View.INVISIBLE);
        }
    }
}
