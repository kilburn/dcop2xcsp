/*
 * Software License Agreement (BSD License)
 * 
 * Copyright (c) 2010, IIIA-CSIC, Artificial Intelligence Research Institute
 * All rights reserved.
 * 
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * 
 *   Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 * 
 *   Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 * 
 *   Neither the name of IIIA-CSIC, Artificial Intelligence Research Institute 
 *   nor the names of its contributors may be used to
 *   endorse or promote products derived from this
 *   software without specific prior written permission of
 *   IIIA-CSIC, Artificial Intelligence Research Institute
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package es.csic.iiia.dcop2xcsp.dcop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Base implementation of a cost function.
 *
 * This class provides some basic methods implementing the operations that can
 * be made over cost functions, while delegating the actual cost/utility values
 * representation/storage to the concrete class that extends lit.
 *
 * @author Marc Pujol <mpujol at iiia.csic.es>
 */
public class CostFunction {

    /**
     * Ordered list of variables involved in this function.
     */
    private COPVariable[] variables;

    /**
     * Unordered set of variables involved in this function.
     */
    private SortedSet<COPVariable> variableSet;

    /**
     * Total size (in elements) of the hypercube formed by this function's
     * variables.
     */
    private int size;

    /**
     * List of aggregated dimensionality up to "index".
     */
    protected int[] sizes;
    
    /**
     * Hypercube values storage array.
     */
    private double[] values;

    /**
     * Creates a new CostFunction, with unknown values.
     *
     * @param variables involved in this factor.
     */
    public CostFunction(COPVariable[] variables) {
        this.variables = variables;
        this.variableSet = new TreeSet<COPVariable>(Arrays.asList(variables));
        computeFunctionSize();
        
        if (size < 0) {
            return;
        }
        
        values = new double[size];
    }

    /**
     * Constructs a new factor by copying the given one.
     *
     * @param factor factor to copy.
     */
    public CostFunction(CostFunction factor) {
        variableSet = new TreeSet<COPVariable>(factor.getVariableSet());
        variables = variableSet.toArray(new COPVariable[0]);
        size = factor.size;
        sizes = factor.sizes.clone();
    }

    
    public void initialize(Double initialValue) {
        for (int i=0; i<size; i++) {
            values[i] = initialValue;
        }
    }

    /**
     * Computes the function's size and dimensionalities.
     * @see #size
     * @see #sizes
     */
    private void computeFunctionSize() {
        final int len = variables.length;
        size = 1;
        sizes = new int[len];
        boolean overflow = false;
        for (int i=0; i<len; i++) {
            sizes[i] = size;
            size *= variables[len-i-1].getDomain();
            if (size < 0) {
                overflow = true;
                break;
            }
        }

        if (overflow) {
            size = -1;
        }
    }

    /**
     * Get the linearized index corresponding to the given variable mapping.
     * 
     * Warning: if there's more than one item matching the specified mapping,
     * only the first one is returned by this function!
     *
     * @param mapping of the desired configuration.
     * @return corresponding linearized index.
     */
    public int getIndex(COPTuple mapping) {
        final int len = variables.length;
        if (len == 0) {
            // This can be an empty or a constant factor
            return size == 0 ? -1 : 0;
        }

        int idx = 0;
        for (int i = 0; i < len; i++) {
            Integer v = mapping.get(variables[i]);
            if (v != null) {
                idx += sizes[len - i - 1] * v;
            } else {
                
            }
        }
        return idx;
    }

    /**
     * Get the linearized index corresponding to the given variable mapping.
     *
     * @param mapping of the desired configuration.
     * @return corresponding linearized index.
     */
    public List<Integer> getIndexes(COPTuple mapping) {
        List<Integer> idxs = new ArrayList<Integer>();

        final int len = variables.length;
        if (len == 0) {
            if (size > 0) {
                idxs.add(0);
            }
            return idxs;
        }
        idxs.add(0);
        
        for (int i = 0; i < len; i++) {
            Integer v = mapping.get(variables[i]);
            if (v != null) {
                // We might be tracking multiple valid indidces
                for (int j = 0; j < idxs.size(); j++) {
                    idxs.set(j, idxs.get(j) + sizes[len - i - 1] * v);
                }
            } else {
                // For each current index, we have to spawn "n" new indices,
                // where "n" is the free variable dimensionality
                for (int j = 0, ilen = idxs.size(); j < ilen; j++) {
                    final int n = variables[i].getDomain();
                    for (v = 0; v < n; v++) {
                        idxs.add(idxs.get(j) + sizes[len - i - 1] * v);
                    }
                }
            }
        }
        return idxs;
    }

    /**
     * Get the variable mapping corresponding to the given linearized index.
     *
     * @param index linearized index of the desired configuration.
     * @param mapping variable mapping to be filled. If null, a new mapping
     *                is automatically instantiated.
     * @return variable mapping filled with the desired configuration.
     */
    public COPTuple getMapping(int index, COPTuple mapping) {
        if (mapping == null) {
            mapping = new COPTuple(variables.length);
        } else {
            mapping.clear();
        }

        final int len = variables.length;
        for (int i = 0; i < len; i++) {
            final int ii = len - 1 - i;
            mapping.put(variables[i], (int)(index / sizes[ii]));
            index = index % sizes[ii];
        }
        return mapping;
    }

    /**
     * Get the function's size (in number of possible configurations).
     * @return number of function's possible configurations.
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the functions's aggregated dimesionalities vector.
     * @return function's aggregated dimensionalities vector.
     */
    protected int[] getSizes() {
        return sizes;
    }

    /** {@inheritDoc} */
    public String getName() {
        StringBuilder buf = new StringBuilder();
        buf.append("F(");
        if (variables.length > 0) {
            buf.append(variables[0].getName());
            for (int i = 1; i < variables.length; i++) {
                buf.append(",");
                buf.append(variables[i].getName());
            }
        }
        buf.append(")");
        return buf.toString();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getName());
        buf.append(" {");
        if (size>0 && values != null) {
            buf.append(values[0]);
            for(int i=1; i<size; i++) {
                buf.append(",");
                buf.append(values[i]);
            }
        }
        buf.append("}");

        return buf.toString();
    }

    public String toLongString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getName());
        buf.append(" {\n");
        if (size>0 && values != null) {
            COPTuple map = null;
            for(int i=0; i<size; i++) {
                map = getMapping(i, map);
                for (COPVariable v : variables) {
                    buf.append(map.get(v));
                    buf.append(" ");
                }
                buf.append("| ");
                buf.append(values[i]);
                buf.append("\n");
            }
        }
        buf.append("}");

        return buf.toString();
    }
    
    public double getValue(int index) {
        return values[index];
    }
    
    public double getValue(int[] index) {
        return getValue(subindexToIndex(index));
    }

    /** {@inheritDoc} */
    public double getValue(COPTuple mapping) {
        int idx = this.getIndex(mapping);
        
        if (idx < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        return getValue(idx);
    }

    /** {@inheritDoc} */
    public SortedSet<COPVariable> getVariableSet() {
        return variableSet;
    }

    /** {@inheritDoc} */
    public Set<COPVariable> getSharedVariables(CostFunction factor) {
        return getSharedVariables(factor.getVariableSet());
    }

    /** {@inheritDoc} */
    public Set<COPVariable> getSharedVariables(COPVariable[] variables) {
        return getSharedVariables(Arrays.asList(variables));
    }

    /** {@inheritDoc} */
    public Set<COPVariable> getSharedVariables(Collection variables) {
        HashSet<COPVariable> res = new HashSet<COPVariable>(variableSet);
        res.retainAll(variables);
        return res;
    }

    /**
     * Returns the subindices list (ordered list of values for each variable of
     * this factor) corresponding to the given values array index.
     * index.
     *
     * @param index values array index.
     * @return subindices list.
     */
    public int[] indexToSubindex(int index) {
        int[] idx = new int[variables.length];
        final int len = variables.length;
        for (int i = 0; i < len; i++) {
            final int ii = len - 1 - i;
            idx[i] = (int)(index / sizes[ii]);
            index = index % sizes[ii];
        }
        return idx;
    }
    
    public void setValue(int index, double value) {
        values[index] = value;
    }
    
    public void setValues(double[] values) {
        if (values.length != this.values.length) {
            throw new IllegalArgumentException("Invalid index specification");
        }

        this.values = values;
    }
    
    public void setValue(int[] index, double value) {
        setValue(subindexToIndex(index), value);
    }

    /**
     * Converts a vector of indices (one for each variable) to the corresponding
     * linearized index of the whole function.
     *
     * @param subindex vector of variable configurations (indices).
     * @return corresponding linearized index.
     */
    protected int subindexToIndex(int[] subindex) {
        // Check index lengths
        if (subindex.length != sizes.length) {
            throw new IllegalArgumentException("Invalid index specification");
        }
        // Compute subindex -> index offset
        int idx = 0;
        for (int i = 0; i < subindex.length; i++) {
            // Check domain limits
            if (variables[i].getDomain() <= subindex[i]) {
                throw new IllegalArgumentException("Invalid index " + subindex[i] + " for dimension " + i);
            }
            idx += sizes[subindex.length - i - 1] * subindex[i];
        }
        return idx;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CostFunction)) {
            return false;
        }
        final CostFunction other = (CostFunction) obj;

        return equals(other, 0.0001);
    }

    /**
     * Indicates whether some other factor is "equal to" this one, concerning a
     * delta.
     *
     * @param other the reference object with which to compare.
     * @param delta the maximum delta between factor values for which both
     * numbers are still considered equal.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    public boolean equals(CostFunction other, double delta) {

        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (this.variableSet != other.getVariableSet() &&
                (this.variableSet == null ||
                !this.variableSet.equals(other.getVariableSet())))
        {
            return false;
        }

        // Constant cost function handling
        if (variableSet.size() == 0) {
            final double e = getValue(0) - other.getValue(0);
            return Math.abs(e) <= delta;
        }

        COPTuple map = null;
        for (int i=0; i<size; i++) {
            map = this.getMapping(i, map);
            final double v1 = getValue(i);
            final double v2 = other.getValue(map);
            if (Double.isNaN(v1) || Double.isNaN(v2)) {
                return false;
            }
            final double e = getValue(i) - other.getValue(map);
            if (Math.abs(e) > delta) {
                return false;
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.variableSet != null ? this.variableSet.hashCode() : 0);
        return hash;
    }

}
