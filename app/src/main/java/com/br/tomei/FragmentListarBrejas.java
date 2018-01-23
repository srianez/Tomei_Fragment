package com.br.tomei;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.br.tomei.api.BrejaAPI;
import com.br.tomei.model.Breja;
import com.br.tomei.util.RecyclerAdapter;
import com.br.tomei.util.RetroFit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragmentListarBrejas extends Fragment{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerAdapter adapter;
    private List<Breja> brejasListas = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private EditText etFiltroListaBreja;

    public FragmentListarBrejas() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_listar_brejas, container, false);
        //etFiltroListaBreja = v.findViewById(R.id.etFiltroListaBreja);

/*        if(!etFiltroListaBreja.toString().equals("") && etFiltroListaBreja.toString() !=null) {

            getBrejas(etFiltroListaBreja.toString());

            mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_recyclerbreja);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            adapter = new RecyclerAdapter(getActivity(), brejasListas);
            mRecyclerView.setAdapter(adapter);
x
        } else {*/

            getBrejas();

            mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_recyclerbreja);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            adapter = new RecyclerAdapter(getActivity(), brejasListas);
            mRecyclerView.setAdapter(adapter);
        //}

        return v;
    }

    //remove o item da lista
    public void onCloseButton(Object object)
    {
/*        final Breja u = (Breja) object;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(getWindow().getDecorView().getRootView().getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getWindow().getDecorView().getRootView().getContext());
        }
        builder.setTitle("Remover breja")
                .setMessage("Deseja remover essa breja?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        apagaBreja(u.getId());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/
    }


    public void onCustomClick(Object object)
    {
/*        Breja i = (Breja) object;

        if(i==null)
        {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Breja não encontrada", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(ListarBrejas.this, ManterBreja.class);

            intent.putExtra("Breja", i);
            startActivity(intent);
        }*/
    }

    private void getBrejas()
    {
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    RetroFit retroFit = new RetroFit();
                    BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);
                    brejasListas = api.findAll().execute().body();
                }
                catch(NetworkOnMainThreadException | IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getBrejas(final String filtro)
    {
        Thread t = (new Thread()
        {
            public void run()
            {
                try
                {
                    RetroFit retroFit = new RetroFit();
                    BrejaAPI api = retroFit.getRetrofit().create(BrejaAPI.class);
                    brejasListas = api.buscarItemNomeParc(filtro).execute().body();
                }
                catch(NetworkOnMainThreadException | IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    // TODO: Rename method, update argument and hook method into UI event
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
}