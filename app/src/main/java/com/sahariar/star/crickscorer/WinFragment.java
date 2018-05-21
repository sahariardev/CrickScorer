package com.sahariar.star.crickscorer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sahariar.star.crickscorer.Model.Match;
import com.sahariar.star.crickscorer.Others.FragmentChangeHelper;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.MatchDB;


/**
 * A simple {@link Fragment} subclass.
 */
public class WinFragment extends Fragment {


    DB db;
    MatchDB mdb;
    int match_id;

    public WinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_win, container, false);
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            match_id=Integer.parseInt(bundle.getString("id"));

        }
        else
        {
            match_id=0;
        }

        db=new DB(getContext());
        mdb=new MatchDB();

        Match m=mdb.getOne(db.getReadableDatabase(),match_id);

        TextView text=(TextView) root.findViewById(R.id.textView4);
        text.setText(m.getResult());

        Button btn=(Button) root.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentChangeHelper fg=new FragmentChangeHelper();
                fg.change(new SummeryFragment(),getFragmentManager(),"id",match_id+"");
            }
        });



        return root;
    }

}
