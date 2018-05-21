package com.sahariar.star.crickscorer;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.sahariar.star.crickscorer.Model.Match;
import com.sahariar.star.crickscorer.Model.PlayerModel;
import com.sahariar.star.crickscorer.Model.Team;
import com.sahariar.star.crickscorer.Others.FragmentChangeHelper;
import com.sahariar.star.crickscorer.Others.SwipeMenuImpl;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.MatchDB;
import com.sahariar.star.crickscorer.database.TeamDB;

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
    TeamDB teamDb;
    SwipeMenuListView list;
    int tournament_id;
    Match matches[];
    List<Match> matchesList;
    List<Team> selectedTeams;
    List<Team> allteams;
    ArrayAdapter<String> modalAdapter;


    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_matches, container, false);

        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            tournament_id=Integer.parseInt(bundle.getString("id"));
        }
        else
        {
            tournament_id=1;
        }


        addTeamToMatch=new Dialog(getContext());
        changer=new FragmentChangeHelper();
        db=new DB(getContext());
        matchDb=new MatchDB();
        teamDb=new TeamDB();

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


                long match_id=matches[position].getId();
                Match match=matchDb.getOne(db.getReadableDatabase(),match_id);

                try
                {
                    Log.v("Error",match.getWinner_id()+"");
                    if(match.getWinner_id()!=0)
                    {
                        changer.change(new WinFragment(),getFragmentManager(),"id",matches[position].getId()+"");
                    }
                    else
                    {
                        changer.change(new ScorePage(),getFragmentManager(),"id",matches[position].getId()+"");
                    }
                }
                catch (Exception e)
                {
                   // changer.change(new ScorePage(),getFragmentManager(),"id",matches[position].getId()+"");
                }



            }
        });

        Button rules=(Button)root.findViewById(R.id.rules);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changer.change(new RulesFragment(),getFragmentManager(),"id",tournament_id+"");
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
    public Team findTeamByName(String s)
    {
        for(Team t:allteams)
        {
            if(t.getName().equals(s))
            {
                return t;
            }
        }
        return null;

    }
    public void showPopDialog()
    {
        addTeamToMatch.setContentView(R.layout.addplayertoteammodal);
        selectedTeams=new ArrayList<>();
        ListView list=(ListView)addTeamToMatch.findViewById(R.id.listplayers);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        allteams=teamDb.getAllTeams(db.getReadableDatabase());
        List<String> teamnames=new ArrayList<>();
        for(Team t:allteams)
        {
            teamnames.add(t.getName());
        }
        modalAdapter=new ArrayAdapter<String>(getContext(),R.layout.rowlayout,R.id.checkBox,teamnames);
        list.setAdapter(modalAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  teamname=((TextView)view).getText().toString();
                Team s_team=findTeamByName(teamname);

                if(selectedTeams.contains(s_team))
                {

                    selectedTeams.remove(s_team);
                }
                else
                {

                    selectedTeams.add(s_team);
                }
            }
        });
        Button submit=(Button) addTeamToMatch.findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //basically add teams to the match
                if(selectedTeams.size()==2)
                {
                    Team t1=selectedTeams.get(0);
                    Team t2=selectedTeams.get(1);
                    //helping the garbadge collector



                    matchDb.add(db.getWritableDatabase(),t1.getId(),t2.getId(),tournament_id);
                    resetList();

                    addTeamToMatch.dismiss();

                }


            }
        });


        Button close=(Button)addTeamToMatch.findViewById(R.id.closebtnteammodal);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeamToMatch.dismiss();
            }
        });


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
