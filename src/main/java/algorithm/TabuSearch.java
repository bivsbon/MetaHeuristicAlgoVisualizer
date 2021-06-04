package algorithm;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;
import datamodel.CityData;
import datamodel.Tour;
import java.util.LinkedList;
import java.util.Queue;

public class TabuSearch extends MetaHeuristicAlgorithm{
	private int Init_Step = 1;
	private static final int Stop_Condition = 1000;
	private static final int Stop_Random_Condition = 100;
	
	private static TabuSearch instance = new TabuSearch();
	public static TabuSearch getInstance() {
    	return instance;
    }
	private Tour currentTour;
	
	private TabuSearch() {};
	
	private Queue<Pair<Integer, Integer>> TabuList = new LinkedList<>();
	
	
	public void readData(CityData data) {
		this.data = data;
    	nCities = data.size();
        bestTour = new Tour(nCities);
        currentTour = new Tour(nCities);
        Init_Step = 1;
	}
	
	public boolean iterate() {
		int[] indexCity = new int[2];
		
		double bestDistance = bestTour.getCost(data);
		if (Init_Step < Stop_Condition){
				
			Tour newSolution = new Tour(currentTour);
			double minDistance=99999;
			for(int i=0; i < Stop_Random_Condition; i++) {
				newSolution.swapRanDomCity();
				indexCity = newSolution.swapRanDomCity();
				logScreen.addLine("Swap City " + indexCity[0] + " and City " + indexCity[1]);
				if(TabuList.isEmpty() ) {
					TabuList.add( new Pair <Integer,Integer> (indexCity[0], indexCity[1]) );
				}
				else if(TabuList.peek() != new Pair <Integer,Integer> (indexCity[0], indexCity[1])) {
					TabuList.add( new Pair <Integer,Integer> (indexCity[0], indexCity[1]) );
					TabuList.poll();
				}
				else 
					continue;
					
					
				minDistance = Math.min(minDistance, newSolution.getCost(data));
				if(minDistance == newSolution.getCost(data)) {
					currentTour = new Tour(newSolution);
				}
				if(newSolution.getCost(data)<bestTour.getCost(data))
					bestTour = new Tour(newSolution);
				logScreen.addLine("Final Best Cost is " + bestTour.getCost(data));
			}
			bestDistance = Math.min(minDistance, bestDistance);
			logScreen.addLine("Current step " + Init_Step);
			Init_Step ++;
			return true;
		}
		else {
			logScreen.addLine("Final Best Cost is " + bestTour.getCost(data));
			return false;
		}
	}

	@Override
	public String toString() {
		return "Tabu Search";
	}

	@Override
	public String getVariableString() {
		return super.getVariableString();
	}

	@Override
	public List<Tour> getMinorTours() {
		List<Tour> list = new ArrayList<>();
		list.add(currentTour);
		return list;
	}
}
