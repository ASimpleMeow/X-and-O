package wit.cgd.xando.game;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @file        Board
 * @author      Oleksandr Kononov 20071032
 * @assignment  XandO assignment
 * @brief       Contains all the elements of the board (cells) and
 * 				handles all the drawing and move making for the game.
 *
 * @notes       
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import wit.cgd.xando.game.util.AudioManager;
import wit.cgd.xando.game.util.Constants;

public class Board {
	public static final String TAG = Board.class.getName();
	
	public static enum GameState{
		PLAYING, DRAW, EVEN_WON, ODD_WON
	}
	
	public GameState		gameState;
	
	public TextureRegion	board;
	
	public final int		EMPTY	= 0;
	public final int		EVEN	= 2;
	public final int		ODD		= 1;
	
	public int[][]			cells	= new int [3][3];
	public ArrayList<Integer> numsOnBoard;
	public BasePlayer 		firstPlayer, secondPlayer;
	public BasePlayer 		currentPlayer;
	public Stack<Integer>	movesMade;
	
	public Board(){
		init();
	}
	
	private void init(){
		board = Assets.instance.board.region;
		movesMade = new Stack<Integer>();
		start();
	}
	
	public void render(SpriteBatch batch) {
		TextureRegion region = Assets.instance.board.region;
		
		batch.draw(region.getTexture(), -2,
				-Constants.VIEWPORT_HEIGHT / 2 + 0.1f, 0, 0, 4, 4, 1, 1, 0,
				region.getRegionX(), region.getRegionY(),
				region.getRegionWidth(), region.getRegionHeight(), false, false);

		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++) {
				if (cells[row][col] == EMPTY) continue;
				region = Assets.instance.numbers.get(cells[row][col]-1).region;//NUMS[cells[row][col]-1];
				batch.draw(region.getTexture(), col*1.4f-1.9f,
						row*1.4f-2.3f, 0, 0, 1, 1, 1, 1, 0,
						region.getRegionX(), region.getRegionY(),
						region.getRegionWidth(), region.getRegionHeight(),
						false, false);
		}
		
		// draw drag and drop pieces
		
		int rowPos = -2;
		//ODD
        for(int i = 0; i < 9; i+= 2){
        	rowPos ++;
        	if(numsOnBoard.contains(i+1)) continue;
        	region =  Assets.instance.numbers.get(i).region;
            batch.draw(region.getTexture(), (-1) * 1.4f - 1.9f, rowPos - 1.5f, 0, 0, 1, 1, 1, 1, 0,
                    region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
                    false, false);
        }
        rowPos = -2;
        //EVEN
        for(int i = 1; i < 9; i+= 2){
        	rowPos ++;
        	if(numsOnBoard.contains(i+1)) continue;
        	region =  Assets.instance.numbers.get(i).region;
            batch.draw(region.getTexture(), (3) * 1.4f - 1.9f, rowPos - 1.5f, 0, 0, 1, 1, 1, 1, 0,
                    region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
                    false, false);
        }

	}
	
	public void start(){
		cells = new int[3][3];
		numsOnBoard = new ArrayList<Integer>();
		gameState = GameState.PLAYING;
		currentPlayer = firstPlayer;
	}
	
	public boolean move(){
		return move(-1,-1,-1);
	}
	
	public boolean move(int row, int col, int value){
		if (currentPlayer.human){
			if(row < 0 || row >= cells.length || col < 0 || col >= cells.length || cells[row][col] != EMPTY){
				//Gdx.app.debug(TAG, "Player can't make a move, will return false!");
				return false;
			}
		}else{
			int pos = currentPlayer.move();
			value = currentPlayer.value;
			row = pos / 3;
			col = pos % 3;
		}
		
		cells[row][col] = value;//currentPlayer.mySymbol;
		movesMade.add(row*3+col);
		Gdx.app.debug(TAG, "Move has been made");
		
		numsOnBoard.add(value);
		
		if(currentPlayer.equals(firstPlayer)) AudioManager.instance.play(Assets.instance.sounds.first);
		else AudioManager.instance.play(Assets.instance.sounds.second);
		
		if(hasWon(currentPlayer.mySymbols, row,col)) {
			AudioManager.instance.play(Assets.instance.sounds.win);
			gameState = (currentPlayer.mySymbols == EVEN)? GameState.EVEN_WON : GameState.ODD_WON;
			Gdx.app.debug(TAG, "THE GAME IS WON BY : "+gameState);
		}
		
		if(gameState == GameState.PLAYING){
			if(isDraw()){
				AudioManager.instance.play(Assets.instance.sounds.draw);
				Gdx.app.debug(TAG, "Game is a draw!");
				gameState = GameState.DRAW;
			}
			else currentPlayer = (currentPlayer.equals(firstPlayer))? secondPlayer : firstPlayer;
		}
		return true;
	}
	
	public boolean isDraw(){
		for(int i = 0; i<cells.length;i++)
			for(int j=0; j<cells[0].length;j++)
				if(cells[i][j] == EMPTY) return false;
		return true;
	}
	
	public boolean hasWon(int symbol, int row, int col){
		
		//Check rows down from the column
		int cellsFilled = 0;
		int winningNum = 0;
		for(int i=0; i<cells.length; i++){
			winningNum += cells[i][col];
			if(cells[i][col] != EMPTY) cellsFilled++;
		}
		//Gdx.app.debug(TAG, "SAME ROW : "+winningNum);
		if((winningNum == 15) && (cellsFilled == 3)) return true;
		
		//Check columns from the row
		winningNum = 0;
		cellsFilled = 0;
		for(int i=0; i<cells[0].length; i++){
			winningNum += cells[row][i];
			if(cells[row][i] != EMPTY) cellsFilled++;
		}
		//Gdx.app.debug(TAG, "SAME COL : "+winningNum);
		if((winningNum == 15) && (cellsFilled == 3)) return true;
		
		//Check diagonals
		//Gdx.app.debug(TAG, "DIAG 1 : ");
		if((cells[1][1] != EMPTY && cells[0][0] != EMPTY && cells[2][2] != EMPTY)
				&&((cells[1][1] + cells[0][0] + cells[2][2]) == 15)) return true;
		//Gdx.app.debug(TAG, "DIAG 2 : ");
		if((cells[1][1] !=EMPTY && cells[0][2] !=EMPTY && cells[2][0] != EMPTY) 
				&&((cells[1][1] + cells[0][2] + cells[2][0]) == 15)) return true;
		
		return false;
	}
	
	public ArrayList<Integer> availableNumber(int symbols){
		ArrayList<Integer> playerAvailableNums = new ArrayList<Integer>() ;
		
		if(symbols == EVEN){
			for(int i : new int[] {2,4,6,8})
				if(!numsOnBoard.contains(i)) playerAvailableNums.add(i);
		}else{
			for(int i : new int[] {1,3,5,7,9})
				if(!numsOnBoard.contains(i)) playerAvailableNums.add(i);
		}
		return playerAvailableNums;
	}
}
