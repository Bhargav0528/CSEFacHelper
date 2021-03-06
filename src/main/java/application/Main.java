package application;
import java.lang.*;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.log4j.BasicConfigurator;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			String filename = Main.class.getProtectionDomain().getCodeSource().getLocation()
				    .toURI().getPath().toString();

	     int lastIndex=filename.lastIndexOf('/');
		filename = filename.substring(0,filename.lastIndexOf("/"));
			System.out.println(filename);
			BasicConfigurator.configure();
			FileInputStream serviceAccount =
					  new FileInputStream(filename+"/serviceAccountKey.json");

					FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
					  .setDatabaseUrl("https://fachelper-9147f.firebaseio.com")
					  .build();

					FirebaseApp.initializeApp(options);
			
			
			String rootpath = "C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM";
			Path SDMPath = Paths.get("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SDM");
			if(Files.notExists(SDMPath))
			{
				new File(rootpath+"\\StudentData").mkdirs();
				new File(rootpath+"\\Lab").mkdir();
				new File(rootpath+"\\Marks").mkdir();
				new File(rootpath+"\\Attendance").mkdir();
			}
			Path studdat = Paths.get(rootpath+"\\StudentData");
			Path lab = Paths.get(rootpath+"\\Lab");
			Path marks = Paths.get(rootpath+"\\Marks");
			Path Attendance = Paths.get(rootpath+"\\Attendance");
			Path FacInfo = Paths.get(rootpath+"\\FacInfo");
			if(Files.notExists(studdat))
			{
				new File(rootpath+"\\StudentData").mkdir();
			}
			
			if(Files.notExists(lab))
			{
				new File(rootpath+"\\Lab").mkdir();
			}
			if(Files.notExists(marks))
			{
				new File(rootpath+"\\Marks").mkdir();
			}
			if(Files.notExists(Attendance))
			{
				new File(rootpath+"\\Attendance").mkdir();
			}
			if(Files.notExists(FacInfo))
			{
				new File(rootpath+"\\FacInfo").mkdir();
			}
			
			
			Parent root=FXMLLoader.load(getClass().getClassLoader().getResource("SignUp.fxml.fxml"));
			//Parent root=FXMLLoader.load(getClass().getClassLoader().getResource("Marks.fxml"));
			
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			Screen screen = Screen.getPrimary();
			
			 int width = (int) screenBounds.getWidth();
		     width = (width/100)*38;
		     
		     int height = (int) screenBounds.getWidth();
		      height = (height/100)*30;
			
			//primaryStage.setMaximized(true);
		      primaryStage.initStyle(StageStyle.UNDECORATED);
			Scene scene = new Scene(root,width,height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setMaximized(true);
			primaryStage.setTitle("Main");
			primaryStage.show();
			
			
			/* primaryStage = new Stage();
			Parent root=FXMLLoader.load(getClass().getResource("Marks.fxml"));
			Screen screen = Screen.getPrimary();
			//BorderPane root = new BorderPane();
			
			
			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
			Scene scene = new Scene(root,screenBounds.getWidth()/2, screenBounds.getHeight()/2);
			primaryStage.setMaximized(true);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Student Setup");
			primaryStage.show();
			*/
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
