package com.ape;

import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.Comparator;

public class TablePositionDifficultyComparator implements Comparator<TablePosition> {
	@Override
	public int compare(final TablePosition a, final TablePosition b) {
		return new CompareToBuilder().append(a.getTable().getId(), b.getTable().getId()).append(a.getPos(), b.getPos())
				.toComparison();
	}
}
