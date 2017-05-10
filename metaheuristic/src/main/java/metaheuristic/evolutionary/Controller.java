package metaheuristic.evolutionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.eclipse.uml2.uml.resource.UMLResource;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import logicalSpecification.Action;
import metaheuristic.managers.Manager;
import metaheuristic.managers.uml.UMLManager;

public class Controller extends AbstractAlgorithmRunner {

	public static boolean append = false;
	public static Logger logger_;
	public static FileHandler handler;

	private static String BASENAME = "/src/main/resources/models/refactored/BGCS/BGCS_";
	private static String EXTENSION = ".uml";

	public static void main(String[] args) throws IOException {

		logger_ = Logger.getLogger(Controller.class.getName());
		handler = new FileHandler("default.log", append);
		logger_.addHandler(handler);

		boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString()
				.indexOf("jdwp") >= 0;

		String filename = args[0];

		try {
			filename = new java.io.File(".").getCanonicalPath() + filename;
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger_.info("Logger Name: " + logger_.getName());
		logger_.warning("Can cause IOException");
		logger_.info("Config_file is set to " + filename);

		Properties prop = new Properties();

		InputStream inputStream = new FileInputStream(filename);
		prop.load(inputStream);

		if (isDebug)
			System.out.println("Reading Model: " + filename);

		String model_file_name = "";
		try {
			model_file_name += new java.io.File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		model_file_name += prop.getProperty("model_path");
		logger_.info("model_file_name is set to " + model_file_name);

		int length = Integer.parseInt(prop.getProperty("length"));
		logger_.info("length is set to " + length);

		int number_of_actions = Integer.parseInt(prop.getProperty("number_of_actions"));
		logger_.info("number_of_actions is set to " + number_of_actions);

		double crossoverProbability = Double.parseDouble(prop.getProperty("p_crossover"));
		logger_.info("crossoverProbability is set to " + crossoverProbability);

		double mutationProbability = Double.parseDouble(prop.getProperty("p_mutation"));
		logger_.info("mutationProbability is set to " + mutationProbability);

		double distribution_index = Double.parseDouble(prop.getProperty("d_index_mutation"));
		logger_.info("distribution_index is set to " + distribution_index);

		int maxEvaluations = Integer.parseInt(prop.getProperty("maxEvaluations"));
		logger_.info("maxEvaluations is set to " + maxEvaluations);

		int populationSize = Integer.parseInt(prop.getProperty("populationSize"));
		logger_.info("populationSize is set to " + populationSize);

		int allowed_failures = Integer.parseInt(prop.getProperty("allowed_failures"));
		logger_.info("allowed_failures is set to " + allowed_failures);

		logger_.info("Starting number of elements: " + populationSize);
		logger_.info("Debug mode: " + isDebug);

		RProblem problem = new RProblem(model_file_name, length, number_of_actions, allowed_failures, populationSize);

		CrossoverOperator<RSolution> crossover = new RCrossover(crossoverProbability);

		MutationOperator<RSolution> mutation = new RMutation(mutationProbability, distribution_index);

		SelectionOperator<List<RSolution>, RSolution> selection = new BinaryTournamentSelection<RSolution>(
				new RankingAndCrowdingDistanceComparator<RSolution>());

		SolutionListEvaluator<RSolution> ev = new RSolutionListEvaluator();

		Algorithm<List<RSolution>> algorithm = new CustomNSGAII<RSolution>(problem, maxEvaluations, populationSize,
				crossover, mutation, selection, ev);

		long[] id_s = new long[1];
		id_s[0] = java.lang.Thread.currentThread().getId();
		long initTime = getCpuTime(id_s);

		algorithm.run();
		long estimatedTime = getCpuTime(id_s) - initTime;

		((CustomNSGAII<RSolution>) algorithm).computeCrowdingDistances();
		List<RSolution> population = algorithm.getResult();
		Collections.sort(population, new RankingAndCrowdingDistanceComparator<RSolution>());
		Collections.reverse(population);

		logger_.info("Number of non-dominated solutions (sorted by Crowding): " + population.size());

		int count = 0;

		for (RSolution r : population) {

			System.out.print(count + " " + r.toString());

//			System.out.println("---------START LOGGING-----------");
			for (Action a : r.getVariableValue(0).getRefactoring().getActions()) {
				a.log();
			}
//			System.out.println("---------END LOGGING-----------");

//			System.out.println("---------START EXECUTING-----------");
			for (Action a : r.getVariableValue(0).getRefactoring().getActions()) {
				a.execute();
			}
//			System.out.println("---------END EXECUTING-----------");

			createNewUmlModelFile(BASENAME + r.getVariableValue(0).getRefactoring().getName());

			// saves refactored model
			UMLManager.getInstance().save(Manager.getInstance().getModel(), new java.io.File(".").getCanonicalPath()
					+ BASENAME + r.getVariableValue(0).getRefactoring().getName() + "." + UMLResource.FILE_EXTENSION);

			count++;
		}

		
		// Result messages
		// logger_.info("Estimated total execution time: " + new
		// DecimalFormat("#.##").format(estimatedTime).replaceAll(",",".") + "
		// nanoseconds");

		logger_.info("Estimated total execution time: "
				+ new DecimalFormat("#.##").format(estimatedTime * Math.pow(10, -9)).replaceAll(",", ".") + " seconds");

	}

	private static void createNewUmlModelFile(String refModelURI) {
		String path;
		try {
			path = new java.io.File(".").getCanonicalPath() + refModelURI + "." + UMLResource.FILE_EXTENSION;
			File newFile = new File(path);
			newFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static long getCpuTime(long[] ids) {
		/** Get CPU time in nanoseconds. */

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		if (!bean.isThreadCpuTimeSupported())
			return 0L;
		long time = 0L;
		for (long i : ids) {
			// System.out.println(i);
			long t = bean.getThreadCpuTime(ids[(int) i - 1]);
			if (t != -1)
				time += t;
		}
		return time;

	}

}
