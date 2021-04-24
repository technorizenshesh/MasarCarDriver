package com.masarcardriver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterPast;
import com.masarcardriver.databinding.FragmentPasBinding;

public class PastTripFragment extends Fragment {
    FragmentPasBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  binding.calenderView.setVisibility(View.VISIBLE);
        binding.rvPast.setVisibility(View.GONE);*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pas,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      //  binding.calenderView.setVisibility(View.VISIBLE);
     //   binding.rvPast.setVisibility(View.GONE);

        binding.calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                binding.calenderView.setVisibility(View.GONE);
                binding.rvPast.setVisibility(View.VISIBLE);
                binding.rvPast.setAdapter(new AdapterPast(getActivity()));
            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
