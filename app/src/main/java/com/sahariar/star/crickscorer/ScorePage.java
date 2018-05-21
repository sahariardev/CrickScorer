package com.sahariar.star.crickscorer;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.sahariar.star.crickscorer.Model.Ball;
import com.sahariar.star.crickscorer.Model.Match;
import com.sahariar.star.crickscorer.Model.Over;
import com.sahariar.star.crickscorer.Model.PlayerModel;
import com.sahariar.star.crickscorer.Model.Team;
import com.sahariar.star.crickscorer.Others.FragmentChangeHelper;
import com.sahariar.star.crickscorer.database.BallDB;
import com.sahariar.star.crickscorer.database.DB;
import com.sahariar.star.crickscorer.database.MatchDB;
import com.sahariar.star.crickscorer.database.OverDB;
import com.sahariar.star.crickscorer.database.Player;
import com.sahariar.star.crickscorer.database.TeamDB;

import java.util.ArrayList;
import java.util.List;


public class ScorePage extends Fragment {


    Dialog updateRuns;
    Dialog selectDialog;
    int match_id;
    Match match;
    Team team1;
    Team team2;
    Team battingTeam;
    Team ballingteam;
    List<PlayerModel> battersList;
    PlayerModel batters[];
    List<PlayerModel> ballersList;
    PlayerModel ballers[];
    List<Ball> ballList;
    Ball [] balls;
    Dialog declareDialog;
    boolean countExtra;

    ArrayAdapter<String> mainAdapter;

    int selectedModal;
    String [] teamNames;
    DB db;
    MatchDB mdb;
    TeamDB tdb;
    Player pdb;
    OverDB odb;
    BallDB bdb;
    int totalRun;
    int totalWicket;

    PlayerModel selectedBaller;
    PlayerModel selectedBatsman;
    long currentOver_id=-1;
    boolean isNewOver=false;
    View root;
    String batsmanName="Not selected";
    String ballerName="Not selected";

    public ScorePage() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_score_page, container, false);
        countExtra=true;
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
        tdb=new TeamDB();
        pdb=new Player();
        bdb=new BallDB();
        odb=new OverDB();
        Log.v("Score",match_id+"");
        match=mdb.getOne(db.getReadableDatabase(),match_id);
        team1=tdb.getOne(db.getReadableDatabase(),match.getTeam1_id());
        team2=tdb.getOne(db.getReadableDatabase(),match.getTeam2_id());

        teamNames=new String[2];
        teamNames[0]=team1.getName();
        teamNames[1]=team2.getName();
        battingTeam=team1;
        ballingteam=team2;


        ballList=bdb.getAllBallsByMatchTeam(db.getReadableDatabase(),match_id,battingTeam.getId());
        balls=ballList.toArray(new Ball[ballList.size()]);
        if(balls.length!=0) {
            currentOver_id = balls[balls.length - 1].getOver_id();
        }

        updateRuns=new Dialog(getContext());
        selectDialog=new Dialog(getContext());
        declareDialog=new Dialog(getContext());
        Button showSummery=(Button)root.findViewById(R.id.showSummery);
        showSummery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentChangeHelper fch=new FragmentChangeHelper();
                fch.change(new SummeryFragment(),getFragmentManager(),"id",match_id+"");
            }
        });

        Button updateButton=(Button)root.findViewById(R.id.buttonforupdatescore);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedBaller==null || selectedBatsman ==null)
                {
                    Toast.makeText(getContext(),"Please select baller and batsman first",Toast.LENGTH_LONG).show();
                }
                else
                {
                    showDialog();
                }


            }
        });

        Button selectTeam=(Button)root.findViewById(R.id.selectTeam);
        selectTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedModal=0;
                listDialog();
            }
        });

        Button selectBatsman=(Button)root.findViewById(R.id.selectBatsman);
        selectBatsman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedModal=1;
                listDialog();
            }
        });
        Button selectBaller=(Button) root.findViewById(R.id.selectBaller);
        selectBaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedModal=2;
                listDialog();
            }
        });

        Button declare=(Button) root.findViewById(R.id.declare);
        declare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDelareDialog();
            }
        });

        Button selectedOver=(Button)root.findViewById(R.id.selectedOver);
        selectedOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBaller=null;
                isNewOver=true;
                updateScore();

            }
        });
        Button delete=(Button) root.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ballList.size()!=0)
                {
                     long id=balls[ballList.size()-1].getId();
                     if(ballList.size()==1)
                     {
                         isNewOver=true;
                         bdb.delete(db.getWritableDatabase(),id);
                     }
                     else
                     {
                         bdb.delete(db.getWritableDatabase(),id);
                         currentOver_id=balls[ballList.size()-2].getOver_id();
                         Log.v("Runs","OverId"+currentOver_id);
                     }
                     resetList();

                }
                else
                {
                    Toast.makeText(getContext(),"Nothing to delete",Toast.LENGTH_LONG).show();
                }
            }
        });




        updateScore();

         List<String> scores=new ArrayList<>();
        for(Ball b:balls)
        {
            String ballNumber=b.getBallNUmber();
            if(b.getType()==0)
            {
                scores.add(ballNumber+"---"+b.getRuns()+"");
            }
            else
            {
                if(b.getType()==1)
                {
                    scores.add(ballNumber+"---"+"Wide");
                }
                else if(b.getType()==2)
                {
                    scores.add(ballNumber+"---"+"No Ball");
                }
                else if(b.getType()==3)
                {
                    scores.add(ballNumber+"---"+"Bounce");
                }
                else
                {
                    scores.add(ballNumber+"---"+"Out");
                }
            }


        }

        mainAdapter=new ArrayAdapter<String>(getContext(),R.layout.listcontainer,R.id.listviewtextcontainer,scores);
        ListView list=(ListView) root.findViewById(R.id.listRuns);
        list.setAdapter(mainAdapter);


        return root;
    }
    int winTeamPos=-1;
    String comment;
    public void showDelareDialog()
    {
        declareDialog.setContentView(R.layout.declaremodal);
         ListView list=(ListView) declareDialog.findViewById(R.id.listteams);
         ArrayAdapter<String> teamSelectAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout, R.id.checkBox, teamNames);
         list.setAdapter(teamSelectAdapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

         list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 winTeamPos=position;
             }
         });




        Button submit=(Button) declareDialog.findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int team_id;
                if(winTeamPos!=-1)
                {
                    if(winTeamPos==0)
                    {
                        team_id=team1.getId();
                    }
                    else
                    {
                        team_id=team2.getId();
                    }
                    EditText text=(EditText) declareDialog.findViewById(R.id.comment);
                    comment=text.getText().toString();
                    if(comment==null)
                    {
                        comment="";
                    }

                    //update the mdb

                    mdb.declear(db.getWritableDatabase(),match_id,team_id,comment);
                    Match m=mdb.getOne(db.getReadableDatabase(),match_id);
                    Log.v("Match",m.getResult()+"");
                }


                declareDialog.dismiss();
            }
        });



        Button close=(Button)declareDialog.findViewById(R.id.closebtnteammodal);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winTeamPos=-1;
                declareDialog.dismiss();
            }
        });





        declareDialog.show();
    }

    public void resetList()
    {

        mainAdapter.clear();
        ballList=bdb.getAllBallsByMatchTeam(db.getReadableDatabase(),match_id,battingTeam.getId());
        balls=ballList.toArray(new Ball[ballList.size()]);

        List<String> scores=new ArrayList<>();
        for(Ball b:balls)
        {
            String ballNumber=b.getBallNUmber();
            if(b.getType()==0)
            {
                scores.add(ballNumber+"---"+b.getRuns()+"");
            }
            else
            {
                if(b.getType()==1)
                {
                    scores.add(ballNumber+"---"+"Wide");
                }
                else if(b.getType()==2)
                {
                    scores.add(ballNumber+"---"+"No Ball");
                }
                else if(b.getType()==3)
                {
                    scores.add(ballNumber+"---"+"Bounce");
                }
                else
                {
                    scores.add(ballNumber+"---"+"Out");
                }
            }



        }
        if(balls.length!=0) {
            currentOver_id = balls[balls.length - 1].getOver_id();
        }
        else
        {
            currentOver_id=-1;
        }

        //update the score
        updateScore();

        mainAdapter.addAll(scores);
    }
    public void updateScore()
    {
        totalRun=0;
        totalWicket=0;
        for(Ball b:balls)
        {
            totalRun=totalRun+b.getRuns();

            if(b.getType()==5)
            {
                totalWicket++;
            }
            if(!countExtra)
            {
                if(b.getType()==1 || b.getType()==2)
                {
                    totalRun--;
                }
            }
        }
        TextView textView=(TextView) root.findViewById(R.id.scoreText);
        if(balls.length!=0) {
            String scoreText = battingTeam.getName() + "  " + totalRun + "/" + totalWicket + "  " + balls[balls.length - 1].getBallNUmber();

            textView.setText(scoreText);
        }
        else
        {
               textView.setText("No run");
        }

        if(selectedBatsman==null)
        {
            batsmanName="Not selected";
        }
        else
        {
            batsmanName=selectedBatsman.getName();
        }
        if(selectedBaller==null)
        {
            ballerName="Not selected";
        }
        else
        {
            ballerName=selectedBaller.getName();
        }

        //setting the baller and batsman name
        TextView btn=(TextView)root.findViewById(R.id.batsmanName);
        btn.setText(batsmanName);
        TextView bln=(TextView) root.findViewById(R.id.ballerName);
        bln.setText(ballerName);
    }

    public long handleOver()
    {
        if(currentOver_id==-1)
        {
            isNewOver=true;
        }

        if(isNewOver)
        {
            Over over=new Over(ballingteam.getId(),battingTeam.getId());
            long overId=odb.add(db.getWritableDatabase(),over);
            isNewOver=false;
            currentOver_id=overId;

        }



        return currentOver_id;
    }

    public void showDialog()
    {
        updateRuns.setContentView(R.layout.runsmodal);

        Button buttonforout=(Button)updateRuns.findViewById(R.id.out);
        buttonforout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(5);
                ball.setRuns(0);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);

                selectedBatsman=null;
                updateRuns.dismiss();
                resetList();
            }
        });

        Button buttonforsix=(Button)updateRuns.findViewById(R.id.buttonforsix);
        buttonforsix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(0);
                ball.setRuns(6);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });

        //for 4
        Button buttonforfour=(Button)updateRuns.findViewById(R.id.buttonforfour);
        buttonforfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(0);
                ball.setRuns(4);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });

        // for 1
        Button buttonforsingle=(Button)updateRuns.findViewById(R.id.buttonforsingle);
        buttonforsingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(0);
                ball.setRuns(1);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });

        // for 2
        Button buttonfordouble=(Button)updateRuns.findViewById(R.id.buttonfordouble);
        buttonfordouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(0);
                ball.setRuns(2);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });

        //for 3
        Button buttonfortripple=(Button)updateRuns.findViewById(R.id.buttonfortripple);
        buttonfortripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(0);
                ball.setRuns(3);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });
        Button buttonfordot=(Button)updateRuns.findViewById(R.id.buttonfordot);
        buttonfordot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(0);
                ball.setRuns(0);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });
        Button buttonforbounce=(Button)updateRuns.findViewById(R.id.buttonforbounce);
        buttonforbounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(3);
                ball.setRuns(0);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });

        Button buttonfornoball=(Button)updateRuns.findViewById(R.id.buttonfornoball);
        buttonfornoball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(2);
                ball.setRuns(1);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });

        Button buttonforwide=(Button)updateRuns.findViewById(R.id.buttonforwide);
        buttonforwide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ball ball=new Ball();
                ball.setBaller_id(selectedBaller.getId());
                ball.setBatsman_id(selectedBatsman.getId());
                ball.setMatch_id(match_id);
                ball.setType(1);
                ball.setRuns(1);
                ball.setOver_id(handleOver());
                bdb.add(db.getWritableDatabase(),ball);
                updateRuns.dismiss();
                resetList();

            }
        });



        updateRuns.show();
    }
    int pos=3;
    int batPos;
    int ballPos;

    public void listDialog()
    {

        selectDialog.setContentView(R.layout.addplayertoteammodal);
        ListView list=(ListView)selectDialog.findViewById(R.id.listplayers);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Log.v("Score",ballingteam.getId()+"aaa a a ");
        ballersList=pdb.getOneTeamplayers(db.getReadableDatabase(),ballingteam.getId());
        ballers=ballersList.toArray(new PlayerModel[ballersList.size()]);
        TextView textTitle=(TextView) selectDialog.findViewById(R.id.textTitle);
        textTitle.setText("Select player");

        Button addbtn=(Button)selectDialog.findViewById(R.id.add);
        addbtn.setText("Select");



        if(selectedModal==0) {
            textTitle.setText("Select team");
             ArrayAdapter<String> teamSelectAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout, R.id.checkBox, teamNames);
             list.setAdapter(teamSelectAdapter);
        }
        else  if(selectedModal==1)
        {

            battersList=pdb.getOneTeamplayers(db.getReadableDatabase(),battingTeam.getId());
            batters=battersList.toArray(new PlayerModel[battersList.size()]);

            List<String> batterNamees=new ArrayList<>();
            for(PlayerModel p:batters)
            {
                batterNamees.add(p.getName());
            }


            ArrayAdapter<String> teamSelectAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout, R.id.checkBox, batterNamees);
            list.setAdapter(teamSelectAdapter);

        }
        else
        {
            List<String> ballerNames=new ArrayList<>();
            for(PlayerModel p:ballers)
            {
                ballerNames.add(p.getName());
            }


            ArrayAdapter<String> teamSelectAdapter = new ArrayAdapter<String>(getContext(), R.layout.rowlayout, R.id.checkBox, ballerNames);
            list.setAdapter(teamSelectAdapter);
        }


        //setting the action listener

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                  if(selectedModal==0)
                  {
                      pos=position;
                  }
                  else if(selectedModal==1)
                  {
                     batPos=position;
                  }
                  else
                  {
                     ballPos=position;
                  }





            }
        });
        Button submit=(Button) selectDialog.findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedModal==0)
                {
                    if(pos==0 || pos==1)
                    {
                        selectedBatsman=null;
                        selectedBaller=null;
                        if(pos ==0)
                        {

                            battingTeam=team1;
                            ballingteam=team2;
                        }
                        else
                        {
                            battingTeam=team2;
                            ballingteam=team1;
                        }
                        resetList();

                    }
                    Log.v("Score",battingTeam.getName());
                }
                else if(selectedModal==1)
                {

                   selectedBatsman=batters[batPos];
                    Log.v("Runs",selectedBatsman.getName());
                }
                else
                {
                    selectedBaller=ballers[ballPos];
                    Log.v("Runs",selectedBaller.getName());
                }
                updateScore();
                selectDialog.dismiss();
            }
        });



        Button close=(Button)selectDialog.findViewById(R.id.closebtnteammodal);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
            }
        });
       selectDialog.show();

    }

}
