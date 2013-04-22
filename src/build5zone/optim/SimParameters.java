package build5zone.optim;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SimParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8163963167338782430L;
	double [] params = new double[53];
	
	public double[] getParams() {
		return params;
	}


	public void setParams(double[] params) {
		this.params = params;
	}

	

	public SimParameters(double[] params) {
		super();
		this.params = ArrayUtils.clone(params);
	}


	@Override
	public boolean equals(Object obj){
		if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        
        SimParameters rhs = (SimParameters) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(params, rhs.getParams()).
            isEquals();
        
	}
	
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
	            // if deriving: appendSuper(super.hashCode()).
	            append(params).
	            toHashCode();
		
	}
	
	public SimParameters clone(){
		return new SimParameters(params);
		
	}
}
