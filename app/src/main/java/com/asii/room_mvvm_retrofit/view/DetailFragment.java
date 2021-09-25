package com.asii.room_mvvm_retrofit.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asii.room_mvvm_retrofit.R;
import com.asii.room_mvvm_retrofit.model.Dog;
import com.asii.room_mvvm_retrofit.util.Util;
import com.asii.room_mvvm_retrofit.viewmodel.DetailViewModel;


import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailFragment extends Fragment {
    private static final String TAG = "detailFragment";
    private int dogUuid;
    private DetailViewModel viewModel;

    @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.dogName)
    TextView dogName;

    @BindView(R.id.dogPurpose)
    TextView dogPurpose;

    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifespan)
    TextView dogLifespan;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null){
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication())
                .create(DetailViewModel.class);
        viewModel.fetchDog(dogUuid);
        
        observeViewModel();

    }

    private void observeViewModel() {
        viewModel.dogLiveData.observe(getViewLifecycleOwner(), dog -> {
            if (dog != null && dog instanceof Dog){

                dogName.setText(dog.dogBreed);
                Log.d(TAG, "observeViewModel: "+dog.dogBreed);
                dogPurpose.setText(dog.bredFor);
                dogTemperament.setText(dog.temperament);
                dogLifespan.setText(dog.lifeSpan);
                if (dogImage != null){
                    Util.loadImage(dogImage,dog.imageUrl, new CircularProgressDrawable(getContext()));
                }
            }
        });
    }


}