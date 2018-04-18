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
import com.sahariar.star.crickscorer.Model.PlayerModel;
import com.sahariar.star.crickscorer.Others.SwipeMenuImpl;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.Player;
import com.sahariar.star.crickscorer.database.PlayerToTeam;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamPlayersFragment extends Fragment {


    PlayerModel players[];
    List<PlayerModel> playerlist;
    DB db;
    Player player;
    PlayerToTeam playerToTeam;
    long team_id;
    ArrayAdapter<String> playerListAdapter;
    SwipeMenuListView playerlistView;

    Dialog addPlayerToTeam;
    List<PlayerModel> selectedPlayers;
    List<PlayerModel> availablePlayers;
    ArrayAdapter<String> modalAdapter;

    public TeamPlayersFragment() {
        // Required empty public constructor
    }

    public List<String> getplayernamesfrommodel()
    {
        List<String> playersname=new ArrayList<>();
        for(PlayerModel p:players)
        {
            playersname.add(p.getName());

        }
        return  playersname;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_team_players, container, false);

        //getting the team id form bundle and setting the data
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            team_id=Integer.parseInt(bundle.getString("team_id"));
        }
        else
        {
            team_id=100;
        }

        //get the list data and generate it
         db=new DB(getContext());
         player=new Player();
         playerToTeam=new PlayerToTeam();
         playerlist=player.getOneTeamplayers(db.getReadableDatabase(),team_id);
         players=playerlist.toArray(new PlayerModel[playerlist.size()]);
         playerListAdapter =new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,getplayernamesfrommodel());
         playerlistView=(SwipeMenuListView)root.findViewById(R.id.playerlist);
         playerlistView.setAdapter(playerListAdapter);
         playerlistView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
         SwipeMenuCreator creator=new SwipeMenuImpl(getContext());
         playerlistView.setMenuCreator(creator);
         playerlistView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        int id=players[position].getId();
                        playerToTeam.deleteByPlayerId(db.getWritableDatabase(),id);
                        resetList();
                        break;

                }

                return true;
            }
        });
        // list data ends

        //fabicon event
        addPlayerToTeam=new Dialog(getContext());
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
        //show the dialog with excluded players

        selectedPlayers=new ArrayList<>();
        addPlayerToTeam.setContentView(R.layout.addplayertoteammodal);
        ListView list=(ListView)addPlayerToTeam.findViewById(R.id.listplayers);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //feth the data
        availablePlayers=player.getSpecificPlayer(db.getReadableDatabase(),playerlist);

        List<String> availablePlayersName=new ArrayList<>();
        for(PlayerModel p:availablePlayers)
        {
            availablePlayersName.add(p.getName());
        }
        modalAdapter=new ArrayAdapter<String>(getContext(),R.layout.rowlayout,R.id.checkBox,availablePlayersName);
        list.setAdapter(modalAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  playername=((TextView)view).getText().toString();
                PlayerModel splayer=findPlayerByName(playername);

                if(selectedPlayers.contains(splayer))
                {
                    selectedPlayers.remove(splayer);
                }
                else
                {
                    selectedPlayers.add(splayer);
                }
            }
        });

        Button submit=(Button)addPlayerToTeam.findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(PlayerModel p:selectedPlayers)
                {
                    playerToTeam.add(db.getWritableDatabase(),p.getId(),team_id);
                    resetList();
                }
                addPlayerToTeam.dismiss();
            }
        });

        Button close=(Button)addPlayerToTeam.findViewById(R.id.closebtnteammodal);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayerToTeam.dismiss();
            }
        });
        addPlayerToTeam.show();




    }
    public void resetList()
    {
        playerListAdapter.clear();
        playerlist=player.getOneTeamplayers(db.getReadableDatabase(),team_id);
        players=playerlist.toArray(new PlayerModel[playerlist.size()]);
        playerListAdapter.addAll(getplayernamesfrommodel());
    }
    public PlayerModel findPlayerByName(String name)
    {

        for(PlayerModel p:availablePlayers )
        {
            if(p.getName().equals(name))
            {
                return p;
            }
        }

        return null;
    }

}
