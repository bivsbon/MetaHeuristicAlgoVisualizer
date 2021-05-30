package org.openjfx.MetaHeuristicAlgoVisualizer;

public class SimulatedAnnealing extends MetaHeuristicAlgorithm{
    private double temperature = 9999;
    private static final double COOLING_RATE = 0.00005;
    private static final double MIN_TEMPERATURE = 0.99;
    private static SimulatedAnnealing instance = new SimulatedAnnealing();
    
    private SimulatedAnnealing() {
    	
    }
    
    public static SimulatedAnnealing getInstance() {
    	return instance;
    }
    
    public void readData(CityData data ) {
    	this.data = data;
    	nCities = data.size();
        currentTour = new Tour(nCities);
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
        
        while (temperature > 0.99) {
            Tour newSolution = new Tour(currentTour);
            newSolution.swapRanDomCity();
            
            // Get distance of 2 tours
            double currentDistance   = currentTour.getCost(data);
            double neighbourDistance = newSolution.getCost(data);

            // Decide the distance base on fomula
            double rand = generator.nextDouble();
            if (acceptanceProbability(currentDistance, neighbourDistance) > rand) {
                currentTour = new Tour(newSolution);
            }
            
            temperature *= 1 - COOLING_RATE;   // Cool system
        }
        return currentTour.getCost(data);
	}

	@Override
	public boolean iterate() {
		if (temperature > MIN_TEMPERATURE)
		{
            Tour newSolution = new Tour(currentTour);
            newSolution.swapRanDomCity();
            
            // Get distance of 2 tours
            double currentDistance   = currentTour.getCost(data);
            double neighbourDistance = newSolution.getCost(data);

            // Decide the distance base on fomula
            double rand = generator.nextDouble();
            if (acceptanceProbability(currentDistance, neighbourDistance) > rand) {
                currentTour = new Tour(newSolution);
            }
            
            temperature *= 1 - COOLING_RATE;   // Cool system
			return true;
		}
		else {
			return false;			
		}
	}
	
	@Override
	public String toString() {
		return "Simulated Annealing";
	}
}