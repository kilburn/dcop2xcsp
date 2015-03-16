/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.csic.iiia.dcop2xcsp.xcsp;

import es.csic.iiia.dcop2xcsp.util.Tuple;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marc Pujol <mpujol@iiia.csic.es>
 */
public class Relation {
    
    private String name;
    
    private int arity;
    
    private String semantics ="soft";
    
    private double defaultCost = 0;
    
    private List<Tuple<Double, int[]>> values = new ArrayList<Tuple<Double, int[]>>();

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

    public String getSemantics() {
        return semantics;
    }

    public void setSemantics(String semantics) {
        this.semantics = semantics;
    }

    public double getDefaultCost() {
        return defaultCost;
    }

    public void setDefaultCost(double defaultCost) {
        this.defaultCost = defaultCost;
    }
    
    public void addValue(double value, int[] idx) {
        values.add(new Tuple(value, idx));
    }
    
    @Override
    public String toString() {
        
        //    <relation name="NEQ" arity="2" nbTuples="3" semantics="soft" defaultCost="0">  
        //      infinity: 1 1|2 2|3 3  
        //    </relation>
        
        StringBuilder buf = new StringBuilder();
        buf.append("<relation name=\"").append(name).append("\" ")
                .append("arity=\"").append(arity).append("\" ")
                .append("nbTuples=\"").append(values.size()).append("\" ")
                .append("semantics=\"").append(semantics).append("\" ")
                .append("defaultCost=\"").append(defaultCost).append("\">\n\t");
        
        buf.append(values.get(0).getValue0()).append(":");
        for (int i : values.get(0).getValue1()) {
            buf.append(" ").append(i);
        }
        for (int j=1, len=values.size(); j<len; j++) {
            final Tuple<Double, int[]> t = values.get(j);
            
            buf.append(" | ").append(t.getValue0()).append(":");
            for (int i : t.getValue1()) {
                buf.append(" ").append(i);
            }
        }
        
        buf.append("\n</relation>");
        
        return buf.toString();
    }
    
}
