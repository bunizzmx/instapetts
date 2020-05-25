package com.bunizz.instapetts.fragments.comentarios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.CommentariosBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.jvm.internal.PackageReference;

public class ComentariosFragment extends Fragment implements  ComentariosContract.View{

    @BindView(R.id.refresh_commentarios)
    SwipeRefreshLayout refresh_commentarios;

    @BindView(R.id.list_comentarios)
    RecyclerView list_comentarios;

    @BindView(R.id.input_commentarios)
    EditText input_commentarios;

    @BindView(R.id.image_user_comment)
    ImagenCircular image_user_comment;

    @BindView(R.id.no_comments)
    RelativeLayout no_comments;

    @BindView(R.id.root_no_data)
    RelativeLayout root_no_data;

    @BindView(R.id.title_no_data)
    TextView title_no_data;

    @BindView(R.id.body_no_data)
    TextView body_no_data;

    @BindView(R.id.text_label)
    TextView text_label;

    @BindView(R.id.desactivate_comments)
    RelativeLayout desactivate_comments;

    @BindView(R.id.layout_commentarios)
    RelativeLayout layout_commentarios;

    @BindView(R.id.comment_now)
    RelativeLayout comment_now;

    @BindView(R.id.layout_loanding_comments)
    RelativeLayout layout_loanding_comments;



    CommentsAdapter adapter;

    ComentariosPresenter presenter;

    Style style = Style.values()[12];
    Sprite drawable = SpriteFactory.create(style);


    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    int ID_POST=0;
    boolean CAN_COMMENT =true;

    private boolean loading =true;
    private boolean IS_ALL = false;
    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main)
    void back_to_main() {
      getActivity().onBackPressed();
    }


    @SuppressLint("MissingPermission")
    @OnClick(R.id.comment_now)
    void comment_now() {
       if( input_commentarios.getText().toString().trim().length()>0){
            root_no_data.setVisibility(View.GONE);
            CommentariosBean commentariosBean = new CommentariosBean();
            commentariosBean.setFecha_comentario(App.formatDateGMT(new Date()));
            commentariosBean.setId_user(App.read(PREFERENCES.ID_USER_FROM_WEB, 0));
            commentariosBean.setFoto_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH, "INVALID"));
            commentariosBean.setId_post(ID_POST);
            commentariosBean.setCommentario(input_commentarios.getText().toString());
            commentariosBean.setName_user(App.read(PREFERENCES.NAME_USER, "INVALID"));
            commentariosBean.setLikes(0);
            presenter.comment(commentariosBean);
            input_commentarios.setText("");
            adapter.addBelow(commentariosBean);
           scrolbottom();
        }

    }


    public static ComentariosFragment newInstance() {
        return new ComentariosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CommentsAdapter(getContext());
        adapter.setListener(new ListenerComments() {
            @Override
            public void onLike(int post, String document) {
                if(document!=null && !document.isEmpty())
                  presenter.likeComment(post,document);
                else
                    Log.e("DOCUMENTO_NULO","no se envia el like");
            }
        });
        presenter = new ComentariosPresenter(this,getContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
           ID_POST = bundle.getInt(BUNDLES.ID_POST);
           CAN_COMMENT = bundle.getBoolean(BUNDLES.CAN_COMMENT);
        }
    }


    public void refresh_coments(){
        if(presenter!=null && adapter!=null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                ID_POST = bundle.getInt(BUNDLES.ID_POST);
                CAN_COMMENT = bundle.getBoolean(BUNDLES.CAN_COMMENT);
            }
            adapter.clear();
            presenter.getComentarios(ID_POST);
            Glide.with(getContext()).load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"))
                    .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder))
                    .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                    .into(image_user_comment);
            if(!CAN_COMMENT){
                comment_now.setVisibility(View.GONE);
                input_commentarios.setVisibility(View.GONE);
                desactivate_comments.setVisibility(View.VISIBLE);
            }else
            {
                comment_now.setVisibility(View.VISIBLE);
                input_commentarios.setVisibility(View.VISIBLE);
                desactivate_comments.setVisibility(View.GONE);
            }
        }else{
            Log.e("AUN_NO_ESTA_CONSTRUIDO","EJECUTO ONCREATE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comentarios_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        list_comentarios.setLayoutManager(new LinearLayoutManager(getContext()));
        list_comentarios.setAdapter(adapter);
        presenter.getComentarios(ID_POST);
        refresh_commentarios.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getComentarios(ID_POST);
            }
        });
        Glide.with(getContext())
                .load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"))
                .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder))
                .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                .into(image_user_comment);

        if(!CAN_COMMENT){
            comment_now.setVisibility(View.GONE);
            input_commentarios.setVisibility(View.GONE);
            desactivate_comments.setVisibility(View.VISIBLE);
        }else
        {
            comment_now.setVisibility(View.VISIBLE);
            input_commentarios.setVisibility(View.VISIBLE);
            desactivate_comments.setVisibility(View.GONE);
        }

        input_commentarios.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(input_commentarios.getText().toString().trim().length()>0)
                    text_label.setTextColor(getContext().getResources().getColor(R.color.primary));
                else
                    text_label.setTextColor(Color.parseColor("#888888"));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(input_commentarios.getText().toString().trim().length()>0)
                    text_label.setTextColor(getContext().getResources().getColor(R.color.primary));
                else
                    text_label.setTextColor(Color.parseColor("#888888"));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        list_comentarios.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (dy > 0) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if(loading){
                            loading = false;
                            if(IS_ALL == false) {
                                layout_loanding_comments.setVisibility(View.VISIBLE);
                                Log.e("DONWLOAD_MORE_COMMENTS","SI");
                                refresh_commentarios.setVisibility(View.VISIBLE);
                                presenter.loadNextComments(ID_POST);
                            }else {
                                Log.e("DONWLOAD_MORE_COMMENTS","NO");
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void showComments(ArrayList<CommentariosBean> commentariosBeans) {
        spin_kit.setVisibility(View.GONE);
        refresh_commentarios.setRefreshing(false);
        if(commentariosBeans.size()>0){
            root_no_data.setVisibility(View.GONE);
            no_comments.setVisibility(View.GONE);
            adapter.setData(commentariosBeans);
        }else
        {
            body_no_data.setText(getContext().getResources().getString(R.string.first_comment));
            title_no_data.setText(getContext().getResources().getString(R.string.no_commnets));
            root_no_data.setVisibility(View.VISIBLE);
            no_comments.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showNextComments(ArrayList<CommentariosBean> commentariosBeans) {
        spin_kit.setVisibility(View.GONE);
        loading =true;
        refresh_commentarios.setRefreshing(false);
        layout_loanding_comments.setVisibility(View.GONE);
        if(commentariosBeans.size()>0){
            root_no_data.setVisibility(View.GONE);
            no_comments.setVisibility(View.GONE);
            adapter.setDataBelow(commentariosBeans);
        }else{
            IS_ALL =true;
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.full_comments),Toast.LENGTH_LONG).show();
        }
    }


    public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<CommentariosBean> data = new ArrayList<>();
        Context context;

        ListenerComments listener;

        public CommentsAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<CommentariosBean> getData() {
            return data;
        }

        public void setData(ArrayList<CommentariosBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public void setDataBelow(ArrayList<CommentariosBean> data) {
            Log.e("INSERT_DATA_BELOW","TRUE: " + data.size());
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public ListenerComments getListener() {
            return listener;
        }

        public void setListener(ListenerComments listener) {
            this.listener = listener;
        }

        public void addBelow(CommentariosBean commentariosBean){
            this.data.add(commentariosBean);
            notifyDataSetChanged();
        }

        public void clear(){
            this.data.clear();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentarios,parent,false);
            return new CommentsHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CommentsHolder h = (CommentsHolder)holder;

            if(data.get(position).isIs_liked())
                h.icon_like_comment.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_comment_on));
            else
                h.icon_like_comment.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_comment_off));

            h.item_comentarios_name.setText(data.get(position).getName_user());
            Glide.with(context).load(data.get(position).getFoto_user())
                    .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder))
                    .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                    .into(h.imagen_user_comment);
            h.item_comentarios_comentario.setText(data.get(position).getCommentario());
            h.fecha_comment.setText(App.getInstance().fecha_lenguaje_humano(data.get(position).getFecha_comentario()));
            h.like_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.get(position).isIs_liked()) {
                        data.get(position).setIs_liked(false);
                        h.icon_like_comment.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_comment_off));
                        if(data.get(position).getLikes() - 1 > 0)
                           h.n_likes_comment.setText((data.get(position).getLikes() - 1) + " Me gusta");
                        else
                            h.n_likes_comment.setText(" Me gusta");
                    }
                    else {
                        data.get(position).setIs_liked(true);
                        h.icon_like_comment.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_comment_on));
                        h.n_likes_comment.setText((data.get(position).getLikes() + 1) + " Me gusta");
                        listener.onLike(data.get(position).getId_post(),data.get(position).getId_document());
                    }
                }
            });
            if(data.get(position).getLikes() == 0)
               h.n_likes_comment.setText("Me gusta");
            else
                h.n_likes_comment.setText(data.get(position).getLikes() + " Me gusta");
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class CommentsHolder extends RecyclerView.ViewHolder{
             TextView item_comentarios_name;
             ImagenCircular imagen_user_comment;
             TextView item_comentarios_comentario,fecha_comment;
             RelativeLayout like_comment;
             ImageView icon_like_comment;
             TextView n_likes_comment;
            public CommentsHolder(@NonNull View itemView) {
                super(itemView);
                item_comentarios_name = itemView.findViewById(R.id.item_comentarios_name);
                imagen_user_comment = itemView.findViewById(R.id.imagen_user_comment);
                item_comentarios_comentario = itemView.findViewById(R.id.item_comentarios_comentario);
                fecha_comment = itemView.findViewById(R.id.fecha_comment);
                like_comment = itemView.findViewById(R.id.like_comment);
                icon_like_comment = itemView.findViewById(R.id.icon_like_comment);
                n_likes_comment = itemView.findViewById(R.id.n_likes_comment);
            }
        }
    }

    public interface ListenerComments{
        void onLike(int post,String document);
    }

    private void scrolbottom(){
        list_comentarios.scrollToPosition(adapter.getItemCount() - 1);
    }
}
