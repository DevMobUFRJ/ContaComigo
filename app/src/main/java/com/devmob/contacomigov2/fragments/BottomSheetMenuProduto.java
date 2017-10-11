package com.devmob.contacomigov2.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigov2.R;
import com.devmob.contacomigov2.activities.AddProdutoActivity;
import com.devmob.contacomigov2.activities.MainActivity;
import com.devmob.contacomigov2.dao.ProdutoDAO;
import com.devmob.contacomigov2.model.Produto;

/**
 * Created by DevMob on 15/05/2017.
 */

public class BottomSheetMenuProduto extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetProduto";

    private Produto produto;
    private ItemFragmento itemFragmento;
    private RelativeLayout btn_cancel;
    private TextView mTitulo;
    private RelativeLayout btn_delete;
    private RelativeLayout btn_edit;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final ProdutoDAO pdao = new ProdutoDAO(getActivity());
        produto = pdao.getProdutoById(getArguments().getInt("idProd"));

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        btn_cancel = (RelativeLayout) contentView.findViewById(R.id.btn_cancel);
        btn_delete = (RelativeLayout) contentView.findViewById(R.id.btn_delete);
        btn_edit = (RelativeLayout) contentView.findViewById(R.id.btn_edit);
        mTitulo = (TextView) contentView.findViewById(R.id.titulo);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String titulo = produto.getNome()+"\n"
                +produto.getQuantidade()+" x "
                +nf.format(Double.parseDouble(String.format("%.2f", produto.getPreco())))+" = "
                +nf.format(Double.parseDouble(String.format("%.2f", produto.getPreco()*produto.getQuantidade())));
        mTitulo.setText(titulo);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddProdutoActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("produtoId", produto.getId());
                startActivity(intent);
                dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdao.deletaProduto(produto);
                pdao.deletaRelacao(produto);
                MainActivity.forceReloadAllFragments();
                Toast.makeText(getActivity(), "deletando " + produto.getNome(), Toast.LENGTH_SHORT).show();
                dismiss();

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
    }
}
