                
package mygame;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
/** Sample 7 - how to load an OgreXML model and play an animation, 
 * using channels, a controller, and an AnimEventListener. */
public class HelloAnimation extends SimpleApplication {
  Node player;
  private AnimChannel channel;
  private AnimControl control;
  Game game;
  StartScreen startScreen;
  
  public static void main(String[] args) {
    HelloAnimation app = new HelloAnimation();
    app.start();
  }
  @Override
  public void simpleInitApp() {
      startScreen = new StartScreen();
      game = new Game();
      stateManager.attach(startScreen);
  }
  
  protected static void clearJMonkey(SimpleApplication m) {
      m.getGuiNode().detachAllChildren();
      m.getRootNode().detachAllChildren();
      m.getInputManager().clearMappings();
  }
  
  public AppSettings getSettings() {
      return (settings);
  }
}