package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import application.DashBoardController.Person;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class Attendance 
{

	Logger logger = LoggerFactory.getLogger("Attendance");

	public static String in= "INSTRUCTIONS  FOR USAGE:\r\n" + 
			"\r\n" + 
			"  #TO LOAD A SPREADSHEET: \r\n" + 
			"  1) CLICK THE LOAD SPREADSHEET\r\n" + 
			"     BUTTON\r\n" + 
			"  2) ENTER THE SEMESTER, SECTION,\r\n" + 
			"     AND BATCH DATA\r\n" + 
			"  3) LOAD THE REQUIRED FILE FROM \r\n" + 
			"     THE SYSTEM EXPLORER\r\n" + 
			"\r\n" + 
			"  #TO SAVE SPREADSHEET:\r\n" + 
			"  1) ENTER THE NUMBER  OF CLASSES\r\n" + 
			"     FOR EACH STUDENT (OR HOW \r\n" + 
			"     MANY EVER REQUIRED)\r\n" + 
			"  2) ENTER THE TOTAL NUMBER OF \r\n" + 
			"     CLASSES\r\n" + 
			"  3) SELECT A DATE FROM THE \r\n" +
			"     CALENDAR (STORED ON A MONTHLY\r\n" +
			"     BASIS)\r\n" +
			"  3) CLICK THE SAVE SPREADSHEET\r\n" + 
			"     BUTTON\r\n" + 
			"\r\n" + 
			"  #TO SAVE AND CONSOLIDATE ONLINE: \r\n" +  
			"  1) CLICK THE SAVE BUTTON. THE  \r\n" + 
			"     DATA GETS SYNCED TO FIREBASE\r\n" + 
			"  2) CLICK THE  CONSOLIDATE \r\n" + 
			"     BUTTON TO TRANSFER THE DATA INTO\r\n" + 
			"     THE OFFICIAL WORD DOCUMENT \r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" ;
			


   String sub;
   String[] subs;
 //  String res;
   	String dur = "";
	String tfsem = "";
	String tfsec = "";
	
	Boolean exp = false;
	
	ArrayList<String> special_case_usns = new ArrayList<String>();
	ArrayList<String> special_case_cc = new ArrayList<String>();

	@FXML
	TitledPane tpatt;
	@FXML
	StackPane stack = new StackPane();
	@FXML
	ListView<HBoxCell> special_case_lv = new ListView<HBoxCell>();
	@FXML
	ListView<String> list=new ListView<String>();
	@FXML
	Label in1;
	@FXML
	TextField semester=new TextField();
	@FXML
    TextField batch=new TextField();
	@FXML
    TextField section=new TextField();
	@FXML
	Label lb;
	@FXML 
	AnchorPane ap, ap_attendance, calendar, outerscreen, innerscreen, anchorlabs, anchorspecial;
	
	@FXML
	HBox hb,gianthbox;
	@FXML
	DatePicker datePicker1=new DatePicker();
	@FXML
	TextField subject=new TextField();
	@FXML
	VBox utility_attend, leftvbox;

	ArrayList<String> labBatch = new ArrayList<String>();
	
	@FXML
	CheckBox lab_selector = new CheckBox();
	@FXML
	TextField batch1 = new TextField();
	@FXML
	TextField batch2 = new TextField();
	@FXML
	TextField batch3 = new TextField();
	@FXML
	TitledPane titlelabs;

	
	String rootpath = "C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\";
	String coursecode ="";
	@FXML
	private TableView<Person> table = new TableView<Person>();
	TableColumn usnCol1,nameCol1,classesCol,perCol;
	static ArrayList<String> studdat = new ArrayList<String>();
	ObservableList<String> abc=  FXCollections.observableArrayList();
	
	
	   @FXML
	   private TextField addTotalClasses;
	    
	    private ObservableList<Person> data =
		        FXCollections.observableArrayList();
	    
	    @FXML
	    AnchorPane ap_calendar;
	    
	    @FXML
		Button savespbtn,loadspbtn, savefir, syncsave, loadAttendanceFromDbBtn;
	    
	    @FXML
	    ComboBox drop = new ComboBox();
	    
	    Map<String, List<String>> subjects = new HashMap<String, List<String>>();
	    //This is one instance of the  map you want to store in the above list.

	    
	    String output="";
	    public void initialize() throws IOException
		{		
	    	tpatt.setExpanded(true);
	    	in1.setText(in);
	    	in1.setWrapText(true);
	        in1.setStyle("-fx-font-family: \"Times New Roman \"; -fx-font-size: 0; -fx-text-fill: white; -fx-background-color:#6464a5;");
	    	 
	        
	        setDimen();
		        
		        datePicker1.setOnAction(e -> {
		            LocalDate date = datePicker1.getValue();
		            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		            Date conv_date = java.sql.Date.valueOf(date);
		            String finalDate = formatter.format(conv_date);
		            System.out.println(finalDate);
		            finalDate = finalDate.replace('/', '-');
		        });
		        
		        calendar.getChildren().add(datePicker1);
		        table.setEditable(true);
		    	
				usnCol1 = new TableColumn("USN");
		        usnCol1.setMinWidth(80);
		        usnCol1.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("usn"));
		       
		        nameCol1 = new TableColumn("NAME");
		        nameCol1.setMinWidth(120);
		        nameCol1.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("name"));
		 
		        classesCol = new TableColumn("Classes Attended");
		        classesCol.setMinWidth(80);
		        classesCol.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("classes"));
		        //classesCol.setCellFactory(TextFieldTableCell.forTableColumn());
		        classesCol.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
		            @Override
		            public TableCell<Person, String> call(TableColumn<Person, String> orderItemStringTableColumn) {
		                return new AttendanceEditingCell();
		            }
		        });
		        classesCol.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person, String> t) {
		        ((Person) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setClasses(t.getNewValue());
		        }
		        }
		        );
		        
		        perCol = new TableColumn("Percentage");
		        perCol.setMinWidth(80);
		        perCol.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("per"));
		        
		        table.setItems(data);
		        table.getColumns().addAll(usnCol1,nameCol1, classesCol);
		       
		    	
		    	abc.add("Enter Semester");
		    	abc.add("Enter Section");
		    	abc.add("Enter Batch");
		    	
		    	perCol.setVisible(false);
		    	usnCol1.setVisible(false);
		    	nameCol1.setVisible(false);
		    	classesCol.setVisible(false);
		    	list.setVisible(false);
		    	list.setItems(abc);
		    	
		    	semester.textProperty().addListener((observable, oldValue, newValue) -> {
		    	    dropDown(newValue);
		    	});
		    	
		    	
		    	//List<HBoxCell> list1 = new ArrayList<>();
		       

		          
		          //ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		          //special_case_lv.setItems(myObservableList);
		    	
		    	
		    	table.setRowFactory(tv -> new TableRow<Person>() {
		    	    @Override
		    	    protected void updateItem(Person item, boolean empty) {
		    	        super.updateItem(item, empty);
		    	        //System.out.println("**"+item.getName()+"**");
		    	        if (item == null || item.getName() == null)
		    	            setStyle("");
		    	        else if (!item.getPer().equals(""))
		    	        {
		    	        	if(!item.getPer().equals("-")) {
		    	        	if((Integer.parseInt(item.getPer().toString())<75))
		    	        	{
		    	        		setStyle("-fx-background-color: #DA6147;");
		    	        	}
		    	        	else if((Integer.parseInt(item.getPer().toString())>75) && (Integer.parseInt(item.getPer().toString())<85))
		    	        	{
		    	        		setStyle("-fx-background-color: #F8E243;");
		    	        	}
		    	        	else
		    	        	{
		    	        		setStyle("-fx-background-color: #75C00E;");	
		    	        	}
		    	        } 
		    	        	else
			    	        {
		    	        					setStyle("-fx-background-color: #a1a1a1;");
			    	        }
		    	        	
		    	        	}
		    	        
		    	    }
		    	});
		    
		    	loadSubjects();
		       }
	    
	    public void loadSubjects()
	    {
	    	 try {
		            final CountDownLatch latch1 = new CountDownLatch(1);
		            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Subjects/");


		             ref.addListenerForSingleValueEvent(
		          		new ValueEventListener() {
			              public void onDataChange(DataSnapshot d) {
			            	  
			            	  
			            	  
			                  for(DataSnapshot ds : d.getChildren()) {
			                	  //ArrayList<String> names= new ArrayList<>();
			                      String name = ds.getValue(String.class).trim();
			                      List<String> items = Arrays.asList(name.split(","));
			                      for(int i=0;i<items.size();i++)
			                    	  items.get(i).trim();
			                      subjects.put(ds.getKey().toString().trim(), items);
			 
			                  }
			            		  //ObservableList<String> options = 
			            		    //   	    FXCollections.observableArrayList(d.child(output).getValue().toString());
			            		  System.out.println("----------------"+subjects.toString());
			            		// String ar[] = d.child(output).getValue().toString().split(",");
			            		// drop.getItems().addAll(options);
			            		 // cname.setText(d.child(output).getValue().toString());
			            	  
			                  latch1.countDown();
			            	  
			   				}
			
			  			  public void onCancelled(DatabaseError error) {
			  			      latch1.countDown();
			  			        		  
			  			  }
		  			  });
		  			  latch1.await();
		   			} 
		   			catch (InterruptedException en) {
		  				en.printStackTrace();
		  			}
	    }
	    
	    public void openDetails(ActionEvent e)
	    {
	    	if(!exp==true)
	    	{
	    	exp =true;
	    	tpatt.setExpanded(true);
	    	}
	    	else
	    	{
	    		exp=false;
	    		tpatt.setExpanded(false);
	    	}
	    }
	    
	    public void SaveFirebaseAttendance(ActionEvent e) throws IOException
		{
	    	coursecode=drop.getSelectionModel().getSelectedItem().toString().trim();
			int tc = Integer.parseInt(addTotalClasses.getText().toString());

	   	 ArrayList<String> attend = new ArrayList<>();
	   	 ArrayList<String> percent = new ArrayList<>();
	   	 
	   	ObservableList<HBoxCell> specialcases; 
    	specialcases = special_case_lv.getItems();
    	System.out.println("SC"+specialcases.toString());
    	for (HBoxCell each: specialcases)
    	{
    	  TextField tf = (TextField) each.getChildren().get(1);
    	  Label lv = (Label) each.getChildren().get(0);
    	  if(tf.getText().equals(""))
    	  {
    		  Alert alerts=new Alert(AlertType.WARNING);
		        alerts.setTitle("Warning Dialog");
		        alerts.setHeaderText(null);
		        alerts.setContentText("Kindly enter all the classes conducted for special case students in the left pane");
		        alerts.showAndWait();
		        
		        special_case_cc.clear();
		        special_case_usns.clear();
		        return;
    	  }
    	  else
    	  {
    	  special_case_cc.add(tf.getText());
    	  special_case_usns.add(lv.getText());
    	  }
    	  
    	  System.out.println(special_case_cc);
    	}
	   	 	int labs= 0 ;
			for(Person dsce: data)
			{
				if(!dsce.getClasses().equals(""))
				{
					attend.add(dsce.getClasses().toString());
				
					if(dsce.getClasses().equals("-"))
					{
						percent.add("-");
						labs++;
					}
					else
					{
							
						Boolean flag = new Boolean(false);
						System.out.println("usns"+special_case_usns.toString());
						System.out.println("ccs"+special_case_cc.toString());
					for(int scu=0; scu < special_case_usns.size(); scu++)
					{
						if(dsce.getUsn().equals(special_case_usns.get(scu)))
						{
							int cc = Integer.parseInt(special_case_cc.get(scu));
							
							Double percentage = Double.parseDouble((dsce.getClasses().toString()))/cc;
							percentage= percentage *100;
							
							int perc = (int) Math.round(percentage);
							
							percent.add(perc+"");
							
							flag = true;
							break;
						}
					}
					
					if(!flag)
					{
						
						if(lab_selector.isSelected())
						{
							if(!batch1.getText().equals(""))
							{
								if(labBatch.get(labs).equals("1"))
								{
								Double percentage = Double.parseDouble((dsce.getClasses().toString()))/Integer.parseInt(batch1.getText());
								percentage= percentage *100;
								
								int perc = (int) Math.round(percentage);
								
								percent.add(perc+"");
								}
							}
							if(!batch2.getText().equals(""))
							{
								if(labBatch.get(labs).equals("2"))
								{
								Double percentage = Double.parseDouble((dsce.getClasses().toString()))/Integer.parseInt(batch2.getText());
								percentage= percentage *100;
								
								int perc = (int) Math.round(percentage);
								
								percent.add(perc+"");
								}
							}
							if(!batch3.getText().equals(""))
							{
								if(labBatch.get(labs).equals("3"))
								{
								Double percentage = Double.parseDouble((dsce.getClasses().toString()))/Integer.parseInt(batch3.getText());
								percentage= percentage *100;
								
								int perc = (int) Math.round(percentage);
								
								percent.add(perc+"");
								}
							}
							
						}
						else
						{
						Double percentage = Double.parseDouble((dsce.getClasses().toString()))/tc;
						percentage= percentage * 100;
						
						int perc = (int) Math.round(percentage);
						
						 percent.add(perc+"");
						}
					}
					}
				}
				
				labs++;
			}
			
			
		    try {
		    	
		    	ArrayList<ArrayList<String>> big = new ArrayList<ArrayList<String>>();
		      	
		      	
		      	
		      	
		            final CountDownLatch latch1 = new CountDownLatch(1);
		            DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
		            DatabaseReference ref2;    
		             ref2 = ref1.child("Attendance");

		        	 String tchr_name = coursecode;
		        	
		        	
		        	 String att = String.join(",", attend);
		        	 String perc = String.join(",", percent);
		        	 DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Attendance/"+tfsem+"/"+tfsec+"/"+tchr_name);
		        	 	 DatabaseReference child_name = FirebaseDatabase.getInstance().getReference();
		        	 	
		        	 child_name=ref.child("att");
		        	 child_name.setValueAsync(att);
		        	 child_name=ref.child("perc");
		        	 child_name.setValueAsync(perc);
		        	 child_name=ref.child("sub");
		        	 child_name.setValueAsync(coursecode);
		        	 child_name=ref.child("totalClasses");
		        	 child_name.setValueAsync(tc+"");
		        	 latch1.countDown();
		        	 
		        	System.out.println("Succesfull");
		        	 
		        	latch1.await();
		    			   } 
		    			 catch (InterruptedException ef) {
		    			        ef.printStackTrace();
		    			    }
		    Alert alerts=new Alert(AlertType.INFORMATION);
	        alerts.setTitle("Information Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Saved Online!");
	        alerts.showAndWait();
	        return;
	        
				
		}
		
		public void dropDown(String sem)
		{
			if(subjects.containsKey(sem))
			{
			ObservableList<String> options = 
		       	    FXCollections.observableArrayList(subjects.get(sem));
		       		drop.getItems().clear();
			        drop.getItems().addAll(options);
			}
			else
			{
				drop.getItems().clear();
			}
		}
		
		
		public void LoadFirebaseAttendance(ActionEvent e) throws IOException
		{
			TextInputDialog dialog = new TextInputDialog("Enter here");
			 
			dialog.setTitle("Set Session");
		dialog.setHeaderText("Enter duration ('Date 1 - Date 2'):");
				dialog.setContentText("Duration:");
				 
				Optional<String> result = dialog.showAndWait();
				 
				result.ifPresent(name -> {
					dur = name;
				});
				
				 tfsem = semester.getText().toString();
				 tfsec = section.getText().toString().toUpperCase();

		     ArrayList<DataSnapshot> Userlist = new ArrayList<DataSnapshot>();

		      try {
		              final CountDownLatch latch1 = new CountDownLatch(1);
		              DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
		              DatabaseReference ref2;    
		               ref2 = ref1.child("Attendance/"+tfsem+"/");


		               ref2.addListenerForSingleValueEvent(
		            		   new ValueEventListener() {
		                public void onDataChange(DataSnapshot dataSnapshot) {

		                    //ArrayList<Object> Userlist = new ArrayList<Object>();   
		                    ArrayList<ArrayList<String>> big_arr = new ArrayList<ArrayList<String>>();
		                   	   	for (DataSnapshot dsp : dataSnapshot.getChildren()) {
		                   	   		System.out.println(dsp.getKey());
		                   	      if(dsp.getKey().equalsIgnoreCase(tfsec))  
		                   	    	  Userlist.add(dsp); 
		                         
		                        }
		                    //big_arr.add(Userlist);
		                    
		         				 // System.out.println(Userlist.get(0)+"dsad"+Userlist.size());
		         				     
		                                                latch1.countDown();
		     				     }

		    			        	  public void onCancelled(DatabaseError error) {
		    			        		  latch1.countDown();
		    			        		  
		    			        	  }
		    			        	});
		    			        	 latch1.await();
		     			   } 
		     			 catch (InterruptedException en) {
		    				        en.printStackTrace();
		    				    }
		     			
		      	ArrayList<ArrayList<String>> big = new ArrayList<ArrayList<String>>();
		      	ArrayList<String> smol = new ArrayList<String>();
		      	
		      	for(DataSnapshot d: Userlist.get(0).getChildren())
		      	{
		      		FireData fir = d.getValue(FireData.class);
		      		System.out.println(fir.getAtt());
		      		
		      		smol = new ArrayList<String>();
		      		smol.add(fir.getSub());
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getAtt().split(",")));
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getPerc().split(",")));
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.add(fir.getTotalClasses());
		      		big.add(smol);
		      		
		      		//smol = new ArrayList<String>();
		      		//smol.add(fir.getTotalClasses());
		      		//big.add(smol);
		      		//one sec
		      	}
		    	
		      	System.out.println(big);
		      	combine(big, tfsem, tfsec);
		      	 Alert alerts=new Alert(AlertType.INFORMATION);
			        alerts.setTitle("Information Dialog");
			        alerts.setHeaderText(null);
			        alerts.setContentText("Consolidated document created!\nKindly check designated file directory for docx file.");
			        alerts.showAndWait();
		
		}	
		
		public void saveAttendance(ActionEvent e) throws IOException
		{
		 
			
		    studdat.clear();
		    tfsem = semester.getText().toString();
		    tfsem = tfsem.toUpperCase();
		    tfsec = section.getText().toString();
		    tfsec = tfsec.toUpperCase();
		    studdat.add(tfsem);
		    studdat.add(tfsec);
		    String a=addTotalClasses.getText();
	    	perCol.setVisible(true);
	    	
	    	ObservableList<HBoxCell> specialcases; 
	    	specialcases = special_case_lv.getItems();
	    	System.out.println("SC"+specialcases.toString());
	    	for (HBoxCell each: specialcases)
	    	{
	    	  TextField tf = (TextField) each.getChildren().get(1);
	    	  Label lv = (Label) each.getChildren().get(0);
	    	  if(tf.getText().equals(""))
	    	  {
	    		  Alert alerts=new Alert(AlertType.WARNING);
			        alerts.setTitle("Warning Dialog");
			        alerts.setHeaderText(null);
			        alerts.setContentText("Kindly enter all the classes conducted for special case students in the left pane");
			        alerts.showAndWait();
			        
			        special_case_cc.clear();
			        special_case_usns.clear();
			        return;
	    	  }
	    	  else
	    	  {
	    	  special_case_cc.add(tf.getText());
	    	  special_case_usns.add(lv.getText());
	    	  }
	    	  
	    	  System.out.println(special_case_cc);
	    	}

	    	
	    	int tc = 1;
	    	if(!a.equals(""))
	    		tc=Integer.parseInt(a);
	    	else
	    	{
	    		Alert alerts=new Alert(AlertType.WARNING);
		        alerts.setTitle("Warning Dialog");
		        alerts.setHeaderText(null);
		        alerts.setContentText("Kindly enter all the text fields!");
		        alerts.showAndWait();
		        return;
	    	}
	    	  String sub=drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim();
		        System.out.println(sub);
		        String finalDate="";
				LocalDate date = datePicker1.getValue();
		       DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		       if(date != null)
		       {
		        Date conv_date = java.sql.Date.valueOf(date);
		        finalDate = formatter.format(conv_date);
		        finalDate = finalDate.replace('/', '-');
		       }
		       else
		    	{
		    		Alert alerts=new Alert(AlertType.WARNING);
			        alerts.setTitle("Warning Dialog");
			        alerts.setHeaderText(null);
			        alerts.setContentText("Kindly enter all the text fields!");
			        alerts.showAndWait();
			        return;
		    	}
	 	  InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+".xls");
			HSSFWorkbook  workbook = new HSSFWorkbook(ExcelFileToRead);
	        HSSFSheet spreadsheet = workbook.getSheetAt(0);
	 	   /*Workbook workbook = new HSSFWorkbook();
	        Sheet spreadsheet = workbook.createSheet("sample");

	        Row row = spreadsheet.createRow(0);*/
	      
	      spreadsheet.getRow(0).createCell(0).setCellValue("DAYANANDA SAGAR COLLEGE OF ENGINEERING");
	        spreadsheet.getRow(1).createCell(0).setCellValue("DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING");
	        spreadsheet.getRow(2).createCell(0).setCellValue(studdat.get(0)+studdat.get(1)+" ATTENDANCE: "+finalDate);
	        spreadsheet.getRow(3).createCell(0).setCellValue("SUBJECT: "+sub);
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 5));
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(1, 1, 0, 5));
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(2, 2, 0, 5));
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(3, 3, 0, 5));
	        System.out.println(table.getItems().size());
	        
	        int i=5;
	        int labs = 0;
	        for(Person dsce: data)
			{
				if(!dsce.getClasses().equals("") && !dsce.getUsn().equals("") )
				{
					//System.out.println(spreadsheet.getRow(i).getCell(0).getStringCellValue());
					spreadsheet.getRow(i).createCell(2).setCellValue(dsce.getClasses());
				System.out.println(dsce.getClasses());
				
				if(dsce.getClasses().equals("-"))
				{
					spreadsheet.getRow(i).createCell(3).setCellValue("-");
					labs++;
					i++;
					continue;
				}
				else
				{
					Boolean flag = new Boolean(false);
					System.out.println("usns"+special_case_usns.toString());
					System.out.println("ccs"+special_case_cc.toString());
				for(int scu=0; scu < special_case_usns.size(); scu++)
				{
					if(dsce.getUsn().equals(special_case_usns.get(scu)))
					{
						int cc = Integer.parseInt(special_case_cc.get(scu));
						
						Double percentage = Double.parseDouble((dsce.getClasses().toString()))/cc;
						percentage= percentage *100;
						
						int perc = (int) Math.round(percentage);
						
						spreadsheet.getRow(i).createCell(3).setCellValue(perc+"");
						
						flag = true;
						break;
					}
				}
				
				if(!flag)
				{
					if(lab_selector.isSelected())
					{
						if(!batch1.getText().equals(""))
						{
							if(labBatch.get(labs).equals("1"))
							{
							Double percentage = Double.parseDouble((dsce.getClasses().toString()))/Integer.parseInt(batch1.getText());
							percentage= percentage *100;
							
							int perc = (int) Math.round(percentage);
							
							spreadsheet.getRow(i).createCell(3).setCellValue(perc+"");
							}
						}
						if(!batch2.getText().equals(""))
						{
							if(labBatch.get(labs).equals("2"))
							{
							Double percentage = Double.parseDouble((dsce.getClasses().toString()))/Integer.parseInt(batch2.getText());
							percentage= percentage *100;
							
							int perc = (int) Math.round(percentage);
							
							spreadsheet.getRow(i).createCell(3).setCellValue(perc+"");
							}
						}
						if(!batch3.getText().equals(""))
						{
							if(labBatch.get(labs).equals("3"))
							{
							Double percentage = Double.parseDouble((dsce.getClasses().toString()))/Integer.parseInt(batch3.getText());
							percentage= percentage *100;
							
							int perc = (int) Math.round(percentage);
							
							spreadsheet.getRow(i).createCell(3).setCellValue(perc+"");
							}
						}
					}
					else
					{
					Double percentage = Double.parseDouble((dsce.getClasses().toString()))/tc;
					percentage= percentage *100;
					
					int perc = (int) Math.round(percentage);
					
					spreadsheet.getRow(i).createCell(3).setCellValue(perc+"");
					}
				}
				
				}
				
				}
				i++;
				labs++;
			}
	        
	        
	        
	      
	        
	        String directoryName=rootpath+tfsem+tfsec+"-"+sub;
	        File directory = new File(directoryName);
	        System.out.println(directoryName);
	        if (! directory.exists()){
	            directory.mkdir();
	        }
	        String fileName="\\"+finalDate+".xls";
			
	  
	        FileOutputStream fileOut = new FileOutputStream(directoryName+fileName);
	        workbook.write(fileOut);
	        fileOut.close();
	        InputStream ExcelFileToRead1 = new FileInputStream(directoryName+fileName);
	       // InputStream ExcelFileToRead1 = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+"-"+sub+"\\"+finalDate+".xls");
			HSSFWorkbook  wb = new HSSFWorkbook(ExcelFileToRead1);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			
			HSSFRow row; 
			HSSFCell cell;

			Iterator rows = sheet.rowIterator();
			
			data.clear();
			int k = 5;
			System.out.println(sheet.getPhysicalNumberOfRows());
			while(k<sheet.getPhysicalNumberOfRows())
			{
				data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
						sheet.getRow(k).getCell(1).getStringCellValue(),
						sheet.getRow(k).getCell(2).getStringCellValue(),
						sheet.getRow(k).getCell(3).getStringCellValue()));
				//System.out.println(sheet.getRow(k).getCell(2).getStringCellValue());
				k++;
			}
			 table.setItems(data);
			/* int i1=0;
			 for (Node n: table.lookupAll("TableRow")) {
			      if (n instanceof TableRow) {
			        TableRow row1 = (TableRow) n;
			        if (Integer.parseInt(table.getItems().get(i1).getPer().toString().trim())<75) {
			          row1.getStyleClass().add("red");
			          //row1.setDisable(false);
			        } else if(Integer.parseInt(table.getItems().get(i1).getPer().toString().trim())>=75 && Integer.parseInt(table.getItems().get(i1).getPer().toString().trim())<85){
			          row1.getStyleClass().add("yellow");
			          //row1.setDisable(true);
			        }
			        else if(Integer.parseInt(table.getItems().get(i1).getPer().toString().trim())>=85)
			        {
			        row1.getStyleClass().add("green");
			        }
			        i1++;
			        if (i1 == table.getItems().size())
			          break;
			      }
			    }*/
			 
			 
			
	        Alert alert=new Alert(AlertType.INFORMATION);
	        alert.setTitle("Information Dialog");
	        alert.setHeaderText(null);
	        alert.setContentText("Spreadsheet Saved!");
	        alert.showAndWait();
	        }

	protected void setText(Object object) {
			// TODO Auto-generated method stub
			
		}

	protected void setStyle(String string) {
			// TODO Auto-generated method stub
			
		}

	public void loadAttendance(ActionEvent e)throws IOException
	{
		labBatch.clear();
		special_case_lv.getItems().clear();
		semester.setVisible(true);
    	batch.setVisible(true);
    	section.setVisible(true);
    	perCol.setVisible(false);
    	list.setVisible(true);
    	list.setEditable(true);
    	usnCol1.setVisible(true);
    	nameCol1.setVisible(true);
    	classesCol.setVisible(true);
    	
    	
	    studdat.clear();
	    tfsem = semester.getText().toString();
	    tfsem = tfsem.toUpperCase();
	    tfsec = section.getText().toString();
	    tfsec = tfsec.toUpperCase();
	    studdat.add(tfsem);
	    studdat.add(tfsec);
	   // String sub=drop.getSelectionModel().getSelectedItem().toString().trim();
	    LocalDate date=datePicker1.getValue();
	    String a=addTotalClasses.getText();
	    int tc=1;
	    if(!a.equals(""))
    		tc=Integer.parseInt(a);
    	else
    	{
    		Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly enter all the fields!");
	        alerts.showAndWait();
	        return;
    	}
	    if(tfsem.equals("")||tfsec.equals("")||date==null)
		{
			Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly enter all the fields!");
	        alerts.showAndWait();
	        return;
	        }
		table.getColumns().clear();
		table.getColumns().addAll(usnCol1, nameCol1, classesCol, perCol);
		table.setItems(data);
		
		String sub=drop.getSelectionModel().getSelectedItem().toString().trim();
		if(drop == null)
		{
			Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly select a subject!");
	        alerts.showAndWait();
	        return;
		}
		
	    
		
			data.clear();
			table.setItems(data);
		
			String[] sheetrows ;
			
			InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+".xls");
			HSSFWorkbook  wb = new HSSFWorkbook(ExcelFileToRead);
			HSSFSheet sheet = wb.getSheetAt(0);
			 
			String finalDate="";
	        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	        if(date != null)
	        {
	         Date conv_date = java.sql.Date.valueOf(date);
	         finalDate = formatter.format(conv_date);
	         finalDate = finalDate.replace('/', '-');
	        }
			if (Files.exists(Paths.get("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+"-"+drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim()))) {
				if (Files.exists(Paths.get("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+"-"+drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim()+"\\"+finalDate.toString()+".xls"))) {
					ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+"-"+drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim()+"\\"+finalDate.toString()+".xls");
					wb = new HSSFWorkbook(ExcelFileToRead);
					sheet = wb.getSheetAt(0);
			    }
			}
			
			
			HSSFRow row; 
			HSSFCell cell;

			Iterator rows = sheet.rowIterator();
			
			sheetrows = new String[4];
			
			int k =5;
			System.out.println(sheet.getPhysicalNumberOfRows());
			while(k<sheet.getPhysicalNumberOfRows())
			{
				data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
						sheet.getRow(k).getCell(1).getStringCellValue(),
						sheet.getRow(k).getCell(2).getStringCellValue(),
						sheet.getRow(k).getCell(3).getStringCellValue()));
				//System.out.println(sheet.getRow(k).getCell(2).getStringCellValue());
				k++;
			}
			table.setItems(data);
			
			InputStream ExcelFileToRead1 = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+".xls");
			HSSFWorkbook  wb1 = new HSSFWorkbook(ExcelFileToRead1);
			HSSFSheet sheet1 = wb1.getSheetAt(0);
			
			
			HSSFRow row1; 
			HSSFCell cell1;

			Iterator rows1 = sheet1.rowIterator();
			
			sheetrows = new String[4];
			
			List<HBoxCell> list = new ArrayList<>();
	          
			
			int k1 =5;
			System.out.println(sheet1.getPhysicalNumberOfRows());
			while(k1<sheet1.getPhysicalNumberOfRows())
			{
				/*data.add(new Person(sheet1.getRow(k1).getCell(0).getStringCellValue(),
						sheet1.getRow(k1).getCell(1).getStringCellValue(),
						sheet1.getRow(k1).getCell(2).getStringCellValue(),
						sheet1.getRow(k1).getCell(3).getStringCellValue()));
				*/
				if(sheet1.getRow(k1).getCell(4).getStringCellValue().equals("true"))
				list.add(new HBoxCell(sheet1.getRow(k1).getCell(0).getStringCellValue(), "" ));
				//System.out.println(sheet.getRow(k1).getCell(2).getStringCellValue());
				try {
				labBatch.add(sheet1.getRow(k1).getCell(5).getStringCellValue());
				} catch(NullPointerException nullex) {
					nullex.printStackTrace();
				}
				
				k1++;
			}
			//table.setItems(data);
			 
			wb1.close();
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
			special_case_lv.setItems(myObservableList);
	}
	
	public void importAttendanceFile(ActionEvent e) throws IOException
	{
		special_case_lv.getItems().clear();
		System.out.println("ASDFGHJ");
		semester.setVisible(true);
    	batch.setVisible(true);
    	section.setVisible(true);
    	perCol.setVisible(false);
    	list.setVisible(true);
    	list.setEditable(true);
    	usnCol1.setVisible(true);
    	nameCol1.setVisible(true);
    	classesCol.setVisible(true);
    	
    	
	    studdat.clear();
	    tfsem = semester.getText().toString();
	    tfsem = tfsem.toUpperCase();
	    tfsec = section.getText().toString();
	    tfsec = tfsec.toUpperCase();
	    studdat.add(tfsem);
	    studdat.add(tfsec);
	    String a=addTotalClasses.getText();
	    int tc=1;
	    if(!a.equals(""))
    		tc=Integer.parseInt(a);
    	else
    	{
    		Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly enter all the fields!");
	        alerts.showAndWait();
	        return;
    	}
	    
	    LocalDate date=datePicker1.getValue();
	    if(tfsem.equals("")||tfsec.equals("")||date==null)
		{
			Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly enter all the fields!");
	        alerts.showAndWait();
	        return;
	        }
		table.getColumns().clear();
		table.getColumns().addAll(usnCol1, nameCol1, classesCol, perCol);
		table.setItems(data);
		
		String sub=drop.getSelectionModel().getSelectedItem().toString().trim();
		if(drop==null)
		{
			Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly select a subject!");
	        alerts.showAndWait();
	        return;
		}
		data.clear();
		table.setItems(data);
		
		String[] sheetrows ;
		
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
		
		String str = file.getAbsolutePath().toString();
		
		System.out.println("asjdakdasujfba");
		
		InputStream ExcelFileToRead = new FileInputStream(str);
		
			//InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+".xls");
		HSSFWorkbook  wb = new HSSFWorkbook(ExcelFileToRead);
		HSSFSheet sheet = wb.getSheetAt(0);
		
		
		HSSFRow row; 
		HSSFCell cell;

		Iterator rows = sheet.rowIterator();
		
		sheetrows = new String[4];
		
		int k =2;
		System.out.println(sheet.getPhysicalNumberOfRows());
			while(k<sheet.getPhysicalNumberOfRows())
			{
				//System.out.println("-----huhuhu");
				if(sheet.getRow(k).getCell(0).getStringCellValue().equals(""))
				{
					k++;
					continue;
				}
				
				//System.out.println("ssss"+sheet.getRow(k).getCell(2).getStringCellValue()+"mmm");
				
				try
				{
				if(sheet.getRow(k).getCell(2).getStringCellValue().equals(""))
				{
					data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
							sheet.getRow(k).getCell(1).getStringCellValue(),
							"-",
							""));
				}
				}
				catch(Exception ee)
				{
					try
					{
					data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
							sheet.getRow(k).getCell(1).getStringCellValue(),
							String.valueOf((int)sheet.getRow(k).getCell(2).getNumericCellValue()),
							""));
					}
					catch(NullPointerException ne)
					{
						data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
								sheet.getRow(k).getCell(1).getStringCellValue(),
								"-",
								""));
					}
				}
				//int perc = (int)sheet.getRow(k).getCell(2).getNumericCellValue()/Integer.parseInt(addTotalClasses.getText().toString()) * 100;
				
				//System.out.println(sheet.getRow(k).getCell(2).getStringCellValue());
				k++;
			}
			table.setItems(data);
			
			 
			InputStream ExcelFileToRead1 = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+".xls");
			HSSFWorkbook  wb1 = new HSSFWorkbook(ExcelFileToRead1);
			HSSFSheet sheet1 = wb1.getSheetAt(0);
			
			
			HSSFRow row1; 
			HSSFCell cell1;

			Iterator rows1 = sheet1.rowIterator();
			
			sheetrows = new String[4];
			
			List<HBoxCell> list = new ArrayList<>();
	          
			
			int k1 =5;
			System.out.println(sheet1.getPhysicalNumberOfRows());
			while(k1<sheet1.getPhysicalNumberOfRows())
			{
				/*data.add(new Person(sheet1.getRow(k1).getCell(0).getStringCellValue(),
						sheet1.getRow(k1).getCell(1).getStringCellValue(),
						sheet1.getRow(k1).getCell(2).getStringCellValue(),
						sheet1.getRow(k1).getCell(3).getStringCellValue()));
				*/
				if(sheet1.getRow(k1).getCell(4).getStringCellValue().equals("true"))
				list.add(new HBoxCell(sheet1.getRow(k1).getCell(0).getStringCellValue(), "" ));
				//System.out.println(sheet.getRow(k1).getCell(2).getStringCellValue());
				k1++;
			}
			//table.setItems(data);
			 
			wb1.close();
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
			special_case_lv.setItems(myObservableList);
		
	}

	public void showAlert(String content) {
		Alert alerts=new Alert(AlertType.INFORMATION);
		alerts.setTitle("Information Dialog");
		alerts.setHeaderText(null);
		alerts.setContentText(content);
		alerts.showAndWait();
	}

	public void loadAttendanceFromDB(ActionEvent e) throws IOException {
		labBatch.clear();
		special_case_lv.getItems().clear();
		semester.setVisible(true);
		batch.setVisible(true);
		section.setVisible(true);
		perCol.setVisible(false);
		list.setVisible(true);
		list.setEditable(true);
		usnCol1.setVisible(true);
		nameCol1.setVisible(true);
		classesCol.setVisible(true);

		table.getColumns().clear();
		table.getColumns().addAll(usnCol1, nameCol1, classesCol, perCol);
		table.setItems(data);
		data.clear();
		table.setItems(data);

		tfsem = semester.getText().toString();
		tfsec = section.getText().toString().toUpperCase();
		String sub = drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim();

		if(tfsem.trim().equals("") || !tfsem.matches("[0-9]")) {
			showAlert("Please check the semester");
		}

		if(tfsec.trim().equals("") || !tfsec.matches("[A-Za-z]")) {
			showAlert("Please check the section");
		}

		Map <String, String> attendanceMap = new HashMap<String, String>();

		try {
			final CountDownLatch latch1 = new CountDownLatch(1);
			DatabaseReference classAttendanceReference =
					FirebaseDatabase.getInstance().getReference().child("Attendance/"+tfsem+"/"+tfsec+"/"+sub);

			classAttendanceReference.addListenerForSingleValueEvent(
					new ValueEventListener() {
						public void onDataChange(DataSnapshot dataSnapshot) {
							for (DataSnapshot dsp : dataSnapshot.getChildren()) {
								attendanceMap.put(dsp.getKey(), dsp.getValue(String.class));
							}
							latch1.countDown();
						}
						public void onCancelled(DatabaseError error) {
							latch1.countDown();
						}
					});
			latch1.await();
		}
		catch (InterruptedException en) {
			en.printStackTrace();
		}

		logger.info(attendanceMap.get("att").toString());
		if(!attendanceMap.containsKey("att")) {
			return;
		}

		InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+tfsem+tfsec+".xls");
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;

		ArrayList<String> attendance = new ArrayList<String>(Arrays.asList(attendanceMap.get("att").split(",")));
		ArrayList<String> percentage = new ArrayList<String>(Arrays.asList(attendanceMap.get("perc").split(",")));
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> usns = new ArrayList<String>();

		List<HBoxCell> list = new ArrayList<>();
		for(int i=5;i<sheet.getPhysicalNumberOfRows();i++) {
			names.add(sheet.getRow(i).getCell(1).toString());
			usns.add(sheet.getRow(i).getCell(0).toString());
			if(sheet.getRow(i).getCell(4).getStringCellValue().equals("true"))
				list.add(new HBoxCell(sheet.getRow(i).getCell(0).getStringCellValue(), "" ));
			try {
				labBatch.add(sheet.getRow(i).getCell(5).getStringCellValue());
			} catch(NullPointerException nullex) {
				nullex.printStackTrace();
			}
		}
		wb.close();
		ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		special_case_lv.setItems(myObservableList);

		logger.info(names.toString());
		logger.info(attendance.toString());

		for(int i=0; i<usns.size();i++) {
			if(i<attendance.size()){
				data.add(new Person(
						usns.get(i),
						names.get(i),
						attendance.get(i),
						percentage.get(i)
				));
			} else {
				data.add(new Person(
						usns.get(i),
						names.get(i),
						"",
						""
				));
			}
		}
		logger.info(data.get(0).getClasses().toString());
		table.setItems(data);
	}

	public void combine( ArrayList<ArrayList<String>> big, String sem, String sec) throws IOException
	{
		 String directoryName=rootpath+"Consolidated";
	     File directory = new File(directoryName);
	    // System.out.println(directoryName);
	     if (! directory.exists()){
	         directory.mkdir();
	        }
	     String directoryName1=rootpath+"Consolidated\\"+sem+sec;
	     File directory1 = new File(directoryName1);
	    // System.out.println(directoryName);
	     if (! directory1.exists()){
	         directory1.mkdir();
	        }
	     
		 int n;
		 XWPFDocument docX2 = new XWPFDocument();
		 
		 
		 CTBody body = docX2.getDocument().getBody();
		 if(!body.isSetSectPr()){
			 body.addNewSectPr();
			 }
			  
			 CTSectPr section = body.getSectPr();
			 if(!section.isSetPgSz()){
			 section.addNewPgSz();
			 }
			  
			 CTPageSz pageSize = section.getPgSz();
			 pageSize.setOrient(STPageOrientation.LANDSCAPE);
			 //A4 = 595x842 / multiply 20 since BigInteger represents 1/20 Point
			 pageSize.setW(BigInteger.valueOf(16840));
			 pageSize.setH(BigInteger.valueOf(11900));
	        
	      XWPFParagraph paragraph = docX2.createParagraph();
	      paragraph.setAlignment(ParagraphAlignment.CENTER);	      
	      XWPFRun paragraphOneRunOne = paragraph.createRun();
	      paragraphOneRunOne.setBold(true);
	      paragraphOneRunOne.setText("DAYANANDA SAGAR COLLEGE OF ENGINEERING");
	      paragraphOneRunOne.addBreak();
	      
	     
	      XWPFRun paragraphOneRunTwo = paragraph.createRun();
	      paragraphOneRunTwo.setBold(true);
	      paragraphOneRunTwo.setText("DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING");
	      paragraphOneRunTwo.addBreak();
	      paragraphOneRunTwo.addBreak();
	      
	      
	      XWPFRun paragraphOneRunThree = paragraph.createRun();
	      paragraphOneRunThree.setBold(true);
	      paragraphOneRunThree.setText("ATTENDANCE DISPLAY");
	      paragraphOneRunThree.addBreak();
	      
	      XWPFRun paragraphOneRunFour = paragraph.createRun();
	      paragraphOneRunFour.setBold(true);
	      paragraphOneRunFour.setText("(Session: "+dur+")");
	      paragraphOneRunFour.addBreak();
	      paragraphOneRunFour.addBreak();
	      paragraphOneRunFour.addBreak();
	      
	      XWPFParagraph paragraph1 = docX2.createParagraph();
	      paragraph1.setAlignment(ParagraphAlignment.LEFT);
	      String finalDate="";
			LocalDate date = datePicker1.getValue();
	       DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	        Date conv_date = java.sql.Date.valueOf(date);
	        finalDate = formatter.format(conv_date);
	        finalDate = finalDate.replace('/', '-');
	     
	      
	      XWPFRun paragraphTwoRunOne = paragraph1.createRun();
	      paragraphTwoRunOne.setBold(true);
	      paragraphTwoRunOne.setText("Class: "+sem+sec+"                                                                                              Cumulative Attendance Record: "+finalDate);
	      
	     
	      
	      
	      //create table
	      XWPFTable table = docX2.createTable();
	      
	      
	      
	      
	      XWPFTableRow tableRowOne = table.createRow();
	      table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(1000));
	      
	      XWPFTableCell cell2=tableRowOne.getCell(0);
	  	cell2.setText("Sl#");
	  	CTTcPr tcpr = cell2.getCTTc().addNewTcPr();
	  	CTVMerge vMerge=tcpr.addNewVMerge();
	  	vMerge.setVal(STMerge.RESTART); 
	      
	  	XWPFTableCell cell3=tableRowOne.createCell();
	  	cell3.setText("USN");
	  	CTTcPr tcpr1 = cell3.getCTTc().addNewTcPr();
	  	CTVMerge vMerge1=tcpr1.addNewVMerge();
	  	vMerge1.setVal(STMerge.RESTART); 
	      
	  	
	  	XWPFTableCell c2 = tableRowOne.createCell();
	  	XWPFRun run = c2.addParagraph().createRun();
	  	run.setBold(true);run.setText("Subject ->");run.setFontSize(12);
	  	c2.removeParagraph(0);
	  	
	  	try {
            final CountDownLatch latch1 = new CountDownLatch(1);
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Subjects/");


             ref.addListenerForSingleValueEvent(
          		new ValueEventListener() {
	              public void onDataChange(DataSnapshot d) {
	            	  if(d.hasChild(sem))
	            	  {
	            		  System.out.println(d.child(sem).getValue().toString());
	            		  subs = d.child(sem).getValue().toString().split(",");
	            		  for(int x=0;x<subs.length;x++)
	            			  subs[x]=subs[x].trim();
	            		  
	            	  }
	            	  else
	            	  {
	            		  Alert alerts=new Alert(AlertType.WARNING);
	  			        alerts.setTitle("Warning Dialog");
	  			        alerts.setHeaderText(null);
	  			        alerts.setContentText("Kindly enter the subjects for this semester in Student Setup!");
	  			        alerts.showAndWait();
	  			        
	            	  }
	                  latch1.countDown();
	   				}
	
	  			  public void onCancelled(DatabaseError error) {
	  			      latch1.countDown();
	  			        		  
	  			  }
  			  });
  			  latch1.await();
   			} 
   			catch (InterruptedException en) {
  				en.printStackTrace();
  			}
	  	
	  	XWPFRun run1;
	  	for(int x=0; x<subs.length; x++)
	  	{
	  		XWPFTableCell cell4=tableRowOne.createCell();
		  	run1 = cell4.addParagraph().createRun();
		  	run1.setBold(true);run1.setText(subs[x].trim());run1.setFontSize(12);
		  	cell4.removeParagraph(0);
		  	CTTcPr tcpr2 = cell4.getCTTc().addNewTcPr();
		  	CTHMerge vMerge2=tcpr2.addNewHMerge();
		  	vMerge2.setVal(STMerge.RESTART); 
		  	
		  	XWPFTableCell cell5=tableRowOne.createCell();
		  	CTTcPr tcpr3 = cell5.getCTTc().addNewTcPr();
		  	CTHMerge vMerge3=tcpr3.addNewHMerge();
		  	vMerge3.setVal(STMerge.CONTINUE);
	  	}
	  	
	  	/*XWPFTableCell cell4=tableRowOne.createCell();
	  	XWPFRun run1 = cell4.addParagraph().createRun();
	  	run1.setBold(true);run1.setText(subs[0]);run1.setFontSize(12);
	  	cell4.removeParagraph(0);
	  	CTTcPr tcpr2 = cell4.getCTTc().addNewTcPr();
	  	CTHMerge vMerge2=tcpr2.addNewHMerge();
	  	vMerge2.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell5=tableRowOne.createCell();
	  	CTTcPr tcpr3 = cell5.getCTTc().addNewTcPr();
	  	CTHMerge vMerge3=tcpr3.addNewHMerge();
	  	vMerge3.setVal(STMerge.CONTINUE); 
	  	
	  	
	  	XWPFTableCell cell6=tableRowOne.createCell();
	  	XWPFRun run2 = cell6.addParagraph().createRun();
	  	run2.setBold(true);run2.setText(subs[1]);run2.setFontSize(12);
	  	cell6.removeParagraph(0);
	  	CTTcPr tcpr4 = cell6.getCTTc().addNewTcPr();
	  	CTHMerge vMerge4=tcpr4.addNewHMerge();
	  	vMerge4.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell7=tableRowOne.createCell();
	  	CTTcPr tcpr5 = cell7.getCTTc().addNewTcPr();
	  	CTHMerge vMerge5=tcpr5.addNewHMerge();
	  	vMerge5.setVal(STMerge.CONTINUE);
	  	
	  	XWPFTableCell cell8=tableRowOne.createCell();
	  	XWPFRun run3 = cell8.addParagraph().createRun();
	  	run3.setBold(true);run3.setText(subs[2]);run3.setFontSize(12);
	  	cell8.removeParagraph(0);
	  	CTTcPr tcpr6 = cell8.getCTTc().addNewTcPr();
	  	CTHMerge vMerge6=tcpr6.addNewHMerge();
	  	vMerge6.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell9=tableRowOne.createCell();
	  	CTTcPr tcpr7 = cell9.getCTTc().addNewTcPr();
	  	CTHMerge vMerge7=tcpr7.addNewHMerge();
	  	vMerge7.setVal(STMerge.CONTINUE);
	  	
	  	XWPFTableCell cell10=tableRowOne.createCell();
	  	XWPFRun run4 = cell10.addParagraph().createRun();
	  	run4.setBold(true);run4.setText(subs[3]);run4.setFontSize(12);
	  	cell10.removeParagraph(0);
	  	CTTcPr tcpr8 = cell10.getCTTc().addNewTcPr();
	  	CTHMerge vMerge8=tcpr8.addNewHMerge();
	  	vMerge8.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell11=tableRowOne.createCell();
	  	CTTcPr tcpr9 = cell11.getCTTc().addNewTcPr();
	  	CTHMerge vMerge9=tcpr9.addNewHMerge();
	  	vMerge9.setVal(STMerge.CONTINUE);
	  	
	  	XWPFTableCell cell12=tableRowOne.createCell();
	  	XWPFRun run5 = cell12.addParagraph().createRun();
	  	run5.setBold(true);run5.setText(subs[4]);run5.setFontSize(12);
	  	cell12.removeParagraph(0);
	  	CTTcPr tcpr10 = cell12.getCTTc().addNewTcPr();
	  	CTHMerge vMerge10=tcpr10.addNewHMerge();
	  	vMerge10.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell13=tableRowOne.createCell();
	  	CTTcPr tcpr11 = cell13.getCTTc().addNewTcPr();
	  	CTHMerge vMerge11=tcpr11.addNewHMerge();
	  	vMerge11.setVal(STMerge.CONTINUE);
	  	
	  	XWPFTableCell cell14=tableRowOne.createCell();
	  	XWPFRun run6 = cell14.addParagraph().createRun();
	  	run6.setBold(true);run6.setText(subs[5]);run6.setFontSize(12);
	  	cell14.removeParagraph(0);
	  	CTTcPr tcpr12 = cell14.getCTTc().addNewTcPr();
	  	CTHMerge vMerge12=tcpr12.addNewHMerge();
	  	vMerge12.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell15=tableRowOne.createCell();
	  	CTTcPr tcpr13 = cell15.getCTTc().addNewTcPr();
	  	CTHMerge vMerge13=tcpr13.addNewHMerge();
	  	vMerge13.setVal(STMerge.CONTINUE);
	  	
	  	XWPFTableCell cell16=tableRowOne.createCell();
	  	XWPFRun run7 = cell16.addParagraph().createRun();
	  	run7.setBold(true);run7.setText(subs[6]);run7.setFontSize(12);
	  	cell16.removeParagraph(0);
	  	CTTcPr tcpr14 = cell16.getCTTc().addNewTcPr();
	  	CTHMerge vMerge14=tcpr14.addNewHMerge();
	  	vMerge14.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell17=tableRowOne.createCell();
	  	CTTcPr tcpr15 = cell17.getCTTc().addNewTcPr();
	  	CTHMerge vMerge15=tcpr15.addNewHMerge();
	  	vMerge15.setVal(STMerge.CONTINUE);
	  	
	  	XWPFTableCell cell18=tableRowOne.createCell();
	  	XWPFRun run8 = cell18.addParagraph().createRun();
	  	run8.setBold(true);run8.setText(subs[7]);run8.setFontSize(12);
	  	cell18.removeParagraph(0);
	  	CTTcPr tcpr16 = cell18.getCTTc().addNewTcPr();
	  	CTHMerge vMerge16=tcpr16.addNewHMerge();
	  	vMerge16.setVal(STMerge.RESTART); 
	  	
	  	XWPFTableCell cell19=tableRowOne.createCell();
	  	CTTcPr tcpr17 = cell19.getCTTc().addNewTcPr();
	  	CTHMerge vMerge17=tcpr17.addNewHMerge();
	  	vMerge17.setVal(STMerge.CONTINUE);*/
	  	
	  	
	  	XWPFTableRow tableRowOne1 = table.createRow();
	  	int twipsPerInch =  1440;
	  	tableRowOne1.setHeight((int)(twipsPerInch*2/10)); //set height 1/10 inch.
	  	tableRowOne1.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"

	  	
	  	
	  	XWPFTableCell cell20=tableRowOne1.getCell(0);
	  	CTTcPr tcpr18 = cell20.getCTTc().addNewTcPr();
	  	CTVMerge vMerge18=tcpr18.addNewVMerge();
	  	vMerge18.setVal(STMerge.CONTINUE); 
	  	
	  	XWPFTableCell cell21=tableRowOne1.createCell();
	  	CTTcPr tcpr19 = cell21.getCTTc().addNewTcPr();
	  	CTVMerge vMerge19=tcpr19.addNewVMerge();
	  	vMerge19.setVal(STMerge.CONTINUE); 
	  	
	  	
	  	XWPFTableCell cel = tableRowOne1.createCell();
	  	cel.setText("Classes Conducted ->");
	  	XWPFRun run9 = cel.addParagraph().createRun();
	  	run9.setBold(true);run9.setText("Classes Conducted ->");run9.setFontSize(10);
	  	cel.removeParagraph(0);
	  	int y=0;
        int totalClasses = 3;
        int subjectCounter = 0;
        int v = 0;
	  	for(int i = 0;i<subs.length;i++)
	  	{
	  		XWPFTableCell cell22=tableRowOne1.createCell();
	  			try {
	  			System.out.println(table.getRow(1).getCell(v+3).getText().toString()+"THis one");
	  	        subjectCounter = 0;
	  			while(subjectCounter<big.size()) {
	  				if((big.get(subjectCounter).get(0).trim().equalsIgnoreCase(table.getRow(1).getCell(v+3).getText().toString().trim())))	
	  				{
	  					System.out.println(big.get(subjectCounter+3)+"this sub");
	  					cell22.setText(big.get(subjectCounter+3).get(0));
  					}
	  				subjectCounter+=4;
	  			}
	  			} catch(Exception e) {
	  				e.printStackTrace();
	  			}
		  	v+=2;
		  	CTTcPr tcpr20 = cell22.getCTTc().addNewTcPr();
		  	CTHMerge vMerge20=tcpr20.addNewHMerge();
		  	vMerge20.setVal(STMerge.RESTART); 
		  	
		  	XWPFTableCell cell23=tableRowOne1.createCell();
		  	CTTcPr tcpr21 = cell23.getCTTc().addNewTcPr();
		  	CTHMerge vMerge21=tcpr21.addNewHMerge();
		  	vMerge21.setVal(STMerge.CONTINUE);
	  	}
	  		
	  	
	  	XWPFTableRow tableRowOne2 = table.createRow();
	  	tableRowOne2.setHeight((int)(twipsPerInch*2/10)); //set height 1/10 inch.
	  	tableRowOne2.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
	  	
	  	
	  	XWPFTableCell cell24=tableRowOne2.getCell(0);
	  	CTTcPr tcpr21 = cell24.getCTTc().addNewTcPr();
	  	CTVMerge vMerge21=tcpr21.addNewVMerge();
	  	vMerge21.setVal(STMerge.CONTINUE); 
	  	
	  	XWPFTableCell cell25=tableRowOne2.createCell();
	  	CTTcPr tcpr22 = cell25.getCTTc().addNewTcPr();
	  	CTVMerge vMerge22=tcpr22.addNewVMerge();
	  	vMerge22.setVal(STMerge.CONTINUE); 
	  	
	  	XWPFTableCell c3 = tableRowOne2.createCell();
	  	run1 = c3.addParagraph().createRun();
	  	run1.setBold(true);run1.setText("Name");run1.setFontSize(12);
	  	c3.removeParagraph(0);
	  	
	  	for(int i=0;i<subs.length;i++) {
	      c3 = tableRowOne2.addNewTableCell();
	      run1 = c3.addParagraph().createRun();
		  	run1.setBold(true);run1.setText("A");run1.setFontSize(12);
		  	c3.removeParagraph(0);
		  	c3 = tableRowOne2.addNewTableCell();
		      run1 = c3.addParagraph().createRun();
			  	run1.setBold(true);run1.setText("%");run1.setFontSize(12);
			  	c3.removeParagraph(0);
	  	}
	     
	  	int[] cols = new int[3+(subs.length*2)];
	    cols[0] = 8000;
	    cols[1] = 20000;
	    cols[2] = 20000;
	    for(int x=0; x<(subs.length*2); x++)
	    	cols[3+x] = 8000;
		     
	      for(int i = 0; i < table.getNumberOfRows(); i++){ 
	            XWPFTableRow row = table.getRow(i); 
	            int numCells = row.getTableCells().size(); 
	            for(int j = 0; j < numCells; j++){ 
	                XWPFTableCell cell = row.getCell(j); 
	                cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(cols[j])); 
	            } 
	        } 
	       
	     
	                                                                                                                                    // "+studdat.get(0)+studdat.get(1)+"-"+sub+"\\"+finalDate+".xls");        
	      InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+sem+sec+".xls");
	  	HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
	  	HSSFSheet sheet = wb.getSheetAt(0);
	  	HSSFRow row; 
	  	
	  	ArrayList<String> names = new ArrayList<String>();
	  	ArrayList<String> usns = new ArrayList<String>();
	  	
	  	for(int i=5;i<sheet.getPhysicalNumberOfRows();i++)
	  	{
	  		
	  	
	  	String usn = sheet.getRow(i).getCell(0).toString();
	  	String name = sheet.getRow(i).getCell(1).toString();
	    
	  	names.add(name);
	  	usns.add(usn);
	      
	  	}

	  	System.out.println(big);
	  	int ar[] = new int [subs.length];
	  	for(int u=0;u<subs.length;u++)
	  		ar[u] = 0;
	  	
	  	/*
	  	int cellno =0;
	  	int l=0, ctr=0;
	  	while(l<big.size())
	  	{
	  		
	  		l = (ctr*4)+3;  
	  		if(l<big.size())
	  			table.getRow(2).getCell(cellno+3).setText(big.get(l).get(0));
	  		ctr = ctr+1;
	  		//System.out.println(big.get((l*4)+3));
	  		cellno = cellno+2;
	  	}*/
	  	
	  	
	  	for(int i=0;i<names.size();i++)
	  	{
	  		XWPFTableRow r1 =  table.createRow();
	  		r1.getCell(0).setText(String.valueOf(i+1));
	  		r1.setHeight((int)(twipsPerInch*2/10)); //set height 1/10 inch.
	  		r1.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
	  		table.getRow(i+4).createCell().setText(usns.get(i));
	  		table.getRow(i+4).createCell().setText(names.get(i));
	  		
	  		int k = 0;
	  		
	  		while(k < (subs.length*2))
	  		{
	  			table.getRow(i+4).createCell().setText("");
	  			table.getRow(i+4).createCell().setText("");
	  			//for(int m = 0; m<big.size();  m++)
	  		//	System.out.println(big.get(m));
	  			
	  			for(int x=0; x < big.size(); x=x+4)
		  		{	System.out.println(big.get(x).get(0).trim()+"  "+table.getRow(1).getCell(k+3).getText().toString().trim() );
	  			
			  		if((big.get(x).get(0).trim().equalsIgnoreCase(table.getRow(1).getCell(k+3).getText().toString().trim())))
			  		{
			  			System.out.println(big.get(x).get(0).trim()+"  "+table.getRow(1).getCell(k+3).getText().toString().trim() );
			  			//table.getRow(2).getCell(k+3).setText(String.valueOf(var));
			  			table.getRow(i+4).getCell(k+3).setText(big.get(x+1).get(i).toString());
			  			
			  			if(big.get(x+2).get(i).equals("-"))
			  				table.getRow(i+4).getCell(k+4).setColor("FC9060");
			  			else
			  			{
			  			if(Integer.parseInt(big.get(x+2).get(i)) > 85)
			  				table.getRow(i+4).getCell(k+4).setColor("C5FB7B");
			  			else if(Integer.parseInt(big.get(x+2).get(i)) >= 75 &&  Integer.parseInt(big.get(x+2).get(i)) <= 85 )
			  				table.getRow(i+4).getCell(k+4).setColor("FEF975");
			  			else
			  				table.getRow(i+4).getCell(k+4).setColor("FC9060");
			  			}
			  			table.getRow(i+4).getCell(k+4).setText(big.get(x+2).get(i).toString());
			  			
			  		/*if(!big.get(x+2).get(i).equals("-"))
			  		{
				  		if(Integer.parseInt(big.get(x+2).get(i)) != 0)
				  		{
				  			 int var = (Integer.parseInt(big.get(x+1).get(i))*100)/Integer.parseInt(big.get(x+2).get(i));
				  			 if(var>ar[k/2] && k/2<subs.length)
				  			 {
				  				 ar[k/2]=var;
				  				//table.getRow(2).getCell(k+3).setText(String.valueOf(ar[k/2]));
				  				
				  			 }
			  			
			  			 
			  			}
			  		}*/
			  		
			  				
			  		}
			  		
			  		
		  		}
	  			
	  			k = k + 2;
	  			
	  		}
	  	}
	 
	  	
	  	File path=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\Consolidated\\"+sem+sec+"\\"+sem+sec+"consolidated["+finalDate+"].docx");
	  	FileOutputStream fileOut = new FileOutputStream(path);
        docX2.write(fileOut);
        fileOut.close();
        
        
        
        InputStream doc = new FileInputStream(path);
        XWPFDocument document = new XWPFDocument(doc);
        

			String rootpath = "C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM";
		      File myObj = new File(rootpath+"\\currentUser.txt");
		      if (!myObj.createNewFile()) {
		        System.out.println("File already exists.");
		        Boolean flag = false;
		        try {
		        
		        BufferedReader br = new BufferedReader(new FileReader(myObj)); 
		        
		        String st; 
		        while ((st = br.readLine()) != null) {
		          if(st.equals("admin@dsce.org")) {
		        	  flag = true;
		          } 
		        }
		        
		        if(flag == false) {
		            path.setReadOnly();
		        }
		        
		        }catch (IOException e) {
		        	e.printStackTrace();
		        } 
		      }

       // PdfOptions options = PdfOptions.create();
        //out = new FileOutputStream(new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\Consolidated\\"+sem+sec+"\\"+sem+sec+"consolidated["+finalDate+"].pdf"));
        //PdfConverter.getInstance().convert(document, out, options);
        System.out.println("Done");
		           //out.close(); 
	            System.out.println(".docx written successully");
	              
	}
	
	
	public static class HBoxCell extends HBox {
        Label label = new Label();
        TextField b = new TextField();

        HBoxCell(String labelText, String buttonText) {
             super();

             label.setText(labelText);
             label.setMaxWidth(Double.MAX_VALUE);
             HBox.setHgrow(label, Priority.ALWAYS);

             b.setText(buttonText);

             this.getChildren().addAll(label, b);
        }
   }
	
	
	public void setDimen()
	{
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		 int width = (int) screenBounds.getWidth();
		 int height = (int) screenBounds.getHeight();
	        int utilsize = (width/100)*20;
	        int lab_size = (width/100)*50;
	        int allheights = (height/100)*90;
	        utility_attend.setPrefWidth(utilsize);
	        utility_attend.setPrefHeight(allheights);
	        table.setPrefWidth(lab_size);
	        table.setPrefHeight(allheights);
	        stack.setPrefWidth(lab_size);
	        stack.setPrefHeight(allheights);
	        
	        savespbtn.setPrefWidth(utilsize);
	        loadspbtn.setPrefWidth(utilsize);
	        savefir.setPrefWidth(utilsize);
	        syncsave.setPrefWidth(utilsize);
	        loadAttendanceFromDbBtn.setPrefWidth(utilsize);
	        addTotalClasses.setPrefWidth(utilsize);
	        
	        
	       anchorlabs.setPrefWidth(utilsize);
	       anchorspecial.setPrefWidth(utilsize);
	       titlelabs.setPrefWidth(utilsize);
	       special_case_lv.setPrefWidth(utilsize);
	       
	       
	       anchorlabs.setPrefHeight((height/100)*30);
	       anchorspecial.setPrefHeight((height/100)*30);
	       titlelabs.setPrefHeight((height/100)*30);
	       special_case_lv.setPrefHeight((height/100)*30);
	       
	       leftvbox.setPrefWidth(utilsize);
	       leftvbox.setPrefHeight((height/100)*90);
	       
	       
	       ap_attendance.setPrefWidth((width/100)*90);
	       ap_attendance.setPrefHeight((height/100)*90);

	       gianthbox.setPrefWidth((width/100)*90);
	       gianthbox.setPrefHeight((height/100)*90);

	       outerscreen.setPrefWidth((width/100)*90);
	       outerscreen.setPrefHeight((height/100)*90);
	       

	        innerscreen.setPrefWidth((width/100)*90);
	        innerscreen.setPrefHeight((height/100)*90);
	}
	   
	    
	public static class Person {
	   	 
        private final SimpleStringProperty usn;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty classes;
        private final SimpleStringProperty per;
        
        
        private Person(String string,String string1, String string2,String string3) {
        	this.usn = new SimpleStringProperty(string);
        	this.name =new SimpleStringProperty(string1);
            this.classes =new SimpleStringProperty(string2);
            this.per =new SimpleStringProperty(string3);
            
        }
        
        

		public void setStyle(String string) {
			// TODO Auto-generated method stub
			
		}



		public String getUsn() {
            
            return usn.get();
        }
        
        public void setUsn(String u) {
           usn.set(u);
        }

        public String getName() {
            
            return name.get();
        }

        
        public void setName(String u) {
            name.set(u);
        }

        public String getClasses() {
        	
        	return classes.get();
        }

        public void setClasses(String u) {
            classes.set(u);
           
        }
        
        public String getPer() {
        	
        	return per.get();
        }

        public void setPer(String u) {
            per.set(u);
           
        }
        
        
       
	}

}
