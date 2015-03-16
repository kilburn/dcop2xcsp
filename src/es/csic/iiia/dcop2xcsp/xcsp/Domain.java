/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.csic.iiia.dcop2xcsp.xcsp;

/**
 *
 * @author Marc Pujol <mpujol@iiia.csic.es>
 */
public class Domain {
    
    private String name;
    
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        // <domain name="three_colors" nbValues="3">1..3</domain>
        StringBuilder buf = new StringBuilder();
        buf.append("<domain name=\"").append(name).append("\" ")
                .append("nbValues=\"").append(size).append("\">")
                .append("0..").append(size-1).append("</domain>");
        
        return buf.toString();
    }
    
}
