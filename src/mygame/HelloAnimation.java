
package mygame;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
/** Sample 7 - how to load an OgreXML model and play an animation, 
 * using channels, a controller, and an AnimEventListener. */
public class HelloAnimation extends SimpleApplication {
  Node player;
  private AnimChannel channel;
  private AnimControl control;
  public static void main(String[] args) {
    HelloAnimation app = new HelloAnimation();
    app.start();
  }
  @Override
  public void simpleInitApp() {
    viewPort.setBackgroundColor(ColorRGBA.LightGray);  
    /** Add a light source so we can see the model */
    DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
    rootNode.addLight(dl);  
    //
    Oto oto = new Oto(this);
    oto.setGroundSpeed(1.5f);
  }
}