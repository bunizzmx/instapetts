package com.bunizz.instapetts.fragments.reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.ReportBean;
import com.bunizz.instapetts.beans.ReportListBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
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
import butterknife.OnClick;

import static com.bunizz.instapetts.constantes.BUNDLES.PETBEAN;

public class FinalReportFragment extends Fragment implements ReportsContract.View {

    ReportPresenter presenter;

    changue_fragment_parameters_listener listener;


    @BindView(R.id.motivo_denuncia_label)
    TextView motivo_denuncia_label;

    @BindView(R.id.caracteres_report)
    TextView caracteres_report;

    @BindView(R.id.description_report)
    EditText description_report;


    String motivo="";
    int id_recurso=0;
    int typo_recurso=0;
    int id_motivo=0;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.send_report)
    void send_report() {
        ReportBean reportBean = new ReportBean();
        reportBean.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        reportBean.setDescripcion(description_report.getText().toString());
        reportBean.setMotivo(motivo);
        reportBean.setType_recurso(typo_recurso);
        reportBean.setId_recurso(id_recurso);
        reportBean.setId_motivo(id_motivo);
        reportBean.setTarget(WEBCONSTANTS.NEW);
         presenter.SendReport(reportBean);
    }


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
            id_motivo = bundle.getInt("ID_MOTIVO");
            id_recurso =  bundle.getInt("ID_RECURSO");
            typo_recurso = bundle.getInt("TYPO_RECURSO");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        motivo_denuncia_label.setText(motivo);
        description_report.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                caracteres_report.setText(description_report.getText().toString().length() + "/180");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.final_report_fragment, container, false);
    }

    @Override
    public void showListReports(ArrayList<ReportListBean> reportListBeans) { }

    @Override
    public void reportSended() {
        Toast.makeText(getContext(),getContext().getString(R.string.report_sended),Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }

}
