package test.algo;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<>(5);
		for (int i : list) {
			System.out.println(i);
		}
		System.out.println(list.size());
	}
}
