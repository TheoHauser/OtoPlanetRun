/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Theo
 */
public class World {
    SimpleApplication sa;
    Node rotNode;
    Sphere sphere;
    Geometry world;
    Material mat; 
    WorldControl wc;
    
    public World(SimpleApplication s, Node rotationNode){
        sa = s;
        rotNode = rotationNode;
        createWorld();
        world.getControl(WorldControl.class);
        
        wc = new WorldControl();
        world.addControl(wc);
    }
    
    private void createWorld(){
        sphere = new Sphere(100,100,60);
        world = new Geometry("world", sphere);
        world.setLocalTranslation(0,-62,0);
        
        mat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", sa.getAssetManager().loadTexture("Textures/invertedmap.jpg"));
        //mat.setBoolean("UseMaterialColors", true);  
        //mat.setColor("Diffuse", ColorRGBA.White);
        //mat.setColor("Specular",ColorRGBA.White);
        world.setMaterial(mat);
        
        Quaternion q   = new Quaternion().fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));
        rotNode.attachChild(world);
        rotNode.setLocalRotation(q);
        sa.getRootNode().attachChild(rotNode);
    }
    
    public void setRotationSpeed(){
        
    }
    class WorldControl extends AbstractControl{
        float var =0;
        @Override
        protected void controlUpdate(float tpf){
                
            final WorldControl wc = new WorldControl();
            wc.setSpatial(world);
            Quaternion q = new Quaternion();
            q.fromAngleAxis((var)+= tpf, new Vector3f(0,0,1));
            world.setLocalRotation(q);
        }
        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
        
    }
}
