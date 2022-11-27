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

        AccountManager auth = AccountManager.getInstance();
        if (AccountManager.getInstance().isLoggedIn()) {
            binding.nameView.setText(auth.getAccount().getFirstName() + " " + auth.getAccount().getLastName());
            binding.emailView.setText(auth.getAccount().getEmail());

            binding.accountTypeView.setText("Account Type: " + auth.getAccount().getType().toString());
            binding.passwordView.setText("Password: " + auth.getAccount().getPwd());

        } else {
            binding.logoutButton.setVisibility(View.GONE);
        }

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountManager.getInstance().logout();
                NavHostFragment.findNavController(AccountFragment.this)
                        .navigate(R.id.action_AccountFragment_to_MainFragment);
            }
        });

        return root;
    }
}
