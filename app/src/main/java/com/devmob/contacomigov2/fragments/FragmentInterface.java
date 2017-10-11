package com.devmob.contacomigov2.fragments;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DevMob on 05/07/2017.
 */

public interface FragmentInterface {

    List<String> fragments = Arrays.asList("PessoaFragmento", "RestauranteFragmento", "ItemFragmento", "TotalFragmento");

    void setAtualizar(boolean b);

    void fragmentBecameVisible();

}
