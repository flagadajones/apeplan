package com.ape;

import com.ape.ApeConfigurationScoreCalculator.Erreur;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.value.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.solution.Solution;

import java.util.*;

@PlanningSolution
public class ApeConfiguration implements Solution<HardSoftScore> {

	private Set<TablePosition> positions;
	private Set<Invite> invites;
	private Set<Table> tables;
	private List<Erreur> erreurs;
	
	public void setErreurs(List<Erreur> erreurs) {
		this.erreurs = erreurs;
	}
	public List<Erreur> getErreurs() {
		return erreurs;
	}

	public Set<Table> getTables() {
		return tables;
	}

	public void setTables(Set<Table> tables) {
		this.tables = tables;
	}

	public ApeConfiguration() {
		this(new HashSet<>(), new HashSet<>(), new HashSet<>());
	}

	// public DiningConfiguration(final Set<Table> tables, final Set<Guest>
	// guests) {
	public ApeConfiguration(final Set<Table> tables, final Set<TablePosition> tablePos, final Set<Invite> guests) {
		super();
		setPositions(tablePos);
		setTables(tables);
		setInvites(guests);
	}

	public void setPositions(Set<TablePosition> positions) {
		this.positions = positions;
	}

	@ValueRangeProvider(id = "positions")
	public Set<TablePosition> getPositions() {
		return positions;
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
				result.put(invite.getPosition().getTable(),
						result.get(invite.getPosition().getTable()) + invite.getPosition().getTable().getId() + " - "
								+ invite.getPosition().getPos() + " - " + invite.id + "(" + invite.number + ")" + "\n");
		}

		String res = "";

		for (Table table : tables) {
			res += result.get(table);

		}

		return res;
	}

}
