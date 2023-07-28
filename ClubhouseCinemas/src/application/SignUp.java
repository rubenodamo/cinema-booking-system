package application;

import java.util.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUp {
	
	@FXML
	private TextField fname;
	@FXML
	private TextField sname;
	@FXML
	private TextField reg_email;
	@FXML
	private PasswordField reg_pswd;
	@FXML
	private PasswordField confirm_pswd;
	@FXML
	private Button signupBtn;
	@FXML
	private Label errorMsg;
	@FXML
	private Hyperlink loginLink;
	
	public void goToLogin(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("Login.fxml");
	}
	
	public void userReg(ActionEvent event) throws IOException{
		checkReg();
	}
	
	public static boolean checkFirstName(String firstname) {
		String fnameRegex = "[A-Z][a-z]*";
		if (firstname ==  null) {
			return false;
		}
		return firstname.matches(fnameRegex);
	}
	public static boolean checkLastName(String lastname) {
		String snameRegex = "[A-Z][a-z]*";
		if (lastname ==  null) {
			return false;
		}
		return lastname.matches(snameRegex);
	}
	
	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+

				"[a-zA-Z0-9_+&*-]+)*@"+
							"(?:[a-zA-Z0-9-]+\\.)+[a-z" 
							+"A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if(email == null) {
			return false;
		}
		return pat.matcher(email).matches();	
	}
	
	public static boolean isValidPassword(String password) {
		String pswdRegex = "^(?=.*[0-9])"+ 
							"(?=.*[a-z])(?=.*[A-Z])"+ 
							"(?=.*[@#$%^&+=])"+ 
							"(?=\\S+$).{8,20}$";
		
		Pattern p = Pattern.compile(pswdRegex);
		if (password == null) {
			return false;
		}
		return p.matcher(password).matches();
		
	}

	private void checkReg() throws IOException {
		Main m = new Main();
		String email = reg_email.getText();
		String password = reg_pswd.getText();
		String confirmPswd = confirm_pswd.getText();
		String firstname = fname.getText();
		String lastname = sname.getText();
		if(checkFirstName(firstname) == true 
				&& checkLastName(lastname) == true 
				&& isValidEmail(email) == true 
				&& isValidPassword(password) == true 
				&& confirmPswd.equals(password)) {
			String filepath = "Registration details.txt";
			FileWriter fw = new FileWriter(filepath, true);
			fw.write(firstname+";"+lastname+";"+email+";"+password+"\n");
			fw.close();
			m.changeScene("Login.fxml");
		}
		else if(checkFirstName(firstname) == false) {
			errorMsg.setText("*Invalid first name.*");
		}
		else if(checkLastName(lastname) == false) {
			errorMsg.setText("*Invalid last name.*");
		}
		else if(isValidEmail(email) == false) {
			errorMsg.setText("*Invalid email.*");
		}
		else if(isValidPassword(password) == false ) {
			errorMsg.setText("*Invalid password.*");
		}
		else if(!confirmPswd.equals(password)) {
			errorMsg.setText("*Make sure password is the same.*");
		}
		else {
			errorMsg.setText("*Invalid Entry.*");
		}
	}
}
