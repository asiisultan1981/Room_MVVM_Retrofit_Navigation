package com.asii.room_mvvm_retrofit.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asii.room_mvvm_retrofit.model.Dog;
import com.asii.room_mvvm_retrofit.model.DogDao;
import com.asii.room_mvvm_retrofit.model.DogDatabase;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    private static final String TAG = "detail";
    public MutableLiveData<Dog> dogLiveData = new MutableLiveData<>();
    private final DogDatabase dogDatabase = DogDatabase.getDatabase(getApplication());
    private final DogDao dogDao = dogDatabase.dogDao();

    public DetailViewModel(Application application) {
        super(application);
    }




    private void getDog(Integer... integers){
        DogDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int uuid = integers[0];
                Dog dog = dogDao.getDog(uuid);
                Log.d(TAG, "run: "+dog.dogBreed);

                new Handler(getApplication().getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dogLiveData.setValue(dog);

                    }
                });
            }
        });
    }




    public void fetchDog(int uuid) {
        getDog(uuid);
        Log.d(TAG, "fetchDog: "+dogLiveData);

    }
}
