/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Theo
 */
public class Game extends AbstractAppState {
    SimpleApplication sa;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.sa = (SimpleApplication)app;
        
        sa.getViewPort().setBackgroundColor(ColorRGBA.LightGray);  
        /** Add a light source so we can see the model */
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        sa.getRootNode().addLight(dl);
        initCam();
        //
        Oto oto = new Oto(sa);
        oto.setGroundSpeed(1.5f);
    
        rotationNode = new Node("rotationNode");
        rotationNode.setLocalTranslation(0, -72, 0);
        rootNode.attachChild(rotationNode);
        World world = new World(this, rotationNode);
        for(int i =0; i < o1.length; i++){
            o1[i] = new Obstacle(this,rotationNode);
        }
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
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
