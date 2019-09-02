package opt2flow.com.br.magolandiaapp.Model;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MagoLandiaAppPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    //User
    public static final String USER = "user";

    //Password
    public static final String PASSWORD = "password";

    //Type
    public static final String USER_TYPE = "type";

    //Slider Min
    public static final String SLIDER_MIN = "slider_min";

    //Slider Max
    public static final String SLIDER_MAX = "slider_max";

    //Button 1
    public static final String BUTTON_ONE = "button_one";

    //Button 2
    public static final String BUTTON_TWO = "button_two";

    //Button 3
    public static final String BUTTON_THREE = "button_three";

    //Button 4
    public static final String BUTTON_FOUR = "button_four";

    //Blink Button
    public static final String BLINK_BUTTON = "blink_button";

    //Delay Blink
    public static final String DELAY_BLINK = "delay_blink";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setViraOlho(int viraOlho) {
        editor.putInt("vira_olho", viraOlho);
        editor.commit();
    }

    public void setBoca(int boca) {
        editor.putInt("boca", boca);
        editor.commit();
    }

    public void setPisca(boolean pisca) {
        editor.putBoolean("pisca", pisca);
        editor.commit();
    }

    public int getViraOlho () {
        return pref.getInt("vira_olho", 0);
    }
    public int getBoca(){
        return pref.getInt("boca", 0);
    }
    public boolean getPisca(){
        return pref.getBoolean("pisca", false);
    }

    public void createSession(ManualControl manualControl){
        editor.putInt(SLIDER_MIN, manualControl.getSliderMin());
        editor.putInt(SLIDER_MAX, manualControl.getSliderMax());
        editor.putString(BUTTON_ONE, manualControl.getButtonOne());
        editor.putString(BUTTON_TWO,manualControl.getButtonTwo());
        editor.putString(BUTTON_THREE, manualControl.getButtonThree());
        editor.putString(BUTTON_FOUR, manualControl.getButtonFour());
        editor.putString(BLINK_BUTTON, manualControl.getBlinkButton());
        editor.putInt(DELAY_BLINK, manualControl.getDelayPisca());
        editor.commit();
    }

    public ManualControl getUserDetails(){
        ManualControl manualControl = new ManualControl();
        manualControl.setSliderMin(pref.getInt(SLIDER_MIN, 0));
        manualControl.setSliderMax(pref.getInt(SLIDER_MAX, 0));
        manualControl.setButtonOne(pref.getString(BUTTON_ONE, null));
        manualControl.setButtonTwo(pref.getString(BUTTON_TWO, null));
        manualControl.setButtonThree(pref.getString(BUTTON_THREE, null));
        manualControl.setButtonFour(pref.getString(BUTTON_FOUR, null));
        manualControl.setBlinkButton(pref.getString(BLINK_BUTTON, null));
        manualControl.setDelayPisca(pref.getInt(DELAY_BLINK, 0));
        return manualControl;
    }

    /**
     * Clear session details
     * */
    public void clearSession(){
        editor.clear();
        editor.commit();
    }
}
