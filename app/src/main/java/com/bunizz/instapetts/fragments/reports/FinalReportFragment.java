package com.bunizz.instapetts.fragments.reports;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.ReportListBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.info.InfoPetPresenter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bunizz.instapetts.constantes.BUNDLES.PETBEAN;

public class FinalReportFragment extends Fragment implements ReportsContract.View {

    ReportPresenter presenter;

    changue_fragment_parameters_listener listener;


    @BindView(R.id.motivo_denuncia_label)
    TextView motivo_denuncia_label;

    String motivo="";

    public static FinalReportFragment newInstance() {
        return new FinalReportFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ReportPresenter(this,getContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
            motivo  = bundle.getString("MOTIVO");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        motivo_denuncia_label.setText(motivo);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.final_report_fragment, container, false);
    }

    @Override
    public void showListReports(ArrayList<ReportListBean> reportListBeans) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

}
