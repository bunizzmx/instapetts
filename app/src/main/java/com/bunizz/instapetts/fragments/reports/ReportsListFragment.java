package com.bunizz.instapetts.fragments.reports;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.ReportListBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.qr.FragmentMyQRPreview;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportsListFragment extends Fragment implements   ReportsContract.View {

    @BindView(R.id.list_causas_report)
    RecyclerView list_causas_report;

    ReportListAdapter adapter;

    ReportPresenter presenter;

    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    Style style = Style.values()[12];
    Sprite drawable = SpriteFactory.create(style);



    changue_fragment_parameters_listener listener;
    public static ReportsListFragment newInstance() {
        return new ReportsListFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ReportListAdapter(getContext());
        adapter.setListener(new changue_fragment_parameters_listener() {
            @Override
            public void change_fragment_parameter(int type_fragment, Bundle data) {
                listener.change_fragment_parameter(type_fragment,data);
            }
        });
        presenter = new ReportPresenter(this,getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        list_causas_report.setLayoutManager(new LinearLayoutManager(getContext()));
        list_causas_report.setAdapter(adapter);
        presenter.getList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_list, container, false);
    }

    @Override
    public void showListReports(ArrayList<ReportListBean> reportListBeans) {
        if(reportListBeans!=null){
            if(reportListBeans.size()>0){
                adapter.setData(reportListBeans);
                spin_kit.setVisibility(View.GONE);
            }else{
                spin_kit.setVisibility(View.GONE);
            }
        }else{
            spin_kit.setVisibility(View.GONE);
        }

    }

    @Override
    public void reportSended() {

    }


    public class ReportListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        Context context;

        ArrayList<ReportListBean> data=new ArrayList<>();

        changue_fragment_parameters_listener listener;

        public changue_fragment_parameters_listener getListener() {
            return listener;
        }

        public void setListener(changue_fragment_parameters_listener listener) {
            this.listener = listener;
        }

        public ReportListAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<ReportListBean> getData() {
            return data;
        }

        public void setData(ArrayList<ReportListBean> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_report,parent,false);
          return  new ReportListHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ReportListHolder old = (ReportListHolder) holder;
            old.descripcion_report_item.setText(data.get(position).getName());
            old.root_item_list_reports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString("MOTIVO",data.get(position).getName());
                    b.putInt("ID_MOTIVO",data.get(position).getId());
                    listener.change_fragment_parameter(FragmentElement.INSTANCE_FINAL_REPORT,b);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class ReportListHolder extends RecyclerView.ViewHolder{
           TextView descripcion_report_item;
           RelativeLayout root_item_list_reports;
            public ReportListHolder(@NonNull View itemView) {
                super(itemView);
                descripcion_report_item = itemView.findViewById(R.id.descripcion_report_item);
                root_item_list_reports = itemView.findViewById(R.id.root_item_list_reports);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }
}
