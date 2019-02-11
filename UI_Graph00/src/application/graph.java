package application;
import java.util.List;
import java.util.ArrayList;


/**
 * Graph class
 * 
 * @author bedo
 * @see node
 * @see arc
 */
public class graph {
	private List<node> graphNodes;
	private List<arc> graphArcs;
	private int nodeCounter = 0;

    /**
     * Graph constructor
     * 
     */
	public graph(){
		graphNodes = new ArrayList<node>();
		graphArcs = new ArrayList<arc>();
	}
	
	/**
	 * Used to increment the nodeCounter
	 * 
	 * @param n incremeter
	 */
	public void setnodeCounter(int n) {
		this.nodeCounter = n+1;
	}
	
	/**
	 * Add node to the graph, specifying the form of the node. 
	 *  
	 * @param form of the node ("rectangle" or "circle")
	 * @return boolean, true if node is added correctly, false otherwise.
	 */
	public boolean	AddNode(String form) {
		//String tempId = Character.toString((char) nodeCounter);
		node tempAdd = new node(Integer.toString(nodeCounter), form);
		boolean added = false;
		
		if (graphNodes.contains(tempAdd) == false) {
			added = graphNodes.add(tempAdd);
			this.nodeCounter += 1;
			return true;}
		return false;
	}
	
	/**
	 *  Used to find a specific node in the graphNodes using index node 
	 * 
	 * @param n, index of the node request.
	 * @return node request, null otherwise.
	 */
	public node findNode(String n) {
		for (node el : graphNodes) {	
			if (n.equals(el.getID()))
				return el;
		}
		return null;
	}
	
	/**
	 * Add arc, between two nodes, to the graph. 
	 * Specifying the form/to nodes and dir.
	 * 
	 * @param from, starting node
	 * @param to, ending node
	 * @param dir, "oneway"
	 * @return boolean, true if arc is added correctly, false otherwise. 
	 */
	public boolean AddArc(String from, String to, String dir) {
		node node1 = findNode(from);
		node node2 = findNode(to);
		arc tempAdd = new arc(node1, node2, from+"_"+to, dir);
		// check if the arc is already present, if yes return false and do nothing, if yes add arc and return true
		if (node1.findArc(node2) != null)			
			return false;
		else {
			node1.addLink(tempAdd);
			node2.addLink(tempAdd);
			graphArcs.add(tempAdd);
			return true;
		}
	}
	
	/**
	 * Add arc, between two nodes, to the graph. 
	 * Specifying the form/to nodes and dir.
	 * 
	 * @param from, starting node
	 * @param to, ending node
	 * @param dir, "twoway"
	 * @return boolean, true if arc is added correctly, false otherwise. 
	 */
	public boolean AddBidArc(String from, String to, String dir) {
		return AddArc(from, to, dir) && AddArc(to, from, dir);
	}
	
	/**
	 * Remove a node from the graph and all the dependancy arcs.
	 * 
	 * @param index node 
	 * @return boolean, true if node is added correctly, false otherwise.
	 */
	public boolean RemoveNode(String index) {
		node node1 = findNode(index); 
		graphNodes.remove(node1);
		for (arc el : node1.getOutcomingLinks()) {
			node to = el.getEndNode();
			to.deleteArc(el);
			graphArcs.remove(el);
		}
		for (arc ell : node1.getIncomingLinks()) {
			node from = ell.getStartNode();
			from.deleteArc(ell);
			graphArcs.remove(ell);
		}
		
		return true;
	}
	
	/**
	 *  Remove arc from the graph, specifying the fron node and the to node.
	 * 
	 * @param from, index of the from node
	 * @param to, index of the to node
	 * @return boolean, true if arc is remove correctly, false otherwise.
	 */
	public boolean RemoveArc(String from, String to) {
		node node1 = findNode(from);
		node node2 = findNode(to);
		
		arc arc1 = node1.findArc(node2);
		//arc1.printArc();
		if (arc1 == null)
			return false;
		else {
			node1.deleteArc(arc1);
			node2.deleteArc(arc1);
			graphArcs.remove(arc1);
		}	
		if (arc1.getDir() == "twoWay") {
			arc arc2 = node2.findArc(node1);
			node1.deleteArc(arc2);
			node2.deleteArc(arc2);
			graphArcs.remove(arc2);
		}
		return true;
	}
	
	/**
	 * Remove arc from the graph, specifying the arc index.
	 * 
	 * @param el arc index.
	 */
	public void RemoveArc(String el){
		for (arc i : getArcs()) {
			if (i.getIdArc().equals(el)) {
				RemoveArc(i.getStartNode().getID(), i.getEndNode().getID());
			}
		}
	}

	/**
	 * Return the number of Nodes in the graph.
	 * 
	 * @return graphNodes size.
	 */
	public int sizeNodes() {
		return graphNodes.size();
	}
	
	/**
	 * Return the number of Arcs in the graph.
	 * 
	 * @return graphArcs size.
	 */
	public int sizeArcs() {
		return graphArcs.size();
	}
	
	/**
	 * Return the List of graph arcs.
	 * 
	 * @return List<arc> graphArcs.
	 */
	public List<arc> getArcs() {
		return graphArcs;
	}

	/**
	 * Return the List of graph nodes
	 * 
	 * @return List<arc> graphNodes.
	 */
	public List<node> getNodes() {
		return graphNodes;
	}
	
	/**
	 * Return the last node inserted.
	 * 
	 * @return last node inserted.
	 */
	public node lastNode() {
		return graphNodes.get(graphNodes.size()-1);
	}

	/**
	 * Return the last arc inserted.
	 * 
	 * @return last arc inserted.
	 */
	public arc lastArc() {
		return graphArcs.get(graphArcs.size()-1);
	}
	
	/**
	 * Check if an arc is already in the graph.
	 * 
	 * @param i index arc.
	 * @return boolean, true if arc is in the graph, false otherwise.
	 */
	public boolean hasArc(String i) {
		for (arc el : getArcs()) {
			if(el.getIdArc().equals(i))
				return true;
		}
		return false;
	}
	
	/**
	 * Clear the graph.
	 */
	public void clear() {
		this.getArcs().clear();
		this.getNodes().clear();
	}
	
}
