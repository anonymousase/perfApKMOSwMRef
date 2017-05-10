package metaheuristic.evolutionary;

import java.util.ArrayList;
import java.util.List;

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.


import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


@SuppressWarnings("serial")
public class RCrossover implements CrossoverOperator<RSolution> {
	
private double crossoverProbability ;
private JMetalRandom randomGenerator ;

/** Constructor */
public RCrossover(double crossoverProbability) {
if (crossoverProbability < 0) {
  throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
}
this.crossoverProbability = crossoverProbability;
randomGenerator = JMetalRandom.getInstance() ;
}

/* Getter */
public double getCrossoverProbability() {
return crossoverProbability;
}

@Override
public List<RSolution> execute(List<RSolution> solutions) {
if (solutions == null) {
  throw new JMetalException("Null parameter") ;
} else if (solutions.size() != 2) {
  throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
}

return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
}

/**
* Perform the crossover operation.
*
* @param probability Crossover setProbability
* @param parent1     The first parent
* @param parent2     The second parent
* @return An array containing the two offspring
*/
public List<RSolution> doCrossover(double probability, RSolution parent1, RSolution parent2)  {
List<RSolution> offspring = new ArrayList<>(2);
offspring.add((RSolution) parent1.copy()) ;
offspring.add((RSolution) parent2.copy()) ;
//WE NEED TO DEFINE HERE THE CROSSOVER POLICY


//CROSSOVER PROBABILITY: how many couples will be picked for mating
//System.out.println("r: "+randomGenerator.nextDouble() + " p: " + probability);
if (randomGenerator.nextDouble() < probability) {
  
  // 1. Get the total number of bits
  int length = parent1.getLength();

  // 2. Calculate the point to make the crossover
  int crossoverPoint = randomGenerator.nextInt(1, length - 1);



  // 3. The variable containing the sequence 
  
  int variable = 0;
  /*
  int bitsAccount = parent1.getVariableValue(variable).getBinarySetLength();
  
  while (bitsAccount < (crossoverPoint + 1)) {
    variable++;
    bitsAccount += parent1.getVariableValue(variable).getBinarySetLength() ;
  }
*/
  // 4. Compute the crossover point 
  
  assert(parent1.getVariableValue(variable).getLength()==parent2.getVariableValue(variable).getLength());

  // 5. Apply the crossover to the variable;
  
  assert(parent1!=null);
  assert(parent2!=null);
  RSolution offspring1 = new RSolution(parent1,parent2,crossoverPoint,true);
  RSolution offspring2 = new RSolution(parent1,parent2,crossoverPoint,false);

  offspring.add(offspring1);
  offspring.add(offspring2);
//  for (int i = intoVariableCrossoverPoint; i < offspring1.getBinarySetLength(); i++) {
//    boolean swap = offspring1.get(i);
//    offspring1.set(i, offspring2.get(i));
//    offspring2.set(i, swap);
//  }

//  offspring.get(0).setVariableValue(variable, offspring1);
//  offspring.get(1).setVariableValue(variable, offspring2);

//  // 6. Apply the crossover to the other variables
//  for (int i = variable + 1; i < parent1.getNumberOfVariables(); i++) {
//      offspring.get(0).setVariableValue(i, (BinarySet) parent2.getVariableValue(i).clone());
//      offspring.get(1).setVariableValue(i, (BinarySet) parent1.getVariableValue(i).clone());
//  }

}
return offspring ;
}

/**
* Two parents are required to apply this operator.
* @return
*/
public int getNumberOfParents() {
return 2 ;
}


}
