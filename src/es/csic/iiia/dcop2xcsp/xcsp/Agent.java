/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.csic.iiia.dcop2xcsp.xcsp;

/**
 *
 * @author Marc Pujol <mpujol@iiia.csic.es>
 */
public class Agent {
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        // <agent name="agentZ" />
        return "<agent name=\"" + name + "\" />";
    }
}
