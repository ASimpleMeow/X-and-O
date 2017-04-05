package wit.cgd.xando.game.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import wit.cgd.xando.game.BasePlayer;
import wit.cgd.xando.game.Board;
import wit.cgd.xando.game.WorldRenderer;

public class ImpactSpacePlayer extends BasePlayer {

	@SuppressWarnings("unused")
	private static final String	TAG	= WorldRenderer.class.getName(); 

	public ImpactSpacePlayer(Board board, int symbol) {
		super(board, symbol);
		name = "ImpactSpacePlayer";
	}

	@Override
	public int move() {
		//Check for my potential wins
		for(int n : board.availableNumber(mySymbols)){
			for(int pos = 0; pos < 9; pos++){
				if(board.cells[pos/3][pos%3] != board.EMPTY) continue;
				board.cells[pos/3][pos%3] = n;
				if(board.hasWon(mySymbols, pos/3, pos%3)){
					value = n;
					return pos;
				}
				board.cells[pos/3][pos%3] = board.EMPTY;
			}
		}
		
		//Check for opponents potential wins
		for(int n : board.availableNumber(opponentSymbols)){
			for(int pos = 0; pos < 9; pos++){
				if(board.cells[pos/3][pos%3] != board.EMPTY) continue;
				board.cells[pos/3][pos%3] = n;
				if(board.hasWon(opponentSymbols, pos/3, pos%3)){
					value = Collections.max(board.availableNumber(mySymbols));
					return pos;
				}
				board.cells[pos/3][pos%3] = board.EMPTY;
			}
		}
		
		//Check for corners -if there's a number in a corner,
		//place your piece in the opposite corner,
		//one that will make up 15 only with your pieces!
		for(int r = 0; r < 3; r+=2){
			for(int c = 0; c < 3; c+=2){
				if(board.cells[r][c] == board.EMPTY) continue;
				int oppRow = (r == 0)? 2 : 0;
				int oppCol = (c == 0)? 2: 0;
				if(board.cells[oppRow][oppCol] != board.EMPTY) continue;
				for(int n : board.availableNumber(mySymbols)){
					int thirdNum = n + board.cells[r][c];
					if((15 - thirdNum) % 2 == 1){
						value = n;
						return (oppRow*3+oppCol);
					}
				}
			}
		}
		
		//If there are none, place the largest piece in a corner
		for(int r = 0; r < 3; r+=2){
			for(int c = 0; c < 3; c+=2){
				if(board.cells[r][c] == board.EMPTY && ThreadLocalRandom.current().nextDouble() < 0.1){
					value = Collections.max(board.availableNumber(mySymbols));
					return (r*3+c);
				}
			}
		}
		
		//Return a random value to a random free space
		ArrayList<Integer> nums = board.availableNumber(mySymbols);
		value = nums.get(ThreadLocalRandom.current().nextInt(0, nums.size()));
		int pos = 0;
		while(true){
			pos = ThreadLocalRandom.current().nextInt(0, 9);
			if(board.cells[pos/3][pos%3] == board.EMPTY) return pos;
		}
		
		
		/*
		//Check center
		if(board.cells[1][1] == board.EMPTY) return 4;
		
		//Check coners
		if(board.cells[2][0] == board.EMPTY) return 6;
		if(board.cells[2][2] == board.EMPTY) return 8;
		if(board.cells[0][0] == board.EMPTY) return 0;
		if(board.cells[0][2] == board.EMPTY) return 2;
		
		int pos = 0;
		while(true){
			pos = ThreadLocalRandom.current().nextInt(0, 9);
			if(board.cells[pos/3][pos%3] == board.EMPTY) return pos;
		}*/
	}

}
