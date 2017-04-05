package wit.cgd.xando.game;
/**
 * @file        HumanPlayer
 * @author      Oleksandr Kononov 20071032
 * @assignment  XandO
 * @brief       Creates the human player object to handle human actions 
 *
 * @notes       
 */


public class HumanPlayer extends BasePlayer{
	
	public HumanPlayer(Board board, int symbol){
		super(board, symbol);
		human = true;
	}
	
	@Override
	public int move(){
		return 0;
	}
}
