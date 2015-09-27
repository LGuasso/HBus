/*
 * ************************************************************************************************
 * Juramento do Programador no desenvolvimento de Código Limpo:
 *
 *  Antes de codificar, me colocarei na posição dos outros colaboradores desenvolvedores,
 *  buscando me expressar de maneira simples, logo:
 *
 *  - Nomearei as entidades como classes, métodos e variáveis com nomes significativos, pronunciáveis
 *  e pesquisável, que revelem a sua verdadeira e atual intenção;
 *
 *  - Farei com que cada método e cada deve ter apenas uma única responsabilidade, caso contrário,
 *  deve ser refatorados em métodos unitários;
 *
 *  - Ao comentar sobre uma entidade, deixarei claro qual é o seu papel atual e sugerirei  melhorias
 *  futuras, se for o caso; Atualizar comentários sempre quando atualizar código;
 * ************************************************************************************************
 */

package br.com.expressobits.hbus.ui.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Random;


public class ContentPagerAdapter extends FragmentPagerAdapter {

//    public static int pagerCount = 3;
    private Random random = new Random();
    private final ArrayList<Fragment> mFragments;


    public ContentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }


    @Override
    public Fragment getItem(int i) {
//        return PagerFragment.newInstance(0xff000000 | random.nextInt(0x00ffffff));
        return mFragments.get(i);
    }


    @Override
    public int getCount() {
//        return pagerCount;
        return mFragments.size();
    }



}
