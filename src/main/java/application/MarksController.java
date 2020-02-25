package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class MarksController 
{
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

	@FXML
	TitledPane tpatt, titlecie, saveonlinetitle;

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
	AnchorPane ap, ap_attendance, calendar,anchorcie, saveonlineanchor;
	@FXML
	HBox hb,gianthbox,tableshbox;
	@FXML
	DatePicker datePicker1=new DatePicker();
	@FXML
	TextField subject=new TextField();
	@FXML
	VBox utility_attend;
	String rootpath = "C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\";
	String coursecode ="";
	@FXML
	private TableView<Person> studDetails_table = new TableView<Person>();
	@FXML
	private TableView<Person> cie1Table = new TableView<Person>();
	@FXML
	private TableView<Person> cie2Table = new TableView<Person>();
	@FXML
	private TableView<Person> cie3Table = new TableView<Person>();
	@FXML
	private ListView<String> asnmt_lv = new ListView<String>();
	@FXML
	private ListView<String> aat_lv = new ListView<String>();
	@FXML
	private ListView<String> total_lv = new ListView<String>();
	
	@FXML
	RadioButton cie1rb = new RadioButton("CIE1");
	@FXML
	RadioButton cie2rb = new RadioButton("CIE2");
	@FXML
	RadioButton cie3rb = new RadioButton("CIE3");
	@FXML
	RadioButton asnmtrb = new RadioButton("Assignment");
	@FXML
	RadioButton aatrb = new RadioButton("AAT");
	
	@FXML
	RadioButton ftot = new RadioButton("50");
	@FXML
	RadioButton ttof = new RadioButton("10");
	
	
	TableColumn usnCol1,nameCol1,cie150, cie110, cie250, cie210, cie350, cie310;
	static ArrayList<String> studdat = new ArrayList<String>();
	ObservableList<String> abc=  FXCollections.observableArrayList();
	
	
	   @FXML
	   private TextField addTotalClasses;
	    
	    private ObservableList<Person> studDetails_data =
		        FXCollections.observableArrayList();
	    private ObservableList<Person> cie1data =
		        FXCollections.observableArrayList();
	    private ObservableList<Person> cie2data =
		        FXCollections.observableArrayList();
	    private ObservableList<Person> cie3data =
		        FXCollections.observableArrayList();
	    
	    private ObservableList<String> aatdata =
		        FXCollections.observableArrayList();
	    private ObservableList<String> asnmtdata =
		        FXCollections.observableArrayList();
	    private ObservableList<String> totaldata =
		        FXCollections.observableArrayList();
	    
	    @FXML
	    AnchorPane ap_calendar, innerscreen, outerscreen;
	    
	    @FXML
		Button savespbtn,loadspbtn, savefir, syncsave;
	    
	    @FXML
	    ComboBox drop = new ComboBox();
	    
	    String selectedCIErb = "";
	    String selectedmode = "";
	    
	    @FXML
	    VBox leftvbox,vboxcie,enterdetailsvbox;
	    
	    Map<String, List<String>> subjects = new HashMap<String, List<String>>();
	    //This is one instance of the  map you want to store in the above list.

	    
	    String output="";
	    public void initialize() throws IOException
		{		
	    	tpatt.setExpanded(true);
	    	in1.setText(in);
	    	in1.setWrapText(true);
	        in1.setStyle("-fx-font-family: \"Times New Roman \"; -fx-font-size: 20; -fx-text-fill: white; -fx-background-color:#6464a5;");
	    	 
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
		        //cie1table.setEditable(true);
		    	
				usnCol1 = new TableColumn("USN");
		        usnCol1.setMinWidth(150);
		        usnCol1.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val1"));
		       
		        nameCol1 = new TableColumn("NAME");
		        nameCol1.setMinWidth(150);
		        nameCol1.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val2"));
		        
		        
		        studDetails_table.setItems(studDetails_data);
		        studDetails_table.getColumns().addAll(usnCol1,nameCol1);
		        
		        
		 
		        cie150 = new TableColumn("CIE1 - 50");
		        cie150.setMinWidth(40);
		        cie150.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val1"));
		        cie150.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
		            @Override
		            public TableCell<Person, String> call(TableColumn<Person, String> orderItemStringTableColumn) {
		                return new EditingCell();
		            }
		        });
		     /*   cie150.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person, String> t) {
		        ((Person) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setVal1(t.getNewValue());
		        }
		        }
		        );*/
		        
		        
		        
		        cie110 = new TableColumn("CIE1 - 10");
		        cie110.setMinWidth(40);
		        cie110.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val2"));
		        cie110.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
		            @Override
		            public TableCell<Person, String> call(TableColumn<Person, String> orderItemStringTableColumn) {
		                return new EditingCell();
		            }
		        });
		        /*cie110.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person, String> t) {
		        ((Person) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setVal2(t.getNewValue());
		        }
		        }
		        );*/
		        
		        cie1Table.setEditable(true);
		        cie1Table.setItems(cie1data);
		        cie1Table.getColumns().addAll(cie150,cie110);
		        
		        
		        
		        cie250 = new TableColumn("CIE2 - 50");
		        cie250.setMinWidth(40);
		        cie250.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val1"));
		        cie250.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
		            @Override
		            public TableCell<Person, String> call(TableColumn<Person, String> orderItemStringTableColumn) {
		                return new EditingCell();
		            }
		        });
		        /*cie250.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person, String> t) {
		        ((Person) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setVal1(t.getNewValue());
		        }
		        }
		        );*/
		        
		        
		        
		        cie210 = new TableColumn("CIE2 - 10");
		        cie210.setMinWidth(40);
		        cie210.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val2"));
		        cie210.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
		            @Override
		            public TableCell<Person, String> call(TableColumn<Person, String> orderItemStringTableColumn) {
		                return new EditingCell();
		            }
		        });
		        /*cie210.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person, String> t) {
		        ((Person) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setVal2(t.getNewValue());
		        }
		        }
		        );*/
		        
		        cie2Table.setEditable(true);
		        cie2Table.setItems(cie2data);
		        cie2Table.getColumns().addAll(cie250,cie210);
		        
		        
		        
		        cie350 = new TableColumn("CIE3 - 50");
		        cie350.setMinWidth(40);
		        cie350.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val1"));
		        cie350.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
		            @Override
		            public TableCell<Person, String> call(TableColumn<Person, String> orderItemStringTableColumn) {
		                return new EditingCell();
		            }
		        });
		        /*cie350.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person, String> t) {
		        ((Person) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setVal1(t.getNewValue());
		        }
		        }
		        );*/
		        
		        
		        
		        cie310 = new TableColumn("CIE3 - 10");
		        cie310.setMinWidth(40);
		        cie310.setCellValueFactory(
		                new PropertyValueFactory<Person, String>("val2"));
		        cie310.setCellFactory(new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
		            @Override
		            public TableCell<Person, String> call(TableColumn<Person, String> orderItemStringTableColumn) {
		                return new EditingCell();
		            }
		        });
		        /*cie310.setOnEditCommit(
		        new EventHandler<CellEditEvent<Person, String>>() {
		        @Override
		        public void handle(CellEditEvent<Person, String> t) {
		        ((Person) t.getTableView().getItems().get(
		        t.getTablePosition().getRow())
		        ).setVal2(t.getNewValue());
		        }
		        }
		        );*/
		        
		        cie3Table.setEditable(true);
		        cie3Table.setItems(cie3data);
		        cie3Table.getColumns().addAll(cie350,cie310);
		        
		        
		        
		        

				ToggleGroup tg = new ToggleGroup();
				
				
		        cie1rb.setToggleGroup(tg); 
		        cie2rb.setToggleGroup(tg); 
		        cie3rb.setToggleGroup(tg); 
		        asnmtrb.setToggleGroup(tg);
		        aatrb.setToggleGroup(tg); 
		        
		        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
		        { 
		            public void changed(ObservableValue<? extends Toggle> ob,  
		                                                    Toggle o, Toggle n) 
		            { 
		  
		                RadioButton rb = (RadioButton)tg.getSelectedToggle(); 
		  
		                if (rb != null) { 
		                    selectedCIErb = rb.getText(); 
		                    	
		                } 
		            }

					
		        });
		        
		        
		        
		        
		        
		        ToggleGroup moderb = new ToggleGroup();
		        
		        ttof.setToggleGroup(moderb);
		        ftot.setToggleGroup(moderb);
		        
		        moderb.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
		        { 
		            public void changed(ObservableValue<? extends Toggle> ob,  
		                                                    Toggle o, Toggle n) 
		            { 
		  
		                RadioButton rb = (RadioButton)moderb.getSelectedToggle(); 
		  
		                if (rb != null) { 
		                    selectedmode = rb.getText(); 
		                    	
		                } 
		            }

					
		        }); 
		    	
		    	abc.add("Enter Semester");
		    	abc.add("Enter Section");
		    	abc.add("Enter Batch");
		    	
		    	//perCol.setVisible(false);
		    	//usnCol1.setVisible(false);
		    	//nameCol1.setVisible(false);
		    	//classesCol.setVisible(false);
		    	list.setVisible(false);
		    	list.setItems(abc);
		    	
		    	asnmt_lv.setItems(asnmtdata);
		    	aat_lv.setItems(aatdata);
		    	total_lv.setItems(totaldata);
		    	
		    	semester.textProperty().addListener((observable, oldValue, newValue) -> {
		    	    dropDown(newValue);
		    	});
		    	
		    	//********************IMPORTANT***********************
		    	/*
		    	cie1table.setRowFactory(tv -> new TableRow<Person>() {
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
			    	        	  setStyle("-fx-background-color: #D3D3D3;");
			    	        }
		    	        	
		    	        	}
		    	        else
		    	        {
		    	        	  setStyle("-fx-background-color: #D3D3D3;");
		    	        }
		    	          
		    	    }
		    	});
		    	*/
		    
		    	loadSubjects();
		    	asnmt_lv.setEditable(true);
		    	asnmt_lv.setCellFactory(TextFieldListCell.forListView());	
		    	asnmt_lv.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
					@Override
					public void handle(ListView.EditEvent<String> t) {
						asnmt_lv.getItems().set(t.getIndex(), t.getNewValue());
						System.out.println("setOnEditCommit");
					}
								
				});
		    	
		    	aat_lv.setEditable(true);
		    	aat_lv.setCellFactory(TextFieldListCell.forListView());	
		    	aat_lv.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
					@Override
					public void handle(ListView.EditEvent<String> t) {
						aat_lv.getItems().set(t.getIndex(), t.getNewValue());
						System.out.println("setOnEditCommit");
					}
				});
		    	
		    	total_lv.setEditable(true);
		    	total_lv.setCellFactory(TextFieldListCell.forListView());	
		    	total_lv.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
					@Override
					public void handle(ListView.EditEvent<String> t) {
				    	total_lv.getItems().set(t.getIndex(), t.getNewValue());
						System.out.println("setOnEditCommit");
					}
								
				});

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
			//int tc = Integer.parseInt(addTotalClasses.getText().toString());

	   	 ArrayList<String> cie1 = new ArrayList<>();
	   	 ArrayList<String> cie2 = new ArrayList<>();
	   	 ArrayList<String> cie3 = new ArrayList<>();
	   	 ArrayList<String> assignment = new ArrayList<>();
	   	 ArrayList<String> aat = new ArrayList<>();
	   	 ArrayList<String> total = new ArrayList<>();
	   	 
	   	 for(int i=0;i<cie1data.size();i++)
	   	 { 
	   		 if(cie1data.get(i).getVal2().toString().equals(""))
	   			cie1.add(" ");
	   		 else
	   		 cie1.add(cie1data.get(i).getVal2().toString());
	   		 
	   		if(cie2data.get(i).getVal2().toString().equals(""))
	   			cie2.add(" ");
	   		 else
	   		 cie2.add(cie2data.get(i).getVal2().toString());
	   		
	   		if(cie3data.get(i).getVal2().toString().equals(""))
	   			cie3.add(" ");
	   		 else
	   		 cie3.add(cie3data.get(i).getVal2().toString());
	   		
	   		
	   		if(asnmtdata.get(i).toString().equals(""))
	   			assignment.add(" ");
	   		 else
	   			assignment.add(asnmtdata.get(i).toString());
	   		
	   		if(aatdata.get(i).toString().equals(""))
	   			aat.add(" ");
	   		 else
	   			aat.add(aatdata.get(i).toString());
	   		
	   		if(totaldata.get(i).toString().equals(""))
	   			total.add(" ");
	   		 else
	   			total.add(totaldata.get(i).toString());
	   		
	   		
	   	 }
	   	 
			
			
			
		    try {
		    	
		    	ArrayList<ArrayList<String>> big = new ArrayList<ArrayList<String>>();
		      	
		      	
		      	
		      	
		            final CountDownLatch latch1 = new CountDownLatch(1);
		            DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
		            

		        	 String tchr_name = coursecode;
		        	
		        	
		        	 String cie1str = String.join(",", cie1);
		        	 String cie2str = String.join(",", cie2);
		        	 String cie3str = String.join(",", cie3);
		        	 String assignmentstr = String.join(",", assignment);
		        	 String aatstr = String.join(",", aat);
		        	 String totalstr = String.join(",", total);
		        	 DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Marks/"+tfsem+"/"+tfsec+"/"+tchr_name);
		        	 	 DatabaseReference child_name = FirebaseDatabase.getInstance().getReference();
		        	 	
		        	 child_name=ref.child("cie1");
		        	 child_name.setValueAsync(cie1str);
		        	 child_name=ref.child("cie2");
		        	 child_name.setValueAsync(cie2str);
		        	 child_name=ref.child("cie3");
		        	 child_name.setValueAsync(cie3str);
		        	 child_name=ref.child("assignment");
		        	 child_name.setValueAsync(assignmentstr);
		        	 child_name=ref.child("aat");
		        	 child_name.setValueAsync(aatstr);
		        	 child_name=ref.child("total");
		        	 child_name.setValueAsync(totalstr);
		        	 child_name=ref.child("sub");
		        	 child_name.setValueAsync(tchr_name);
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
			/*TextInputDialog dialog = new TextInputDialog("Enter here");
			 
			dialog.setTitle("Set Session");
		dialog.setHeaderText("Enter duration ('Date 1 - Date 2'):");
				dialog.setContentText("Duration:");
				 
				Optional<String> result = dialog.showAndWait();
				 
				result.ifPresent(name -> {
					dur = name;
				});
				*/
				 tfsem = semester.getText().toString();
				 tfsec = section.getText().toString().toUpperCase();

		     ArrayList<DataSnapshot> Userlist = new ArrayList<DataSnapshot>();

		      try {
		    	  	  String reference = "Marks/"+tfsem.toString()+"/"+tfsec.toString();
		    	  	  System.out.println(reference);
		              final CountDownLatch latch1 = new CountDownLatch(1);
		              final FirebaseDatabase database = FirebaseDatabase.getInstance();
		              DatabaseReference ref2 = database.getReference(reference);
		               //ref2 = ref1.child("Marks/"+tfsem+"/"+tfsec+"/");


		               ref2.addListenerForSingleValueEvent(
		            		   new ValueEventListener() {
		                public void onDataChange(DataSnapshot dataSnapshot) {

		                    //ArrayList<Object> Userlist = new ArrayList<Object>();   
		                    ArrayList<ArrayList<String>> big_arr = new ArrayList<ArrayList<String>>();
		                    	
		                    
		                    
		                   	   	for (DataSnapshot dsp : dataSnapshot.getChildren()) {
		                   	   		System.out.println("ssss"+dsp.getKey());
		                   	   	
		                   	      //if(dsp.getKey().equalsIgnoreCase(tfsec))  
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
		      	
		      	System.out.println( Userlist.get(0).getChildren());
		      	
		      	
		      	for(DataSnapshot d: Userlist)
		      	{
		      		System.out.println(d.getKey());
		      		MarksData fir = d.getValue(MarksData.class);
		      		
		      		System.out.println(fir.getCie1());
		      		
		      		smol = new ArrayList<String>();
		      		smol.add(fir.getSub());
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getCie1().split(",")));
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getCie2().split(",")));
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getCie3().split(",")));
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getAssignment().split(",")));
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getAat().split(",")));
		      		big.add(smol);
		      		
		      		smol = new ArrayList<String>();
		      		smol.addAll(Arrays.asList(fir.getTotal().split(",")));
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
		 
			for(int i = 0; i< studDetails_data.size();i++)
			{
				System.out.println(studDetails_data.get(i).getVal2());

				System.out.println(cie1data.get(i).getVal2());

				System.out.println(cie2data.get(i).getVal2());
			}
			

		    studdat.clear();
		    tfsem = semester.getText().toString();
		    tfsem = tfsem.toUpperCase();
		    tfsec = section.getText().toString();
		    tfsec = tfsec.toUpperCase();
		    studdat.add(tfsem);
		    studdat.add(tfsec);
		    /*String a=addTotalClasses.getText();
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
	    	}*/
		    
	    	  String sub=drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim();
		        System.out.println(sub);
		        //String finalDate="";
				//LocalDate date = datePicker1.getValue();
		       //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		       /*if(date != null)
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
		    	}*/
	 	  InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+".xls");
			HSSFWorkbook  workbook = new HSSFWorkbook(ExcelFileToRead);
	        HSSFSheet spreadsheet = workbook.getSheetAt(0);
	 	   /*Workbook workbook = new HSSFWorkbook();
	        Sheet spreadsheet = workbook.createSheet("sample");

	        Row row = spreadsheet.createRow(0);*/
	      
		
	      spreadsheet.getRow(0).createCell(0).setCellValue("DAYANANDA SAGAR COLLEGE OF ENGINEERING");
	        spreadsheet.getRow(1).createCell(0).setCellValue("DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING");
	        spreadsheet.getRow(2).createCell(0).setCellValue(studdat.get(0)+studdat.get(1)+" MARKS");
	        spreadsheet.getRow(3).createCell(0).setCellValue("SUBJECT: "+sub);
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 5));
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(1, 1, 0, 5));
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(2, 2, 0, 5));
	        spreadsheet.addMergedRegionUnsafe(new CellRangeAddress(3, 3, 0, 5));
	        //System.out.println(cie1table.getItems().size());
	        spreadsheet.getRow(4).createCell(0).setCellValue("USN");
	        spreadsheet.getRow(4).createCell(1).setCellValue("NAME");
	        spreadsheet.getRow(4).createCell(2).setCellValue("CIE1-50");
	        spreadsheet.getRow(4).createCell(3).setCellValue("CIE1-10");
	        spreadsheet.getRow(4).createCell(4).setCellValue("CIE2-50");
	        spreadsheet.getRow(4).createCell(5).setCellValue("CIE2-10");
	        spreadsheet.getRow(4).createCell(6).setCellValue("CIE3-50");
	        spreadsheet.getRow(4).createCell(7).setCellValue("CIE3-10");
	        spreadsheet.getRow(4).createCell(8).setCellValue("ASSIGNMENT");
	        spreadsheet.getRow(4).createCell(9).setCellValue("AAT");
	        spreadsheet.getRow(4).createCell(10).setCellValue("TOTAL");
	        
	        
	        int i=5;
	        for(int j = 0; j< studDetails_data.size();j++)
			{
				spreadsheet.getRow(i).createCell(0).setCellValue(studDetails_data.get(j).getVal1());
				spreadsheet.getRow(i).createCell(1).setCellValue(studDetails_data.get(j).getVal2());
				spreadsheet.getRow(i).createCell(2).setCellValue(cie1data.get(j).getVal1());
				spreadsheet.getRow(i).createCell(3).setCellValue(cie1data.get(j).getVal2());
				spreadsheet.getRow(i).createCell(4).setCellValue(cie2data.get(j).getVal1());
				spreadsheet.getRow(i).createCell(5).setCellValue(cie2data.get(j).getVal2());
				spreadsheet.getRow(i).createCell(6).setCellValue(cie3data.get(j).getVal1());
				spreadsheet.getRow(i).createCell(7).setCellValue(cie3data.get(j).getVal2());
				spreadsheet.getRow(i).createCell(8).setCellValue(asnmtdata.get(j));
				spreadsheet.getRow(i).createCell(9).setCellValue(aatdata.get(j));
				spreadsheet.getRow(i).createCell(10).setCellValue(totaldata.get(j));
			
				i++;
			}
	        
	      
	       
	  
	        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+"-"+drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim()+".xls");
	        workbook.write(fileOut);
	        fileOut.close();
	        
	        /*InputStream ExcelFileToRead1 = new FileInputStream(directoryName+fileName);
	       // InputStream ExcelFileToRead1 = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Attendance\\"+studdat.get(0)+studdat.get(1)+"-"+sub+"\\"+finalDate+".xls");
			HSSFWorkbook  wb = new HSSFWorkbook(ExcelFileToRead1);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			
			HSSFRow row; 
			HSSFCell cell;

			Iterator rows = sheet.rowIterator();
			
			studDetails_data.clear();
			int k = 5;
			System.out.println(sheet.getPhysicalNumberOfRows());
			while(k<sheet.getPhysicalNumberOfRows())
			{
				studDetails_data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
						sheet.getRow(k).getCell(1).getStringCellValue(),
						sheet.getRow(k).getCell(2).getStringCellValue(),
						sheet.getRow(k).getCell(3).getStringCellValue()));
				//System.out.println(sheet.getRow(k).getCell(2).getStringCellValue());
				k++;
			}*/
			 //cie1table.setItems(studDetails_data);
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
	        
	        
	        saveWordDoc();
	        }
	
	
	
	protected void setText(Object object) {
			// TODO Auto-generated method stub
			
		}

	protected void setStyle(String string) {
			// TODO Auto-generated method stub
			
		}

	
	public void loadAttendance(ActionEvent e)throws IOException
	{
		semester.setVisible(true);
    	batch.setVisible(true);
    	section.setVisible(true);
    	list.setVisible(true);
    	list.setEditable(true);
    	
    	
	    studdat.clear();
	    tfsem = semester.getText().toString();
	    tfsem = tfsem.toUpperCase();
	    tfsec = section.getText().toString();
	    tfsec = tfsec.toUpperCase();
	    studdat.add(tfsem);
	    studdat.add(tfsec);
	    cie1data.clear();
	    cie2data.clear();
	    cie3data.clear();
	    aatdata.clear();
	    asnmtdata.clear();
	    totaldata.clear();
	    
	   // String sub=drop.getSelectionModel().getSelectedItem().toString().trim();
	    LocalDate date=datePicker1.getValue();
	    /*String a=addTotalClasses.getText();
	    int tc=1;
	   // if(!a.equals(""))
    	//	tc=Integer.parseInt(a);
    	else
    	{
    		Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly enter all the fields!");
	        alerts.showAndWait();
	        return;
    	}*/
	    if(tfsem.equals("")||tfsec.equals(""))
		{
			Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly enter all the fields!");
	        alerts.showAndWait();
	        return;
	        }
		
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
		
	    
		
			studDetails_data.clear();
			//cie1table.setItems(studDetails_data);
		
			String[] sheetrows ;
			
			InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+".xls");
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
	        
			if (Files.exists(Paths.get("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+"-"+drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim()+".xls"))) {
					
				ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+studdat.get(0)+studdat.get(1)+"-"+drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim()+".xls");
				wb = new HSSFWorkbook(ExcelFileToRead);
				sheet = wb.getSheetAt(0);
				
				
				int k =5;
				System.out.println(sheet.getPhysicalNumberOfRows());
				while(k<sheet.getPhysicalNumberOfRows())
				{
					studDetails_data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
							sheet.getRow(k).getCell(1).getStringCellValue()));
					//System.out.println(sheet.getRow(k).getCell(2).getStringCellValue());
					try
					{
						cie1data.add(new Person(sheet.getRow(k).getCell(2).getStringCellValue(),
								sheet.getRow(k).getCell(3).getStringCellValue()));
					}
					catch(NullPointerException ne)
					{
						cie1data.add(new Person("",""));
					}
					
					try
					{
						cie2data.add(new Person(sheet.getRow(k).getCell(4).getStringCellValue(),
								sheet.getRow(k).getCell(5).getStringCellValue()));
					}
					catch(NullPointerException ne)
					{
						cie2data.add(new Person("",""));
					}
					
					try
					{
						cie3data.add(new Person(sheet.getRow(k).getCell(6).getStringCellValue(),
								sheet.getRow(k).getCell(7).getStringCellValue()));
					}
					catch(NullPointerException ne)
					{
						cie3data.add(new Person("",""));
					}
					
					try
					{
						asnmtdata.add(sheet.getRow(k).getCell(8).getStringCellValue());
					}
					catch(NullPointerException ne)
					{
						asnmtdata.add("");
					}
					
					try
					{
						aatdata.add(sheet.getRow(k).getCell(9).getStringCellValue());
					}
					catch(NullPointerException ne)
					{
						aatdata.add("");
					}
					
					try
					{
						totaldata.add(sheet.getRow(k).getCell(10).getStringCellValue());
					}
					catch(NullPointerException ne)
					{
						totaldata.add("");
					}
	
					
					k++;
				}
				
			}
			else
			{
				HSSFRow row; 
				HSSFCell cell;

				Iterator rows = sheet.rowIterator();
				
				sheetrows = new String[4];
				
				int k =5;
				System.out.println(sheet.getPhysicalNumberOfRows());
				while(k<sheet.getPhysicalNumberOfRows())
				{
					studDetails_data.add(new Person(sheet.getRow(k).getCell(0).getStringCellValue(),
							sheet.getRow(k).getCell(1).getStringCellValue()));
					//System.out.println(sheet.getRow(k).getCell(2).getStringCellValue());
					cie1data.add(new Person("",""));
					cie2data.add(new Person("",""));
					cie3data.add(new Person("",""));
					aatdata.add("");
					asnmtdata.add("");
					totaldata.add("");
					
					k++;
				}
			}
			
			
			
			
			 // Bind the ListView scroll property
	        Node n0 = studDetails_table.lookup(".scroll-bar");
	        
	        if (n0 instanceof ScrollBar) {
	            final ScrollBar bar0 = (ScrollBar) n0;
	            Node n1 = cie1Table.lookup(".scroll-bar");
	            Node n2 = cie2Table.lookup(".scroll-bar");
	            Node n3 = cie3Table.lookup(".scroll-bar");
	            Node n4 = asnmt_lv.lookup(".scroll-bar");
	            Node n5 = aat_lv.lookup(".scroll-bar");
	            Node n6 = total_lv.lookup(".scroll-bar");
	            
	            
	            final ScrollBar bar1 = (ScrollBar) n1;
	            final ScrollBar bar2 = (ScrollBar) n2;
	            final ScrollBar bar3 = (ScrollBar) n3;
	            final ScrollBar bar4 = (ScrollBar) n4;
	            final ScrollBar bar5 = (ScrollBar) n5;
	            final ScrollBar bar6 = (ScrollBar) n6;
	            
	            
	            
	            if (n1 instanceof ScrollBar) {
	                bar1.valueProperty().bindBidirectional(bar0.valueProperty());
	                bar1.valueProperty().bindBidirectional(bar2.valueProperty());
	                bar1.valueProperty().bindBidirectional(bar3.valueProperty());
	                bar1.valueProperty().bindBidirectional(bar4.valueProperty());
	                bar1.valueProperty().bindBidirectional(bar5.valueProperty());
	                bar1.valueProperty().bindBidirectional(bar6.valueProperty());
	                
	            }
	        }
	        
	        
	}
	
	
	
	public void importAttendanceFile(ActionEvent e) throws IOException
	{
		System.out.println("ASDFGHJ");
		semester.setVisible(true);
    	batch.setVisible(true);
    	section.setVisible(true);
    	list.setVisible(true);
    	list.setEditable(true);
    	
    	
	    studdat.clear();
	    tfsem = semester.getText().toString();
	    tfsem = tfsem.toUpperCase();
	    tfsec = section.getText().toString();
	    tfsec = tfsec.toUpperCase();
	    studdat.add(tfsem);
	    studdat.add(tfsec);
	    /*String a=addTotalClasses.getText();
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
    	}*/
	    
	    LocalDate date=datePicker1.getValue();
	    if(tfsem.equals("")||tfsec.equals(""))
		{
			Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly enter all the fields!");
	        alerts.showAndWait();
	        return;
	        }
	    
	    
		
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
		
		
		if(selectedCIErb.equals("") || selectedmode.equals(""))
		{
			Alert alerts=new Alert(AlertType.WARNING);
	        alerts.setTitle("Warning Dialog");
	        alerts.setHeaderText(null);
	        alerts.setContentText("Kindly select which category of marks is entered");
	        alerts.showAndWait();
	        return;
		}
		
		System.out.println("RB"+selectedCIErb);
		
		
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
		
		if(selectedCIErb.equals("CIE1"))
		{
			cie1data.clear();
			
		}
		else if(selectedCIErb.equals("CIE2"))
		{
			cie2data.clear();
		}
		else if(selectedCIErb.equals("CIE3"))
		{
			cie3data.clear();
		}
		else if(selectedCIErb.equals("Assignment"))
		{
			asnmtdata.clear();
		}
		else
		{
			aatdata.clear();
		}
		
		int k =1;
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
					System.out.println(selectedmode);
					if(selectedCIErb.equals("CIE1"))
					{
						if(selectedmode.equals("50"))
						{
							int marks = (int)sheet.getRow(k).getCell(2).getNumericCellValue();
							Double finalmarks = marks*0.2;
							 int scale = (int) Math.pow(10, 1);
							    finalmarks =  (double) Math.round(finalmarks * scale) / scale;
							
							cie1data.add(new Person(
									String.valueOf((int)sheet.getRow(k).getCell(2).getNumericCellValue()),
									finalmarks.toString()));
						ftot.setSelected(false);
						}
						else
						{
							Double marks = (Double)sheet.getRow(k).getCell(2).getNumericCellValue();
							int scale = (int) Math.pow(10, 1);
						    marks =  (double) Math.round(marks * scale) / scale;
							Double finalmarks = marks*5;
							
							cie1data.add(new Person(
									finalmarks.toString(),
									String.valueOf(marks)));
							ttof.setSelected(false);
						}
						cie1rb.setSelected(false);
						
					}
					else if(selectedCIErb.equals("CIE2"))
					{
						if(selectedmode.equals("50"))
						{
							int marks = (int)sheet.getRow(k).getCell(2).getNumericCellValue();
							Double finalmarks = marks*0.2;
							 int scale = (int) Math.pow(10, 1);
							    finalmarks =  (double) Math.round(finalmarks * scale) / scale;
							
							cie2data.add(new Person(
									String.valueOf((int)sheet.getRow(k).getCell(2).getNumericCellValue()),
									finalmarks.toString()));
							ftot.setSelected(false);
						}
						else
						{
							Double marks = (Double)sheet.getRow(k).getCell(2).getNumericCellValue();
							int scale = (int) Math.pow(10, 1);
						    marks =  (double) Math.round(marks * scale) / scale;
							Double finalmarks = marks*5;
							
							cie2data.add(new Person(
									finalmarks.toString(),
									String.valueOf(marks)));
							ttof.setSelected(false);
						}
						cie2rb.setSelected(false);
					}
					else if(selectedCIErb.equals("CIE3"))
					{
						if(selectedmode.equals("50"))
						{
							int marks = (int)sheet.getRow(k).getCell(2).getNumericCellValue();
							Double finalmarks = marks*0.2;
							 int scale = (int) Math.pow(10, 1);
							    finalmarks =  (double) Math.round(finalmarks * scale) / scale;
							
							cie3data.add(new Person(
									String.valueOf((int)sheet.getRow(k).getCell(2).getNumericCellValue()),
									finalmarks.toString()));
							ftot.setSelected(false);
						}
						else
						{
							Double marks = (Double)sheet.getRow(k).getCell(2).getNumericCellValue();
							int scale = (int) Math.pow(10, 1);
						    marks =  (double) Math.round(marks * scale) / scale;
							Double finalmarks = marks*5;
							
							cie3data.add(new Person(
									finalmarks.toString(),
									String.valueOf(marks)));
							ttof.setSelected(false);
						}
						cie3rb.setSelected(false);
					}
					else if(selectedCIErb.equals("Assignment"))
					{
						asnmtdata.add(String.valueOf((int)sheet.getRow(k).getCell(2).getNumericCellValue()));
						asnmtrb.setSelected(false);		
					}
					else
					{
						aatdata.add(String.valueOf((int)sheet.getRow(k).getCell(2).getNumericCellValue()));
						aatrb.setSelected(false);
					}
					
					}
					catch(NullPointerException ne)
					{
						
					}
				
				//int perc = (int)sheet.getRow(k).getCell(2).getNumericCellValue()/Integer.parseInt(addTotalClasses.getText().toString()) * 100;
				
				//System.out.println(sheet.getRow(k).getCell(2).getStringCellValue());
				k++;
			}
			//cie1table.setItems(studDetails_data);
			
			
			selectedCIErb = "";
			selectedmode = "";
	}


	public void convertMarks(ActionEvent e)
	{
		//
		//System.out.println("-----huhuhu");
			
			
			//System.out.println("ssss"+sheet.getRow(k).getCell(2).getStringCellValue()+"mmm");
			
			
				try
				{
				System.out.println(selectedmode);
				if(selectedCIErb.equals("CIE1"))
				{	
					if(selectedmode.equals("50"))
					{
						

						for(Person p: cie1data)
						{
							if(p.val1.equals(""))
							{
								Alert alerts=new Alert(AlertType.WARNING);
						        alerts.setTitle("Warning Dialog");
						        alerts.setHeaderText(null);
						        alerts.setContentText("Kindly enter all the values in CIE1");
						        alerts.showAndWait();
						        return;
										
							}
						}
						
					    ObservableList<Person> temp =
						        FXCollections.observableArrayList();
						for(int i=0;i<cie1data.size();i++)
						{
							int marks = Integer.parseInt(cie1data.get(i).val1.get());
							Double finalmarks = marks*0.2;
							 int scale = (int) Math.pow(10, 1);
							    finalmarks =  (double) Math.round(finalmarks * scale) / scale;
							    System.out.println(finalmarks);
							 temp.add(new Person(marks+"", finalmarks+""));
							 
						}
						System.out.println(temp.get(0).getVal2());
						cie1data.clear();
					for (int i=0;i<temp.size();i++)
					{
						cie1data.add(temp.get(i));
					}
						temp.clear();
						
					ftot.setSelected(false);
					}
					else
					{
						for(Person p: cie1data)
						{
							if(p.val2.equals(""))
							{
								Alert alerts=new Alert(AlertType.WARNING);
						        alerts.setTitle("Warning Dialog");
						        alerts.setHeaderText(null);
						        alerts.setContentText("Kindly enter all the values in CIE1");
						        alerts.showAndWait();
						        return;
										
							}
						}
						
				
						
						ObservableList<Person> temp =
						        FXCollections.observableArrayList();
						for(int i=0;i<cie1data.size();i++)
						{
							Double marks = Double.parseDouble(cie1data.get(i).val2.get());
							int scale = (int) Math.pow(10, 1);
						    marks =  (double) Math.round(marks * scale) / scale;
							int finalmarks = (int) (marks*5);
							 temp.add(new Person(finalmarks+"", marks+""));
							 
						}
						System.out.println(temp.get(0).getVal2());
						cie1data.clear();
					for (int i=0;i<temp.size();i++)
					{
						cie1data.add(temp.get(i));
					}
						temp.clear();
						
						ttof.setSelected(false);
					}
					cie1rb.setSelected(false);
					
				}
				else if(selectedCIErb.equals("CIE2"))
				{
					if(selectedmode.equals("50"))
					{
						

						for(Person p: cie2data)
						{
							if(p.val1.equals(""))
							{
								Alert alerts=new Alert(AlertType.WARNING);
						        alerts.setTitle("Warning Dialog");
						        alerts.setHeaderText(null);
						        alerts.setContentText("Kindly enter all the values in CIE1");
						        alerts.showAndWait();
						        return;
										
							}
						}
						
						 ObservableList<Person> temp =
							        FXCollections.observableArrayList();
							for(int i=0;i<cie2data.size();i++)
							{
								int marks = Integer.parseInt(cie2data.get(i).val1.get());
								Double finalmarks = marks*0.2;
								 int scale = (int) Math.pow(10, 1);
								    finalmarks =  (double) Math.round(finalmarks * scale) / scale;
								    System.out.println(finalmarks);
								 temp.add(new Person(marks+"", finalmarks+""));
								 
							}
							System.out.println(temp.get(0).getVal2());
							cie2data.clear();
						for (int i=0;i<temp.size();i++)
						{
							cie2data.add(temp.get(i));
						}
							temp.clear();
						
					ftot.setSelected(false);
					}
					else
					{
						for(Person p: cie2data)
						{
							if(p.val2.equals(""))
							{
								Alert alerts=new Alert(AlertType.WARNING);
						        alerts.setTitle("Warning Dialog");
						        alerts.setHeaderText(null);
						        alerts.setContentText("Kindly enter all the values in CIE1");
						        alerts.showAndWait();
						        return;
										
							}
						}
						
						ObservableList<Person> temp =
						        FXCollections.observableArrayList();
						for(int i=0;i<cie2data.size();i++)
						{
							Double marks = Double.parseDouble(cie2data.get(i).val2.get());
							int scale = (int) Math.pow(10, 1);
						    marks =  (double) Math.round(marks * scale) / scale;
							int finalmarks = (int) (marks*5);
							 temp.add(new Person(finalmarks+"", marks+""));
							 
						}
						System.out.println(temp.get(0).getVal2());
						cie2data.clear();
					for (int i=0;i<temp.size();i++)
					{
						cie2data.add(temp.get(i));
					}
						temp.clear();
					
						
						ttof.setSelected(false);
					}
					cie2rb.setSelected(false);
				}
				else if(selectedCIErb.equals("CIE3"))
				{
					if(selectedmode.equals("50"))
					{
						

						for(Person p: cie3data)
						{
							if(p.val1.equals(""))
							{
								Alert alerts=new Alert(AlertType.WARNING);
						        alerts.setTitle("Warning Dialog");
						        alerts.setHeaderText(null);
						        alerts.setContentText("Kindly enter all the values in CIE1");
						        alerts.showAndWait();
						        return;
										
							}
						}
						
						 ObservableList<Person> temp =
							        FXCollections.observableArrayList();
							for(int i=0;i<cie3data.size();i++)
							{
								int marks = Integer.parseInt(cie3data.get(i).val1.get());
								Double finalmarks = marks*0.2;
								 int scale = (int) Math.pow(10, 1);
								    finalmarks =  (double) Math.round(finalmarks * scale) / scale;
								    System.out.println(finalmarks);
								 temp.add(new Person(marks+"", finalmarks+""));
								 
							}
							System.out.println(temp.get(0).getVal2());
							cie3data.clear();
						for (int i=0;i<temp.size();i++)
						{
							cie3data.add(temp.get(i));
						}
							temp.clear();
						
					ftot.setSelected(false);
					}
					else
					{
						for(Person p: cie3data)
						{
							if(p.val2.equals(""))
							{
								Alert alerts=new Alert(AlertType.WARNING);
						        alerts.setTitle("Warning Dialog");
						        alerts.setHeaderText(null);
						        alerts.setContentText("Kindly enter all the values in CIE1");
						        alerts.showAndWait();
						        return;
										
							}
						}
						
						ObservableList<Person> temp =
						        FXCollections.observableArrayList();
						for(int i=0;i<cie3data.size();i++)
						{
							Double marks = Double.parseDouble(cie3data.get(i).val2.get());
							int scale = (int) Math.pow(10, 1);
						    marks =  (double) Math.round(marks * scale) / scale;
							int finalmarks = (int) (marks*5);
							temp.add(new Person(finalmarks+"", marks+""));
							 
						}
						System.out.println(temp.get(0).getVal2());
						cie3data.clear();
					for (int i=0;i<temp.size();i++)
					{
						cie3data.add(temp.get(i));
					}
						temp.clear();
					
						
						ttof.setSelected(false);
					}
					cie3rb.setSelected(false);
				}
				
				}
				catch(NullPointerException ne)
				{
					
				}
			
	}
	
	public void calculateTotal(ActionEvent e)
	{
		for(Person p: cie1data)
		{
			if(p.val1.equals(""))
			{
				Alert alerts=new Alert(AlertType.WARNING);
		        alerts.setTitle("Warning Dialog");
		        alerts.setHeaderText(null);
		        alerts.setContentText("Kindly enter all the values in CIE1");
		        alerts.showAndWait();
		        return;
						
			}
		}
		for(Person p: cie2data)
		{
			if(p.val1.equals(""))
			{
				Alert alerts=new Alert(AlertType.WARNING);
		        alerts.setTitle("Warning Dialog");
		        alerts.setHeaderText(null);
		        alerts.setContentText("Kindly enter all the values in CIE1");
		        alerts.showAndWait();
		        return;
						
			}
		}
		for(Person p: cie3data)
		{
			if(p.val1.equals(""))
			{
				Alert alerts=new Alert(AlertType.WARNING);
		        alerts.setTitle("Warning Dialog");
		        alerts.setHeaderText(null);
		        alerts.setContentText("Kindly enter all the values in CIE1");
		        alerts.showAndWait();
		        return;
						
			}
		}
		for(String p: asnmtdata)
		{
			if(p.equals(""))
			{
				Alert alerts=new Alert(AlertType.WARNING);
		        alerts.setTitle("Warning Dialog");
		        alerts.setHeaderText(null);
		        alerts.setContentText("Kindly enter all the values in CIE1");
		        alerts.showAndWait();
		        return;
						
			}
		}
		for(String p: aatdata)
		{
			if(p.equals(""))
			{
				Alert alerts=new Alert(AlertType.WARNING);
		        alerts.setTitle("Warning Dialog");
		        alerts.setHeaderText(null);
		        alerts.setContentText("Kindly enter all the values in CIE1");
		        alerts.showAndWait();
		        return;
						
			}
		}
		
		totaldata.clear();
		
		for(int i =0;i<cie1data.size();i++)
		{
			Double cie1 = Double.parseDouble(cie1data.get(i).getVal2());
			Double cie2 = Double.parseDouble(cie2data.get(i).getVal2());
			Double cie3 = Double.parseDouble(cie3data.get(i).getVal2());
			Double assignment = Double.parseDouble(asnmtdata.get(i));
			Double aat = Double.parseDouble(aatdata.get(i));
			int sum = (int)(cie1 + cie2 + cie3 + assignment + aat);
			totaldata.add(sum+"");
		}
		
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
			//LocalDate date = datePicker1.getValue();
	       //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	        //Date conv_date = java.sql.Date.valueOf(date);
	        //finalDate = formatter.format(conv_date);
	        //finalDate = finalDate.replace('/', '-');
	     
	      
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
	      InputStream ExcelFileToRead = new FileInputStream("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+sem+sec+".xls");
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

		  	int twipsPerInch=1440;
	  		r1.setHeight((int)(twipsPerInch*2/10)); //set height 1/10 inch.
	  		r1.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
	  		table.getRow(i+2).createCell().setText(usns.get(i));
	  		table.getRow(i+2).createCell().setText(names.get(i));
	  		
	  		int k = 0;
	  		
	  		while(k < (subs.length))
	  		{
	  			table.getRow(i+2).createCell().setText("");
	  			//for(int m = 0; m<big.size();  m++)
	  		//	System.out.println(big.get(m));
	  			
	  			for(int x=0; x < big.size(); x=x+7)
		  		{
			  		if((big.get(x).get(0).trim().equalsIgnoreCase(table.getRow(1).getCell(k+3).getText().toString().trim())))
			  		{
			  			//table.getRow(2).getCell(k+3).setText(String.valueOf(var));
			  			table.getRow(i+2).getCell(k+3).setText(big.get(x+1).get(i).toString());
			  			
			  		}
			  		
			  		
		  		}
	  			
	  			k = k + 1;
	  			
	  		}
	  	}
	 
	  	
	  	File path=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\Consolidated\\"+sem+sec+"\\"+sem+sec+"consolidated[s].docx");
	  	FileOutputStream fileOut = new FileOutputStream(path);
        docX2.write(fileOut);
        fileOut.close();
		            
	            System.out.println(".docx written successully");
	              
	}
	
	public void saveWordDoc() throws IOException
	{
		tfsem = semester.getText().toString();
	    tfsem = tfsem.toUpperCase();
	    tfsec = section.getText().toString();
	    tfsec = tfsec.toUpperCase();
	    
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
	      paragraphOneRunThree.setText("MARKS DISPLAY");
	      paragraphOneRunThree.addBreak();
	      
	      XWPFRun paragraphOneRunFour = paragraph.createRun();
	      paragraphOneRunFour.setBold(true);
	      paragraphOneRunFour.setText("(Subject: "+drop.getSelectionModel().getSelectedItem().toString().toUpperCase().trim());
	      paragraphOneRunFour.addBreak();
	      paragraphOneRunFour.addBreak();
	      paragraphOneRunFour.addBreak();
	      
	      XWPFParagraph paragraph1 = docX2.createParagraph();
	      paragraph1.setAlignment(ParagraphAlignment.LEFT);
	 
	     
	      
	      XWPFRun paragraphTwoRunOne = paragraph1.createRun();
	      paragraphTwoRunOne.setBold(true);
	      paragraphTwoRunOne.setText("Class: "+tfsem+tfsec);
	      
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
	  	run.setBold(true);run.setText("CIE ->");run.setFontSize(12);
	  	c2.removeParagraph(0);
	  	
	  	
	  	XWPFRun run1;
	  		XWPFTableCell cell4=tableRowOne.createCell();
		  	run1 = cell4.addParagraph().createRun();
		  	run1.setBold(true);run1.setText("CIE1");run1.setFontSize(12);
		  	cell4.removeParagraph(0);
		  	CTTcPr tcpr2 = cell4.getCTTc().addNewTcPr();
		  	CTHMerge vMerge2=tcpr2.addNewHMerge();
		  	vMerge2.setVal(STMerge.RESTART); 
		  	
		  	XWPFTableCell cell5=tableRowOne.createCell();
		  	CTTcPr tcpr3 = cell5.getCTTc().addNewTcPr();
		  	CTHMerge vMerge3=tcpr3.addNewHMerge();
		  	vMerge3.setVal(STMerge.CONTINUE);
		  	
		  	 cell4=tableRowOne.createCell();
		  	run1 = cell4.addParagraph().createRun();
		  	run1.setBold(true);run1.setText("CIE2");run1.setFontSize(12);
		  	cell4.removeParagraph(0);
		  	tcpr2 = cell4.getCTTc().addNewTcPr();
		  	vMerge2=tcpr2.addNewHMerge();
		  	vMerge2.setVal(STMerge.RESTART); 
		  	
		  	 cell5=tableRowOne.createCell();
		  	tcpr3 = cell5.getCTTc().addNewTcPr();
		  	vMerge3=tcpr3.addNewHMerge();
		  	vMerge3.setVal(STMerge.CONTINUE);
		  	
		  	 cell4=tableRowOne.createCell();
		  	run1 = cell4.addParagraph().createRun();
		  	run1.setBold(true);run1.setText("CIE3");run1.setFontSize(12);
		  	cell4.removeParagraph(0);
		  	tcpr2 = cell4.getCTTc().addNewTcPr();
		  	vMerge2=tcpr2.addNewHMerge();
		  	vMerge2.setVal(STMerge.RESTART); 
		  	
		  	 cell5=tableRowOne.createCell();
		  	tcpr3 = cell5.getCTTc().addNewTcPr();
		  	vMerge3=tcpr3.addNewHMerge();
		  	vMerge3.setVal(STMerge.CONTINUE);
	  	
		  	 cell4=tableRowOne.createCell();
			  	run1 = cell4.addParagraph().createRun();
			  	run1.setBold(true);run1.setText("Assignment");run1.setFontSize(12);
			  	cell4.removeParagraph(0);
			  	 cell4=tableRowOne.createCell();
				  	run1 = cell4.addParagraph().createRun();
				  	run1.setBold(true);run1.setText("AAT");run1.setFontSize(12);
				  	cell4.removeParagraph(0);
				  	 cell4=tableRowOne.createCell();
					  	run1 = cell4.addParagraph().createRun();
					  	run1.setBold(true);run1.setText("Total");run1.setFontSize(12);
					  	cell4.removeParagraph(0);
	  	
	  	
	  	
	  	XWPFTableRow tableRowOne2 = table.createRow();
	  	
	  	int twipsPerInch=1440;
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
	  	run1.setBold(true);run1.setText("");run1.setFontSize(12);
	  	c3.removeParagraph(0);
	  	
	  	for(int i=0;i<3;i++) {
	      c3 = tableRowOne2.addNewTableCell();
	      run1 = c3.addParagraph().createRun();
		  	run1.setBold(true);run1.setText("50");run1.setFontSize(12);
		  	c3.removeParagraph(0);
		  	c3 = tableRowOne2.addNewTableCell();
		      run1 = c3.addParagraph().createRun();
			  	run1.setBold(true);run1.setText("10");run1.setFontSize(12);
			  	c3.removeParagraph(0);
	  	}
	  	
	  	
	  	
	  	c3 = tableRowOne2.addNewTableCell();
	      run1 = c3.addParagraph().createRun();
		  	run1.setBold(true);run1.setText("");run1.setFontSize(12);
		  	c3.removeParagraph(0);
		  	c3 = tableRowOne2.addNewTableCell();
		      run1 = c3.addParagraph().createRun();
			  	run1.setBold(true);run1.setText("");run1.setFontSize(12);
			  	c3.removeParagraph(0);
			  	
			  	c3 = tableRowOne2.addNewTableCell();
			      run1 = c3.addParagraph().createRun();
				  	run1.setBold(true);run1.setText("");run1.setFontSize(12);
				  	c3.removeParagraph(0);
			  	
			  	
			  	
	  	
	     
	  	int[] cols = new int[3+(10*2)];
	    cols[0] = 8000;
	    cols[1] = 20000;
	    cols[2] = 20000;
	    for(int x=0; x<(10*2); x++)
	    	cols[3+x] = 8000;
		     
	      for(int i = 0; i < table.getNumberOfRows(); i++){ 
	            XWPFTableRow row = table.getRow(i); 
	            int numCells = row.getTableCells().size(); 
	            for(int j = 0; j < numCells; j++){ 
	                XWPFTableCell cell = row.getCell(j); 
	                cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(cols[j])); 
	            } 
	        }
	      
	      
	      int i=3;
	        for(int j = 0; j< studDetails_data.size();j++)
			{
        	  table.createRow();
	  	      table.getRow(i).createCell().setText(studDetails_data.get(j).getVal1());
		      table.getRow(i).createCell().setText(studDetails_data.get(j).getVal2());
		      table.getRow(i).createCell().setText(cie1data.get(j).getVal1());
		      table.getRow(i).createCell().setText(cie1data.get(j).getVal2());
		      table.getRow(i).createCell().setText(cie2data.get(j).getVal1());
		      table.getRow(i).createCell().setText(cie2data.get(j).getVal2());
		      table.getRow(i).createCell().setText(cie3data.get(j).getVal1());
		      table.getRow(i).createCell().setText(cie3data.get(j).getVal2());
		      table.getRow(i).createCell().setText(asnmtdata.get(j));
		      table.getRow(i).createCell().setText(aatdata.get(j));
		      table.getRow(i).createCell().setText(totaldata.get(j));
			
				i++;
			}
	       
	  	
	  	File path=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM\\Marks\\"+tfsem+tfsec+"consolidated"+".docx");
	  	FileOutputStream fileOut = new FileOutputStream(path);
       docX2.write(fileOut);
       fileOut.close();
		            
	            System.out.println(".docx written successully");
	}
	
	
	public void destroyWindow(ActionEvent e)
	{
		Stage primstage = (Stage) savespbtn.getScene().getWindow();
		primstage.close();
	}
	
	
	
	public void setDimen()
	{
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		 int width = (int) screenBounds.getWidth();
		 int height = (int) screenBounds.getHeight();
	        int utilsize = (width/100)*30;
	        int lab_size = (width/100)*50;
	        int allheights = height;
	        System.out.println(allheights);
	        utility_attend.setPrefWidth(utilsize);
	        utility_attend.setPrefHeight(allheights);
	        utility_attend.setMinHeight((allheights/100)*95);
	        
	        savespbtn.setPrefWidth((width/100)*20);
	        loadspbtn.setPrefWidth((width/100)*20);
	        savefir.setPrefWidth((width/100)*20);
	        syncsave.setPrefWidth((width/100)*20);
	        addTotalClasses.setPrefWidth((width/100)*20);
	        
	        
	        tpatt.setPrefWidth((width/100)*25);
	        saveonlinetitle.setPrefWidth((width/100)*25);
	        saveonlineanchor.setPrefWidth((width/100)*25);
	        saveonlineanchor.setPrefHeight((height/100)*30);
	        enterdetailsvbox.setPrefWidth((width/100)*25);
	        
	       titlecie.setPrefWidth(utilsize);
	       titlecie.setPrefHeight((height/100)*70);
	       anchorcie.setPrefWidth(utilsize);
	       anchorcie.setPrefHeight((height/100)*70);
	       vboxcie.setPrefWidth(utilsize);
	       vboxcie.setPrefHeight((height/100)*70);
	       
	       leftvbox.setPrefWidth(utilsize);
	       leftvbox.setPrefHeight((height/100)*70);
	       
	       
	       ap_attendance.setPrefWidth((width/100)*90);
	       ap_attendance.setPrefHeight((height/100)*80);

	       gianthbox.setPrefWidth((width/100)*100);
	       gianthbox.setPrefHeight((height/100)*80);

	       outerscreen.setPrefWidth((width/100)*90);
	       outerscreen.setPrefHeight((height/100)*80);
	       

	        innerscreen.setPrefWidth((width/100)*90);
	        innerscreen.setPrefHeight((height/100)*80);
	        System.out.println(width+" "+height);
	        
	        tableshbox.setPrefHeight(allheights);
	        tableshbox.setPrefWidth((width/100)*65);
	        
	        cie1Table.setMinWidth((width/100)*11);
	        cie2Table.setMinWidth((width/100)*11);
	        cie3Table.setMinWidth((width/100)*11);
	        asnmt_lv.setMinWidth((width/100)*4);
	        aat_lv.setMinWidth((width/100)*4);
	        total_lv.setMinWidth((width/100)*4);
	        studDetails_table.setMinWidth((width/100)*20);
	}
	   
	    
	public static class Person {
	   	 
        private final SimpleStringProperty val1;
        private final SimpleStringProperty val2;
        
        
        public Person(String val1, String val2) {
       
            this.val1 =new SimpleStringProperty(val1);
            this.val2 =new SimpleStringProperty(val2);
            
        }
        
        

		public void setStyle(String string) {
			// TODO Auto-generated method stub
			
		}

    	public String getVal1() {
        	
        	return val1.get();
        }

        public void setVal1(String u) {
            val1.set(u);
           
        }
        
        public String getVal2() {
        	
        	return val2.get();
        }

        public void setVal2(String u) {
            val2.set(u);
           
        }
        
        
       
	}

}
