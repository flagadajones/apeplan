package com.ape;

import com.ape.ApeConfigurationScoreCalculator.Erreur;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class TablePosition {
final Table table;
final int pos;

TablePosition prev ;
TablePosition next ;

Invite invite;

@Override
	public String toString() {
		// TODO Auto-generated method stub
		return table + " " +pos + " - "+ invite;
	}

public void setInvite(Invite invite) {
	this.invite = invite;
}
public Invite getInvite() {
	return invite;
}

public void setPrev(TablePosition prev) {
	this.prev = prev;
}
public TablePosition getPrev() {
	return prev;
}
public void setNext(TablePosition next) {
	this.next = next;
}
public TablePosition getNext() {
	return next;
}



public int getPos() {
	return pos;
}
public TablePosition(){
	this(null, -1, null);
}
public TablePosition(Table table, int pos, TablePosition prev){
	this.table=table;
	this.pos=pos;
	this.prev=prev;
}

public Table getTable() {
	return table;
}
public List<Erreur> exceededOccupation(final Set<Invite> guests) {
	int size =0;
	List<Erreur> err= new ArrayList<ApeConfigurationScoreCalculator.Erreur>();
	for (Invite invite : guests) {
	if(this.equals(invite.position))
		size++;
	}
	if(size>1)
		err.add(new Erreur(String.valueOf(this.getTable().getId()+"-"+this.pos),"Trop de monde sur cette place"));
	return err;
}
}
