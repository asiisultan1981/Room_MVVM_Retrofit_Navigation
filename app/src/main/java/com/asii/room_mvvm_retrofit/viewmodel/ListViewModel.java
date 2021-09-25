package com.asii.room_mvvm_retrofit.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Database;

import com.asii.room_mvvm_retrofit.model.Dog;
import com.asii.room_mvvm_retrofit.model.DogDao;
import com.asii.room_mvvm_retrofit.model.DogDatabase;
import com.asii.room_mvvm_retrofit.model.DogsApiService;
import com.asii.room_mvvm_retrofit.util.SharedPreferencesHelper;
import com.asii.room_mvvm_retrofit.view.DogsListAdapter;
import com.asii.room_mvvm_retrofit.view.ListFragmentDirections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel  {
    private static final String TAG = "view";
    public MutableLiveData<List<Dog>> dogs = new MutableLiveData<>();
    public MutableLiveData<Boolean> dogsLoadingError = new MutableLiveData<>();
    public MutableLiveData<Boolean> dogsLoading = new MutableLiveData<>();
    private final SharedPreferencesHelper prefHelper =
            SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 30  * 1000 * 1000 * 1000L;
    private final DogDatabase dogDatabase = DogDatabase.getDatabase(getApplication());
    private final DogDao dogDao = dogDatabase.dogDao();
    private final DogsApiService dogsService = new DogsApiService();
    private final CompositeDisposable disposable = new CompositeDisposable();

    public ListViewModel(Application application) {
        super(application);
    }

    public void refresh() {
        long lastSavedTime = prefHelper.getUpdateTime();
        long currentTime = System.nanoTime();

        if (lastSavedTime != 0 && currentTime - lastSavedTime < refreshTime) {
            fetchFromDatabase();
        } else {
            fetchFromRemote();
        }
    }

    public void byPassRefreshCache(){
        fetchFromRemote();
    }

    private void fetchFromRemote() {
        dogsLoading.setValue(true);
        disposable.add(
                dogsService.getDogs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Dog>>() {
                            @Override
                            public void onSuccess(@NonNull List<Dog> dogsList) {
                                insertDogs(dogsList);
                                Toast.makeText(getApplication(), "Dogs Retrieving From" +
                                        " Network..", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                dogsLoadingError.setValue(true);
                                dogsLoading.setValue(false);
                                e.printStackTrace();

                            }
                        })
        );
    }

    private void fetchFromDatabase() {
        dogsLoading.setValue(true);
        getAllDogs();
        Toast.makeText(getApplication(), "Dogs Retrieving From DataBase..",
                Toast.LENGTH_LONG).show();

    }

    private void dogsRetrieved(List<Dog> dogList) {
        dogs.setValue(dogList);
        dogsLoadingError.setValue(false);
        dogsLoading.setValue(false);
    }

    private void getAllDogs() {
        DogDatabase.databaseWriterExecutor.execute(() -> {
           List<Dog> dogsList =  dogDao.getALlDogs();
//                List<Dog> dogsList = dogDao.getALlDogs();
            Log.d(TAG, "Fetching Dogs from DataBase on: " + Thread.currentThread());

            //       https://stackoverflow.com/a/11125271/14324617 (for the main thread)
           new Handler(Looper.getMainLooper()).post(() -> {
               dogsRetrieved(dogsList);
               Log.d(TAG, "Dogs Retrieved from Database on: " +
                       Thread.currentThread().getName());
               Toast.makeText(getApplication(), "Main Thread",
                       Toast.LENGTH_LONG).show();
           });
        });
    }

    @SafeVarargs
    private final void insertDogs(List<Dog>... dogsList) {
        DogDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Dog> list = dogsList[0];
//              dogDao.deleteALlDogs(); its already given in database
                ArrayList<Dog> newList = new ArrayList<>(list);
                List<Long> result = dogDao.insertAll(newList.toArray(new Dog[0]));
                Log.d(TAG, "Fetching Dogs from internet on: " + Thread.currentThread());
                int i = 0;
                while (i < list.size()) {
                    list.get(i).uuid = result.get(i).intValue();
                    i++;
                }

              new Handler(getApplication().getMainLooper()).post(new Runnable() {
                  @Override
                  public void run() {
                      dogsRetrieved(list);
                      prefHelper.saveUpdateTime(System.nanoTime());

                      Toast.makeText(getApplication(), "I am on main Thread",
                              Toast.LENGTH_LONG).show();
                      Log.d(TAG, "Dogs Retrieved on " + Thread.currentThread()+" at "+
                              Calendar.getInstance().getTime());
                  }
              });
            }


        });

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }




}
