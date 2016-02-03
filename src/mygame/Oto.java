package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
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
    }

    // -------------------------------------------------------------------------
    // init model
    // load a model that contains animation
    private void initModel() {
        otoNode = (Node) sa.getAssetManager().loadModel("Models/Oto/Oto.mesh.xml");
        otoNode.setLocalScale(0.5f);
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
                    if (!stateIsInitialized) {
                        stateIsInitialized = true;
                        channel.setAnim("pull");
                    }
                    // Jump
                    float y = FastMath.sin(stateTime * 5);
                    otoNode.setLocalTranslation(0, y, 0);
                    //
                    // end of state?
                    if (y <= 0.0f) {
                        otoNode.setLocalTranslation(0, 0, 0);
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
