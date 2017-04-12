package wit.cgd.xando.game;

/**
 * @file        WorldController
 * @author      Oleksandr Kononov 20071032
 * @assignment  XandO
 * @brief       Handles player input and handles the update
 * 				for the board.
 *
 * @notes     	Known issue: If the screen is stretched too wide,
 * 							 user input becomes inaccurate  
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Game;
import wit.cgd.xando.screens.MenuScreen;
import wit.cgd.xando.game.ai.FirstSpacePlayer;
import wit.cgd.xando.game.ai.ImpactSpacePlayer;
import wit.cgd.xando.game.ai.MinimaxPlayer;
import wit.cgd.xando.game.ai.RandomSpacePlayer;
import wit.cgd.xando.game.util.GamePreferences;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldController extends InputAdapter{
	private static final String TAG = WorldController.class.getName();
	
	private Game 				game;
	
	public float viewportWidth;
	public int width,height;
	float					timeLeftGameOverDelay;
	
	public Board board;
	public boolean                     	dragging = false;
    public int                         	dragX, dragY;
    public TextureRegion               	dragRegion;
    public TextureRegion				hintButton;
    public TextureRegion				undoButton;
    
    final float                 TIME_LEFT_GAME_OVER_DELAY = 10f;
	
    int wins = 0;
    int draws = 0;
    int loses = 0;
    int games = 0;
   // boolean nextMove = true;
    
	public WorldController(Game game){
		this.game = game;
		init();
	}
	
	private void backToMenu(){
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
	
	private void init() {
		Gdx.input.setInputProcessor(this);
		Gdx.input.setInputProcessor(this);
        board = new Board();
        hintButton = Assets.instance.hintBtn.up;
        undoButton = Assets.instance.undoBtn.up;
        
        if(GamePreferences.instance.firstPlayerHuman){
        	board.firstPlayer = new HumanPlayer(board, board.ODD);
        }else{
        	board.firstPlayer = new MinimaxPlayer(board, board.ODD);
        	board.firstPlayer.skill = (int) GamePreferences.instance.firstPlayerSkill;
        }
        
        if(GamePreferences.instance.secondPlayerHuman){
        	board.secondPlayer = new HumanPlayer(board, board.EVEN);
        }else{
        	board.secondPlayer = new MinimaxPlayer(board, board.EVEN);
        	board.secondPlayer.skill = (int) GamePreferences.instance.secondPlayerSkill;
        }
        timeLeftGameOverDelay = TIME_LEFT_GAME_OVER_DELAY;
        board.start();
    }
	
	public void update(float deltaTime) {
        if (board.gameState == Board.GameState.PLAYING ) {
            board.move();
           // nextMove = false;
        } else if(board.gameState != Board.GameState.PLAYING ){
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0) {
            	games++;
            	if(board.gameState == Board.GameState.ODD_WON) wins++;
            	else if(board.gameState == Board.GameState.EVEN_WON) loses++;
            	else draws++;
            	timeLeftGameOverDelay = TIME_LEFT_GAME_OVER_DELAY;
            	if(games < 100) board.start();
            	else System.out.println("WINS : "+wins+"  LOSES: "+loses+"  DRAWS: "+draws);
            }
        }
    }
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		 if (board.gameState == Board.GameState.PLAYING && board.currentPlayer.human) {
			 
			 // convert to cell position
	         int row = 5 * (height - screenY) / height;
	         int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;
	         
	         dragX = screenX;
	         dragY = screenY;
	         
	         //check if hint button is pressed
	         if(col == 0 && row == 4){
	        	 hintButton = Assets.instance.hintBtn.down;
	        	 showHint();
	        	 return true;
	         }
	         
	       //check if undo button is pressed
	         if(col == 2 && row == 4){
	        	 undoButton = Assets.instance.undoBtn.down;
	        	 undoMove();
	        	 return true;
	         }
	         

	         // check if valid start of a drag for first player
	         if (col == -1 && board.currentPlayer==board.firstPlayer) {
	            for(int r=0; r<5; ++r){
	            	if(row == r){
	            		dragging = true;
			            dragRegion = Assets.instance.numbers.get(r*2).region;
			            return true;
	            	}
	            }
	         }
	         // check if valid start of a drag for second player
	         if (col == 3 && board.currentPlayer==board.secondPlayer) {
	           for(int r=0; r<4; ++r){
	            	if(row == r){
	            		dragging = true;
			            dragRegion = Assets.instance.numbers.get((r*2)+1).region;
			            return true;
	            	}
	            }
	         }
		 }
	     return true;
	}
	
	@Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragX = screenX;
        dragY = screenY;        
        return true;
    }
	
	@Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		hintButton = Assets.instance.hintBtn.up;
		undoButton = Assets.instance.undoBtn.up;
		
        dragging = false;
        if(dragRegion == null) return true;
        
        // convert to cell position
        int row = 4 * (height - screenY) / height;
        int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;

        int value = -1;
        for(int i = 0; i < 9; ++i){
        	if(dragRegion.equals(Assets.instance.numbers.get(i).region)){
        		value = i+1;
        		break;
        	}
        }
        
        // if a valid board cell then place piece
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            board.move(row, col,value);
        }

        dragRegion = null;
        
        return true;
    }
	
	@Override
	public boolean keyUp(int keycode){
		if(keycode == Keys.ESCAPE || keycode == Keys.BACK){
			backToMenu();
		}
		return false;
	}
	
	private void showHint(){
		
	}
	
	private void undoMove() {
		if(board.firstPlayer.human && board.secondPlayer.human && !board.movesMade.isEmpty()){
			int pos = board.movesMade.pop();
			board.cells[pos/3][pos%3] = board.EMPTY;
			board.numsOnBoard.remove(board.numsOnBoard.size()-1);
			board.currentPlayer = (board.currentPlayer == board.firstPlayer)?
					board.secondPlayer : board.firstPlayer;
			return;
		}
		
		if(board.movesMade.size() < 2) return;
		for(int i=0; i<2; ++i){
			int pos = board.movesMade.pop();
			board.cells[pos/3][pos%3] = board.EMPTY;
			board.numsOnBoard.remove(board.numsOnBoard.size()-1);
		}
	}
}
