package com.ape;

import com.ape.ApeConfigurationScoreCalculator.Erreur;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@PlanningEntity(difficultyComparatorClass = InviteDifficultyComparator.class)
public class Invite {
	String id;
	int number;
	TablePosition position;
	int groupeNumber;
    int groupe;

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public int getGroupeNumber() {
		return groupeNumber;
	}

	public void setGroupeNumber(int groupeNumber) {
		this.groupeNumber = groupeNumber;
	}

	public void setPosition(TablePosition position) {

		if (position != null)
			position.setInvite(this);
		else
			this.position.setInvite(null);
		this.position = position;
	}

	@PlanningVariable(valueRangeProviderRefs = { "positions" })
	public TablePosition getPosition() {
		return position;
	}

	public List<Erreur>  asPlace() {
		List<Erreur> err= new ArrayList<ApeConfigurationScoreCalculator.Erreur>();
		if (position == null)
			err.add(new Erreur(String.valueOf(this.id),"N'a pas de place assise"));
		return err;
	}

	private final Set<Invite> close = new HashSet<>();

	
	public List<Erreur> closeToClose(){
		List<Erreur> err= new ArrayList<ApeConfigurationScoreCalculator.Erreur>();
		if(position==null)
			return err;
		int result=0;
		List<Invite> find = new ArrayList<Invite>();
		find.addAll(close);
		for (Invite invite : close) {
			if(position.next!=null && position.next.equals(invite.position))
				find.remove(invite);
			if(position.prev!=null && position.prev.equals(invite.position))
				find.remove(invite);
		}
		for (Invite invite : find) {
			err.add(new Erreur(this.id, "ne se trouve pas a cote de " + invite.id));
		}
//			if(position.next!=null && position.next.equals(invite.position))
//				result+=1;
//			if(position.prev!=null && position.prev.equals(invite.position))
//				result+=1;

			
		
		return err;
	}
	
	
	public Invite addClose(final Invite person) {
		this.close.add(person);
		return this;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getId() {
		return id;
	}

	public int getNumber() {
		return number;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Invite() {
		this(null, -1);
	}

	public Invite(String id, int number) {
		this.id = id;
		this.number = number;
	}

}
