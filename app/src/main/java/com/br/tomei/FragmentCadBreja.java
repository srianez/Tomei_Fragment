package com.br.tomei;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.br.tomei.api.BrejaAPI;
import com.br.tomei.model.Breja;
import com.br.tomei.util.RetroFit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentCadBreja extends Fragment{

    private EditText txtNome;
    private EditText txtDescricao;
    private Spinner txtTipo;
    private RatingBar ratingBar;
    private ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    public FragmentCadBreja() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FloatingActionButton btSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_cad_breja, container, false);
        btSave = v.findViewById(R.id.btSalvarBreja);

        txtNome = v.findViewById(R.id.txtNome);
        txtTipo = v.findViewById(R.id.txtTipo);
        txtDescricao = v.findViewById(R.id.txtDescricao);
        ratingBar = v.findViewById(R.id.ratingBar);

        progressDialog = new ProgressDialog(getContext());

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtNome.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.errorNameBeerRequired), Toast.LENGTH_LONG).show();
                }

                showProgress( getString(R.string.Beer), getString(R.string.savingBeer));

                RetroFit retroFit = new RetroFit();
                BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);

                Breja item = new Breja();

                item.setNome(txtNome.getText().toString());
                item.setTipo(String.valueOf(txtTipo.getSelectedItem()));
                item.setDescricao(txtDescricao.getText().toString());
                item.setAvaliacao(ratingBar.getRating());

                api.salvarItem(item)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                dismissProgress();
                                Toast.makeText(getContext(),
                                        getString(R.string.saveBeerSucess), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                dismissProgress();
                                Toast.makeText(getContext(),
                                        getString(R.string.errorBeerCreated), Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

        return v;

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void showProgress(String titulo, String mensagem) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(getContext());

        if(!progressDialog.isShowing()) {
            progressDialog.setMessage(mensagem);
            progressDialog.setTitle(titulo);
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }

}
