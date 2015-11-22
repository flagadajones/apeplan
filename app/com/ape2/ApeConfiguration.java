package com.ape2;

import com.ape2.ApeConfigurationScoreCalculator.Erreur;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;

import java.util.*;

@PlanningSolution
public class ApeConfiguration implements Solution<HardSoftScore> {

	private Set<Invite> invites;
	private Set<Table> tables;
	private List<Erreur> erreurs;
	Map<String,List<Invite>> mapBody;
	public void setErreurs(List<Erreur> erreurs) {
		this.erreurs = erreurs;
	}
	public List<Erreur> getErreurs() {
		return erreurs;
	}

	public void setMapBody(Map<String, List<Invite>> mapBody) {
		this.mapBody = mapBody;
	}

	public Map<String, List<Invite>> getMapBody() {
		return mapBody;
	}

	@ValueRangeProvider(id = "positions")

	public Set<Table> getTables() {
		return tables;
	}

	public void setTables(Set<Table> tables) {
		this.tables = tables;
	}

	public ApeConfiguration() {
		this(new HashSet<>(),  new HashSet<>(),null);
	}

	// public DiningConfiguration(final Set<Table> tables, final Set<Guest>
	// guests) {
	public ApeConfiguration(final Set<Table> tables, final Set<Invite> guests, Map<String,List<Invite>> mapBody) {
		super();
		setTables(tables);
		setInvites(guests);
		setMapBody(mapBody);
	}



	public void setInvites(Set<Invite> invites) {
		this.invites = invites;
	}

	@PlanningEntityCollectionProperty
	public Set<Invite> getInvites() {
		return invites;
	}

	@Override
	public HardSoftScore getScore() {
		return score;
	}

	@Override
	public void setScore(final HardSoftScore score) {
		this.score = score;
	}

	private HardSoftScore score;

	@Override
	public Collection<? extends Object> getProblemFacts() {
		final List<Object> facts = new ArrayList<Object>();
		facts.addAll(invites);
		return facts;
	}

	@Override
	public String toString() {
		Map<Table, String> result = new HashMap<Table, String>();
		for (Table table : tables) {
			result.put(table, "");

		}
		for (Invite invite : invites) {
			if (invite.getPosition() != null)
				result.put(invite.getPosition(),
						result.get(invite.getPosition()) + invite.getPosition().getId() + " - "
								+  " - " + invite.id + "(" + invite.number + ")" + "\n");
		}

		String res = "";

		for (Table table : tables) {
			res += result.get(table);

		}

		return res;
	}

}
