package com.sahariar.star.crickscorer;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.Tournament;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tournaments_list_fragment extends Fragment {

    Dialog addTournament;
    ArrayAdapter<String> adapter;

    public Tournaments_list_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_tournaments_list_fragment, container, false);

        addTournament=new Dialog(getContext());
        DB d=new DB(getContext());
        Tournament t=new Tournament();
        //t.Insert(d.getWritableDatabase(),"Masdair");
        String data[]=t.getTournamentList(d.getReadableDatabase());
        List<String> tournaments=new ArrayList<String>(Arrays.asList(data));
        adapter=new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,tournaments);



        FloatingActionButton fab=(FloatingActionButton)root.findViewById(R.id.fab);
        fab.setEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //custom dialog goes here
                showPopDialog();


            }
        });





        ListView list=(ListView)root.findViewById(R.id.listviewTournamnet);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ChangeFragment(new MatchesFragment());


            }
        });

        return root;
    }
    EditText text;
    DB d;
    Tournament t;

    public void ChangeFragment(Fragment fragment)
    {

        FragmentManager fm=getActivity().getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragments_container,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.addToBackStack(null);
        ft.commit();

    }
    public void showPopDialog()
    {

        addTournament.setContentView(R.layout.addtournamentsoriginal);

        text=(EditText)addTournament.findViewById(R.id.editText3);



        Button close=(Button)addTournament.findViewById(R.id.closebtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTournament.dismiss();
            }
        });
        d=new DB(getContext());
        t=new Tournament();
        Button add=(Button)addTournament.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().length()!=0)
                {
                    //Add new Tournament
                    t.Insert(d.getWritableDatabase(),text.getText().toString());
                    //Update adapter
                    adapter.clear();
                    String data[]=t.getTournamentList(d.getReadableDatabase());
                    adapter.addAll(data );
                    addTournament.dismiss();


                }
            }
        });

        addTournament.show();

    }


}
