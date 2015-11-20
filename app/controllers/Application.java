package controllers;

import com.ape.*;
import org.apache.commons.lang.builder.CompareToBuilder;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Gaetan on 20/11/2015.
 */
public class Application extends Controller {

    public static Result index() {
        Set<Table> tables = new HashSet<Table>();
        Map<String, Invite> map = new HashMap<String, Invite>();
       Set<Invite> guests;
  Set<TablePosition> tablePositions;
Map<String,List<Invite>> mapBody = new HashMap<>();
        guests = new HashSet<>();
        tablePositions = new HashSet<>();
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
                    guests.add(invite);



                invite.setNumber(Integer.valueOf(cols[1].trim()));

                if (!cols[2].trim().equals("")) {
                    Invite close = map.get(cols[2].trim());
                    if (close == null) {
                        close = new Invite(cols[2].trim(), -1);
                        map.put(close.getId(), close);
                    }
                    invite.addClose(close);
                }


            }

        }

        for (List<Invite> invites:mapBody.values()
             ) {
            Invite last =null;
            for (Invite invite : invites
                 ) {
                if(last!=null){
                    invite.addClose(last);
                }
                last=invite;
            }
        }


        for (Table table : tables) {
            TablePosition prev = null;
            for (int i = 1; i <= table.getCapacity(); i++) {
                TablePosition pos = new TablePosition(table, i, prev);
                table.addPlace(pos);
                tablePositions.add(pos);
                if (prev != null)
                    prev.setNext(pos);
                prev = pos;
            }

        }





        ApeTablesPlanner planner = new ApeTablesPlanner();



        final ApeConfiguration solution = planner.planTables(new ApeConfiguration(tables, tablePositions, guests));

        new ApeConfigurationScoreCalculator().calculateScore(solution);

        Map<Table, String> result = new HashMap<Table, String>();
        Map<Table, List<Invite>> resultByInv = new HashMap<Table, List<Invite>>();
        for (Table table : solution.getTables()) {
            result.put(table, "");

        }

        for (Invite invite : solution.getInvites()) {

            if (invite.getPosition() != null){
                List<Invite> lstInv = resultByInv.get(invite.getPosition().getTable());
                if(lstInv==null){
                    lstInv= new ArrayList<Invite>();
                    resultByInv.put(invite.getPosition().getTable(), lstInv);
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
                        return new CompareToBuilder().append( arg0.getPosition().getPos(),arg1.getPosition().getPos()).toComparison();

                    }
                });
                for (Invite invite : lst) {
                    resultSolution+=invite.getPosition().getTable() + ";"+invite.getPosition().getPos()+";"+invite.getId()+";"+invite.getNumber()+"\n";
                }

            }
        resultSolution+="\n";

            for (ApeConfigurationScoreCalculator.Erreur err : solution.getErreurs()) {
                resultSolution+=err.id+";"+err.msg+"\n";
            }





        return ok(resultSolution);
    }

}
