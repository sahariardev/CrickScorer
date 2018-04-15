package com.sahariar.star.crickscorer;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TeamsListFragment extends Fragment {
    Dialog addTeam;
    ArrayAdapter<String> adapter;


    public TeamsListFragment() {
        // Required empty public constructor
    }



  //  Dialog addTournament;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_teams_list, container, false);

        addTeam=new Dialog(getContext());


        FloatingActionButton fab=(FloatingActionButton)root.findViewById(R.id.fab);
        fab.setEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //custom dialog goes here
               showPopDialog();


            }
        });


        return root;
    }

    ArrayList<String> selectedTeams=new ArrayList<String>();


     public void showPopDialog()
     {
         //close_btn_1





         addTeam.setContentView(R.layout.addteanmodal);

         ListView list=(ListView)addTeam.findViewById(R.id.listplayers);

         list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

         String[] items={"Bangladesh","India","China","Srilanka","India","China","Srilanka","India","China","Srilanka","India","China","Srilanka","India","China","Srilanka","India","China","Srilanka"};


         ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),R.layout.rowlayout,R.id.checkBox,items);
         list.setAdapter(adapter);
         list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 String  team=((TextView)view).getText().toString();

                 if(selectedTeams.contains(team))
                 {
                     selectedTeams.remove(team);
                 }
                 else
                 {
                     selectedTeams.add(team);
                 }
             }
         });


         Button close=(Button)addTeam.findViewById(R.id.closebtnteammodal);
         close.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 addTeam.dismiss();
             }
         });
         addTeam.show();

     }

}
