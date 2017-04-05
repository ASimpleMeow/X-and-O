package wit.cgd.xando.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {

    public static final String          TAG         = GamePreferences.class.getName();

    public static final GamePreferences instance    = new GamePreferences();
    private Preferences                 prefs;
    
    public boolean firstPlayerHuman;
    public float firstPlayerSkill;
    public boolean secondPlayerHuman;
    public float secondPlayerSkill;

    public boolean sound;
    public float soundVolume;
    public boolean music;
    public float musicVolume;
    
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load() {
    	firstPlayerHuman = prefs.getBoolean("firstPlayerHuman",true);
    	firstPlayerSkill = MathUtils.clamp(prefs.getFloat("firstPlayerSkill", 0f), 0f, 5f);
    	secondPlayerHuman = prefs.getBoolean("secondPlayerHuman",false);
    	secondPlayerSkill = MathUtils.clamp(prefs.getFloat("secondPlayerSkill", 2f), 0f, 5f);
    	
    	sound = prefs.getBoolean("sound", true);
    	soundVolume = MathUtils.clamp(prefs.getFloat("soundVolume", 1f), 0f, 1f);
    	music = prefs.getBoolean("music", true);
    	musicVolume = MathUtils.clamp(prefs.getFloat("musicVolume", 1f), 0f, 1f);
    }

    public void save() {
    	prefs.putBoolean("firstPlayerHuman", firstPlayerHuman);
    	prefs.putFloat("firstPlayerSkill", firstPlayerSkill);
    	prefs.putBoolean("secondPlayerHuman", secondPlayerHuman);
    	prefs.putFloat("secondPlayerSkill", secondPlayerSkill);
    	
    	prefs.putBoolean("sound", sound);
    	prefs.putFloat("soundVolume", soundVolume);
    	prefs.putBoolean("music", music);
    	prefs.putFloat("musicVolume", musicVolume);
        prefs.flush();
    }

}
