package com.sahariar.star.crickscorer.Others;

/**
 * Created by STAR on 3/22/2018.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sahariar.star.crickscorer.R;

public class FragmentChangeHelper {


    public void change(Fragment fragment,FragmentManager manager,int containerId)
    {
        FragmentManager fm=manager;
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragments_container,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void change(Fragment fragment,FragmentManager manager,String key,String value)
    {
        Bundle bundle =new Bundle();
        bundle.putString(key,value);
        fragment.setArguments(bundle);

        FragmentManager fm=manager;
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragments_container,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.addToBackStack(null);
        ft.commit();
    }



}
