package com.asii.room_mvvm_retrofit.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DogDao {

//    Yahan hm ne internet se jo receive krni hay wo aik list of dogs ho gi isliy hum yahan insertDog(Dog dog)
//    k bjaey List<Long> insertAllDogs(Dog...dogs) use kr rahy hain aur variable arguments ka matlb hay k aik
//    dog bhi ho skta hay aur boht se bhi ho sktay hain

    @Insert
    List<Long> insertAll(Dog... dogs);

    @Query("SELECT * FROM table_dog")
    List<Dog> getALlDogs();

    @Query("SELECT * FROM table_dog WHERE uuid =:dogId")
    Dog getDog(int dogId);

    @Query("DELETE FROM table_dog")
    void deleteALlDogs();

    @Delete
    void deleteDog(Dog dog);
}
