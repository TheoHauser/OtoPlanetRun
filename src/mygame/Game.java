/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

/**
 *
 * @author Theo
 */
public class Game extends AbstractAppState {
    HelloAnimation sa;
    AppStateManager sm;
    private EndScreen es = new EndScreen();
    
    Node rotationNode;
    Obstacle[] o1 = new Obstacle[50];
    Oto oto;
    World world;
    CollisionResults res;
    int obn = 0;
    int obn1 = 0;
    int numColl = 0;
    float score= 0;
    boolean paused = false;
    
    Game game = this;
    
    BitmapText pauseText, scoreText;
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (keyPressed) {
                if (name.equals("Pause")) {
                    if(!paused){
                        game.setEnabled(false);
                        world.setEnabled(false);
                        oto.setEnabled(false);
                        paused = true;
                    }
                    else{
                        game.setEnabled(true);
                        world.setEnabled(true);
                        oto.setEnabled(true);
                        paused = false;
                    }
                }
                if (name.equals("Quit")) {
                    System.exit(0);
                }
                
            }
        }
    };
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.sa = (HelloAnimation)app;
        this.sm = stateManager;
        
        sa.clearJMonkey(sa);
        
        res = new CollisionResults();
        
        sa.getViewPort().setBackgroundColor(ColorRGBA.LightGray);  
        sa.getRootNode().attachChild(SkyFactory.createSky(sa.getAssetManager(), 
                "Textures/spaceBox.png", false));
        /** Add a light source so we can see the model */
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        sa.getRootNode().addLight(dl);
        
        initCam();
        initKeys();
        //
        oto = new Oto(sa);
        oto.setGroundSpeed(1.5f);
    
        rotationNode = new Node("rotationNode");
        rotationNode.setLocalTranslation(0, -72, 0);
        sa.getRootNode().attachChild(rotationNode);
        world = new World(sa, rotationNode);
        for(int i =0; i < o1.length; i++){
            o1[i] = new Obstacle(sa,rotationNode);
        }
        
        BitmapFont bmf = app.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
        pauseText = new BitmapText(bmf);
        pauseText.setSize(bmf.getCharSet().getRenderedSize() * 10);
        pauseText.setColor(ColorRGBA.Green);
        pauseText.setText("To resume press p");
        
    }
    private void initCam(){
        Vector3f cl = new Vector3f(0,20, 16);
        sa.getCamera().setLocation(cl);
        sa.getCamera().lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
        sa.getFlyByCamera().setEnabled(false);
    }
    
    private void initKeys(){
        InputManager im = sa.getInputManager();
        im.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        im.addMapping("Quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        im.addListener(actionListener, "Pause", "Quit");
    }
    
    @Override
    public void setEnabled(boolean en){
        super.setEnabled(en);
        if(!en){
            sa.getGuiNode().attachChild(pauseText);
            AppSettings s = sa.getSettings();
            float lineY = s.getHeight() / 2; 
            float lineX = (s.getWidth() - pauseText.getLineWidth()) / 2;
            pauseText.setLocalTranslation(lineX, lineY, 0f);
        }
        else{
            sa.getGuiNode().detachChild(pauseText);
        }
    }
    
    @Override
    public void update(float tpf) {
        if(this.isEnabled()){
            checkCollision();
            if(numColl>=15){
                endGame();
            }
            score = score+tpf;
        }
        showScore();

    }
    
    private void showScore(){
        sa.getGuiNode().detachAllChildren();
        BitmapFont bmf = sa.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
        scoreText = new BitmapText(bmf);
        scoreText.setSize(bmf.getCharSet().getRenderedSize()*2);
        scoreText.setColor(ColorRGBA.White);
        scoreText.setText("Hits: "+ numColl + "\nScore: "+ score);
        
        sa.getGuiNode().attachChild(scoreText);
        
        AppSettings s = sa.getSettings();
        float lineY = s.getHeight(); 
        float lineX = (s.getWidth() - pauseText.getLineWidth()) / 6;
        scoreText.setLocalTranslation(lineX, lineY, 0f);
    }
    
    private void endGame(){
        es.setScore(score);
        sm.detach(this);
        sm.attach(es);
    }
    
    private void checkCollision(){
        int count = 0;
        for (Obstacle o:o1){          
            BoundingVolume bv = o.r.getWorldBound();
            oto.otoNode.collideWith(bv, res);
            if (res.size()>0&&obn != count&& obn1!=count){
                obn1 = obn;
                obn = count;
                System.out.println("Collision with obstacle "+count+"   >>  "+res.getCollision(0).toString());
                new SingleBurstParticleEmitter(sa, oto.otoNode, Vector3f.ZERO);
                res.clear();
                numColl++;
            }
            res.clear();
            count++;
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
