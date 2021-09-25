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
import com.asii.room_mvvm_retrofit.model.Dog;
import com.asii.room_mvvm_retrofit.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment implements DogsListAdapter.ClickHandler{
    private ListViewModel listViewModel;
    private DogsListAdapter adapter = new DogsListAdapter(new ArrayList<>(), this);

    @BindView(R.id.recyclerView_dogsList)
    RecyclerView recyclerView;

    @BindView(R.id.tv_listError)
    TextView tv_listError;

    @BindView(R.id.progressBar_loadingView)
    ProgressBar progressBar_loadingView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);

              return view;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity()
                .getApplication())
                .create(ListViewModel.class);
        listViewModel.refresh();

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(() -> {
            recyclerView.setVisibility(View.GONE);
            tv_listError.setVisibility(View.GONE);
            progressBar_loadingView.setVisibility(View.VISIBLE);
            listViewModel.byPassRefreshCache();
            refreshLayout.setRefreshing(false);
        });


        observeViewModel();



    }


    private void observeViewModel() {
        listViewModel.dogs.observe(getViewLifecycleOwner(), dogs -> {
            if (dogs != null && dogs instanceof List){
                recyclerView.setVisibility(View.VISIBLE);
                adapter.updateDogsList(dogs);
            }
        });

        listViewModel.dogsLoadingError.observe(getViewLifecycleOwner(), isError -> {
            if (isError != null && isError instanceof Boolean){
                tv_listError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        listViewModel.dogsLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean){
                progressBar_loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading){
                    tv_listError.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
    }


    @Override
    public void onDogClick(Dog dog, View view) {
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDogUuid(dog.uuid);
        Navigation.findNavController(view).navigate(action);

    }
}