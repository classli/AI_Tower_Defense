package com.ai_test;

import java.util.Comparator;

public class AStarComparator implements Comparator<int [][]>{

	GameLogic game;
	
	public AStarComparator(GameLogic game){
		this.game=game;
	}
	@Override
	public int compare(int[][] lhs, int[][] rhs) {
		// TODO Auto-generated method stub
		int[] t1=lhs[1];
		int[] t2=rhs[1];
		
		int []target=game.target;
		
		int a = Math.abs(t1[0]-target[0])+Math.abs(t1[1]-target[1]);
		int b = Math.abs(t2[0]-target[0])+Math.abs(t2[1]-target[1]);
		
		return a-b;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}