package com.br.tomei;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.widget.EditText;
import android.widget.Toast;

import com.br.tomei.api.BrejaAPI;
import com.br.tomei.model.Usuario;
import com.br.tomei.util.RetroFit;

public class FragmentCadUsu extends Fragment{

    private EditText etUsuario;
    private EditText etSenha;
    private EditText etSenha2;
    private FloatingActionButton btSave;

    private ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;

    public FragmentCadUsu() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.content_cad_usuario, container, false);
        btSave = v.findViewById(R.id.btSalvarUsuario);

        etUsuario =  v.findViewById(R.id.etUsuario);
        etSenha = v.findViewById(R.id.etSenha);
        etSenha2 = v.findViewById(R.id.etSenha2);

        progressDialog = new ProgressDialog(getContext());

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etUsuario.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Informe o usuário!", Toast.LENGTH_LONG).show();
                }

                if (etSenha.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Informe a senha!", Toast.LENGTH_LONG).show();
                }

                if (etSenha.getText().toString().equals(etSenha2.getText().toString())) {

                    showProgress("Usuário", "Salvando usuário...");

                    RetroFit retroFit = new RetroFit();
                    BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);

                    Usuario usuario = new Usuario();

                    usuario.setUsuario(etUsuario.getText().toString());
                    usuario.setSenha(etSenha.getText().toString());

                    api.salvarUser(usuario)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    dismissProgress();
                                    Toast.makeText(getContext(),
                                            "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    dismissProgress();
                                    Toast.makeText(getContext(),
                                            "Ohh shiiit...deu erro! :/", Toast.LENGTH_SHORT).show();

                                }

                            });
                } else  {
                    Toast.makeText(getContext(), "As senhas devem ser iguais!", Toast.LENGTH_SHORT).show();
                    etSenha.setText("");
                    etSenha2.setText("");
                    etSenha.requestFocus();

                }

            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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
