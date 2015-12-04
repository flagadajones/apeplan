package com.ape2;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.simple
        .SimpleScoreCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApeConfigurationScoreCalculator implements SimpleScoreCalculator<ApeConfiguration> {
	public static class Erreur {
		public String id;
		public  String msg;

		Erreur(String id, String msg) {
			this.id = id;
			this.msg = msg;
		}
	}

	@Override
	public HardSoftScore calculateScore(ApeConfiguration solution) {
		// TODO Auto-generated method stub
		List<Erreur> erreurs = new ArrayList<Erreur>();
		int score = 0;

		// hardScore
		int hardScore = 0;

		for (Table table : solution.getTables()) {
			List<Erreur> err = table.exceededOccupation(solution.getInvites());
			erreurs.addAll(err);
			hardScore += 10000 * err.size();
		}

		for (Invite invite : solution.getInvites()) {
			List<Erreur> err = invite.asPlace();
					erreurs.addAll(err);
			hardScore += 10000 * err.size();
		}



		System.out.print(hardScore + "/");

        Map<Integer, List<Invite>> mapBody = new HashMap<>();

        for (Invite invite : solution.getInvites()) {


            List<Invite> invites = mapBody.get(invite.getGroupe());
            if (invites == null) {
                invites = new ArrayList<Invite>();
                mapBody.put(invite.getGroupe(), invites);
            }
            invites.add(invite);


        }

        for (List<Invite> invites : mapBody.values()) {
        Table table=null;
            for (Invite invite:invites
                 ) {
                if (invite.getPosition()==null)
                    continue;
                if(table==null)
                    table=invite.getPosition();
                else
                    if(! table.equals(invite.getPosition())) {
                        hardScore += 10 *invite.getGroupeNumber() ;
						erreurs.add(new Erreur(String.valueOf(invite.getGroupe()),"n'est pas complet"));

                    }}


        }

        //tous les gens du meme groupe sur la meme table


            System.out.print(hardScore + "/");



		
		solution.setErreurs(erreurs);
		System.out.print(hardScore + "/");
		System.out.println(score);
		// System.out.println(hardScore + "/" +score);
		return HardSoftScore.valueOf(-hardScore, -score);
	}

}
