package application;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


@SuppressWarnings("unused")
public class Main extends Application {
	
	static Parent root;
	static Stage primaryStage;
	
	private static Stage stg;
	static Boolean employeeMode = false;
	static String selectedFilmTitle = "";
	
	//Creates LinkedLists of type String for titles, startDates, endDates, time1, time2 and time3
	public static LinkedList<String> titles=new LinkedList<String>();
	public static LinkedList<String> startDates = new LinkedList<String>();
	public static LinkedList<String> endDates = new LinkedList<String>();
	public static LinkedList<String> time1=new LinkedList<String>();
	public static LinkedList<String> time2 = new LinkedList<String>();
	public static LinkedList<String> time3 = new LinkedList<String>();

	static HashSet<BookingHistoryItem> bookings = new HashSet<BookingHistoryItem>();
	
	@Override
	public void start(Stage primaryStage) throws IOException{
		stg = primaryStage;
		primaryStage.setResizable(false);
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Clubhouse Cinemas");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();

	}
	
	//Method 'changeScene()' allows any FXML window to be opened, that is integrated within the application window
	public void changeScene(String fxml) throws IOException {
		Parent pane = FXMLLoader.load(getClass().getResource(fxml)); //Defines the variable 'pane' of the FXML window wanted
		stg.getScene().setRoot(pane); //Sets the stage as the new pane
		stg.sizeToScene(); //Resizes the stage
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	static HashSet<BookingHistoryItem> getBookingList() {
		return bookings;
	}
	
	static void resetBookingList() {
		bookings.clear();
	}
	
	static boolean isEmployee() {
		return employeeMode;
	}
	static void setEmployeeMode(boolean employeeMode) {
		Main.employeeMode = employeeMode;
	}
	
	//Method filereading() reads 'films.txt' file and adds respective data to each LinkedList
	public static void filereading() throws IOException {
		//Creates FileReader and BufferedReader to be able to read the file
		FileReader fr=new FileReader("films.txt");
		BufferedReader br=new BufferedReader(fr);
		String line=br.readLine(); //Reads the first line of the file
		while(line!=null) { //WHILE loop to iterate through the lines of the file
			String[] data=line.split(";"); //Assings a semi-colon as the data delimiter
			//Adds data values to their respective lists
			titles.add(data[0]);
			startDates.add(data[3]);
			endDates.add(data[4]);
			time1.add(data[5]);
			time2.add(data[6]);
			time3.add(data[7]);
			line=br.readLine(); //Reads the next line
		}	
	}

	
	static void setSelectedFilmTitle(String selectedFilmTitle) {
		Main.selectedFilmTitle = selectedFilmTitle;
	}
	static String getSelectedFilmTitle() {
		return selectedFilmTitle;
	}
	
    static Parent getRoot() {
        return root;
    }

    static void setRoot(Parent root) {

        Main.root = root;
    }

    static Stage getStage() {

        return primaryStage;
    }

    static void setStage(Stage stage) {

        Main.primaryStage = stage;
    }
	

}
