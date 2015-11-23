package controllers;

import com.ape2.*;
import org.apache.commons.lang.builder.CompareToBuilder;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.*;

/**
 * Created by Gaetan on 20/11/2015.
 */
public class Application3 extends Controller {

    public static Result index() {
        Set<Table> tables = new HashSet<Table>();
        Map<String, Invite> map = new HashMap<String, Invite>();
       Set<Invite> guests;
 Map<String,List<Invite>> mapBody = new HashMap<>();
        guests = new HashSet<>();
        Http.RequestBody body = request().body();
       String[] bodyArray= body.asText().split("\n");
        boolean tableOk=false;
        for (String ligne: bodyArray
             ) {
            if("".equals(ligne)) {
                tableOk = true;
            continue;
            }
            String[] cols = ligne.split(";");
            if(!tableOk){

                tables.add(new Table(Integer.valueOf(cols[0].trim()), Integer.valueOf(cols[1].trim())));
            }
            else{
List<Invite> invites = mapBody.get(cols[2]);
                if(invites==null){
                    invites= new ArrayList<Invite>();
                    mapBody.put(cols[2],invites);
                }

                Invite invite= new Invite(cols[0].trim(), 0);

                invite.setGroupe(Integer.parseInt(cols[2]));
                    guests.add(invite);

invites.add(invite);

                invite.setNumber(Integer.valueOf(cols[1].trim()));

            }

        }



           for (List<Invite> invites:mapBody.values()
             ) {
            Invite last =null;
            int grpNum=0;
            for (Invite invite : invites
                    ) {
                grpNum+=invite.getNumber();

            }

            for (Invite invite : invites
                 ) {
                if(last!=null){
                    invite.addClose(last);
                //last.addClose(invite);
                }
                invite.setGroupeNumber(grpNum);
                last=invite;
            }
        }
        Set<Invite> g2=new HashSet<>();


        for (String grp : mapBody.keySet()){
            Invite ne= new Invite();
            ne.setNumber(mapBody.get(grp).get(0).getGroupeNumber());
            ne.setId(grp);
            ne.setGroupe(Integer.parseInt(grp));
            g2.add(ne);
        }










        ApeTablesPlanner planner = new ApeTablesPlanner();



        final ApeConfiguration solution = planner.planTables(new ApeConfiguration(tables, g2,mapBody));

        new ApeConfigurationScoreCalculator().calculateScore(solution);

        Map<Table, String> result = new HashMap<Table, String>();
        Map<Table, List<Invite>> resultByInv = new HashMap<Table, List<Invite>>();
        for (Table table : solution.getTables()) {
            result.put(table, "");

        }

        for (Invite invite : solution.getInvites()) {

            if (invite.getPosition() != null){
                List<Invite> lstInv = resultByInv.get(invite.getPosition());
                if(lstInv==null){
                    lstInv= new ArrayList<Invite>();
                    resultByInv.put(invite.getPosition(), lstInv);
                }
                lstInv.add(invite);
            }
        }

String resultSolution="";

            for (Table table : resultByInv.keySet()) {
                List<Invite> lst = resultByInv.get(table);
                lst.sort(new Comparator<Invite>() {
                    @Override
                    public int compare(Invite arg0, Invite arg1) {
                        return new CompareToBuilder().append( arg0.getPosition().getId(),arg1.getPosition().getId())

                                .append(arg0.getGroupe(),arg1.getGroupe())
                                .toComparison();

                    }
                });
                for (Invite inviteG : lst) {
                    for (Invite invite : mapBody.get( String.valueOf(inviteG.getGroupe()))) {

                        resultSolution+=inviteG.getPosition().getId() + ";"+invite.getGroupe()+";"+invite.getId()+";"+invite.getNumber()+"\n";
                }
                }

            }
        resultSolution+="\n";

            for (ApeConfigurationScoreCalculator.Erreur err : solution.getErreurs()) {
                resultSolution+=err.id+";"+err.msg+"\n";
            }





        return ok(resultSolution);
    }

}
