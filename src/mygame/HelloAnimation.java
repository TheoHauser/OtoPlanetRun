                
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
  Node rotationNode;
  Obstacle[] o1 = new Obstacle[30];
  
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
    initCam();
    //
    Oto oto = new Oto(this);
    oto.setGroundSpeed(1.5f);
    
    rotationNode = new Node("rotationNode");
    rotationNode.setLocalTranslation(0, -72, 0);
    rootNode.attachChild(rotationNode);
    World world = new World(this, rotationNode);
    for(int i =0; i < o1.length; i++){
        o1[i] = new Obstacle(this,rotationNode);
    }
    
  }
  
  public void initCam(){
        Vector3f cl = new Vector3f(0,20, 16);
        cam.setLocation(cl);
        cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
        flyCam.setEnabled(false);
    }
  
}