package controllers;

import com.ape2.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.CompareToBuilder;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.*;
import play.libs.Json;
/**
 * Created by Gaetan on 20/11/2015.
 */
public class Application3 extends Controller {
private static class Tab{
public int id;
public int nb;
}
private static class Invites{
public String id;
public int nb;
public int grp;
public String cte;
}
private static class Content{
public List<Application3.Tab> tables;
public List<Application3.Invites> invites;
}
    public static Result index() {
        Set<Table> tables = new HashSet<Table>();
        Map<String, Invite> map = new HashMap<String, Invite>();
        Set<Invite> guests;
		Map<String,List<Invite>> mapBody = new HashMap<>();
        guests = new HashSet<>();
        Http.RequestBody body = request().body();
		
		Application3.Content content= Json.fromJson(request().body().asJson(), Application3.Content.class);
		
        for (Tab tab: content.tables  ) {
            tables.add(new Table(tab.id, tab.nb));
		}
		for(Invites inv :content.invites){
				List<Invite> invites = mapBody.get(String.valueOf(inv.grp));
                if(invites==null){
                    invites= new ArrayList<Invite>();
                    mapBody.put(String.valueOf(inv.grp),invites);
                }

                Invite invite= new Invite(inv.id, 0);

                invite.setGroupe(inv.grp);
                invite.setContrainte(inv.cte);
                guests.add(invite);
				invites.add(invite);
                invite.setNumber(inv.nb);
        }



           for (List<Invite> invites:mapBody.values() ) {
				Invite last =null;
				int grpNum=0;
				String contrainte="";
				for (Invite invite : invites) {
				    grpNum+=invite.getNumber();
					if(!"".equals(invite.getContrainte().trim())){
						contrainte=invite.getContrainte().trim();
					}
            }

            for (Invite invite : invites
                 ) {
                if(last!=null){
                    invite.addClose(last);
                //last.addClose(invite);
                }
                invite.setGroupeNumber(grpNum);
                invite.setContrainte(contrainte);
                last=invite;
            }
        }
        Set<Invite> g2=new HashSet<>();
Map<Integer,Invite> groupes= new HashMap<Integer,Invite>();
        for (String grp : mapBody.keySet()){

            Invite ne=groupes.get(Integer.parseInt(grp)) ;
            if(ne==null) {
                ne = new Invite();
            groupes.put(Integer.parseInt(grp),ne);
            }
            ne.setNumber(mapBody.get(grp).get(0).getGroupeNumber());
            ne.setContrainte(mapBody.get(grp).get(0).getContrainte());
            ne.setId(grp);
            ne.setGroupe(Integer.parseInt(grp));
String cte =ne.getContrainte();
            if(cte.startsWith("D")){
                String grpDeteste=cte.replace("D","").replace("(","").replace(")","");
              //  System.out.println(cte + " "+ grpDeteste);
                Invite invDeteste=groupes.get(Integer.parseInt(grpDeteste));
                if(invDeteste==null){
                    invDeteste=new Invite();
                    groupes.put(Integer.parseInt(grpDeteste),invDeteste);

                }
                ne.getDeteste().add(invDeteste);
            }
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
                        CompareToBuilder cmp=new CompareToBuilder();//.append( arg0.getPosition().getId(),arg1.getPosition().getId());


                     //   if(arg0.getNumber()%2==0 && arg1.getNumber()%2==0)
                     //       cmp.append(arg1.getContrainte(),arg0.getContrainte());
                     //   else if(arg0.getNumber()%2!=0 && arg1.getNumber()%2!=0)
                     //       cmp.append(arg0.getContrainte(),arg1.getContrainte());

                        return cmp
                                .append(arg0.getNumber()%2,arg1.getNumber()%2)

                      //  .append(arg0.getGroupe(),arg1.getGroupe())

                                .toComparison();

                    }
                });
Invite boutTable=null;
                for (int i = 0; i < lst.size(); i++) {
                    if("B".equals(lst.get(i).getContrainte())) {
                        boutTable = lst.remove(i);
                    }
                }
                if(boutTable!=null){
                    if(boutTable.getNumber()%2==0){
                        lst=Lists.reverse(lst);
                        lst.add(boutTable);
                    }else{
                        lst.add(boutTable);
                    }
                }


				
                for (Invite inviteG : lst) {
                System.out.print(""+inviteG.getGroupe()+"["+inviteG.getNumber()+"]"+inviteG.getContrainte()+" - ");
				for (Invite invite : mapBody.get( String.valueOf(inviteG.getGroupe()))) {

                        resultSolution+=inviteG.getPosition().getId() + ";"+invite.getGroupe()+";"+invite.getId()+";"+invite.getNumber()+";"+inviteG.getContrainte()+"\n";
                }
                }
				System.out.println("");

            }
        resultSolution+="\n";

            for (ApeConfigurationScoreCalculator.Erreur err : solution.getErreurs()) {
                resultSolution+=err.id+";"+err.msg+"\n";
            }





        return ok(resultSolution);
    }

}
