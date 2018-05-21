package com.sahariar.star.crickscorer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.sahariar.star.crickscorer.Model.PlayerModel;
import com.sahariar.star.crickscorer.Model.Team;
import com.sahariar.star.crickscorer.Others.SwipeMenuImpl;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.Player;
import com.sahariar.star.crickscorer.database.PlayerToTeam;
import com.sahariar.star.crickscorer.database.TeamDB;

import java.util.ArrayList;
import java.util.List;


public class TeamsListFragment extends Fragment {
    Dialog addTeam;
    ArrayAdapter<String> adapter;
    List<PlayerModel> pms;
    PlayerModel playerpool[];
    DB db;
    Player player;
    ArrayList<PlayerModel> selectedPlayer;
    EditText teamname;
    TeamDB teamDb;
    PlayerToTeam playerToTeam;
    List<Team> teams;
    Team teamListArray[];
    SwipeMenuListView teamlistView;
    ArrayAdapter<String> modalAdapter;
    ListView list;
    //List on the window
    ArrayAdapter<String> teamListAdapter;

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

        //databse
        db=new DB(getContext());
        player=new Player();
        teamDb=new TeamDB();
        playerToTeam=new PlayerToTeam();
        //database ends


        FloatingActionButton fab=(FloatingActionButton)root.findViewById(R.id.fab);
        fab.setEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //custom dialog goes here
               showPopDialog();


            }
        });


        //generating the list--start
        teams=teamDb.getAllTeams(db.getReadableDatabase());
        teamListArray= teams.toArray(new Team[teams.size()]);

        List<String> teamnames=new ArrayList<>();
        for(Team t:teams)
        {
            teamnames.add(t.getName());
        }
        teamListAdapter=new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,teamnames);
        teamlistView=(SwipeMenuListView)root.findViewById(R.id.teamlist);
        teamlistView.setAdapter(teamListAdapter);
        teamlistView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        teamlistView.setMenuCreator(new SwipeMenuImpl(getContext()));
        teamlistView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        int id=teamListArray[position].getId();
                        teamDb.delete(db.getWritableDatabase(),id);
                        resetList();

                        break;

                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });
        teamlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Team_id is : ",teamListArray[position].getId()+"");
                ChangeFragment(new TeamPlayersFragment(),teamListArray[position].getId());
            }
        });


        //generating the list--end


        return root;
    }

    public void resetList()
    {
        teamListAdapter.clear();
        teams=teamDb.getAllTeams(db.getReadableDatabase());
        teamListArray=teams.toArray(new Team[teams.size()]);
        List<String> teamnamelist=new ArrayList<>();
        for(Team t:teamListArray)
        {
            teamnamelist.add(t.getName());
        }
        teamListAdapter.addAll(teamnamelist);

    }
    public void ChangeFragment(Fragment fragment, long id)
    {
        Bundle bundle=new Bundle();
        bundle.putString("team_id",id+"");
        fragment.setArguments(bundle);

        FragmentManager fm=getActivity().getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        ft.replace(R.id.main_fragments_container,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.addToBackStack(null);
        ft.commit();

    }

     public void showPopDialog()
     {
         //close_btn_1




         selectedPlayer=new ArrayList<>();
         addTeam.setContentView(R.layout.addteanmodal);
         teamname=(EditText)addTeam.findViewById(R.id.teamnametext);
         list=(ListView)addTeam.findViewById(R.id.listplayers);

         list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

         pms=player.getAllplayers(db.getReadableDatabase());
         playerpool=pms.toArray(new PlayerModel[pms.size()]);

         List<String> items=new ArrayList<>();

         for(PlayerModel p:pms)
         {
             items.add(p.getName());
         }
          adapter=new ArrayAdapter<String>(getContext(),R.layout.rowlayout,items);
          list.setAdapter(adapter);

          list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 String  playername=((TextView)view).getText().toString();
                 PlayerModel splayer=findPlayerByName(playername);

                 if(selectedPlayer.contains(splayer))
                 {

                     selectedPlayer.remove(splayer);
                 }
                 else
                 {

                     selectedPlayer.add(splayer);
                 }
             }
         });



         Button submit=(Button)addTeam.findViewById(R.id.add);
         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Add team name
                 //add players


                 String name=teamname.getText().toString();
                 if(name.length()!=0)
                 {
                     //add team to database
                     long id=teamDb.add(db.getWritableDatabase(),name);
                     //add player to to team_player

                     for(PlayerModel p:selectedPlayer)
                     {
                        
                         playerToTeam.add(db.getWritableDatabase(),p.getId(),id);
                     }
                     resetList();


                 }


                addTeam.dismiss();




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

     public PlayerModel findPlayerByName(String name)
     {

         for(PlayerModel p:playerpool )
         {
             if(p.getName().equals(name))
             {
                 return p;
             }
         }

         return null;
     }

}
