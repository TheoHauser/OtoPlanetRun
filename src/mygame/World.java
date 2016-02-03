/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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
    
    public World(SimpleApplication s, Node rotationNode){
        sa = s;
        rotNode = rotationNode;
        createWorld();
    }
    
    private void createWorld(){
        sphere = new Sphere(100,100,50);
        world = new Geometry("world", sphere);
        world.setLocalTranslation(0,0,0);
        
        mat = new Material(sa.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"); 
          
        mat.setColor("Color", ColorRGBA.Blue);
        //mat.setColor("Specular",ColorRGBA.Blue);
        world.setMaterial(mat);
        
        rotNode.attachChild(world);
        sa.getRootNode().attachChild(rotNode);
    }
    
    public void setRotationSpeed(){
        
    }
}
