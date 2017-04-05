package wit.cgd.xando.game.ai;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import wit.cgd.xando.game.BasePlayer;
import wit.cgd.xando.game.Board;
import wit.cgd.xando.game.WorldRenderer;

public class RandomSpacePlayer extends BasePlayer{
	@SuppressWarnings("unused")
	private static final String	TAG	= WorldRenderer.class.getName(); 
	
	public RandomSpacePlayer(Board board, int symbol) {
		super(board, symbol);
		name = "RandomImpactSpacePlayer";
	}

	@Override
	public int move() {
		ArrayList<Integer> nums = board.availableNumber(mySymbols);
		value = nums.get(ThreadLocalRandom.current().nextInt(0, nums.size()));
		int pos = 0;
		while(true){
			pos = ThreadLocalRandom.current().nextInt(0, 9);
			if(board.cells[pos/3][pos%3] == board.EMPTY) return pos;
		}
	}
}
