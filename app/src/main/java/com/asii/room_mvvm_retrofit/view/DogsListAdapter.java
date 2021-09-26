package com.asii.room_mvvm_retrofit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavAction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.asii.room_mvvm_retrofit.R;
import com.asii.room_mvvm_retrofit.databinding.ItemDogBinding;
import com.asii.room_mvvm_retrofit.model.Dog;
import com.asii.room_mvvm_retrofit.util.Util;
import com.bumptech.glide.Glide;

import java.util.List;

class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogsViewHolder> implements ClickListener {
    private List<Dog> dogsList;

    public DogsListAdapter(List<Dog> dogsList) {
        this.dogsList = dogsList;
    }

    public void updateDogsList(List<Dog> newDogsList) {
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged();

    }

    @Override
    public DogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Now the code will change after the implementation of DataBinding

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent, false);
        return new DogsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DogsListAdapter.DogsViewHolder holder, int position) {
        Dog dog = dogsList.get(position);
        holder.itemView.setDog(dog);
//        last step is to give listener here in this way:
        holder.itemView.setListener(this);
    }

    @Override
    public int getItemCount() {
        if (dogsList == null || dogsList.size() == 0) {
            return 0;
        } else {
            return dogsList.size();
        }
    }

    @Override
    public void onDogClick(View view) {
// dogId has to be fetched from TextView in following way
        String uuidString = ((TextView) view.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(uuidString);
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDogUuid(uuid);
        Navigation.findNavController(view).navigate(action);
    }

    public class DogsViewHolder extends RecyclerView.ViewHolder {
        ItemDogBinding itemView;

        public DogsViewHolder(ItemDogBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

    }
}
