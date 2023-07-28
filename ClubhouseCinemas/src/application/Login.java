package application;


import java.util.*;
import java.io.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login {
	
	@FXML
	private Button loginBtn;
	@FXML
	private Label invalidEntry;
	@FXML
	private TextField email;
	@FXML
	private PasswordField password;
	@FXML
	private Hyperlink regLink;
	
	static String currentUser = "";
	
	public void userLogin(ActionEvent event) throws IOException{
		checkLogin();
	}
	
	//When the 'Sign up here' Hyperlink is pressed it transitions to the 'Sign Up.fxml' page
	public void goToReg(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("Sign Up.fxml");
	}
	
	//Method 'checkLogin()' checks the login details of the user
	@SuppressWarnings("unused")
	private void checkLogin() throws IOException{
		Main m = new Main();
		
		//IF the user is an employee the program transitions to the 'Employee Home.fxml' page
		if(email.getText().toString().equals("clubhousecinemas@gmail.com") && password.getText().toString().equals("123456")) {
			Main.setEmployeeMode(true);
			m.changeScene("Employee Home.fxml");
		}
		// (...)
		
		//ELSE IF any of the entry fields are empty an error message is displayed
		else if(email.getText().isEmpty() || password.getText().isEmpty()) {
			if(email.getText().isEmpty() && password.getText().isEmpty()){
				invalidEntry.setText("*Please enter email and password.*");
			}
			else if(email.getText().isEmpty()) {
				invalidEntry.setText("*Please enter email.*");
			}
			else if(password.getText().isEmpty()) {
				invalidEntry.setText("*Please enter password.*");
			}
			
		}
		//ELSE checks the user's details against the values in the 'Registration details.txt' file
		else {
			//Retrieves the user inputted details from the text fields and stores them in variables
			String loginEmail = email.getText();
			String loginPswd = password.getText();
			
			//Stores the file path in the String variable 'filepath'
			String filepath = "/Users/rubenodamo/eclipse-workspace/ClubhouseCinemas/Registration details.txt";
			
			//Creates FileReader and BufferedReader to be able to read the file
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			//Creates LinkedLists to store email and passwords
			LinkedList<String> emails = new LinkedList<String>();
			LinkedList<String> passwords = new LinkedList<String>();
			String line = br.readLine(); //Reads the first line of the file
			while (line != null) { //WHILE loop to iterate through the lines of the file
				String[] data =line.split(";"); //Assings a semi-colon as the data delimiter
				//Adds data values to their respective lists
				String emailData = data[2];
				String pswdData = data[3];
				emails.add(emailData);
				passwords.add(pswdData);
				line = br.readLine(); //Reads the next line			
			}
			br.close(); //Closes BufferedReader
			
			//Iterartes through LinkedLists to authenticate the email and password
			int userPos = 0;
			for(int i=0;i<emails.size();i++) {
				if(emails.get(i).equals(loginEmail) && passwords.get(i).equals(loginPswd)) {
					userPos = i;
					currentUser = loginEmail;
					m.changeScene("View Films.fxml");
				}
				else {
					invalidEntry.setText("*Invalid email or password*");
				}
			}
			
		}
	}
	
	static void setCurrentUser(String currentUser) {
		Login.currentUser = currentUser;
	}
	static String getCurrentUser() {
		return currentUser;
	}
	
}
