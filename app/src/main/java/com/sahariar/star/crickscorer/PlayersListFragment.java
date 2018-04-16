package com.sahariar.star.crickscorer;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.Player;
import com.sahariar.star.crickscorer.database.Tournament;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersListFragment extends Fragment {

    Dialog addplayer;
    String playerName;
    ArrayAdapter<String> playerListAdapter;
    Player player;
    DB db;
    EditText playerNameEditText;
    SwipeMenuListView playerlistView;
    PlayerModel pms[];
    List<PlayerModel> playersAlldata;

    public PlayersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_players_list, container, false);

        //Db connect-start
         db=new DB(getContext());
        //Db connect-end

        //MODAL -start
        addplayer=new Dialog(getContext());
        FloatingActionButton fab=(FloatingActionButton)root.findViewById(R.id.fab);
        fab.setEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //custom dialog goes here
                showPopDialog();
            }
        });
        //MODAL -end

        //retriving all players and adding it to the list-start
        player=new Player();


        playersAlldata=player.getAllplayers(db.getReadableDatabase());
        pms=playersAlldata.toArray(new PlayerModel[playersAlldata.size()]);

        List<String> players=new ArrayList<>();
        for(PlayerModel pm:playersAlldata )
        {
            players.add(pm.getName());
        }

        playerListAdapter =new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,players);
        playerlistView=(SwipeMenuListView)root.findViewById(R.id.playerlist);
        playerlistView.setAdapter(playerListAdapter);
        //retriving all players and adding it to the list-end

        playerlistView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        //ListView menu--start
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {


                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        playerlistView.setMenuCreator(creator);


        playerlistView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        int id=pms[position].getId();
                        player.deletePlayer(db.getWritableDatabase(),id);
                        Log.v("id and pos","id: "+id+"  pos: "+position);

                        resetList();

                        break;

                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });
        //ListView --end




        return root;
    }

    //Show popup
    public void showPopDialog()
    {

        addplayer.setContentView(R.layout.addplayermodal);



        playerNameEditText=(EditText)addplayer.findViewById(R.id.playerName);


        Button okay=(Button)addplayer.findViewById(R.id.addplayerbutton);
        Button close=(Button)addplayer.findViewById(R.id.closebtnteammodal);

        //Okay button event
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("player","clicked"+playerName);
                playerName=playerNameEditText.getText().toString();
                if(playerName.length()>0)
                {
                    Log.v("player","added");
                    player.addPlayer(db.getWritableDatabase(),playerName);

                    resetList();
                    addplayer.dismiss();


                }
            }
        });


        //close button event
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addplayer.dismiss();
            }
        });


        addplayer.show();

    }

    public void resetList()
    {
        playerListAdapter.clear();

        playersAlldata=player.getAllplayers(db.getReadableDatabase());
        pms=playersAlldata.toArray(new PlayerModel[playersAlldata.size()]);

        //List<PlayerModel> playersAlldata=player.getAllplayers(db.getReadableDatabase());
        List<String> players=new ArrayList<>();
        for(PlayerModel pm:playersAlldata )
        {
            players.add(pm.getName());
        }


        playerListAdapter.addAll(players);
    }


}
