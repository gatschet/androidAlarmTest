package ch.dvbern.alarmtest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AlarmFragment extends Fragment {

    private static final String TAG = AlarmFragment.class.getSimpleName();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AlarmFragment.this)
                        .navigate(R.id.action_to_FirstFragment);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden == false){
            Log.i(TAG, "------------------------------------------->ch.dvbern.alarmtest : Alarm view showed 1<-------------------------------------------");
        }
    }
}
