package com.sahariar.star.crickscorer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sahariar.star.crickscorer.Model.Match;
import com.sahariar.star.crickscorer.Model.PlayerDetail;
import com.sahariar.star.crickscorer.Model.Team;
import com.sahariar.star.crickscorer.database.BallDB;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.MatchDB;
import com.sahariar.star.crickscorer.database.TeamDB;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummeryFragment extends Fragment {


    DB db;
    MatchDB mdb;
    TeamDB tdb;
    BallDB bdb;
    long match_id;
    Team team1;
    Team team2;
    Match match;
    String team1Name;
    String team2Name;
    List<PlayerDetail> team1players;
    List<PlayerDetail> team2players;

    public SummeryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            match_id=Integer.parseInt(bundle.getString("id"));

        }
        else
        {
            match_id=0;
        }

        View root= inflater.inflate(R.layout.fragment_summery, container, false);


        db=new DB(getContext());
        mdb=new MatchDB();
        tdb=new TeamDB();
        bdb=new BallDB();
        match=mdb.getOne(db.getReadableDatabase(),match_id);
        team1=tdb.getOne(db.getReadableDatabase(),match.getTeam1_id());
        team2=tdb.getOne(db.getReadableDatabase(),match.getTeam2_id());
        team1players=bdb.getSameTeamPlayersDetail(db.getReadableDatabase(),team1.getId(),match_id);
        team2players=bdb.getSameTeamPlayersDetail(db.getReadableDatabase(),team2.getId(),match_id);


        TextView teamname1=(TextView) root.findViewById(R.id.teamname1);
        teamname1.setText(team1.getName());

        TextView teamname2=(TextView) root.findViewById(R.id.teamname2);
        teamname2.setText(team2.getName());

        List<String> team1StringDetail=new ArrayList<>();
        List<String> team2StringDetail=new ArrayList<>();

        for(PlayerDetail p:team1players)
        {
            String s="";
            s=p.getName()+" "+p.getTotalrunsscored()+"-"+p.getTotalballplayed()+"-"+p.getSixes()+"-"+p.getFours()+" || "+p.getRunsgiven()+"-"+p.getOvers()+"-"+p.getWickets()+"-"+p.getEconomy();
            team1StringDetail.add(s);
        }
        for(PlayerDetail p:team2players)
        {
            String s="";
            s=p.getName()+" "+p.getTotalrunsscored()+"-"+p.getTotalballplayed()+"-"+p.getSixes()+"-"+p.getFours()+" || "+p.getRunsgiven()+"-"+p.getOvers()+"-"+p.getWickets()+"-"+p.getEconomy();
            team2StringDetail.add(s);
        }

        ArrayAdapter<String> listadapter1=new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,team1StringDetail);
        ArrayAdapter<String> listadapter2=new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,team2StringDetail);

        ListView team1list=(ListView) root.findViewById(R.id.team1listview);
        team1list.setAdapter(listadapter1);
        ListView team2list=(ListView) root.findViewById(R.id.team2listview);
        team2list.setAdapter(listadapter2);





        return root;

    }

}
