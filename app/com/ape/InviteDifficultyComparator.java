package com.ape;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.Comparator;


public class InviteDifficultyComparator implements Comparator<Invite> {
	@Override
	public int compare(final Invite a, final Invite b) {
		return new CompareToBuilder()
			//	.append(a.getId(), b.getId())

			//	.append(a.getNumber(), b.getNumber())
				.append(a.getGroupeNumber(), b.getGroupeNumber())

				.toComparison();
	}
}
