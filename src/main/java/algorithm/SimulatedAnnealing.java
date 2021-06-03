package algorithm;

import java.util.ArrayList;
import java.util.List;
import datamodel.CityData;
import datamodel.Tour;

public class SimulatedAnnealing extends MetaHeuristicAlgorithm{
    private double temperature;
    private static final double INITIAL_TEMPERATURE = 999;
    private static final double COOLING_RATE = 0.05;
    private static final double MIN_TEMPERATURE = 0.99;
    
    private static SimulatedAnnealing instance = new SimulatedAnnealing();
    public static SimulatedAnnealing getInstance() {
    	return instance;
    }
    	
    private Tour currentTour;
    
    private SimulatedAnnealing() {}
    
   
    
    public void readData(CityData data ) {
    	this.data = data;
    	nCities = data.size();
        bestTour = new Tour(nCities);
        currentTour = new Tour(nCities);
        temperature = INITIAL_TEMPERATURE;
    }
    
    private double acceptanceProbability(double currentDistance, double newDistance) {
		if (newDistance < currentDistance) {
			return 1.0;
		}
		return 1 / (1 + Math.exp(Math.abs(currentDistance - newDistance) / temperature));
	}

	@Override
	public double solve(CityData data) {
		readData(data);
        
        while (temperature > MIN_TEMPERATURE) {
            Tour newSolution = new Tour(currentTour);
            newSolution.swapRanDomCity();
            // Get distance of 2 tours
            double currentDistance   = currentTour.getCost(data);
            double neighbourDistance = newSolution.getCost(data);

            // Decide the distance base on formula
            double rand = generator.nextDouble();
            if (acceptanceProbability(currentDistance, neighbourDistance) > rand) {
                currentTour = new Tour(newSolution);
            }

            if (currentDistance < bestTour.getCost(data)) {
            	bestTour = new Tour(currentTour);
            }
            	
            temperature *= 1 - COOLING_RATE;   // Cool system
        }
        return bestTour.getCost(data);
	}

	@Override
	public boolean iterate() {
		int[] indexCity = new int[2];
		logScreen.addLine("Cooling Rate is " + COOLING_RATE);
		if (temperature > MIN_TEMPERATURE)
		{
			
            Tour newSolution = new Tour(currentTour);
            newSolution.swapRanDomCity();
            indexCity = newSolution.swapRanDomCity();
            
            logScreen.addLine("Swap City " + indexCity[0] + " and City " + indexCity[1]);
            
            // Get distance of 2 tours
            double currentDistance   = currentTour.getCost(data);
            double neighbourDistance = newSolution.getCost(data);

            // Decide the distance base on formula
            double rand = generator.nextDouble();
            if (acceptanceProbability(currentDistance, neighbourDistance) > rand) {
                currentTour = new Tour(newSolution);
            }
            logScreen.addLine("Find New Cost is " + currentTour.getCost(data));
            if (currentDistance < bestTour.getCost(data)) {
            	bestTour = new Tour(currentTour);
            	logScreen.addLine("Update new tour successfully ");
            	logScreen.addLine("New Best Cost is " + bestTour.getCost(data));
            }
            
            else {
            	logScreen.addLine("The Cost is not change");
            	logScreen.addLine("Current Best Cost is " + bestTour.getCost(data));
            }
            logScreen.addLine("Temperature is " + temperature);
            logScreen.addLine("");
            temperature *= 1 - COOLING_RATE;   // Cool system
			return true;
		}
		else {
			logScreen.addLine("Final Best Cost is " + bestTour.getCost(data));
			return false;		
		}
	}
	
	@Override
	public String toString() {
		return "Simulated Annealing";
	}

	@Override
	public String getVariableString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tour> getMinorTours() {
		List<Tour> list = new ArrayList<>();
		list.add(currentTour);
		return list;
	}
}