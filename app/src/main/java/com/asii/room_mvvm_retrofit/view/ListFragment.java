package com.asii.room_mvvm_retrofit.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asii.room_mvvm_retrofit.R;
import com.asii.room_mvvm_retrofit.databinding.FragmentListBinding;
import com.asii.room_mvvm_retrofit.model.Dog;
import com.asii.room_mvvm_retrofit.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {
    FragmentListBinding binding;

    private ListViewModel listViewModel;
    private DogsListAdapter adapter = new DogsListAdapter(new ArrayList<>());

    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        listViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity()
                .getApplication())
                .create(ListViewModel.class);
        listViewModel.refresh();

        binding.recyclerViewDogsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.recyclerViewDogsList.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(() -> {
            binding.recyclerViewDogsList.setVisibility(View.GONE);
            binding.tvListError.setVisibility(View.GONE);
            binding.progressBarLoadingView.setVisibility(View.VISIBLE);
            listViewModel.byPassRefreshCache();
            binding.refreshLayout.setRefreshing(false);
        });

        observeViewModel();
    }

    private void observeViewModel() {
        listViewModel.dogs.observe(getViewLifecycleOwner(), dogs -> {
            if (dogs != null && dogs instanceof List){
                binding.recyclerViewDogsList.setVisibility(View.VISIBLE);
                adapter.updateDogsList(dogs);
            }
        });

        listViewModel.dogsLoadingError.observe(getViewLifecycleOwner(), isError -> {
            if (isError != null && isError instanceof Boolean){
                binding.tvListError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        listViewModel.dogsLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean){
                binding.progressBarLoadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading){
                    binding.tvListError.setVisibility(View.GONE);
                    binding.recyclerViewDogsList.setVisibility(View.GONE);
                }
            }
        });
    }

}