package com.ape;

import com.ape.ApeConfigurationScoreCalculator.Erreur;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Table {

	private final int id;
	private final int capacity;

	private List<TablePosition> places =new ArrayList<TablePosition>();
	
	public void addPlace(TablePosition pos){
		places.add(pos);
	}
	
	public Table() {
		this(-1, -1);
	}

	public Table(final int id, final int size) {
		super();
		this.id = id;
		this.capacity = size;
	}

	public int getId() {
		return id;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(id);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Table other = (Table) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public List<Erreur> exceededOccupation(Set<Invite> invites) {
		int size=0;
		List<Erreur> err= new ArrayList<ApeConfigurationScoreCalculator.Erreur>();
		for (Invite invite : invites) {
			if(invite.getPosition()!=null && this.equals(invite.getPosition().getTable()))
				size+=invite.getNumber();
		}
		if(capacity < size){
			System.out.println(this);
			for (Invite invite : invites) {
				System.out.println(invite);
			}
			err.add(new Erreur(String.valueOf(this.id),"Trop de monde sur cette table"));
		}
		return err;
	}

}
