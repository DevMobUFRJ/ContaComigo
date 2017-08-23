package com.devmob.contacomigo.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.PessoaProdutoDAO;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

/**
 * Created by DevMob on 15/05/2017.
 */

public class BottomSheetMenuPessoa extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetPessoa";

    Pessoa pessoa;
    PessoaFragmento pessoaFragmento;
    RelativeLayout btn_cancel;
    TextView mTitulo;
    private RelativeLayout btn_edit;
    public RelativeLayout btn_delete;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public void setPessoaFragmento(PessoaFragmento frag){
        pessoaFragmento = frag;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        final PessoaDAO pessoaDAO = new PessoaDAO(getActivity());
        final PessoaProdutoDAO ppDao = new PessoaProdutoDAO(getActivity());
        pessoa = pessoaDAO.getPessoaById(getArguments().getInt("idPessoa"));

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        btn_cancel = (RelativeLayout) contentView.findViewById(R.id.btn_cancel);
        btn_edit = (RelativeLayout) contentView.findViewById(R.id.btn_edit);
        btn_delete = (RelativeLayout) contentView.findViewById(R.id.btn_delete);
        mTitulo = (TextView) contentView.findViewById(R.id.titulo);
        mTitulo.setText(pessoa.getNome());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pessoaDAO.deletaPessoa(pessoa);
                pessoaDAO.deletaRelacao(pessoa);
                PessoaFragmento.listAdapter.deletaLista(pessoa);

                Toast.makeText(getActivity(), "deletando " + pessoa.getNome(), Toast.LENGTH_SHORT).show();
                dismiss();

            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit tocado", Toast.LENGTH_SHORT).show();
            }
        });
        mTitulo = (TextView) contentView.findViewById(R.id.titulo);
        //mTitulo.setText(produto.getNome());

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        // this works fine but fires one time too often for my use case, it fires on screen rotation as well, although this is a temporarily dismiss only
        Log.d(TAG, "onDismiss: FOI");
        pessoaFragmento.atualizaListas();
    }
}
