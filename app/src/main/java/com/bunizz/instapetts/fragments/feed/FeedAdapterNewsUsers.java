package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.listeners.SelectUserListener;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapterNewsUsers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Object> data = new ArrayList<>();
    Context context;
    SelectUserListener listener;

    public SelectUserListener getListener() {
        return listener;
    }

    public void setListener(SelectUserListener listener) {
        this.listener = listener;
    }

    public FeedAdapterNewsUsers(Context context) {
        this.context = context;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        Log.e("NUM_IUAERS","-->" + data.size());
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_recomended_new_users,parent,false);
        return new RecomendedHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecomendedHolder h =(RecomendedHolder)holder;
        UserBean data_parsed = (UserBean) data.get(position);
        Glide.with(context).load(data_parsed.getPhoto_user_thumbh()).into(h.img_user_recomended);
        h.name_user_recomended.setText(data_parsed.getName_user());
        h.rate_pets_user_recomended.setText(""+data_parsed.getRate_pets());
        h.root_simple_recomended_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelectUser(data_parsed.getId(),data_parsed.getUuid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecomendedHolder extends RecyclerView.ViewHolder{
       ImagenCircular img_user_recomended;
       TextView name_user_recomended,rate_pets_user_recomended;
       CardView root_simple_recomended_user;
        public RecomendedHolder(@NonNull View itemView) {
            super(itemView);
            img_user_recomended =itemView.findViewById(R.id.img_user_recomended);
            name_user_recomended = itemView.findViewById(R.id.name_user_recomended);
            rate_pets_user_recomended = itemView.findViewById(R.id.rate_pets_user_recomended);
            root_simple_recomended_user = itemView.findViewById(R.id.root_simple_recomended_user);

        }
    }

    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }
}
