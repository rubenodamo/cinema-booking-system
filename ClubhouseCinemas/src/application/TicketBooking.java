package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class TicketBooking {
	
	@FXML
	private Button backBtn, homeBtn, seatsBtn;
	@FXML
	private DatePicker selectedDate;
	@FXML
	private ComboBox<String> adultcombo, childcombo, seniorcombo, filmTimes;
	@FXML
	private Text selectedFilmTitle, totalPrice;
	@FXML
	private TextField screen;
	@FXML
	private CheckBox checkbox;
	@FXML
	private ImageView selectedFilmPoster;
	
	public static double total = 0.00;
	public static int adultTickets = 0, childTickets = 0, seniorTickets = 0;
	private double adultPrice = 8.50, childPrice = 5.00, seniorPrice = 7.50, vip = 2.00;
	public static boolean isVip = false;
	public static String screenNum = "", date = "", time = "";
	
	String selectedFilm = "";
	File imgFile = null;
	
	@FXML
	void initialize() throws IOException {
		selectedFilm = Main.getSelectedFilmTitle(); //Assigns the film chosen by the user to the 'selectedFilm' variable
		
		//Initialises the ObservableLists
		ObservableList<String> times = FXCollections.observableArrayList();
		ObservableList<String> numberOfTickets = FXCollections.observableArrayList("0","1","2","3","4","5","6","7","8");
		ObservableList<String> validation = FXCollections.observableArrayList("0");
		
		//Creates 'String' variables for 'endDate' and 'age'
		String endDate = "";
		String age = "";
		
		//Creates FileReader and BufferedReader to be able to read the file
		FileReader fr = new FileReader("films.txt");
		BufferedReader br=new BufferedReader(fr);
		String line=br.readLine(); //Reads the first line of the file
		while(line!=null) { //WHILE loop to iterate through the lines of the file
			String[] data=line.split(";"); //Assings a semi-colon as the data delimiter
			//Finds data associated with the chosen film and assigns the film details to corresponding variables
			if(data[0].equals(selectedFilm))
			{
				selectedFilmTitle.setText(data[0]);
				age = data[8];
				times = FXCollections.observableArrayList(data[5],data[6],data[7]);
				endDate = data[4];
			}
			line=br.readLine(); //Reads the next line
		}
		
		//Formats the DatePicker so the user cannot pick a date before today's  date or after the end date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate today = LocalDate.now();
		LocalDate end = LocalDate.parse(endDate, formatter);
		final Callback<DatePicker, DateCell> dayCellFactory = 
	            new Callback<DatePicker, DateCell>() {
	                @Override
	                public DateCell call(final DatePicker selectedDate) {
	                    return new DateCell() {
	                        @Override
	                        public void updateItem(LocalDate item, boolean empty) {
	                            super.updateItem(item, empty);
	                           
	                            if (item.isBefore(today) || item.isAfter(end)) {
	                                    setDisable(true);
	                                    setStyle("-fx-background-color: #ffc0cb;");
	                            }   
	                        }
	                    };
	                }
	        	};
	    selectedDate.setDayCellFactory(dayCellFactory);
	    selectedDate.setValue(LocalDate.now());
	    
		filmTimes.setItems(times);
		adultcombo.setItems(numberOfTickets);
		if(age.equals("15") || age.equals("18")) {
			childcombo.setItems(validation);
		}
		else {
			childcombo.setItems(numberOfTickets);
		}
		seniorcombo.setItems(numberOfTickets);
		adultcombo.setValue("0");
		childcombo.setValue("0");
		seniorcombo.setValue("0");
		int min = 1;
	    int max = 3;
		int screenNo = (int)Math.floor(Math.random()*(max-min+1)+min);
		screenNum = Integer.toString(screenNo);
		screen.setText(screenNum);
		
		String path = "/Users/rubenodamo/eclipse-workspace/ClubhouseCinemas/Images/filmImages/";
        //System.out.println(path + selectedFilm.getTitle() + ".png");
    	imgFile = new File(path +selectedFilm+ ".png");
        Image img = SwingFXUtils.toFXImage(ImageIO.read(imgFile), null);
        selectedFilmPoster.setImage(img);
        checkbox.setIndeterminate(true);
	}
	
	public void updateTotal(ActionEvent e) throws NumberFormatException{
		adultTickets = Integer.parseInt((String)adultcombo.getValue());
		childTickets = Integer.parseInt((String)childcombo.getValue());
		seniorTickets = Integer.parseInt((String)seniorcombo.getValue());
		total = (adultTickets * adultPrice)+(childTickets * childPrice)+(seniorTickets * seniorPrice);
		if(checkbox.isSelected()) {
			total = total + vip;
			isVip = true;
		}
		totalPrice.setText("£" + (String.format( "%.2f", total)));
	}
	
	public void goSelectSeats(ActionEvent event) throws IOException{
		if((adultTickets + childTickets +seniorTickets) == 0) {
			Alert alert = new Alert(AlertType.WARNING, "Error: Select a ticket!",
					ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				return;
			}
		}
		else if((filmTimes.getValue().toString()).equals("")) {
			Alert alert = new Alert(AlertType.WARNING, "Error: Select a time!",
					ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				return;
			}
		}
		else {
			LocalDate pickedDate = selectedDate.getValue();
			String pickedDateFormatted = pickedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			date = pickedDateFormatted;
			time = filmTimes.getValue().toString();
			
			Main m = new Main();
			m.changeScene("Seat Booking.fxml");
		}
	}
	
	public void goBack(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("Film Page.fxml");
	}
	
	public void goHome(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("View Films.fxml");
	}
}
