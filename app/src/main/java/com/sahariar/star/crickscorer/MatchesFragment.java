package com.sahariar.star.crickscorer;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.sahariar.star.crickscorer.Model.Match;
import com.sahariar.star.crickscorer.Others.FragmentChangeHelper;
import com.sahariar.star.crickscorer.Others.SwipeMenuImpl;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.MatchDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment {


    ArrayAdapter<String> adapter;
    FragmentChangeHelper changer;
    Dialog addTeamToMatch;
    DB db;
    MatchDB matchDb;
    SwipeMenuListView list;
    int tournament_id;
    Match matches[];
    List<Match> matchesList;


    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_matches, container, false);

        addTeamToMatch=new Dialog(getContext());
        changer=new FragmentChangeHelper();
        //db connection
        db=new DB(getContext());
        matchDb=new MatchDB();
        // get all matches assoiciated with this tournamnet


        matchesList=matchDb.getAllMatchesNyTournamentId(db.getReadableDatabase(),tournament_id);
        matches=matchesList.toArray(new Match[matchesList.size()]);

        List<String> matchNames=new ArrayList<>();
        for(Match m: matchesList)
        {
            matchNames.add(matchDb.getMatchName(db.getReadableDatabase(),m));
        }

         adapter=new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,matchNames);
         list=(SwipeMenuListView)root.findViewById(R.id.listviewmatches);
         list.setAdapter(adapter);
         list.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
         SwipeMenuCreator creator=new SwipeMenuImpl(getContext());
         list.setMenuCreator(creator);
         list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        int id=matches[position].getId();

                        matchDb.delete(db.getWritableDatabase(),id);
                        resetList();
                        break;

                }

                return true;
            }
        });



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                changer.change(new ScorePage(),getFragmentManager(),0);
            }
        });

        //press the fab icon to to open the modal
        //add matches
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
    public void showPopDialog()
    {
        addTeamToMatch.setContentView(R.layout.addplayertoteammodal);

        //generate all teams
        //show the list
        //on press oka add them to the match
        //On press cancel dismiss



        addTeamToMatch.show();
    }

    public void resetList()
    {
        adapter.clear();

        matchesList=matchDb.getAllMatchesNyTournamentId(db.getReadableDatabase(),tournament_id);
        matches=matchesList.toArray(new Match[matchesList.size()]);

        List<String> matchNames=new ArrayList<>();
        for(Match m: matchesList)
        {
            matchNames.add(matchDb.getMatchName(db.getReadableDatabase(),m));
        }
        adapter.addAll(matchNames);

    }

}
