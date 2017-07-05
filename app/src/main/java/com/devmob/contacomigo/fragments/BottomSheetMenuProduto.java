package com.devmob.contacomigo.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.AddProdutoActivity;
import com.devmob.contacomigo.dao.PessoaProdutoDAO;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Produto;

/**
 * Created by DevMob on 15/05/2017.
 */

public class BottomSheetMenuProduto extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetProduto";

    Produto produto;
    ItemFragmento itemFragmento;
    RelativeLayout btn_cancel;
    TextView mTitulo;
    RelativeLayout mRL1;
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

    public void setItemFragmento(ItemFragmento frag){
        itemFragmento = frag;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final ProdutoDAO dao = new ProdutoDAO(getActivity());
        final PessoaProdutoDAO dao2 = new PessoaProdutoDAO(getActivity());
        produto = dao.getProdutoById(getArguments().getInt("idProd"));

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        btn_cancel = (RelativeLayout) contentView.findViewById(R.id.btn_cancel);
        btn_delete = (RelativeLayout) contentView.findViewById(R.id.btn_delete);
        mRL1 = (RelativeLayout) contentView.findViewById(R.id.rl1);
        mTitulo = (TextView) contentView.findViewById(R.id.titulo);
        mTitulo.setText(produto.getNome());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.deletaProduto(produto);
                dao.deletaRelacao(produto);
                ItemFragmento.listAdapter.deletaLista(produto);
                Toast.makeText(getActivity(), "deletando " + produto.getNome(), Toast.LENGTH_SHORT).show();
                dismiss();

            }
        });
        mRL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Relative tocado", Toast.LENGTH_SHORT).show();
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
        itemFragmento.atualizaListas();
    }
}
