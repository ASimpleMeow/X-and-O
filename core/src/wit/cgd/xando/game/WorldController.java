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
	public boolean                     dragging = false;
    public int                         dragX, dragY;
    public TextureRegion               dragRegion;
    
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
        /*
        board.firstPlayer = (GamePreferences.instance.firstPlayerHuman)?new HumanPlayer(board, board.ODD):
        	new MinimaxPlayer(board, board.ODD);
        board.secondPlayer = (GamePreferences.instance.secondPlayerHuman)? new HumanPlayer(board,board.EVEN):
        	new MinimaxPlayer(board, board.EVEN);
		*/
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

	            // check if valid start of a drag for first player
	            if (col == -1 && board.currentPlayer==board.firstPlayer) {
	                switch(row){
	                case 0:
	                	dragging = true;
		                dragRegion = board.NUMS[0];
		                return true;
	                case 1:
	                	dragging = true;
		                dragRegion = board.NUMS[2];
		                return true;
	                case 2:
	                	dragging = true;
		                dragRegion = board.NUMS[4];
		                return true;
	                case 3:
	                	dragging = true;
		                dragRegion = board.NUMS[6];
		                return true;
	                case 4:
	                	dragging = true;
		                dragRegion = board.NUMS[8];
		                return true;
		            default:
		            	break;
	                }
	            }
	            // check if valid start of a drag for second player
	            if (col == 3 && board.currentPlayer==board.secondPlayer) {
	            	switch(row){
	                case 0:
	                	dragging = true;
		                dragRegion = board.NUMS[1];
		                return true;
	                case 1:
	                	dragging = true;
		                dragRegion = board.NUMS[3];
		                return true;
	                case 2:
	                	dragging = true;
		                dragRegion = board.NUMS[5];
		                return true;
	                case 3:
	                	dragging = true;
		                dragRegion = board.NUMS[7];
		                return true;
		            default:
		            	break;
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

        dragging = false;
        if(dragRegion == null) return true;
        
        // convert to cell position
        int row = 4 * (height - screenY) / height;
        int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;

        int value = -1;
        for(int i = 0; i < 9; ++i){
        	if(dragRegion.equals(board.NUMS[i])){
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
}
