/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.csic.iiia.dcop2xcsp.xcsp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marc Pujol <mpujol@iiia.csic.es>
 */
public class Constraint {
    
    private String name;
    
    private int arity;
    
    private List<String> scope = new ArrayList<String>();
    
    private String reference;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArity() {
        return arity;
    }

    public void setArity(int arity) {
        this.arity = arity;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public void addScope(String scope) {
        this.scope.add(scope);
    }
    
    @Override
    public String toString() {
        // <constraint name="Y_and_Z_have_different_colors" arity="2" scope="Y Z" reference="NEQ" />  
        StringBuilder buf = new StringBuilder();
        
        buf.append("<constraint name=\"").append(name).append("\" ")
                .append("arity=\"").append(arity).append("\" ")
                .append("scope=\"").append(scope.get(0));
        
        for (int i=1, len=scope.size(); i<len; i++) {
            buf.append(" ").append(scope.get(i));
        }
        
        buf.append("\" reference=\"").append(reference).append("\" />");
        
        return buf.toString();
    }
}
