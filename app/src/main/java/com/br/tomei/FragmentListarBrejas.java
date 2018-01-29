package com.br.tomei;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.br.tomei.api.BrejaAPI;
import com.br.tomei.model.Breja;
import com.br.tomei.model.Usuario;
import com.br.tomei.util.ClickRecyclerView_Interface;
import com.br.tomei.util.RecyclerAdapter;
import com.br.tomei.util.RetroFit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentListarBrejas extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerAdapter adapter;
    private List<Breja> brejasListas = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    View v;
    private EditText etFiltroListaBreja;
    String value;

    public FragmentListarBrejas() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.content_listar_brejas, container, false);

        getBrejas();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_recyclerbreja);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setLista();

        etFiltroListaBreja = v.findViewById(R.id.etFiltroListaBreja);
        etFiltroListaBreja.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Toast.makeText(getContext(), "onTextChanged", Toast.LENGTH_SHORT).show();

                getBrejasFiltro("es");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    private void setLista() {
        adapter = new RecyclerAdapter(getActivity(), brejasListas, new ClickRecyclerView_Interface() {
            @Override
            public void onClick(Object object) {
                //Toast.makeText(getContext(), ((Breja) object).getNome(), Toast.LENGTH_SHORT).show();

                RetroFit retroFit = new RetroFit();
                BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);

                api.deleteById(((Breja) object).getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), getString(R.string.beerRemoved), Toast.LENGTH_SHORT).show();
                        getBrejas();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.errorRemoveBeer), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mRecyclerView.setAdapter(adapter);
    }


    private void getBrejas() {

        RetroFit retroFit = new RetroFit();
        BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);
        api.findAll().enqueue(new Callback<List<Breja>>() {
            @Override
            public void onResponse(Call<List<Breja>> call, Response<List<Breja>> response) {
                brejasListas = response.body();
                setLista();
            }

            @Override
            public void onFailure(Call<List<Breja>> call, Throwable t) {

            }
        });

    }


    private void getBrejasFiltro(String filtro) {

        RetroFit retroFit = new RetroFit();
        BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);

        api.buscarItemNomeParc(filtro).enqueue(new Callback<List<Breja>>() {
            @Override
            public void onResponse(Call<List<Breja>> call, Response<List<Breja>> response) {
                brejasListas = response.body();
                setLista();
        }

            @Override
            public void onFailure(Call<List<Breja>> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.errorLoadingBeer), Toast.LENGTH_SHORT).show();
            }
        });

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
        void onFragmentInteraction(Uri uri);
    }
}