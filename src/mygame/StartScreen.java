/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;

/**
 *
 * @author Theo
 */
public class StartScreen extends AbstractAppState implements ActionListener{
    BitmapText text, text1;
    HelloAnimation sa;
    AppStateManager sm;
    boolean realGameStarted;
    
    @Override
    public void onAction(String name, boolean keyDown, float tpf) {
        if (keyDown) {
            if (name.equals("Start") && !realGameStarted) {
                Game game = new Game();
                sm.detach(this);
                sm.attach(game);
                realGameStarted = true; 
            }
            if (name.equals("Quit")) {
                // this is brutal.
                System.exit(0);
            }
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        sa = (HelloAnimation)app;
        sm = stateManager;
        sa.clearJMonkey(sa);
        
        sa.getViewPort().setBackgroundColor(ColorRGBA.Black);

        BitmapFont bmf = app.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
        text = new BitmapText(bmf);
        text.setSize(bmf.getCharSet().getRenderedSize() * 10);
        text.setColor(ColorRGBA.Green);
        text.setText("Oto's Planet Run");
        sa.getGuiNode().attachChild(text);
        
        text1 = new BitmapText(bmf);
        text1.setSize(bmf.getCharSet().getRenderedSize() * 5);
        text1.setColor(ColorRGBA.White);
        text1.setText("Press space to start\nPress ESC to quit");
        sa.getGuiNode().attachChild(text1);
        
        AppSettings s = sa.getSettings();
        float lineY = s.getHeight() / 2; 
        float lineX = (s.getWidth() - text.getLineWidth()) / 2;
        text.setLocalTranslation(lineX, lineY, 0f);
        text1.setLocalTranslation(lineX/2, lineY/2, 0f);
        
        //create listeners
        InputManager im = sa.getInputManager();
        im.addMapping("Start", new KeyTrigger(KeyInput.KEY_SPACE));
        im.addMapping("Quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        im.addListener(this, "Start", "Quit");
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
