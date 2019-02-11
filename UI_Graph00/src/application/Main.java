package application;
	

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;



public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("myGraph");
        	/*  ---------  ---------  ---------  */
        	/*  ---------   MYGRAPH   ---------  */
        	/*  ---------  ---------  ---------  */
			graph mygraph = new graph();
	    	
        	/*  ---------  ---------  ---------  */
        	/*  ---------   UI DEF.   ---------  */
        	/*  ---------  ---------  ---------  */
	        ToggleButton rectNbtn = new ToggleButton("Rectangle");
	        ToggleButton circNbtn = new ToggleButton("Circle");
	        ToggleButton moveNbtn = new ToggleButton("Move Node");
	        ToggleButton delNbtn = new ToggleButton("Delete Node");
	        ToggleButton arcObtn = new ToggleButton("One way Arc");
	        ToggleButton arcBbtn = new ToggleButton("Two ways Arc");
	        ToggleButton delAbtn = new ToggleButton("Delete Arc");
	        
	        // variable temp on button add arc
	        arcObtn.setUserData("-");
	        arcBbtn.setUserData("-");
	        // Toggle Button array
	        ToggleButton[] toolsArr = {rectNbtn, circNbtn, moveNbtn, delNbtn, arcObtn, arcBbtn, delAbtn};
	        // Toggle group to manage events
	        ToggleGroup tools = new ToggleGroup();
	        for (ToggleButton tool : toolsArr) {
	            tool.setMinWidth(90);
	            tool.setToggleGroup(tools);
	            tool.setCursor(Cursor.HAND);
	        }
	        
	        // Button for Open and Save function
	        Button save = new Button("Save");
	        Button open = new Button("Open");
	        Button grid = new Button("Grid");
	        
	        // buttons array
	        Button[] basicArr = {save, open, grid};
	        for(Button btn : basicArr) {
	            btn.setMinWidth(90);
	            btn.setCursor(Cursor.HAND);
	            btn.setTextFill(Color.WHITE);
	            btn.setStyle("-fx-background-color: #666;");
	        }
	        // custom style to Save e Open buttons
	        save.setStyle("-fx-background-color: #80334d;");
	        open.setStyle("-fx-background-color: #80334d;");
	        
	        // add a VBox with all buttons
	        VBox btns = new VBox(10);
	        btns.getChildren().addAll(rectNbtn, circNbtn, moveNbtn, delNbtn, arcObtn, 
	        		arcBbtn, delAbtn, save, open, grid);
	        btns.setPadding(new Insets(5));
	        btns.setStyle("-fx-background-color: #fc6a04");
	        btns.setPrefWidth(100);
	        
						
        	/*  ---------  ---------  ---------  */
        	/*  ---------     PANE    ---------  */
        	/*  ---------  ---------  ---------  */
			Canvas myCanvas = new Canvas(1080, 790);
			drawCanvasGrid(myCanvas, false);
			myCanvas.setId("can");
			myCanvas.setUserData("gridOFF");
	        BorderPane pane = new BorderPane(); 
	        

        	/*  ---------  ---------  ---------  */
        	/*  ---------  ADD EVENTs ---------  */
        	/*  ---------  ---------  ---------  */
	        // temporary variable for arc creation
	        tempPoint puntoTemp = new tempPoint();
	        
	        // event definition on setOnMousePressed ToggleButton and nodes
	        pane.setOnMousePressed(e->{
	        	
	        	/*  ---------  ---------  ---------  */
	        	/*  ---------  RECTANGLE  ---------  */
	        	/*  ---------  ---------  ---------  */
	        	if(rectNbtn.isSelected() && (e.getX()>130 && e.getX()<1170) && (e.getY()>25 && e.getY()<775)) {
	            	// Add a rectangle node in the graph model
	            	mygraph.AddNode("rectangle");
	            	// Add a rectangle node in the UI graph
	               	Rectangle nodeTemp = setRectangle(mygraph.lastNode(),e.getX(), e.getY());
	               	// add event DRAGGER to the node
	            	nodeTemp.setOnMouseDragged(new EventHandler<MouseEvent>()
	                {
	                    @Override
	                    public void handle(MouseEvent t) {
	                    	if(moveNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		if ((t.getX()>130 && t.getX()<1170) && (t.getY()>25 && t.getY()<775)) {
	                    		nodeTemp.setX(t.getX()-20);
	                    		nodeTemp.setY(t.getY()-20);	                    		
	                    		}else {return;}
	                    	}
	                    }
	                });
	            	// add event DELETION to the node
	            	nodeTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
	                {
	                    @Override
	                    public void handle(MouseEvent t) {
	                    	// DELETE node, in the model and in the UI
	                    	if(delNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		deleteLineArc(mygraph.findNode(nodeTemp.getUserData().toString()), pane);
	                    		mygraph.RemoveNode(nodeTemp.getUserData().toString());
	                    		pane.getChildren().remove(nodeTemp);	  
	                    		}
	                    	// Add Arc one way
	                    	if(arcObtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		// if the first node is selected for the arc creation
	                    		if(arcObtn.getUserData().toString().equals("-")) {	             
	                    			puntoTemp.setTempX(nodeTemp.xProperty().add(20));
	                    			puntoTemp.setTempY(nodeTemp.yProperty().add(20));
	                    			puntoTemp.setIdPoint(nodeTemp.getId());
	                    			arcObtn.setUserData(nodeTemp.getId());
	                    		}
	                    		// if the second node is selected for the arc creation
	                    		else {
	                    			// if the same node is selected for the second time	                    		
	                    			if (nodeTemp.getId().equals(puntoTemp.getIdPoint())) {	                    				
		                    			arcObtn.setUserData("-");
	                    			}
	                    			// if another node is selected
	                    			else {
	                    				// check if the arc is already present in the graph
	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
		                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "oneway");
		                    				Line lineTemp = new Line();
		                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
		                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
		                    				lineTemp.endXProperty().bind(nodeTemp.xProperty().add(20));
		                    				lineTemp.endYProperty().bind(nodeTemp.yProperty().add(20));
		                    				lineTemp.setStrokeWidth(6);
		                    				lineTemp.setFill(Color.BLACK);
		                    				lineTemp.setId(puntoTemp.getIdPoint()+"_"+nodeTemp.getId());
		                    				lineTemp.setUserData(puntoTemp.getIdPoint()+"_"+nodeTemp.getId());
		                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
		                    				{
		                	                    @Override
		                	                    public void handle(MouseEvent t) {
			                    					if (delAbtn.isSelected() && lineTemp.contains(t.getX(), t.getY())) {
			                    						mygraph.RemoveArc(lineTemp.getId());
			                    						pane.getChildren().remove(lineTemp);
			                    					}
		                	                  }
		                    				});
		                    				// adding the new arc to the pane
			                    			pane.getChildren().add(lineTemp);
			                    			refresh(pane, mygraph);
	                    				}
	                    				// reset the temporary var for the arc creation
	                    				arcObtn.setUserData("-");
	                    			}
	                    		}
	                    	}
	                    	// same thing for the bidirectional arc
	                    	if(arcBbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		if(arcBbtn.getUserData().toString().equals("-")) {	             
	                    			puntoTemp.setTempX(nodeTemp.xProperty().add(20));
	                    			puntoTemp.setTempY(nodeTemp.yProperty().add(20));
	                    			puntoTemp.setIdPoint(nodeTemp.getId());
	                    			arcBbtn.setUserData(nodeTemp.getId());
	                    		}
	                    		else {	                    		
	                    			if (nodeTemp.getId().equals(puntoTemp.getIdPoint())) {
		                    			arcBbtn.setUserData("-");
	                    			}
	                    			else {
	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
	                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "twoway");
	                    				Line lineTemp = new Line();
	                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
	                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
	                    				lineTemp.endXProperty().bind(nodeTemp.xProperty().add(20));
	                    				lineTemp.endYProperty().bind(nodeTemp.yProperty().add(20));
	                    				lineTemp.setStrokeWidth(6);
	                    				lineTemp.setStroke(Color.BLUE);
	                    				lineTemp.setId(mygraph.lastArc().getIdArc());
	                    				lineTemp.setUserData(mygraph.lastArc().getIdArc());
	                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
	                    				{
	                	                    @Override
	                	                    public void handle(MouseEvent t) {
		                    					if (delAbtn.isSelected() && lineTemp.contains(t.getX(), t.getY())) {
			                    					mygraph.RemoveArc(lineTemp.getId());
			                    					pane.getChildren().remove(lineTemp);
		                    					}
	                	                  }
	                    				});
		                    			pane.getChildren().add(lineTemp);
		                    			refresh(pane, mygraph);
	                    				}
	                    				arcBbtn.setUserData("-");
	                    			}
	                    		}
	                    	}
	                    }	
	                });
	            	
	            	// Add javaFX node to the active pane
	            	pane.getChildren().add(nodeTemp);
	            	refresh(pane, mygraph);
	            }
	                       
	       
	        	/*  ---------  ---------  ---------  */
	        	/*  ---------   CIRCLE    ---------  */
	        	/*  ---------  ---------  ---------  */
	            else if(circNbtn.isSelected() && (e.getX()>130 && e.getX()<1170) && (e.getY()>25 && e.getY()<775)) {
	            	// Add a circle node in the graph model
	            	mygraph.AddNode("circle");
	            	// Add a circle node in the UI graph
	            	Circle nodeTemp = setCircle(mygraph.lastNode(), e.getX(), e.getY());
	              	// add event DRAGGER to the node
	            	nodeTemp.setOnMouseDragged(new EventHandler<MouseEvent>()
	                {
	                    @Override
	                    public void handle(MouseEvent t) {
	                    	if(moveNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		if((t.getX()>130 && t.getX()<1170) && (t.getY()>25 && t.getY()<775)) {
	                    		nodeTemp.setCenterX(t.getX());
	                    		nodeTemp.setCenterY(t.getY());
	                    		}else {return;}	           
	                    	}
	                    }
	                });
	            	// add event DELETION to the node
	            	nodeTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
	                {
	                    @Override
	                    public void handle(MouseEvent t) {
	                    	if(delNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		deleteLineArc(mygraph.findNode(nodeTemp.getUserData().toString()), pane);
	                    		mygraph.RemoveNode(nodeTemp.getUserData().toString());
	                    		pane.getChildren().remove(nodeTemp);
	                    		}
	                    	if(arcObtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		if(arcObtn.getUserData().toString().equals("-")) {	             
	                    			puntoTemp.setTempX(nodeTemp.centerXProperty().add(0));
	                    			puntoTemp.setTempY(nodeTemp.centerYProperty().add(0));
	                    			puntoTemp.setIdPoint(nodeTemp.getId());
	                    			arcObtn.setUserData(nodeTemp.getId());
	                    		}
	                    		else {	                    		
	                    			if (nodeTemp.getId().equals(puntoTemp.getIdPoint())) {
		                    			arcObtn.setUserData("-");
	                    			}
	                    			else {
	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
	                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "oneway");
	                    				Line lineTemp = new Line();
	                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
	                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
	                    				lineTemp.endXProperty().bind(nodeTemp.centerXProperty());
	                    				lineTemp.endYProperty().bind(nodeTemp.centerYProperty());
	                    				lineTemp.setStrokeWidth(6);
	                    				lineTemp.setFill(Color.BLACK);
	                    				lineTemp.setId(mygraph.lastArc().getIdArc());
	                    				lineTemp.setUserData(mygraph.lastArc().getIdArc());
	                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
	                    				{
	                	                    @Override
	                	                    public void handle(MouseEvent p) {
		                    					if (delAbtn.isSelected() && lineTemp.contains(p.getX(), p.getY())) {
			                    					mygraph.RemoveArc(lineTemp.getId());
			                    					pane.getChildren().remove(lineTemp);
		                    					}
	                	                  }
	                    				});
		                    			pane.getChildren().add(lineTemp);
		                    			refresh(pane, mygraph);
	                    				}
	                    				arcObtn.setUserData("-");
	                    			}
	                    		}
	                    	}

	                    	if(arcBbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
	                    		if(arcBbtn.getUserData().toString().equals("-")) {	             
	                    			puntoTemp.setTempX(nodeTemp.centerXProperty().add(0));
	                    			puntoTemp.setTempY(nodeTemp.centerYProperty().add(0));
	                    			puntoTemp.setIdPoint(nodeTemp.getId());
	                    			arcBbtn.setUserData(nodeTemp.getId());
	                    		}
	                    		else {	                    		
	                    			if (nodeTemp.getId().toString().equals(puntoTemp.getIdPoint())) {
		                    			arcBbtn.setUserData("-");
	                    			}
	                    			else {
	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
	                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "twoway");
	                    				Line lineTemp = new Line();
	                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
	                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
	                    				lineTemp.endXProperty().bind(nodeTemp.centerXProperty());
	                    				lineTemp.endYProperty().bind(nodeTemp.centerYProperty());
	                    				lineTemp.setStrokeWidth(6);
	                    				lineTemp.setStroke(Color.BLUE);
	                    				lineTemp.setId(mygraph.lastArc().getIdArc());
	                    				lineTemp.setUserData(mygraph.lastArc().getIdArc());
	                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
	                    				{
	                	                    @Override
	                	                    public void handle(MouseEvent p) {
		                    					if (delAbtn.isSelected() && lineTemp.contains(p.getX(), p.getY())) {
			                    					mygraph.RemoveArc(lineTemp.getId());
			                    					pane.getChildren().remove(lineTemp);
		                    					}
	                	                  }
	                    				});
		                    			pane.getChildren().add(lineTemp);
		                    			refresh(pane, mygraph);
	                    				}
	                    				arcBbtn.setUserData("-");
	                    			}
	                    		}
	                    	}
	                    	
	                    }
	                });
	            	
	            	// Add javaFX node to the active pane
	            	pane.getChildren().add(nodeTemp);
	            	refresh(pane, mygraph);
	            }

	        });

	        
        	/*  ---------  ---------  ---------  */
        	/*  --------- OPEN File   ---------  */
        	/*  ---------  ---------  ---------  */
	        open.setOnAction((e)->{
	        	/* cleaning mygraph and pane before the importing */
	        	for(arc el : mygraph.getArcs()) {
	        		pane.getChildren().remove(pane.lookup("#"+el.getIdArc()));
	        	}
	        	for(node el : mygraph.getNodes()) {
	        		pane.getChildren().remove(pane.lookup("#"+el.getID()));
	        	}
	        	mygraph.clear();
	            
	        	/* choosing and opening file */
	        	FileChooser openFile = new FileChooser();
	            openFile.setTitle("Open File");
	            File file = openFile.showOpenDialog(primaryStage);
	            if (file != null) {
	                try {
	                	BufferedReader br = new BufferedReader(new FileReader(file));
	                	String line;
	                    while ((line = br.readLine()) != null) {
	                       System.out.println(line);
	                       
	                       String[] inputGraph = line.split("\\t");
	                       if("N".equals(inputGraph[0])) {
	                    	   if("rectangle".equals(inputGraph[2])) {
	                    		   mygraph.AddNode("rectangle");
	                    		   mygraph.lastNode().setID(inputGraph[1]);
	                    		   mygraph.setnodeCounter(Integer.parseInt(inputGraph[1]));
	                    		   Rectangle nodeTemp = setRectangle(mygraph.lastNode(), Double.parseDouble(inputGraph[3]), Double.parseDouble(inputGraph[4]));
	                    		   // add event to the node
	                    		   nodeTemp.setOnMouseDragged(new EventHandler<MouseEvent>(){
		           	                    @Override
		           	                    public void handle(MouseEvent t) {
		           	                    	if(moveNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		           	                    		if ((t.getX()>130 && t.getX()<1170) && (t.getY()>25 && t.getY()<775)) {
		           	                    		nodeTemp.setX(t.getX()-20);
		           	                    		nodeTemp.setY(t.getY()-20);	                    		
		           	                    		}else {return;}
		           	                    	}
		           	                    }
		           	                });
		           	            	// add event DELETION to the node
		           	            	nodeTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
		           	                {
		           	                    @Override
		           	                    public void handle(MouseEvent t) {
		           	                    	// DELETE node, in the model and in the UI
		           	                    	if(delNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		           	                    		deleteLineArc(mygraph.findNode(nodeTemp.getUserData().toString()), pane);
		           	                    		mygraph.RemoveNode(nodeTemp.getUserData().toString());
		           	                    		pane.getChildren().remove(nodeTemp);	  
		           	                    		}
		           	                    	// Add Arc one way
		           	                    	if(arcObtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		           	                    		// if the first node is selected for the arc creation
		           	                    		if(arcObtn.getUserData().toString().equals("-")) {	             
		           	                    			puntoTemp.setTempX(nodeTemp.xProperty().add(20));
		           	                    			puntoTemp.setTempY(nodeTemp.yProperty().add(20));
		           	                    			puntoTemp.setIdPoint(nodeTemp.getId());
		           	                    			arcObtn.setUserData(nodeTemp.getId());
		           	                    		}
		           	                    		// if the second node is selected for the arc creation
		           	                    		else {
		           	                    			// if the same node is selected for the second time	                    		
		           	                    			if (nodeTemp.getId().equals(puntoTemp.getIdPoint())) {	                    				
		           		                    			arcObtn.setUserData("-");
		           	                    			}
		           	                    			// if another node is selected
		           	                    			else {
		           	                    				// check if the arc is already present in the graph
		           	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
		           		                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "oneway");
		           		                    				Line lineTemp = new Line();
		           		                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
		           		                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
		           		                    				lineTemp.endXProperty().bind(nodeTemp.xProperty().add(20));
		           		                    				lineTemp.endYProperty().bind(nodeTemp.yProperty().add(20));
		           		                    				lineTemp.setStrokeWidth(6);
		           		                    				lineTemp.setFill(Color.BLACK);
		           		                    				lineTemp.setId(puntoTemp.getIdPoint()+"_"+nodeTemp.getId());
		           		                    				lineTemp.setUserData(puntoTemp.getIdPoint()+"_"+nodeTemp.getId());
		           		                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
		           		                    				{
		           		                	                    @Override
		           		                	                    public void handle(MouseEvent t) {
		           			                    					if (delAbtn.isSelected() && lineTemp.contains(t.getX(), t.getY())) {
		           			                    						mygraph.RemoveArc(lineTemp.getId());
		           			                    						pane.getChildren().remove(lineTemp);
		           			                    					}
		           		                	                  }
		           		                    				});
		           		                    				// adding the new arc to the pane
		           			                    			pane.getChildren().add(lineTemp);
		           			                    			refresh(pane, mygraph);
		           	                    				}
		           	                    				// reset the temporary var for the arc creation
		           	                    				arcObtn.setUserData("-");
		           	                    			}
		           	                    		}
		           	                    	}
		           	                    	// same thing for the bidirectional arc
		           	                    	if(arcBbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		           	                    		if(arcBbtn.getUserData().toString().equals("-")) {	             
		           	                    			puntoTemp.setTempX(nodeTemp.xProperty().add(20));
		           	                    			puntoTemp.setTempY(nodeTemp.yProperty().add(20));
		           	                    			puntoTemp.setIdPoint(nodeTemp.getId());
		           	                    			arcBbtn.setUserData(nodeTemp.getId());
		           	                    		}
		           	                    		else {	                    		
		           	                    			if (nodeTemp.getId().equals(puntoTemp.getIdPoint())) {
		           		                    			arcBbtn.setUserData("-");
		           	                    			}
		           	                    			else {
		           	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
		           	                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "twoway");
		           	                    				Line lineTemp = new Line();
		           	                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
		           	                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
		           	                    				lineTemp.endXProperty().bind(nodeTemp.xProperty().add(20));
		           	                    				lineTemp.endYProperty().bind(nodeTemp.yProperty().add(20));
		           	                    				lineTemp.setStrokeWidth(6);
		           	                    				lineTemp.setStroke(Color.BLUE);
		           	                    				lineTemp.setId(mygraph.lastArc().getIdArc());
		           	                    				lineTemp.setUserData(mygraph.lastArc().getIdArc());
		           	                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
		           	                    				{
		           	                	                    @Override
		           	                	                    public void handle(MouseEvent t) {
		           		                    					if (delAbtn.isSelected() && lineTemp.contains(t.getX(), t.getY())) {
		           			                    					mygraph.RemoveArc(lineTemp.getId());
		           			                    					pane.getChildren().remove(lineTemp);
		           		                    					}
		           	                	                  }
		           	                    				});
		           		                    			pane.getChildren().add(lineTemp);
		           		                    			refresh(pane, mygraph);
		           	                    				}
		           	                    				arcBbtn.setUserData("-");
		           	                    			}
		           	                    		}
		           	                    	}
		           	                    }	
		           	                });
		           	            	
		           	            	// Add javaFX node to the active pane
		           	            	pane.getChildren().add(nodeTemp);
		           	            	refresh(pane, mygraph);
	                    	   }
	                    	   else if("circle".equals(inputGraph[2])) {
	                    		   mygraph.AddNode("circle");
	                    		   mygraph.lastNode().setID(inputGraph[1]);
	                    		   mygraph.setnodeCounter(Integer.parseInt(inputGraph[1]));
	                    		   Circle nodeTemp = setCircle(mygraph.lastNode(), Double.parseDouble(inputGraph[3]), Double.parseDouble(inputGraph[4]));
	                    		   // add event to the node
		           	               nodeTemp.setOnMouseDragged(new EventHandler<MouseEvent>()
		        	                {
		        	                    @Override
		        	                    public void handle(MouseEvent t) {
		        	                    	if(moveNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		        	                    		if((t.getX()>130 && t.getX()<1170) && (t.getY()>25 && t.getY()<775)) {
		        	                    		nodeTemp.setCenterX(t.getX());
		        	                    		nodeTemp.setCenterY(t.getY());
		        	                    		}else {return;}	           
		        	                    	}
		        	                    }
		        	                });
		        	            	// add event DELETION to the node
		        	            	nodeTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
		        	                {
		        	                    @Override
		        	                    public void handle(MouseEvent t) {
		        	                    	if(delNbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		        	                    		deleteLineArc(mygraph.findNode(nodeTemp.getUserData().toString()), pane);
		        	                    		mygraph.RemoveNode(nodeTemp.getUserData().toString());
		        	                    		pane.getChildren().remove(nodeTemp);
		        	                    		}
		        	                    	if(arcObtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		        	                    		if(arcObtn.getUserData().toString().equals("-")) {	             
		        	                    			puntoTemp.setTempX(nodeTemp.centerXProperty().add(0));
		        	                    			puntoTemp.setTempY(nodeTemp.centerYProperty().add(0));
		        	                    			puntoTemp.setIdPoint(nodeTemp.getId());
		        	                    			arcObtn.setUserData(nodeTemp.getId());
		        	                    		}
		        	                    		else {	                    		
		        	                    			if (nodeTemp.getId().equals(puntoTemp.getIdPoint())) {
		        		                    			arcObtn.setUserData("-");
		        	                    			}
		        	                    			else {
		        	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
		        	                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "oneway");
		        	                    				Line lineTemp = new Line();
		        	                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
		        	                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
		        	                    				lineTemp.endXProperty().bind(nodeTemp.centerXProperty());
		        	                    				lineTemp.endYProperty().bind(nodeTemp.centerYProperty());
		        	                    				lineTemp.setStrokeWidth(6);
		        	                    				lineTemp.setFill(Color.BLACK);
		        	                    				lineTemp.setId(mygraph.lastArc().getIdArc());
		        	                    				lineTemp.setUserData(mygraph.lastArc().getIdArc());
		        	                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
		        	                    				{
		        	                	                    @Override
		        	                	                    public void handle(MouseEvent p) {
		        		                    					if (delAbtn.isSelected() && lineTemp.contains(p.getX(), p.getY())) {
		        			                    					mygraph.RemoveArc(lineTemp.getId());
		        			                    					pane.getChildren().remove(lineTemp);
		        		                    					}
		        	                	                  }
		        	                    				});
		        		                    			pane.getChildren().add(lineTemp);
		        		                    			refresh(pane, mygraph);
		        	                    				}
		        	                    				arcObtn.setUserData("-");
		        	                    			}
		        	                    		}
		        	                    	}
	
		        	                    	if(arcBbtn.isSelected() && nodeTemp.contains(t.getX(), t.getY()) ) {
		        	                    		if(arcBbtn.getUserData().toString().equals("-")) {	             
		        	                    			puntoTemp.setTempX(nodeTemp.centerXProperty().add(0));
		        	                    			puntoTemp.setTempY(nodeTemp.centerYProperty().add(0));
		        	                    			puntoTemp.setIdPoint(nodeTemp.getId());
		        	                    			arcBbtn.setUserData(nodeTemp.getId());
		        	                    		}
		        	                    		else {	                    		
		        	                    			if (nodeTemp.getId().toString().equals(puntoTemp.getIdPoint())) {
		        		                    			arcBbtn.setUserData("-");
		        	                    			}
		        	                    			else {
		        	                    				if(!mygraph.hasArc(puntoTemp.getIdPoint()+"_"+nodeTemp.getId())) {
		        	                    				mygraph.AddArc(puntoTemp.getIdPoint(), nodeTemp.getId(), "twoway");
		        	                    				Line lineTemp = new Line();
		        	                    				lineTemp.startXProperty().bind(puntoTemp.getTempX());
		        	                    				lineTemp.startYProperty().bind(puntoTemp.getTempY());
		        	                    				lineTemp.endXProperty().bind(nodeTemp.centerXProperty());
		        	                    				lineTemp.endYProperty().bind(nodeTemp.centerYProperty());
		        	                    				lineTemp.setStrokeWidth(6);
		        	                    				lineTemp.setStroke(Color.BLUE);
		        	                    				lineTemp.setId(mygraph.lastArc().getIdArc());
		        	                    				lineTemp.setUserData(mygraph.lastArc().getIdArc());
		        	                    				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
		        	                    				{
		        	                	                    @Override
		        	                	                    public void handle(MouseEvent p) {
		        		                    					if (delAbtn.isSelected() && lineTemp.contains(p.getX(), p.getY())) {
		        			                    					mygraph.RemoveArc(lineTemp.getId());
		        			                    					pane.getChildren().remove(lineTemp);
		        		                    					}
		        	                	                  }
		        	                    				});
		        		                    			pane.getChildren().add(lineTemp);
		        		                    			refresh(pane, mygraph);
		        	                    				}
		        	                    				arcBbtn.setUserData("-");
		        	                    			}
		        	                    		}
		        	                    	}
		        	                    	
		        	                    }
		        	                });
		        	            	
		        	            	// Add javaFX node to the active pane
		        	            	pane.getChildren().add(nodeTemp);
		        	            	refresh(pane, mygraph);	                    		   
	                    	   }
	                       }
	                       else if("A".equals(inputGraph[0])) {
	                    	   
                  				mygraph.AddArc(inputGraph[3], inputGraph[5], inputGraph[2]);
                  				Line lineTemp = new Line();
                  				if("rectangle".equals(inputGraph[4])) {
                  					Rectangle temp0 = (Rectangle) pane.lookup("#"+inputGraph[3]);
                  					lineTemp.startXProperty().bind(temp0.xProperty().add(20));
                      				lineTemp.startYProperty().bind(temp0.yProperty().add(20));
                  				}
                  				else if("circle".equals(inputGraph[4])) {
                  					Circle temp1 = (Circle) pane.lookup("#"+inputGraph[3]);
                  					lineTemp.startXProperty().bind(temp1.centerXProperty());
                      				lineTemp.startYProperty().bind(temp1.centerYProperty());                  					
                  				}
                  				
                 				if("rectangle".equals(inputGraph[6])) {
                  					Rectangle temp2 = (Rectangle) pane.lookup("#"+inputGraph[5]);
                  					lineTemp.endXProperty().bind(temp2.xProperty().add(20));
                      				lineTemp.endYProperty().bind(temp2.yProperty().add(20));
                  				}
                  				else if("circle".equals(inputGraph[6])) {
                  					Circle temp3 = (Circle) pane.lookup("#"+inputGraph[5]);
                  					lineTemp.endXProperty().bind(temp3.centerXProperty());
                      				lineTemp.endYProperty().bind(temp3.centerYProperty());                  					
                  				}
                  					
                  				if("oneway".equals(inputGraph[2])){
                  					lineTemp.setStroke(Color.BLACK);
                  				}else if("twoway".equals(inputGraph[2])) {
                  					lineTemp.setStroke(Color.BLUE);
                  				}
                  				
                  				lineTemp.setStrokeWidth(6);
                  				lineTemp.setId(inputGraph[1]);
                  				lineTemp.setUserData(inputGraph[1]);
                  				lineTemp.setOnMouseClicked(new EventHandler<MouseEvent>()
                  				{
              	                    @Override
              	                    public void handle(MouseEvent t) {
	                    					if (delAbtn.isSelected() && lineTemp.contains(t.getX(), t.getY())) {
		                    					mygraph.RemoveArc(lineTemp.getId());
		                    					pane.getChildren().remove(lineTemp);
	                    					}
              	                  }
                  				});
	                    		pane.getChildren().add(lineTemp);
	                    		refresh(pane, mygraph);
							
	                       }
	                       else
	                    	   return;
   
	                    }
	                } catch (IOException ex) {
	                    System.out.println("Error!");
	                }
	            }
	        });
	
	        
        	/*  ---------  ---------  ---------  */
        	/*  --------  SAVE to file  -------  */
        	/*  ---------  ---------  ---------  */
	        save.setOnAction((e)->{
	            FileChooser savefile = new FileChooser();
	            savefile.setTitle("Save mygraph");
	            BufferedWriter output = null;
	            
	            try {
	            	File file = savefile.showSaveDialog(primaryStage);
	                output = new BufferedWriter(new FileWriter(file));
	            	// nodes
	            	for (node el : mygraph.getNodes()) {
	            		String outString = new String("N\t"+el.getID());	
	            		if (el.getForm().equals("rectangle")) {
	            			Rectangle temp = (Rectangle) pane.lookup("#"+el.getID());    			
	            			outString = outString+"\t"+el.getForm()+"\t"+Double.toString(temp.getX())+"\t"+Double.toString(temp.getY())+"\n";
	            		}
	            		else {
	            			Circle temp = (Circle) pane.lookup("#"+el.getID());
	            			outString = outString+"\t"+el.getForm()+"\t"+Double.toString(temp.getCenterX())+"\t"+Double.toString(temp.getCenterY())+"\n";
	            		}
	            		output.write(outString);
	            		//System.out.println(arg0);
	            	}
	            	// arcs
	            	for (arc el : mygraph.getArcs()) {
	            		String outString = new String("A\t"+el.getIdArc());				
	            		outString = outString+"\t"+el.getDir()+"\t"+el.getStartNode().getID()+"\t"+el.getStartNode().getForm()+"\t"+el.getEndNode().getID()+"\t"+el.getEndNode().getForm()+"\n";
	            		output.write(outString);
	            	}
	            	output.close();
	            } catch ( IOException p ) {
	                p.printStackTrace();
	            } 
	        });

	        
        	/*  ---------  ---------  ---------  */
        	/*  ---------     GRID    ---------  */
        	/*  ---------  ---------  ---------  */	        
	        grid.setOnAction((e)->{
	        	/* add or remove the background grid */
	        	if(myCanvas.getUserData().toString().equals("gridOFF")) {
	        		drawCanvasGrid(myCanvas, true);
	        		refresh(pane, mygraph);
	        		myCanvas.setUserData("gridON");
	        	}else {
	        		drawCanvasGrid(myCanvas, false);
	        		refresh(pane, mygraph);
	        		myCanvas.setUserData("gridOFF");
	        	}
	        });
	
	        
        	/*  ---------  ---------  ---------  */
        	/*  ---------    SCENE    ---------  */
        	/*  ---------  ---------  ---------  */	      
	        pane.setLeft(btns);
	        pane.setCenter(myCanvas);			
		    Scene scene = new Scene(pane, 1200, 800);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*  ---------  ---------  ---------  */
	/*  --------- CUSTOM FUN. ---------  */
	/*  ---------  ---------  ---------  */
	/*  set UI rectangle node  */
	public Rectangle setRectangle(node n, double X, double Y) {
		Rectangle tempRectangle = new Rectangle();
		tempRectangle.setX(X-20);
		tempRectangle.setY(Y-20);
		tempRectangle.setWidth(40);		
		tempRectangle.setHeight(40);
		tempRectangle.setUserData(n.getID());
		tempRectangle.setId(n.getID());
		
		tempRectangle.setFill(Color.GREEN);
		tempRectangle.setStrokeWidth(2);
		tempRectangle.setStroke(Color.BLACK);

		return tempRectangle;
	}
	/*  set UI Circle node   */
	public Circle setCircle(node n, double X, double Y) {
		Circle tempCircle = new Circle();
		tempCircle.setCenterX(X);
		tempCircle.setCenterY(Y);
		tempCircle.setRadius(20);
		tempCircle.setUserData(n.getID());
		tempCircle.setId(n.getID());
		
		tempCircle.setFill(Color.RED);
		tempCircle.setStrokeWidth(2);
		tempCircle.setStroke(Color.BLACK);
		
		return tempCircle;
	}
	
	/*  delete Arc Line when the a node is deleted   */
	public void deleteLineArc(node n, BorderPane pane) {
		for (arc el : n.getIncomingLinks()) {
			pane.getChildren().remove(pane.lookup("#"+el.getIdArc()));
		}
		for (arc el : n.getOutcomingLinks()) {
			pane.getChildren().remove(pane.lookup("#"+el.getIdArc()));
		}
	}
	
	/* test */
	public void refreshArcs(BorderPane pane, graph g, String s) {
		for (Node el : pane.getChildren()) {
			if (el.getUserData().toString().equals(s)) {
				pane.getChildren().remove(el);
			}
		}
	}
	
	/*  show or hide the grid on Canvas   */
    private void drawCanvasGrid(Canvas canvas, boolean on) {
        GraphicsContext gc = canvas.getGraphicsContext2D() ;
        gc.setStroke(Color.ORANGE);
        gc.strokeRoundRect(0, 0, 1080, 790, 10, 10);
        if(on) {
	        gc.setLineWidth(1.0);
	        for (int x = 0; x < 1080; x+=40) {
	            gc.moveTo(x, 0);
	            gc.lineTo(x, 790);
	            gc.stroke();
	        }
	
	        for (int y = 0; y < 790; y+=40) {
	            gc.moveTo(0, y);
	            gc.lineTo(1080, y);
	            gc.stroke();
	        }
        }
        else {
			gc.setFill(Color.WHITE);
		    gc.fillRect(0, 0, 1080, 790);
	        gc.setStroke(Color.ORANGE);
	        gc.strokeRoundRect(0, 0, 1080, 790, 10, 10);
        	
        }
        
    }
    
    /*   bring the node to front position   */
    public void refresh(BorderPane p, graph g) {
    	for (node i : g.getNodes()) {
    		p.lookup("#"+i.getID()).toFront();
    	}
    }
    /* print graph in a structured way */
    public void toFile(graph g, BorderPane p) {
    	// nodes
    	System.out.println("node:");
    	for (node el : g.getNodes()) {
    		String outString = new String(el.getID());	
    		if (el.getForm().equals("rectangle")) {
    			Rectangle temp = (Rectangle) p.lookup("#"+el.getID());    			
    			outString = outString+"\t"+el.getForm()+"\t"+Double.toString(temp.getX())+"\t"+Double.toString(temp.getY());
    		}
    		else {
    			Circle temp = (Circle) p.lookup("#"+el.getID());
    			outString = outString+"\t"+el.getForm()+"\t"+Double.toString(temp.getCenterX())+"\t"+Double.toString(temp.getCenterY());
    		}
    		System.out.println(outString);
    		//System.out.println(arg0);
    	}
    	// arcs
    	System.out.println("arcs:");
    	for (arc el : g.getArcs()) {
    		String outString = new String(el.getIdArc());				
    		outString = outString+"\t"+el.getDir()+"\t"+el.getStartNode().getID()+"\t"+el.getStartNode().getForm()+"\t"+el.getEndNode().getID()+"\t"+el.getEndNode().getForm();
    		System.out.println(outString);
    	}
    }
	

    
	/*  ---------  ---------  ---------  */
	/*  --------- MAIN LAUNCH ---------  */
	/*  ---------  ---------  ---------  */	
	public static void main(String[] args) {
		launch(args);
	}
}

