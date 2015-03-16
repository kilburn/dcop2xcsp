/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.csic.iiia.dcop2xcsp;

import es.csic.iiia.dcop2xcsp.dcop.COPVariable;
import es.csic.iiia.dcop2xcsp.dcop.CostFunction;
import es.csic.iiia.dcop2xcsp.dcop.ProblemReader;
import es.csic.iiia.dcop2xcsp.xcsp.Agent;
import es.csic.iiia.dcop2xcsp.xcsp.Constraint;
import es.csic.iiia.dcop2xcsp.xcsp.Domain;
import es.csic.iiia.dcop2xcsp.xcsp.Instance;
import es.csic.iiia.dcop2xcsp.xcsp.Relation;
import es.csic.iiia.dcop2xcsp.xcsp.Variable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class.
 * 
 * @author Marc Pujol <mpujol@iiia.csic.es>
 */ 
public class Dcop2xcsp {
    private static final Logger LOG = Logger.getLogger(Dcop2xcsp.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if (args.length < 1) {
            showUsage();
        }
        
        try {
            FileInputStream f = new FileInputStream(args[0]);
            ProblemReader r = new ProblemReader();
            List<CostFunction> problem = r.read(f);
            Instance i = buildInstance(problem);
            System.out.println(i);
            
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private static void showUsage() {
        System.err.println("Usage: dcop2xcsp <problem>\n");
        System.exit(1);
    }
    
    public static Instance buildInstance(List<CostFunction> functions) {
        Instance instance = new Instance();
        
        // Variables
        Set<COPVariable> vars = new TreeSet<COPVariable>();
        for (CostFunction f : functions) {
            vars.addAll(f.getVariableSet());
        }
        
        // Add 1 agent per variable, 1 domain per variable
        for (COPVariable v : vars) {
            
            Agent a = new Agent();
            a.setName("Agent" + v.getId());
            instance.addAgent(a);
            
            Domain d = new Domain();
            d.setName("Domain" + v.getId());
            d.setSize(v.getDomain());
            instance.addDomain(d);
            
            Variable var = new Variable();
            var.setName("Variable" + v.getId());
            var.setAgent("Agent" + v.getId());
            var.setDomain("Domain" + v.getId());
            instance.addVariable(var);
            
        }
        
        // Add 1 constraint per function, 1 relation per constraint
        for (CostFunction f : functions) {
            
            Relation r = new Relation();
            r.setName("Relation" + f.getName());
            r.setArity(f.getVariableSet().size());
            for (int i=0, len=f.getSize(); i<len; i++) {
                int idx[] = f.indexToSubindex(i);
                r.addValue(f.getValue(i), idx);
            }
            instance.addRelation(r);
            
            Constraint c = new Constraint();
            c.setName(f.getName());
            c.setArity(f.getVariableSet().size());
            for (COPVariable v : f.getVariableSet()) {
                c.addScope("Variable" + v.getId());
            }
            c.setReference("Relation" + f.getName());
            instance.addConstraint(c);
            
        }
        
        return instance;
    }
    
}
