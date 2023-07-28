package application;

public class BookingHistoryItem {
	
	//Initialises the variables
	private String status, firstName, lastName, film, date, time, seats, vip, idNumber;
	
	//Setter
    public BookingHistoryItem (String status, String firstName, String lastName, String film, String date, String time, String seats, String idNumber, String vip) {

        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.film = film;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.vip = vip;
        this.idNumber = idNumber;
    }
    
    //Getter
    public String getStatus() {

        return status;
    }

    //Getter
    public String getFirstName() {

        return firstName;
    }

    //Getter
    public String getLastName() {

        return lastName;
    }
    
    //Getter
    public String getFilm() {

        return film;
    }

    //Getter
    public String getDate() {

        return date;
    }
    
    //Getter
    public String getTime() {

        return time;
    }
    
    //Getter
    public String getSeats() {

        return seats;
    }
    
    //Getter
    public String getVip() {
    	
    	return vip;
    }

    //Getter
    public String getIdNumber() {

        return idNumber;
    }
}

