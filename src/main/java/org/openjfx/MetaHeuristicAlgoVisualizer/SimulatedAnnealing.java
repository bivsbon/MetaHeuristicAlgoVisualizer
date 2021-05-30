package org.openjfx.MetaHeuristicAlgoVisualizer;

public class SimulatedAnnealing extends MetaHeuristicAlgorithm{
    private double temperature = 9999;
    private static final double coolingRate = 0.00005;
	
	@Override
	public String getAlgName() {
		return "Simulated Annealing Algorithm ";
	}
    
    private double acceptanceProbability(double currentDistance, double newDistance) {
		if (newDistance < currentDistance) {
			return 1.0;
		}
		return 1 / (1 + Math.exp(Math.abs(currentDistance - newDistance) / temperature));
	}

	@Override
	public double solve(CityData data) {
		nCities = data.size();
		this.data = data;
        //create random initial solution
        currentTour = new Tour(nCities);
        
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
            
            temperature *= 1 - coolingRate;   // Cool system
        }
        return currentTour.getCost(data);
	}

	@Override
	public boolean iterate() {
		// TODO Auto-generated method stub
		return false;
	}
}