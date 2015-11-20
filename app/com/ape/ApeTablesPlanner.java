package com.ape;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.XmlSolverFactory;

public class ApeTablesPlanner {

	

	public ApeConfiguration planTables(
			final ApeConfiguration unsolvedDiningConfiguration) {
		final SolverFactory solverFactory = new XmlSolverFactory(
				"/com/ape/apeTablesPlannerConfig.xml");

		final Solver solver = solverFactory.buildSolver();

		solver.setPlanningProblem(unsolvedDiningConfiguration);

		solver.solve();

		final ApeConfiguration solvedDiningConfiguration = (ApeConfiguration) solver
				.getBestSolution();
		return solvedDiningConfiguration;
	}
}
