package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import datamodel.CityData;
import datamodel.Tour;

public class SolutionGenerator {
	  private final int N, start;
	  private final CityData data;
	  private ArrayList<Integer> order = new ArrayList<>();
	  private double minTourCost = Double.POSITIVE_INFINITY;
	  private boolean ranSolver = false;

	  public SolutionGenerator(CityData data) {
	    this(0, data);
	  }

	  public SolutionGenerator(int start, CityData data) {
	    N = data.size();

	    if (N <= 2) throw new IllegalStateException("N <= 2 not yet supported.");
	    if (start < 0 || start >= N) throw new IllegalArgumentException("Invalid start node.");
	    if (N > 32)
	      throw new IllegalArgumentException(
	          "Matrix too large! A matrix that size for the DP TSP problem with a time complexity of"
	              + "O(n^2*2^n) requires way too much computation for any modern home computer to handle");

	    this.start = start;
	    this.data = data;
	    solve();
	  }

	  // Returns the optimal tour for the traveling salesman problem.
	  public Tour getSolutionTour() {
	    if (!ranSolver) solve();
	    return new Tour(order);
	  }

	  // Returns the minimal tour cost.
	  public double getTourCost() {
	    if (!ranSolver) solve();
	    return minTourCost;
	  }

	  // Solves the traveling salesman problem and caches solution.
	  private void solve() {

	    if (ranSolver) return;

	    final int END_STATE = (1 << N) - 1;
	    Double[][] memo = new Double[N][1 << N];

	    // Add all outgoing edges from the starting node to memo table.
	    for (int end = 0; end < N; end++) {
	      if (end == start) continue;
	      memo[end][(1 << start) | (1 << end)] = data.distance(start, end);
	    }

	    for (int r = 3; r <= N; r++) {
	      for (int subset : combinations(r, N)) {
	        if (notIn(start, subset)) continue;
	        for (int next = 0; next < N; next++) {
	          if (next == start || notIn(next, subset)) continue;
	          int subsetWithoutNext = subset ^ (1 << next);
	          double minDist = Double.POSITIVE_INFINITY;
	          for (int end = 0; end < N; end++) {
	            if (end == start || end == next || notIn(end, subset)) continue;
	            double newDistance = memo[end][subsetWithoutNext] +	data.distance(end, next);
	            if (newDistance < minDist) {
	              minDist = newDistance;
	            }
	          }
	          memo[next][subset] = minDist;
	        }
	      }
	    }

	    // Connect tour back to starting node and minimize cost.
	    for (int i = 0; i < N; i++) {
	      if (i == start) continue;
	      double tourCost = memo[i][END_STATE] + data.distance(i, start);
	      if (tourCost < minTourCost) {
	        minTourCost = tourCost;
	      }
	    }

	    int lastIndex = start;
	    int state = END_STATE;
	    order.add(start);

	    // Reconstruct TSP path from memo table.
	    for (int i = 1; i < N; i++) {

	      int bestIndex = -1;
	      double bestDist = Double.POSITIVE_INFINITY;
	      for (int j = 0; j < N; j++) {
	        if (j == start || notIn(j, state)) continue;
	        double newDist = memo[j][state] + data.distance(j, lastIndex);
	        if (newDist < bestDist) {
	          bestIndex = j;
	          bestDist = newDist;
	        }
	      }

	      order.add(bestIndex);
	      state = state ^ (1 << bestIndex);
	      lastIndex = bestIndex;
	    }

	    order.add(start);
	    Collections.reverse(order);

	    ranSolver = true;
	  }

	  private static boolean notIn(int elem, int subset) {
	    return ((1 << elem) & subset) == 0;
	  }

	  // This method generates all bit sets of size n where r bits
	  // are set to one. The result is returned as a list of integer masks.
	  public static List<Integer> combinations(int r, int n) {
	    List<Integer> subsets = new ArrayList<>();
	    combinations(0, 0, r, n, subsets);
	    return subsets;
	  }

	  // To find all the combinations of size r we need to recurse until we have
	  // selected r elements (a.k.a r = 0), otherwise if r != 0 then we still need to select
	  // an element which is found after the position of our last selected element
	  private static void combinations(int set, int at, int r, int n, List<Integer> subsets) {

	    // Return early if there are more elements left to select than what is available.
	    int elementsLeftToPick = n - at;
	    if (elementsLeftToPick < r) return;

	    // We selected 'r' elements so we found a valid subset!
	    if (r == 0) {
	      subsets.add(set);
	    } else {
	      for (int i = at; i < n; i++) {
	        // Try including this element
	        set ^= (1 << i);

	        combinations(set, i + 1, r - 1, n, subsets);

	        // Backtrack and try the instance where we did not include this element
	        set ^= (1 << i);
	      }
	    }
	  }
}
