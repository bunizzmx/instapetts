package com.bunizz.instapetts.fragments.share_post.Share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.fragments.feed.FeedPresenter;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.services.MyService;
import com.bunizz.instapetts.utils.dilogs.DialogShosePet;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSharePost extends Fragment implements  SharePostContract.View{



    @BindView(R.id.list_image_selected)
    RecyclerView list_image_selected;

    @BindView(R.id.location_user)
    TextView location_user;
    uploads listener;
    @BindView(R.id.description_post)
    EditText description_post;


    @OnClick(R.id.share_post_pet)
    void share_post_pet()
    {
       /* DialogShosePet dialogShosePet = new DialogShosePet(getActivity());
        dialogShosePet.show();*/
       // beginUploadInBackground(paths.get(0));
        PostBean post = new PostBean();
        post.setName_pet("PET 1");
        post.setDescription(description_post.getText().toString());
        post.setName_user("lcklkd");
        post.setUrl_photo_user("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C9%2Fthumbmini_38.png?alt=media&token=0d54c5e9-079b-4d81-9b74-9e7048d36e9f");
        post.setUrls_posts("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C9%2F67.jpeg?alt=media&token=5c927084-53e8-4f5e-afad-dadd3a5ec252,https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C9%2F67.jpeg?alt=media&token=5c927084-53e8-4f5e-afad-dadd3a5ec252");
        post.setDate_post("hoy");
        presenter.sendPost(post);
    }
    ArrayList<String> paths = new ArrayList<>();
    SharePostPresenter presenter;
    int is_video =0;
    ListSelectedAdapter adapter;
    public static FragmentSharePost newInstance() {
        return new FragmentSharePost();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListSelectedAdapter(getContext());
        presenter = new SharePostPresenter(this, getContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
            paths.addAll(bundle.getStringArrayList("data_pahs"));
            is_video = bundle.getInt("is_video");
        }
        adapter.setData(paths);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_image_selected.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        list_image_selected.setAdapter(adapter);
       // presenter.getLocation();
    }

    @Override
    public void postStatus(boolean status) {
        if(status){
            getActivity().finish();
        }
    }

    @Override
    public void showLocation(String corrdenadas) {
        location_user.setText(corrdenadas);
    }

    private void beginUploadInBackground(String filePath) {
        if (filePath == null) {
            Toast.makeText(getContext(), "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);
        Context context = getContext().getApplicationContext();
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra(MyService.INTENT_KEY_NAME, file.getName());
        intent.putExtra(MyService.INTENT_TRANSFER_OPERATION, MyService.TRANSFER_OPERATION_UPLOAD);
        intent.putExtra(MyService.INTENT_FILE, file);
        context.startService(intent);
        listener.onImageProfileUpdated();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener =(uploads)context;
    }
}

