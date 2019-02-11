package application;


/**
 * Arc class
 * 
 * @author bedo
 *
 */

public class arc {
	private String idArc;
	private node startNode;
	private node endNode;
	private String direction;
	
	/**
	 * Arc constructor.
	 * 
	 * @param node1, starting node.
	 * @param node2, ending node.
	 * @param id, index of the arc.
	 * @param direct, direction of the arc, it could be oneway or twoway.
	 */
	public arc(node node1, node node2, String id, String direct) {
		this.idArc = id;
		this.direction = direct; 
		this.startNode = node1;
		this.endNode = node2;	
	}
	
	/**
	 * Return the start node.
	 * 
	 * @return the start node.
	 */
	public node getStartNode() {
		return startNode;		
	}

	/**
	 * Return the end node.
	 * 
	 * @return the end node.
	 */
	public node getEndNode() {
		return endNode;		
	}

	/**
	 * Return the Arc index.
	 * 
	 * @return the Arc index.
	 */
	public String getIdArc() {
		return idArc;		
	}
	
	/**
	 * Return the Arc direction.
	 * 
	 * @return the Arc direction.
	 */
	public String getDir() {
		return direction;		
	}
	
}
