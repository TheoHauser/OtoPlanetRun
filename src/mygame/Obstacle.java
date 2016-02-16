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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author hauser2016
 */
public class Obstacle{
    Sphere rock = new Sphere(30,30,1f);
    Geometry r;
    Vector3f pos;
    Node obs;
    
    public Obstacle(SimpleApplication sa, Node rotNode){
        Material mat = new Material(sa.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        //Node obs = (Node) sa.getAssetManager().loadModel("/Textures/Terrain/Rock.j3m");
        mat.setColor("Color", ColorRGBA.Blue);
        
        r = new Geometry("obstacle", rock);
        r.setMaterial(mat);
        
        pos = getObstLocation(); 
        
        r.setLocalTranslation(pos);
        obs = new Node();
        obs.attachChild(r);
        
        rotNode.attachChild(obs);
        float angle = (2*FastMath.PI)*FastMath.nextRandomFloat();
        obs.rotate(angle, 0, 0);
       
        
    }
    
    private Vector3f getObstLocation(){
        float x = (float)5*FastMath.nextRandomFloat()*(FastMath.pow(-1, FastMath.nextRandomInt(1, 2)));
        return new Vector3f(x,70, 0);
    }
    
}
