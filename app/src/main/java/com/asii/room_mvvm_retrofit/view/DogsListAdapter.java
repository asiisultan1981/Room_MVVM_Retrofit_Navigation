package com.asii.room_mvvm_retrofit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavAction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.asii.room_mvvm_retrofit.R;
import com.asii.room_mvvm_retrofit.model.Dog;
import com.asii.room_mvvm_retrofit.util.Util;
import com.bumptech.glide.Glide;

import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogsViewHolder> {
    private List<Dog> dogsList;
    private ClickHandler clickHandler;

    public DogsListAdapter(List<Dog> dogsList,ClickHandler clickHandler) {
        this.dogsList = dogsList;
        this.clickHandler = clickHandler;
    }

    public void updateDogsList(List<Dog> newDogsList){
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged();

    }

    @Override
    public DogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);
        return new DogsViewHolder(view, clickHandler);
    }

    @Override
    public void onBindViewHolder(DogsListAdapter.DogsViewHolder holder, int position) {
        Dog dog = dogsList.get(position);
        holder.name.setText(dog.getDogBreed());
        holder.lifespan.setText(dog.getLifeSpan());
//        holder.dogLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
//               action.setDogUuid(dog.uuid);
//               Navigation.findNavController(holder.dogLayout).navigate(action);
//            }
//        });



        Util.loadImage(holder.imageView,dog.getImageUrl(),
                Util.getProgressDrawable(holder.imageView.getContext()));

//        Glide.with(holder.imageView.getContext())
//                .load(dog.getImageUrl())
//                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        if (dogsList == null || dogsList.size() == 0){
            return 0;
        }else {
            return dogsList.size();
        }

    }

    public class DogsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name;
        TextView lifespan;
        LinearLayout dogLayout;
        ClickHandler clickHandler;

        public DogsViewHolder(View itemView, ClickHandler clickHandler) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            lifespan = itemView.findViewById(R.id.lifespan);
            dogLayout = itemView.findViewById(R.id.dogLayout);
            this.clickHandler = clickHandler;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Dog dog = dogsList.get(getAdapterPosition());
          clickHandler.onDogClick(dog, view);
        }
    }

    public interface ClickHandler{
        void onDogClick(Dog dog, View view);

    }
}
