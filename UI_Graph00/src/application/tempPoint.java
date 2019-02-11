package application;
import javafx.beans.binding.DoubleBinding;


public class tempPoint {
	private String idPoint;
	private DoubleBinding xPoint;
	private DoubleBinding yPoint;
	
	/**
	 * tempPoint constructor
	 * 
	 */
	public void tempPoint(){
		
	};
	
	/**
	 * Set the xPoint.
	 * 
	 * @param x DoubleBinding of the node
	 */
	public void setTempX(DoubleBinding x) {
			this.xPoint = x;
		
	}
	
	/**
	 * Set the yPoint.
	 * 
	 * @param y DoubleBinding of the node
	 */
	public void setTempY(DoubleBinding y) {
		this.yPoint = y;
	}
	
	/**
	 * Get the xPoint.
	 * 
	 * @return the DoubleBinding xPoint of the node.
	 */
	public DoubleBinding getTempX() {
			return this.xPoint;
	}
	
	/**
	 * Get the yPoint.
	 * 
	 * @return the DoubleBinding yPoint of the node.
	 */
	public DoubleBinding getTempY() {
			return this.yPoint;
	}
	
	/**
	 * Set the index point.
	 * 
	 * @param s index point of the node.
	 */
	public void setIdPoint(String s) {
		this.idPoint = s;
	}
	
	/**
	 * Return the index of the node.
	 * 
	 * @return the index of the node.
	 */
	public String getIdPoint() {
		return this.idPoint;
	}
	
	/**
	 * Clean the tempPoint parameters
	 * 
	 */
	public void clear() {
		xPoint.invalidate();
		yPoint.invalidate();
	}
}
