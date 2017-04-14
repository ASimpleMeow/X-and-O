package wit.cgd.xando;
/**
 * @file        XandOMain
 * @author      Oleksandr Kononov 20071032
 * @assignment  XandO
 * @brief       The main class which governs the WorldController and
 * 				WorldRenderer
 *
 * @notes       
 */

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import wit.cgd.xando.game.Assets;
import wit.cgd.xando.game.util.AudioManager;
import wit.cgd.xando.game.util.GamePreferences;
import wit.cgd.xando.game.util.GameStats;
import wit.cgd.xando.screens.MenuScreen;

public class XandOMain extends Game {
	
	@Override
	public void create(){
		//Set LibGdx log level
		Gdx.app.setLogLevel(Application.LOG_INFO);
		//Load assets
		Assets.instance.init(new AssetManager());
		
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		GameStats.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		
		//Start the game at menu screen
		setScreen(new MenuScreen(this));
	}
}
