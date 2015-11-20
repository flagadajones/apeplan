package com.ape;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.io.*;
import java.util.*;

/**
 * Created by Gaetan on 13/11/2015.
 */
public class Start {
    private static ApeTablesPlanner planner;
    private static Set<Invite> guests;
    private static Set<TablePosition> tablePositions;
    private static Set<Table> tables;
    private static String folder;
    public static void main(String [] args)
    {
                folder=args[0];
        planner = new ApeTablesPlanner();

        guests = new HashSet<>();
        tablePositions = new HashSet<>();
        tables = new HashSet<>();

        final ApeConfiguration solution = planner.planTables(apeConfiguration());
        final Set<TablePosition> tables = solution.getPositions();
        final Set<Invite> guests = solution.getInvites();
        System.out.println(solution);

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


        PrintWriter writer;
        try {
            writer = new PrintWriter(new File(folder,"result.csv"), "UTF-8");



            for (Table table : resultByInv.keySet()) {
                List<Invite> lst = resultByInv.get(table);
                lst.sort(new Comparator<Invite>() {
                    @Override
                    public int compare(Invite arg0, Invite arg1) {
                        return new CompareToBuilder().append( arg0.getPosition().getPos(),arg1.getPosition().getPos()).toComparison();

                    }
                });
                for (Invite invite : lst) {
                    writer.println(invite.getPosition().getTable() + ";"+invite.getPosition().getPos()+";"+invite.getId()+";"+invite.getNumber());
                }

            }
            writer.close();

            writer = new PrintWriter(new File(folder,"erreurs.csv"), "UTF-8");

            for (ApeConfigurationScoreCalculator.Erreur err : solution.getErreurs()) {
                writer.println(err.id+";"+err.msg);
            }

            writer.close();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }







    }

    private static ApeConfiguration apeConfiguration() {

        Map<String, Invite> map = new HashMap<String, Invite>();

        BufferedReader br = null;

        try {

            String sCurrentLine;
//"C:/Users/grua687/Desktop/APE/wedding-tables-planner-master/wedding-tables-planner-master/src/main/resources/
            br = new BufferedReader(new FileReader(
                    new File(folder,"test.csv")));

            while ((sCurrentLine = br.readLine()) != null) {
                String[] split = sCurrentLine.split(";");
                Invite invite = map.get(split[0].trim());
                if (invite == null) {
                    invite = new Invite(split[0].trim(), 0);
                    guests.add(invite);
                    map.put(invite.getId(), invite);
                }
                System.out.println(sCurrentLine);
                invite.setNumber(Integer.valueOf(split[1].trim()));
                if (!split[2].trim().equals("")) {
                    Invite close = map.get(split[2].trim());
                    if (close == null) {
                        close = new Invite(split[2].trim(), -1);
                        map.put(close.getId(), close);
                    }
                    invite.addClose(close);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(
                    new File(folder,"tables.csv")));

            while ((sCurrentLine = br.readLine()) != null) {
                String[] split = sCurrentLine.split(";");
                tables.add(new Table(Integer.valueOf(split[0].trim()), Integer.valueOf(split[1].trim())));

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
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
        return new ApeConfiguration(tables, tablePositions, guests);
    }

}
