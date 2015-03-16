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
public class Instance {
    
    private String name = "Undefined";
    
    private int maxConstraintArity = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private List<Agent> agents = new ArrayList<Agent>();
    private List<Domain> domains = new ArrayList<Domain>();
    private List<Variable> variables = new ArrayList<Variable>();
    private List<Relation> relations = new ArrayList<Relation>();
    private List<Constraint> constraints = new ArrayList<Constraint>();
    
    public void addAgent(Agent a) {
        agents.add(a);
    }
    
    public void addDomain(Domain d) {
        domains.add(d);
    }
    
    public void addVariable(Variable v) {
        variables.add(v);
    }
    
    public void addRelation(Relation r) {
        relations.add(r);
    }
    
    public void addConstraint(Constraint c) {
        maxConstraintArity = Math.max(maxConstraintArity, c.getArity());
        constraints.add(c);
    }
    
    public Instance() {}
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        
        //<instance>  
        
        buf.append("<instance>");
        
        //  <presentation name="sampleProblem" maxConstraintArity="2"  
        //                maximize="false" format="XCSP 2.1_FRODO" />
        
        buf.append("<presentation ")
                .append("name=\"").append(name).append("\" ")
                .append("maxConstraintArity=\"").append(maxConstraintArity).append("\" ")
                .append("maximize=\"false\" ")
                .append("format=\"XCSP 2.1_FRODO\" />\n")
                ;

        //  <agents nbAgents="3">  
        //    <agent name="agentX" />  
        //    <agent name="agentY" />  
        //    <agent name="agentZ" />  
        //  </agents>  
        buf.append("<agents nbAgents=\"").append(agents.size()).append("\">\n");
        for (Agent a : agents) {
            paddedAppend(buf, "\t", a.toString()).append("\n");
        }
        buf.append("</agents>\n");
        
        //  <domains nbDomains="1">  
        //    <domain name="three_colors" nbValues="3">1..3</domain>  
        //  </domains>  
        buf.append("<domains nbDomains=\"").append(domains.size()).append("\">\n");
        for (Domain d : domains) {
            paddedAppend(buf, "\t", d.toString()).append("\n");
        }
        buf.append("</domains>\n");
        
        //  <variables nbVariables="3">  
        //    <variable name="X" domain="three_colors" agent="agentX" />  
        //    <variable name="Y" domain="three_colors" agent="agentY" />  
        //    <variable name="Z" domain="three_colors" agent="agentZ" />  
        //  </variables>
        buf.append("<variables nbVariables=\"").append(variables.size()).append("\">\n");
        for (Variable v : variables) {
            paddedAppend(buf, "\t", v.toString()).append("\n");
        }
        buf.append("</variables>\n");
        
        //  <relations nbRelations="1">  
        //    <relation name="NEQ" arity="2" nbTuples="3" semantics="soft" defaultCost="0">  
        //      infinity: 1 1|2 2|3 3  
        //    </relation>  
        //  </relations>
        buf.append("<relations nbRelations=\"").append(relations.size()).append("\">\n");
        for (Relation r : relations) {
            paddedAppend(buf, "\t", r.toString()).append("\n");
        }
        buf.append("</relations>\n");
        
        //  <constraints nbConstraints="3">  
        //    <constraint name="X_and_Y_have_different_colors" arity="2" scope="X Y" reference="NEQ" />  
        //    <constraint name="X_and_Z_have_different_colors" arity="2" scope="X Z" reference="NEQ" />  
        //    <constraint name="Y_and_Z_have_different_colors" arity="2" scope="Y Z" reference="NEQ" />  
        //  </constraints>
        buf.append("<constraints nbConstraints=\"").append(constraints.size()).append("\">\n");
        for (Constraint c : constraints) {
            paddedAppend(buf, "\t", c.toString()).append("\n");
        }
        buf.append("</constraints>\n");
        
        //</instance>
        buf.append("</instance>");
        
        return buf.toString();
    }
    
    private static StringBuilder paddedAppend(StringBuilder buf, String padding, String what) {
        buf.append(padding);
        for (int i=0; i<what.length(); i++) {
            final char c = what.charAt(i);
            buf.append(c);
            if (c == '\n') {
                buf.append(padding);
            }
        }
        
        return buf;
    }
    
}
