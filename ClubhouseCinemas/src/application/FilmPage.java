package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class FilmPage implements Initializable {
	
	String selectedFilm = "";
	File imgFile = null;
	Desktop desktop = Desktop.getDesktop();
	
	@FXML
	private Button backBtn, bookBtn, deleteFilmBtn;
	@FXML
	private ImageView selectedFilmPoster;
	@FXML
	private Text title;
	@FXML
	private Text description;
	@FXML
	private Text startDate;
	@FXML
	private Text endDate;
	@FXML
	private Text time;
	@FXML
	private Text age;
	@FXML
	private Text rating;
	
	Main m1=new Main();
	ViewFilms vf=new ViewFilms();
	String id = vf.getId();
	
	
	 	@Override
	    public void initialize(URL location, ResourceBundle resources) {	

	        if(Main.isEmployee()) {
	        	bookBtn.setText("SEE BOOKINGS");
	        	//System.out.println(id);
	        }
	        selectedFilm = Main.getSelectedFilmTitle();
			try {
				FileReader fr = new FileReader("films.txt");
				BufferedReader br=new BufferedReader(fr);
				String line=br.readLine();
				while(line!=null) {
					String[] data=line.split(";");
					if(data[0].equals(selectedFilm))
					{
						title.setText(data[0]);
						description.setText(data[1]);
						startDate.setText(data[3]);
						endDate.setText(data[4]);
						time.setText(data[5]+", "+data[6]+", "+data[7]);
						age.setText(data[8]);
						rating.setText(data[9]);
					}
					line=br.readLine();
				}
				
				String path = "/Users/rubenodamo/eclipse-workspace/ClubhouseCinemas/Images/filmImages/";
	            //System.out.println(path + selectedFilm.getTitle() + ".png");
	        	imgFile = new File(path +selectedFilm+ ".png");
	            Image img = SwingFXUtils.toFXImage(ImageIO.read(imgFile), null);
	            selectedFilmPoster.setImage(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (!Main.isEmployee()) {
	        	deleteFilmBtn.setVisible(false);
	        }
	            


	        selectedFilmPoster.setOnMouseClicked((event) -> {

	            try {
	            	FileReader fr = new FileReader("films.txt");
					BufferedReader br=new BufferedReader(fr);
					String line=br.readLine();
					String trailer ="";
					while(line!=null) {
						String[] data=line.split(";");
						if(data[0].equals(selectedFilm))
						{
							trailer = data[2];
						}
						line=br.readLine();
					}
	                desktop.browse(new URI(trailer));
	            } catch (IOException | URISyntaxException e) {
	            }
	        });
	 	}
	 	
	 
	    //Extra feature that allows the user to delete a movie from the list
	    @FXML
	    public void deleteFilm(ActionEvent event) throws IOException {
	    	selectedFilm = Main.getSelectedFilmTitle();

	        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to delete this movie?", ButtonType.NO, ButtonType.YES);
	        alert.showAndWait();

	        if(alert.getResult() == ButtonType.YES) {

	            for (BookingHistoryItem booking : Main.getBookingList()) {
	                
	                // if there is a booking for the selected film
	                // and if the booking's date is in the future
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	                if (booking.getFilm().equals(Main.getSelectedFilmTitle()) && !booking.getStatus().equals("cancelled") && LocalDate.parse(booking.getDate(), formatter).compareTo(LocalDate.now()) >= 0) {
	                    Alert existingBookingAlert = new Alert(AlertType.WARNING, "You cannot delete a film with future bookings!", ButtonType.OK);
	                    existingBookingAlert.showAndWait();
	                    if (existingBookingAlert.getResult() == ButtonType.OK) {
	                        existingBookingAlert.close();
	                        return;
	                    }
	                }
	            }
	            
	            // if there are no future booking for the selected film
	            // the employee can safely delete it
	            
	            imgFile.delete();
	            
	            String tempFile = "temp.txt";
	            File oldFile = new File("films.txt");
	            File newFile = new File("temp.txt");
	            
	            String currentLine;
	            String data[];
	            
	            FileWriter fw = new FileWriter(tempFile, true);
	            BufferedWriter bw = new BufferedWriter(fw);
	            PrintWriter pw = new PrintWriter(bw);
	            
	            FileReader fr = new FileReader("films.txt");
	            BufferedReader br = new BufferedReader(fr);
	            
	            while((currentLine = br.readLine()) != null) {
	            	data = currentLine.split(";");
	            	if(!(data[0].equals(selectedFilm))) {
	            		pw.println(currentLine);
	            	}
	            }
	            
	            pw.flush();
	            pw.close();
	            fr.close();
	            br.close();
	            
	            oldFile.delete();
	            File dump = new File("films.txt");
	            newFile.renameTo(dump);
	            

	            Main.filereading();

	            goBack(event);
	        }
	        else {
	            return;
	        }
	    }
	
	public void goBack(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("View Films.fxml");
	}
	
    public void goToBookingScene(ActionEvent event) throws IOException {
    	if(Main.isEmployee()){
			Main m = new Main();
			m.changeScene("Booking Management.fxml");
		}
    	
		if(!Main.isEmployee()){
			Main m = new Main();
			m.changeScene("Ticket Booking.fxml");
		}
		
    }
}
