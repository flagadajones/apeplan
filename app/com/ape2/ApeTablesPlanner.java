package com.ape2;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

public class ApeTablesPlanner {

	

	public ApeConfiguration planTables(
			final ApeConfiguration unsolvedDiningConfiguration) {
		final SolverFactory solverFactory = new ApeXmlSolverFactory(
				"ape2TablesPlannerConfig.xml");


		final Solver solver = solverFactory.buildSolver();

		solver.setPlanningProblem(unsolvedDiningConfiguration);

		solver.solve();

		final ApeConfiguration solvedDiningConfiguration = (ApeConfiguration) solver
				.getBestSolution();
		return solvedDiningConfiguration;
	}
}
