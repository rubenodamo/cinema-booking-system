package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Confirmation implements Initializable {

	@FXML
	Text firstname, surname, email, datetime, screen, seats;
	
	@FXML
	Text selectedFilmTitle, adult, child, senior, isVip, total;
	
	@FXML
	ImageView selectedFilmPoster;
	
	@FXML
	Button homeBtn, emailBtn;
	
	String selectedFilm = "", date = "", time = "";
	File imgFile = null;
	
	public static String name = "", finalDate = "", finalTime = "", vipConf = "";
	public static int min = 0;
    public static int max = 9999;
	public static int id = (int)Math.floor(Math.random()*(max-min+1)+min);
	public static String bookingId = "CLUBH"+Integer.toString(id);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectedFilmTitle.setText(Main.getSelectedFilmTitle());
		email.setText(Login.getCurrentUser());
		screen.setText(TicketBooking.screenNum);
		seats.setText(SeatBooking.userSeats);
		
		date = TicketBooking.date;
		time = TicketBooking.time;
		datetime.setText(date+" @ "+time);
		finalDate = date;
		finalTime = time;
		
		adult.setText(TicketBooking.adultTickets + "");
		child.setText(TicketBooking.childTickets + "");
		senior.setText(TicketBooking.seniorTickets + "");
		//Checks if the user selected the VIP seating. 
		if(TicketBooking.isVip) {
			isVip.setText("Yes");
			vipConf = "Yes";
		} else {
			isVip.setText("No");
			vipConf = "No";
		}
		total.setText("£"+String.format("%.2f", TicketBooking.total));
		String email = Login.getCurrentUser();
		selectedFilm = Main.getSelectedFilmTitle();
		try {
			FileReader fr = new FileReader("Registration details.txt");
			BufferedReader br=new BufferedReader(fr);
			String line=br.readLine();
			while(line!=null) {
				String[] data=line.split(";");
				if(data[2].equals(email))
				{
					firstname.setText(data[0]);
					surname.setText(data[1]);
					name = data[0]+" "+data[1];
				}
				line=br.readLine();
			}
			fr.close();
			String path = "/Users/rubenodamo/eclipse-workspace/ClubhouseCinemas/Images/filmImages/";
	        //System.out.println(path + selectedFilm.getTitle() + ".png");
	    	imgFile = new File(path +selectedFilm+ ".png");
			Image img = SwingFXUtils.toFXImage(ImageIO.read(imgFile), null);
	        selectedFilmPoster.setImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileWriter fw = new FileWriter("bookings.txt", true);
			//fw.append(fileHeader.toString());
			fw.write("booked"+";"+firstname.getText()+";"+surname.getText()+";"+selectedFilmTitle.getText()+
					";"+date+";"+time+";"+seats.getText()+";"+isVip.getText()+";"+bookingId+"\n");
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Method 'emailConfirmation()' is executed when the user clicks on the 'EMAIL' buttoon
	public void emailConfirmation(ActionEvent event) throws IOException{ 
		//Outputs confirmation alert
		Alert alert = new Alert(AlertType.CONFIRMATION, "Would you like a confirmation to be emailed to you?",
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		//IF the user selects "yes" a booking confirmation email is sent
		if(alert.getResult() == ButtonType.YES) {
			SendEmail.sendEmail(Login.getCurrentUser(), "confirmation");
			alert.close();
		}
		//ELSE the alert is closed
		else {
			alert.close();
			return;
		}
	}
	
	public void goHome(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("View Films.fxml");
	}

}
