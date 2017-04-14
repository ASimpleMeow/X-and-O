package wit.cgd.xando.desktop;
/**
 * @file        DesktopLauncher
 * @author      Oleksandr Kononov 20071032
 * @assignment  XandO
 * @brief       Launches the game for Desktop use
 *
 * @notes       
 */

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import wit.cgd.xando.XandOMain;

public class DesktopLauncher {
	
	private static boolean  rebuildAtlas        = true;
    private static boolean  drawDebugOutline    = false;
	
	public static void main (String[] arg) {
		if(rebuildAtlas){
			Settings settings = new Settings();
			settings.maxHeight = 1024;
			settings.maxWidth = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "../../desktop/assets-raw/images", "images",
                    "xando.atlas");
			TexturePacker.process(settings, "../../desktop/assets-raw/images-ui", "images",
                    "ui.atlas");
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "X and O";
		config.width = 800;
        config.height = 480;
		new LwjglApplication(new XandOMain(), config);
	}
}
