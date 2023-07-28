package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class FilmManagement {
	
	private File selectedImage;
	
	@FXML
	private Button backBtn, homeBtn, uploadImageBtn, viewFilmsBtn, addFilmBtn;
	@FXML
	private Text newFilmTitle, newFilmDescription, newFilmTime1, newFilmTime2, newFilmTime3, newFilmAge, newFilmRating;
	@FXML
	private Label  newFilmStartDate, newFilmEndDate;
	@FXML
	private TextArea filmDescription;
	@FXML
	private DatePicker filmStartDate, filmEndDate;
	@FXML
	private TextField filmTitle, filmTrailer, filmRating;
	@FXML
	private ComboBox<String> filmTime1, filmTime2, filmTime3, filmAge;
	@FXML
	private ImageView uploadedFilmPoster;
	
	//Accessing the LinkedLists from the Main class to use in the FilmManagement class
	LinkedList<String> titles = Main.titles;
	LinkedList<String> startDates = Main.startDates;
	LinkedList<String> endDates = Main.endDates;
	LinkedList<String> time1 = Main.time1;
	LinkedList<String> time2 = Main.time2;
	LinkedList<String> time3 = Main.time3;
	
	@FXML
	void initialize() throws IOException{
		ObservableList<String> obsList1 = FXCollections.observableArrayList("13:00", "14:00", "15:00", "16:00", "17:00",
				"18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00");
		ObservableList<String> obsList2 = FXCollections.observableArrayList("U", "PG", "12A", "15", "18", "R");
		filmAge.setItems(obsList2);
		filmAge.setValue("12A");
		newFilmAge.setText("12A");
		filmTime1.setItems(obsList1);
		filmTime2.setItems(obsList1);
		filmTime3.setItems(obsList1);
		filmTime1.setValue("21:00");
		newFilmTime1.setText("21:00");
		filmStartDate.setValue(LocalDate.now());
		LocalDate startDate = filmStartDate.getValue();
		String startDateFormatted = startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		newFilmStartDate.setText(startDateFormatted);
	}
	
	public void goBack(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("Employee Home.fxml");
	}
	
	public void goHome(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("Employee Home.fxml");
	}
	
	public void goToViewFilms(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("View Films.fxml");
	}
	
	//Method that gets called every time the user enters a date, time or age when adding a new movie.
	@FXML
	public void updateDateTimeAge(ActionEvent e) {

		try {
			switch (((Node) e.getSource()).getId()) {
			case "filmStartDate":
				LocalDate startDate = filmStartDate.getValue();
				String startDateFormatted = startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				newFilmStartDate.setText(startDateFormatted);
				break;
			case "filmEndDate":
				LocalDate endDate = filmEndDate.getValue();
				String endDateFormatted = endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				newFilmEndDate.setText(endDateFormatted);
				break;
			case "filmTime1":
				newFilmTime1.setText(filmTime1.getValue().toString());
				break;
			case "filmTime2":
				newFilmTime2.setText(filmTime2.getValue().toString());
				break;
			case "filmTime3":
				newFilmTime3.setText(filmTime3.getValue().toString());
				break;
			case "filmAge":
				newFilmAge.setText(filmAge.getValue().toString());
				break;
			}
		} catch (NullPointerException ex) {
			ex.getMessage();
		}
	}
	
	
	//Method that gets called every time the user types in any TextField when adding a movie
	@FXML
	public void updateFilmText(KeyEvent e) {

		switch (((Node) e.getSource()).getId()) {
		case "filmTitle":
			if (filmTitle.getText().length() > 20) {
				filmTitle.setEditable(false);
			}
			break;
		case "filmDescription":
			if (filmDescription.getText().length() > 220) {
				filmDescription.setEditable(false);
			}
			break;
		case "filmRating":
			//String ratingRegex = "^([1-9]\\d*|0)(\\.\\d)?$";
			//Pattern p = Pattern.compile(ratingRegex)
			if (filmRating.getText().length() > 3) {
				filmRating.setEditable(false);
			}
			break;
		}

		if (e.getCode().equals(KeyCode.BACK_SPACE)) {
			filmTitle.setEditable(true);
			filmDescription.setEditable(true);
			filmRating.setEditable(true);
		}

		switch (((Node) e.getSource()).getId()) {
		case "filmTitle":
			newFilmTitle.setText(filmTitle.getText());
			break;
		case "filmDescription":
			newFilmDescription.setText(filmDescription.getText());
			break;
		case "filmRating":
			newFilmRating.setText(filmRating.getText()+"/10");
			break;
		}
	}
	
	//Method that gets called when the user clicks on the upload image button
	@FXML
	public void uploadImage(ActionEvent event) throws IOException {

		try {
			FileChooser fc = new FileChooser();
			selectedImage = fc.showOpenDialog(null);
			// checking that input file is not null and handling the exception
			if (selectedImage == null) {
				return;
			}
			else if (ImageIO.read(selectedImage) == null) {
				Alert alert = new Alert(AlertType.WARNING, "Please upload an image in JPG or PNG format!",
						ButtonType.OK);
				alert.showAndWait();
				if (alert.getResult() == ButtonType.OK) {
					return;
				}
			} else {
				Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedImage), null);
				uploadedFilmPoster.setImage(img);
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	public void storeFilmInfo(ActionEvent event) throws ParseException {

		try {
			validateFilmInput();
			
			FileWriter fw = new FileWriter("films.txt", true);
			//fw.append(fileHeader.toString());
			fw.write(newFilmTitle.getText()+";"+newFilmDescription.getText()+";"+filmTrailer.getText()+";"+newFilmStartDate.getText()+
					";"+newFilmEndDate.getText()+";"+newFilmTime1.getText()+";"+newFilmTime2.getText()+";"+newFilmTime3.getText()+";"+
					newFilmAge.getText()+";"+newFilmRating.getText()+"\n");
			fw.close();

			// storing film poster in film images folder
			String folderPath = "/Users/rubenodamo/eclipse-workspace/ClubhouseCinemas/Images";
			File uploads = new File(folderPath);
			File file = new File(uploads, filmTitle.getText() + ".png");
			InputStream input = Files.newInputStream(selectedImage.toPath());
			Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			// confirmation alert to inform the employee of the newly added film
			Alert alert = new Alert(AlertType.INFORMATION, "The film " + filmTitle.getText() + " has been added!",
					ButtonType.OK);
			alert.showAndWait();

			// reloading film list to include the recently added film, and
			// restoring all fields to empty
			// and closing alert on click
			if (alert.getResult() == ButtonType.OK) {
				filmDescription.setText("");
				filmTrailer.setText("");
				filmTitle.setText("");
				filmStartDate.setPromptText("dd/MM/yyyy");
				filmEndDate.setPromptText("dd/MM/yyyy");
				filmTime1.setPromptText("HH:mm");
				filmTime2.setPromptText("HH:mm");
				filmTime3.setPromptText("HH:mm");
				filmAge.setPromptText("");
				filmRating.setText("");
				alert.close();
			}
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.WARNING, "File Not Found!", ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.WARNING, "Error: " + e.getMessage(), ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		} catch (InvalidFilmInputException e) {
			Alert alert = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		}
	}
	
	//Method 'validateFilmInput()' checks if the employee is adding a film that is unique, ensuring none of the fields are duplicate
	@SuppressWarnings("unlikely-arg-type")
	void validateFilmInput() throws InvalidFilmInputException, ParseException { 
		try {
			if (filmTitle.getText().equals("") || filmDescription.getText().equals("")
					|| filmTrailer.getText().equals("") || filmStartDate.getValue().equals("dd/MM/yyyy")
					|| filmEndDate.getValue().equals("dd/MM/yyyy") || filmAge.getValue().equals("") || filmRating.getText().equals(""))
				throw new InvalidFilmInputException("Please complete all fields!");
			else if (selectedImage == null)
				throw new InvalidFilmInputException("Please add the film poster!");
			else if (filmStartDate.getValue().compareTo(LocalDate.now()) < 0)
				throw new InvalidFilmInputException("Start date cannot be before today!");
			else if (filmStartDate.getValue().compareTo(filmEndDate.getValue()) == 0)
				throw new InvalidFilmInputException("Screenings cannot start and end on the same day!");
			else if (filmStartDate.getValue().compareTo(filmEndDate.getValue()) > 0)
				throw new InvalidFilmInputException("End date cannot be before start date!");
			
			// checking that the title of the movie is unique
			for (int i=0; i<titles.size();i++) {
				if (titles.get(i).equals(filmTitle.getText()))
					throw new InvalidFilmInputException(
							"The title " + filmTitle.getText() + " belongs to another scheduled movie!");
			}

			// looping through the films to find date and time conflicts
			for (int i=0; i<titles.size();i++) {

				// converting movie start and end dates to LocalDate for
				// comparison
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate startDateFilms = LocalDate.parse(startDates.get(i), formatter);
				LocalDate endDateFilms = LocalDate.parse(endDates.get(i), formatter);

				// if the dates overlap...
				if (!(filmStartDate.getValue().compareTo(endDateFilms) > 0
						|| filmEndDate.getValue().compareTo(startDateFilms) < 0)) {

					// ... and the time(s) overlap as well
					if (time1.get(i).equals(filmTime1.getValue()) || time2.get(i).equals(filmTime2.getValue()) ||
							time3.get(i).equals(filmTime3.getValue())) {
						throw new InvalidFilmInputException("The screening time(s) of your film: " + filmTitle.getText()
								+ " overlap(s) with another film!");
					}
				}
			}
		} catch (NullPointerException e) {
			throw new InvalidFilmInputException("Please complete all fields!");
		}
	}
}

//InvalidFilmInputException class (subclass) inherits the attributes and methods from the Exception class (superclass)
class InvalidFilmInputException extends Exception {
// (...)
	
	private static final long serialVersionUID = 1L;

	InvalidFilmInputException(String s) {
		super(s);
	}
}
