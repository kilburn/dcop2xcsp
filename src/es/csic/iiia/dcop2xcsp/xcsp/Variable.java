/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.csic.iiia.dcop2xcsp.xcsp;

/**
 *
 * @author Marc Pujol <mpujol@iiia.csic.es>
 */
public class Variable {
    
    private String name;
    
    private String domain;
    
    private String agent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
    
    @Override
    public String toString() {
        // <variable name="X" domain="three_colors" agent="agentX" /> 
        StringBuilder buf = new StringBuilder();
        buf.append("<variable name=\"").append(name).append("\" ")
                .append("domain=\"").append(domain).append("\" ")
                .append("agent=\"").append(agent).append("\" />");
        
        return buf.toString();
    }
    
}
