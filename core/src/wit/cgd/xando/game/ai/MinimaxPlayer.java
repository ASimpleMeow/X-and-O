package wit.cgd.xando.game.ai;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import wit.cgd.xando.game.BasePlayer;
import wit.cgd.xando.game.Board;
import wit.cgd.xando.game.WorldRenderer;
import wit.cgd.xando.game.util.GamePreferences;

public class MinimaxPlayer extends BasePlayer{
	
	@SuppressWarnings("unused")
	private static final String	TAG	= WorldRenderer.class.getName(); 
	
	private Random randomGenerator;
	
	public MinimaxPlayer(Board board, int symbol){
		super(board,symbol);
		name = "MinimaxPlayer";
		
		skill = 2; // skill is measure of search depth
		
		randomGenerator = new Random();
	}
	
	@Override
	public int move(){
		//ArrayList<Integer> myNumbers = board.availableNumber(mySymbols);
		return (int) minimax( mySymbols, opponentSymbols, 0);
	}
	
	private float minimax(int p_mySymbols, int p_opponentSymbols, int depth){
		
		final float WIN_SCORE = 100;        
        final float DRAW_SCORE = 0;

        float score;
        float maxScore = -10000;
        int maxPos = -1;
        int finalValue = -1;
        
        //for each available number
        for(int n : board.availableNumber(p_mySymbols)){
        	// for each board position
            for (int r=0; r<3; ++r) {
                for (int c=0; c<3; ++c) {
                	// skip over used positions
                    if (board.cells[r][c]!=board.EMPTY) continue;

                    //String indent = new String(new char[depth]).replace("\0", "  ");
                    //Gdx.app.log(indent, "search ("+r+","+c+")");

                    // place move 
                    board.cells[r][c] = n;

                    // evaluate board (recursively)
                    if (board.hasWon(p_mySymbols, r, c)) {
                        score = WIN_SCORE;
                    } else if (board.isDraw()) {
                        score = DRAW_SCORE;
                    } else {
                        if (depth<skill) {
                            score = -minimax(p_opponentSymbols, p_mySymbols, depth+1);
                        } else {
                            score = 0;
                        }
                    }

                    // update ranking

                    if (Math.abs(score-maxScore)<1.0E-5 && randomGenerator.nextDouble()<0.1) {
                        maxScore = score;
                        maxPos = 3*r+c;
                        finalValue = n;

                    } else if (score>maxScore) {    // clear 
                        maxScore = score;
                        maxPos = 3*r+c;
                        finalValue = n;
                    } 

                    //Gdx.app.log(indent, "Score "+score);

                    // undo move 
                    board.cells[r][c] = board.EMPTY;
                }
            }
        }
        value = finalValue;
        // on uppermost call return move not score
        return (depth==0? maxPos : maxScore);
	}
}
