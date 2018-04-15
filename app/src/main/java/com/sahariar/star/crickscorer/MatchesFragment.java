package com.sahariar.star.crickscorer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment {


    ArrayAdapter<String> adapter;
    FragmentChangeHelper changer;

    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_matches, container, false);

        changer=new FragmentChangeHelper();
        String mathces[]={"firts","second"};
        List<String> m=new ArrayList<>(Arrays.asList(mathces));

        adapter=new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,m);


        ListView list=(ListView)root.findViewById(R.id.listviewmatches);
         list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                changer.change(new ScorePage(),getFragmentManager(),0);
            }
        });



        return root;
    }

}
