package application;

import java.io.IOException;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SeatBooking {

	@FXML
	private Button backBtn, confirmationBtn;
	@FXML
	private Button A1, A2, A3, A4, A5, A6, A7, A8, B1, B2, B3, B4, B5, B6, B7, B8, C1, C2, C3, C4, C5, C6, C7, C8;
	
	//Instantiates array variables
	private Button[] seats = new Button[24];
	public static boolean[] bookings = new boolean[24];
	public static boolean[] booked;
	
	private int maxSeats = (TicketBooking.adultTickets + TicketBooking.childTickets + TicketBooking.seniorTickets);
	private int numberOfSeats = 0;
	public static String userSeats = "";
	
	private boolean rotatedpane = false;
	public static boolean seatsSelected = false;
	
	@FXML
	private Text filmName, totalTickets, totalPrice;
	
	@FXML
	private Label selectedSeats;
	
	
	@FXML
	void initialize() throws IOException{
		selectedSeats.setText("");
		booked = new boolean[24];
		initialiseArray();
		setUpSeats();
		filmName.setText(Main.getSelectedFilmTitle());
		totalTickets.setText(""+maxSeats);
		totalPrice.setText("£"+String.format("%.2f", TicketBooking.total));
		
	}
	
	/*
	 * Method 'setUpSeats()' constructs the visual outlook of the cinema floorplan giving seats a colour depending on availability and selection
	 * available = #edf0f4	selected = #23b33b	unavailable = #e40606 
	 */
	private void setUpSeats() {
		for(int i=0; i<seats.length; i++){ //FOR loop - iterates through arrays
            if(bookings[i]==false){ //IF the seat is available...
                seats[i].setStyle("-fx-background-color:  #edf0f4"); //Gives button a GREY colour
                int finalI1 = i;

                seats[i].setOnAction(event -> { //Using a lambda expression it acts as an action listener executing the code
                    if(booked[finalI1]==false){ //IF the seat is selected...
                    	//Checks user hasn't booked too many seats
                        if(numberOfSeats<maxSeats){ 
                            numberOfSeats++;
                            seats[finalI1].setStyle("-fx-background-color:  #23b33b"); //Gives button a GREEN colour
                            setBookedSeats(seats[finalI1], true);
                        }else { //Outputs error message
                        	Alert alert = new Alert(AlertType.WARNING, "Error: Maximum seat limit reached!",
            						ButtonType.OK);
            				alert.showAndWait();
            				if (alert.getResult() == ButtonType.OK) {
            					return;
            				}
                        }
                    }
                    else if(booked[finalI1]==true){ //IF the user unselects a seat...
                        numberOfSeats--; //Subtracts 1 from the number of selected seats
                        seats[finalI1].setStyle("-fx-background-color:  #edf0f4");//Gives button a GREY colour
                        setBookedSeats(seats[finalI1], false);
                    }
                    popSeat(seats[finalI1]);
                });
            }else if(bookings[i]==true){ //IF the seat is unavailable...
                seats[i].setStyle("-fx-background-color:  #e40606"); //Gives button a RED colour
                int finalI = i;
                seats[i].setOnAction(event -> rotateButton(seats[finalI]));
            }
        }
	}
	
	//Method 'popSeat()' performs the animation functions when a seat button is selected
	private void popSeat(Button btn) {
		ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setToX(1.2); //Sets the final X position of the button
        st.setToY(1.2); //Sets the final X position of the button
        st.setRate(1.5); //Sets the speed of the animation
        st.setCycleCount(1); //Sets the number of cycles in the animation
        st.play(); //Executes the button scalling
        
        
        st.setOnFinished(event -> { //Using a lambda expression it acts as an action listener executing the code
            ScaleTransition st2 = new ScaleTransition(Duration.millis(200), btn);
            st2.setToX(1);
            st2.setToY(1);
            st2.setRate(1.5);
            st2.setCycleCount(1);
            st2.play();
        });	
	}
	
	private void setBookedSeats(Button btn, boolean selected) {
		int seat = 0;
		if(btn.getId().startsWith("A")) {
			seat = (Integer.parseInt(btn.getId().substring(1)))-1;
		}
		else if(btn.getId().startsWith("B")) {
			seat = (Integer.parseInt(btn.getId().substring(1)))+7;
		}
		else if(btn.getId().startsWith("C")) {
			seat = (Integer.parseInt(btn.getId().substring(1)))+15;
		}
		booked[seat] = selected;
		String btnid = btn.getId();
		String s = selectedSeats.getText();
		if(selected==true) {
			if(s.isEmpty()) {
				selectedSeats.setText(btnid);
			}
			else {
				selectedSeats.setText(s+", "+btnid);
			}
		}
		else {
			if(s.startsWith(btnid) && s.length()==2) {
				selectedSeats.setText(s.replaceAll(btn.getId(), ""));
			}
			else if(s.startsWith(btnid) && s.length()>2) {
				selectedSeats.setText(s.replaceAll(btn.getId()+", ", ""));
			}
			else {
				selectedSeats.setText(s.replaceAll(", "+btn.getId(), ""));
			}
		}
		
	}
	
	//Method 'rotateButton()' performs the animation functions when a seat button is selected
    public void rotateButton(Button btn){
        if(rotatedpane ==false){
            rotatedpane =true;
            RotateTransition rt=new RotateTransition(Duration.millis(60),btn);
            rt.setByAngle(45); //Sets the angle the button will rotate by
            rt.setCycleCount(2); //Sets the number of cycles in the animation
            rt.setAutoReverse(true); //Sets auto reverse flag to make animation run back and forth
            rt.play(); //Executes the button rotation
            
            //When animation is complete, returns the button back to original position
            rt.setOnFinished(event -> { //Using a lambda expression it acts as an action listener executing the code
                RotateTransition rt2=new RotateTransition(Duration.millis(60),btn);
                rt2.setByAngle(-45);
                rt2.setCycleCount(2);
                rt2.setAutoReverse(true);
                rt2.play();
                rt2.setOnFinished(event1 -> rotatedpane =false);
            });
        }
    }
    
    //Method initialises 'Button[] seats' array giving each index a unique seat ID
	private void initialiseArray() {
		seats[0]=A1;
		seats[1]=A2;
		seats[2]=A3;
		seats[3]=A4;
		seats[4]=A5;
		seats[5]=A6;
		seats[6]=A7;
		seats[7]=A8;
		
		seats[8]=B1;
		seats[9]=B2;
		seats[10]=B3;
		seats[11]=B4;
		seats[12]=B5;
		seats[13]=B6;
		seats[14]=B7;
		seats[15]=B8;
		
		seats[16]=C1;
		seats[17]=C2;
		seats[18]=C3;
		seats[19]=C4;
		seats[20]=C5;
		seats[21]=C6;
		seats[22]=C7;
		seats[23]=C8;
	}
	
	public void goBack(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("Ticket Booking.fxml");
	}
	
	public void goToConfirmation(ActionEvent event) throws IOException{
		if(numberOfSeats == maxSeats) {
			seatsSelected = true;
			userSeats = selectedSeats.getText();
			Main m = new Main();
			m.changeScene("Confirmation Page.fxml");
		}
		else {
			Alert alert = new Alert(AlertType.WARNING, "Error: Select all seats!",
					ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				return;
			}
		}
	}
}
