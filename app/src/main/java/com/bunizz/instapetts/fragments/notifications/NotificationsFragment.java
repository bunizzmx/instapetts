package com.bunizz.instapetts.fragments.notifications;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsFragment extends Fragment implements  NotificationsContract.View{


    @BindView(R.id.list_notifications)
    RecyclerView list_notifications;

    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;




    NotificationsAdapter notificationsAdapter;
    NotificationsPresenter presenter;
    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsAdapter = new NotificationsAdapter(getContext());
        presenter = new NotificationsPresenter (this,getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_notifications.setLayoutManager(new LinearLayoutManager(getContext()));
        list_notifications.setAdapter(notificationsAdapter);
        presenter.getNotifications();
    }



    @Override
    public void showNotifications(ArrayList<NotificationBean> notificationBeans) {
        if(notificationBeans.size()>0) {
            list_notifications.setVisibility(View.VISIBLE);
            notificationsAdapter.setNotificationBeans(notificationBeans);
        }
        else{
            root_no_data.setVisibility(View.VISIBLE);
            list_notifications.setVisibility(View.GONE);
        }
    }


    public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<NotificationBean> notificationBeans = new ArrayList<>();
        Context context;

        public NotificationsAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<NotificationBean> getNotificationBeans() {
            return notificationBeans;
        }

        public void setNotificationBeans(ArrayList<NotificationBean> notificationBeans) {
            this.notificationBeans.addAll(notificationBeans);
            notifyDataSetChanged();
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
            return new notificationsHOlder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            notificationsHOlder h = (notificationsHOlder)holder;
                h.body_notification.setText(notificationBeans.get(position).getBody());
                h.title_notification.setText(notificationBeans.get(position).getTitle());



        }

        @Override
        public int getItemCount() {
            return notificationBeans.size();
        }

        public class notificationsHOlder extends RecyclerView.ViewHolder{
           TextView title_notification,body_notification;
           RelativeLayout root_notification;
           ImageView ic_notification;
            public notificationsHOlder(@NonNull View itemView) {
                super(itemView);
                ic_notification = itemView.findViewById(R.id.ic_notification);
                title_notification = itemView.findViewById(R.id.title_notification);
                body_notification = itemView.findViewById(R.id.body_notification);
                root_notification = itemView.findViewById(R.id.root_notification);
            }
        }
    }
}

