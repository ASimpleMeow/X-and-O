package wit.cgd.xando.game;
/**
 * @file        BasePlayer
 * @author      Oleksandr Kononov 20071032
 * @assignment  XandO
 * @brief       Superclass for all players in the game 
 *
 * @notes       
 */

public abstract class BasePlayer {
	
	public boolean	human;
	public int		mySymbols, opponentSymbols;
	public String	name;
	public Board	board;
	public int		value;
	
	public int skill;
	
	public BasePlayer(Board board, int symbol){
		this.board = board;
		setSymbol(symbol);
		human = false;
	}
	
	public void setSymbol(int symbol){
		this.mySymbols = symbol;
		this.opponentSymbols = (symbol == board.EVEN)? board.ODD : board.EVEN;
	}
	
	public abstract int move();
	
	
}
