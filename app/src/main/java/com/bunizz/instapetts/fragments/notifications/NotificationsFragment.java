package com.bunizz.instapetts.fragments.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.listeners.delete;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogDeletes;
import com.bunizz.instapetts.utils.dilogs.DialogLogout;

import java.util.ArrayList;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationsFragment extends Fragment implements  NotificationsContract.View{


    @BindView(R.id.list_notifications)
    RecyclerView list_notifications;

    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;

    @BindView(R.id.refresh_notificacions)
    SwipeRefreshLayout refresh_notificacions;

    @BindView(R.id.a1)
    RelativeLayout a1;

    @BindView(R.id.title_no_data)
    TextView title_no_data;

    @BindView(R.id.body_no_data)
    TextView body_no_data;





    @SuppressLint("MissingPermission")
    @OnClick(R.id.delete_trash)
    void delete_trash() {
        DialogDeletes  dialogDeletes = new DialogDeletes(getContext(),0,1);
        dialogDeletes.setListener(new delete() {
            @Override
            public void delete(boolean delete) {
                presenter.deleteAll();
            }

            @Override
            public void deleteOne(int id) {

            }
        });
        dialogDeletes.show();
    }




    NotificationsAdapter notificationsAdapter;
    NotificationsPresenter presenter;
    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsAdapter = new NotificationsAdapter(getContext());
        notificationsAdapter.setListener(new notifications_events() {
            @Override
            public void delete(int id) {
                presenter.deleteNotification(id);
            }

            @Override
            public void deleteAll(int id) {
                DialogDeletes  dialogDeletes = new DialogDeletes(getContext(),id,0);
                dialogDeletes.setListener(new delete() {
                    @Override
                    public void delete(boolean delete) {
                    }

                    @Override
                    public void deleteOne(int id) {
                        presenter.deleteNotification(id);
                    }
                });

            }
        });
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
        refresh_notificacions.setOnRefreshListener(() -> presenter.getNotifications());
    }



    @Override
    public void showNotifications(ArrayList<NotificationBean> notificationBeans) {
        refresh_notificacions.setRefreshing(false);
        if(notificationBeans.size()>0) {
            Log.e("NOTIFICATIOSN","SI HAY");
            refresh_notificacions.setVisibility(View.VISIBLE);
            notificationsAdapter.setNotificationBeans(notificationBeans);
            a1.setVisibility(View.GONE);
        }
        else{
            Log.e("NOTIFICATIOSN","NO HAY");
            body_no_data.setText("Cuando alguien te siga,comente o publique algo nuevo apareceran notificaciones en este apartado.");
            title_no_data.setText("No hay notificaciones aun");
            root_no_data.setVisibility(View.VISIBLE);
            a1.setVisibility(View.VISIBLE);
            list_notifications.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteAllComplete() {
        notificationsAdapter.clean();
        root_no_data.setVisibility(View.VISIBLE);
        a1.setVisibility(View.VISIBLE);
        list_notifications.setVisibility(View.GONE);
    }


    public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<NotificationBean> notificationBeans = new ArrayList<>();
        Context context;
        notifications_events listener;

        public notifications_events getListener() {
            return listener;
        }

        public void setListener(notifications_events listener) {
            this.listener = listener;
        }

        public NotificationsAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<NotificationBean> getNotificationBeans() {
            return notificationBeans;
        }

        public void setNotificationBeans(ArrayList<NotificationBean> notificationBeans) {
            this.notificationBeans.clear();
            this.notificationBeans.addAll(notificationBeans);
            notifyDataSetChanged();
        }

        public void clean(){
            this.notificationBeans.clear();
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
                Glide.with(context).load(notificationBeans.get(position).getUrl_resource()).into(h.ic_notification);
                h.delete_notification.setOnClickListener(v -> {
                    if(listener!=null)
                        listener.delete(notificationBeans.get(position).getId_database());
                    notificationBeans.remove(position);
                    notifyDataSetChanged();
                });
                if(notificationBeans.get(position).getType_notification()==0) {
                    h.root_notification.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            return false;
                        }
                    });
                    h.delete_notification.setVisibility(View.GONE);
                    h.image_extra.setVisibility(View.VISIBLE);
                    Glide.with(context).load(notificationBeans.get(position).getUrl_image_extra()).into(h.image_extra);
                }else{
                    h.delete_notification.setVisibility(View.VISIBLE);
                    h.image_extra.setVisibility(View.GONE);
                }


        }

        @Override
        public int getItemCount() {
            return notificationBeans.size();
        }

        public class notificationsHOlder extends RecyclerView.ViewHolder{
           TextView title_notification,body_notification;
           RelativeLayout root_notification;
           ImagenCircular ic_notification;
           RelativeLayout delete_notification;
           ImageView image_extra;
            public notificationsHOlder(@NonNull View itemView) {
                super(itemView);
                ic_notification = itemView.findViewById(R.id.ic_notification);
                title_notification = itemView.findViewById(R.id.title_notification);
                body_notification = itemView.findViewById(R.id.body_notification);
                root_notification = itemView.findViewById(R.id.root_notification);
                delete_notification = itemView.findViewById(R.id.delete_notification);
                image_extra = itemView.findViewById(R.id.image_extra);
            }
        }
    }

    public interface  notifications_events{
        void delete(int id);
        void deleteAll(int id);
    }
}

