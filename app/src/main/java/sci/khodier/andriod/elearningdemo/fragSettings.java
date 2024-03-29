package sci.khodier.andriod.elearningdemo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import android.widget.Toast;

import java.util.Objects;

public class fragSettings extends Fragment {
    ImageView profile, password, contact, signOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TransitionInflater inflater0 = TransitionInflater.from(requireContext());
        setExitTransition(inflater0.inflateTransition(R.transition.slide_right));
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.frag_settings, container, false);
        profile = rootView.findViewById(R.id.edit_my_profile_view);
        password = rootView.findViewById(R.id.change_password_view);
        contact = rootView.findViewById(R.id.contact_us_view);
        signOut = rootView.findViewById(R.id.sign_out_view);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =new Intent(getContext() ,activityEditProfile.class);
                startActivity(it);
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new fragChangePassword());}
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Toast.makeText(getContext(), "Soon!", Toast.LENGTH_SHORT).show();}
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext() , LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(fm).beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}