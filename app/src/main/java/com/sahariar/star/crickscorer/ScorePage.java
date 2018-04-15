package com.sahariar.star.crickscorer;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ScorePage extends Fragment {


    Dialog updateRuns;


    public ScorePage() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_score_page, container, false);


        updateRuns=new Dialog(getContext());
        Button updateButton=(Button)root.findViewById(R.id.buttonforupdatescore);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });



        return root;
    }

    public void showDialog()
    {
        updateRuns.setContentView(R.layout.runsmodal);
        updateRuns.show();
    }


}
