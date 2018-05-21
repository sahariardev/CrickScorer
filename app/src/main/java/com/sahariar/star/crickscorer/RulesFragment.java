package com.sahariar.star.crickscorer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sahariar.star.crickscorer.Model.TournamentModel;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.Tournament;


/**
 * A simple {@link Fragment} subclass.
 */


public class RulesFragment extends Fragment {


    long tournament_id;
    DB db;
    boolean countExtra;
    Tournament t;
    TournamentModel tm;
    Switch s;
    public RulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_rules, container, false);
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            tournament_id=Integer.parseInt(bundle.getString("id"));
        }
        else
        {
            tournament_id=1;
        }

         db=new DB(getContext());
         t=new Tournament();

        s=(Switch) root.findViewById(R.id.switch1);
          initRules();

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int myVal=0;
                if(isChecked)
                {
                   myVal=1;
                }
                t.updateRules(db.getWritableDatabase(),tournament_id,myVal);
                initRules();

            }
        });


        return root;
    }
    public void initRules()
    {
        tm=t.getOne(db.getReadableDatabase(),tournament_id);
        if(tm.getExtra()==0)
        {
            countExtra=false;
        }
        else
        {
            countExtra=true;
        }
        s.setChecked(countExtra);

    }

}
