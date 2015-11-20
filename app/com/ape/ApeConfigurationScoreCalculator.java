package com.ape;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.simple.SimpleScoreCalculator;

import java.util.ArrayList;
import java.util.List;

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
			hardScore += 100 * err.size();
		}

		for (Invite invite : solution.getInvites()) {
			List<Erreur> err = invite.asPlace();
					erreurs.addAll(err);
			hardScore += 100 * err.size();
		}

		for (TablePosition table : solution.getPositions()) {
			List<Erreur> err = table.exceededOccupation(solution.getInvites());
			erreurs.addAll(err);
			hardScore += 100 * err.size();

		}

		System.out.print(hardScore + "/");
		for (Invite invite : solution.getInvites()) {
			List<Erreur> err =invite.closeToClose();
			erreurs.addAll(err);
			hardScore += err.size();

		}
		System.out.print(hardScore + "/");

		for (Invite invite : solution.getInvites()) {
			if (invite.getPosition() != null && invite.getPosition().prev != null) {
				boolean find = false;
				for (Invite invite2 : solution.getInvites()) {
					if (invite.getPosition().prev.equals(invite2.getPosition()))
						find = true;

				}
				if (!find) {
					hardScore += 1;
				}
			}

		}
		
		solution.setErreurs(erreurs);
		System.out.print(hardScore + "/");
		System.out.println(score);
		// System.out.println(hardScore + "/" +score);
		return HardSoftScore.valueOf(-hardScore, 0);
	}

}
