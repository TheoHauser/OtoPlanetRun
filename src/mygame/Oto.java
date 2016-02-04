package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Rolf
 */
public class Oto {

    SimpleApplication sa;
    private AnimChannel channel;
    private AnimControl control;
    private String requestedState = "";
    Node otoNode;
    Control otoControl;
    private float groundSpeed = 0.0f;
    //
    // -------------------------------------------------------------------------
    // the key action listener: set requested state
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (keyPressed) {
                requestedState = name;
            }
        }
    };
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf){
            if(name.equals("Left")){
                Vector3f v = otoNode.getLocalTranslation();
                if(!(v.x < -5))
                    otoNode.setLocalTranslation(v.x - value*10, v.y, v.z);
            }
            if(name.equals("Right")){
                Vector3f v = otoNode.getLocalTranslation();
                if(!(v.x > 5))
                    otoNode.setLocalTranslation(v.x + value*10, v.y, v.z);
            }
        }
        
    };

    // -------------------------------------------------------------------------
    public Oto(SimpleApplication sa) {
        this.sa = sa;
        initKeys();
        initModel();
    }

    // -------------------------------------------------------------------------  
    // set ground speed. Used for walking adjustment in control.
    public void setGroundSpeed(float spd) {
        this.groundSpeed = spd;
    }

    // -------------------------------------------------------------------------  
    // Custom Keybindings: Mapping a named action to a key input.
    private void initKeys() {
        sa.getInputManager().addMapping("Push", new KeyTrigger(KeyInput.KEY_V));
        sa.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        sa.getInputManager().addListener(actionListener, new String[]{"Push", "Jump"});
        
        sa.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        sa.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        sa.getInputManager().addListener(analogListener, new String[]{"Left", "Right"});
    }

    // -------------------------------------------------------------------------
    // init model
    // load a model that contains animation
    private void initModel() {
        otoNode = (Node) sa.getAssetManager().loadModel("Models/Oto/Oto.mesh.xml");
        otoNode.setLocalScale(0.5f);
        
        //rotate 180
        Quaternion q = new Quaternion();
        q.fromAngleAxis( FastMath.PI , new Vector3f(0,1,0) );  
        otoNode.setLocalRotation(q);
        sa.getRootNode().attachChild(otoNode);
        //
        // Create a controller and channels.
        control = otoNode.getControl(AnimControl.class);
        channel = control.createChannel();
        channel.setAnim("stand");
        //
        // add control
        otoControl = new OtoControl();
        otoNode.addControl(otoControl);
    }

    // -------------------------------------------------------------------------
    // OtoControl
    class OtoControl extends AbstractControl {

        private final int STATE_WALK = 0;
        private final int STATE_STAND = 1;
        private final int STATE_JUMP = 2;
        private int state;
        private boolean stateIsInitialized = false;
        private float stateTime;

        public OtoControl() {
            switchState(STATE_STAND);
        }

        // ---------------------------------------------------------------------
        @Override
        protected void controlUpdate(float tpf) {
            stateTime += tpf;
            // state machine
            String reqState = requestedState;
            requestedState = "";
            
            // just for debugging purpose: toggle ground speed
            if (reqState.equals("Push")){
                groundSpeed=groundSpeed>0?0:1.0f;
            }
            
            // ----------------------------------------
            switch (state) {
                case (STATE_STAND):
                    if (!stateIsInitialized) {
                        stateIsInitialized = true;
                        channel.setAnim("stand",0.0f);
                    }
                    if (reqState.equals("Jump")) {
                        switchState(STATE_JUMP);
                    }//
                    // if the earth spins, immediately switch to walk.
                    else if (groundSpeed > 0.0f) {
                        switchState(STATE_WALK);
                    }
                    break;
                case (STATE_JUMP):
                    Vector3f pos = otoNode.getLocalTranslation();
                    if (!stateIsInitialized) {
                        stateIsInitialized = true;
                        channel.setAnim("pull");
                    }
                    // Jump
                    float y = FastMath.sin(stateTime * 5);
                    otoNode.setLocalTranslation(pos.x, y, pos.z);
                    //
                    // end of state?
                    pos = otoNode.getLocalTranslation();
                    if (y <= 0.0f) {
                        otoNode.setLocalTranslation(pos.x, 0, pos.z);
                        switchState(STATE_STAND);
                    }
                    break;
                case (STATE_WALK):
                    if (!stateIsInitialized) {
                        stateIsInitialized = true;
                        channel.setAnim("Walk");
                        channel.setSpeed(groundSpeed);
                    }
                    // state action: adjust to groundspeed
                        channel.setSpeed(groundSpeed);
                    //
                    // end of state?
                    if (groundSpeed == 0.0f) {
                        switchState(STATE_STAND);
                    }
                    if (reqState.equals("Jump")) {
                        switchState(STATE_JUMP);
                    }
                    break;
            }
            
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }

        // ---------------------------------------------------------------------
        private void switchState(int state) {
            stateIsInitialized = false;
            this.state = state;
            stateTime = 0.0f;
        }
    }
}
