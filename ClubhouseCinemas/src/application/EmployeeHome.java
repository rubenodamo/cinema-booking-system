package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.util.Pair;

public class EmployeeHome {
	
	@FXML
	private Button logoutBtn, manageFilmsBtn, exportBookingsBtn;
	
	//Method 'exportBookings()' creates a saveable 'bookings exported.txt' file
	public void exportBookings(ActionEvent event) throws IOException{
		
		//Employee chooses a folder in whcih they want to save the newly exported file
		DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("Select folder:");
        File defaultDirectory = new File(".");
        folderChooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = folderChooser.showDialog(null);

        //IF the employee clicks on cancel
        if (selectedDirectory == null) {
        	return;
        }

        //Clearing the export file, in case it exists from before
        PrintWriter pw = new PrintWriter(new FileOutputStream(
                new File(selectedDirectory.toPath() + "/bookings exported.txt")));
        pw.close();

        //Creating the printwriter using the append option now
        pw = new PrintWriter(new FileOutputStream(
                new File(selectedDirectory.toPath() + "/bookings exported.txt"), 
                true));
        
        //Creates FileReader and BufferedReader to be able to read the file
        FileReader fr = new FileReader("bookings.txt");
		BufferedReader br=new BufferedReader(fr);
		String line=br.readLine(); //Reads the first line of the file
		while(line!=null) { //WHILE loop to iterate through the lines of the file
			String[] data=line.split(";"); //Assings a semi-colon as the data delimiter
			pw.append(line+"\n"); //Appends (adds) line to the 'bookings exported.txt' file
			line=br.readLine(); //Reads the next line
		}
        
		pw.close(); //Closes PrintWriter
		
		//Creates message of the completed task
        Alert alert = new Alert(AlertType.INFORMATION, "The bookings.txt file has been exported!",
				ButtonType.OK);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			return;
		}
	}
	
	public void employeeLogout(ActionEvent event) throws IOException{
		Main m = new Main();
		Main.setEmployeeMode(false);
		m.changeScene("Login.fxml");
	}
	
	public void goToFilmManagement(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("Film Management.fxml");
	}
}
