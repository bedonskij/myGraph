package application;
import java.util.List;
import java.util.ArrayList;

/**
 * Node class 
 *
 * @author bedo
 *
 */
public class node{
	private String idNode;
	private String form;
	private List<arc> incoming = new ArrayList<arc>();
	private List<arc> outcoming = new ArrayList<arc>();
	
	/**
	 * Node constructor
	 * 
	 * @param idx, String,  index of the node
	 * @param formx, String, shape of the node
	 */
	public node(String idx, String formx) {
		this.idNode = idx;
		this.form = formx;
	}

	/**
	 * method that returns the index of
	 * 
	 * @return index of the  node
	 */
	public String getID() {
		return this.idNode;
	}

	/**
	 * set the index of the node
	 * 
	 * @param s is the custom index asigned to the node 
	 */
	public void setID(String s) {
		this.idNode = s;
	}
	
	/**
	 *  add arc to the node, incoming or outcoming based on the node, 
	 *  return boolean
	 * 
	 * @param link, arc in input. 
	 * @return boolean, true if the arc is properly add, false if not.
	 */
	public boolean addLink(arc link) {
		if (link.getStartNode()==this) 
			outcoming.add(link);
		else if (link.getEndNode()==this) 
			incoming.add(link);
		else 
			return false;
		return true;
	}
	
	/**
	 * Return the list of incoming arcs. 
	 * 
	 * @return the list of incoming arcs.
	 */
	public List<arc> getIncomingLinks() {
		return incoming;
	}
	
	/**
	 * Return List of outcoming arcs.
	 * 
	 * @return List of outcoming arcs.
	 */
	public List<arc> getOutcomingLinks() {
		return outcoming;
	}
	
	/**
	 * Return true if the node has the arc, false if not.
	 * Used to check if this node has the link arc.
	 * 
	 * @param link, arc to be checked.
	 * @return boolean, true if the arc i
	 * @see graph
	 * @see Main
	 */
	public boolean hasArc(arc link) {
		if (link.getStartNode() == this)
			return  incoming.contains(link);
		else if (link.getEndNode() == this)
			return outcoming.contains(link);
		else
			return false;
	}	
	
	/**
	 * Remove the request arc, return true if the arc is removed, false if not.
	 * 
	 * @param link, arc to be removed.
	 * @return boolean, true if the arc is removed, false otherwise.
	 */
	public boolean deleteArc(arc link) {
		if (link.getStartNode() == this)
			outcoming.remove(link);
		else if (link.getEndNode() == this)
			incoming.remove(link);
		else
			return false;
		return true;
	}

	/**
	 *  Return arc if there is, null otherwise.
	 *  Used to find if the a this node is connected to end node
	 *  
	 * @param end, node connected to this one.
	 * @return arc if there is, null otherwise.
	 * @see graph
	 */
	public arc findArc(node end) {
		for (arc el : outcoming) {
			if (el.getEndNode() == end)
				return el;
		}
		return null;
	}
	
	/**
	 * Return the shape of the node.
	 * Used in the UI to define the shape of the javafx object.
	 * 
	 * @return the shape of the node.
	 */
	public String getForm() {
		return form;
	}

}