package application;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;

public class SendEmail { //Controller class which sends an email to the user
	
	//Method 'sendEmail()' creates a booking confirmation email and sends it to the user
	static void sendEmail(String recipient, String type) {
		
		//Initialises 'username' and 'password' variables that cannot be reassigned
        final String username = "clubhousecinemas@gmail.com";
        final String password = "xldwwfisqlycexhq";
        
        /*JavaMail Properties is used to set in the session objects and to create the session object.
         * SMTP - Simple Mail Transfer Protocol is used*/
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        //Creates 'Session' object that provides access to JavaMail Protocols
        Session session = Session.getInstance(props,new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        //Creates ByteArrayOutputStream and sets it to null
        ByteArrayOutputStream outputStream = null;
        
        //Store the message content in the 'String' variable 'content'
        String content = "Hello " + Confirmation.name + ",\n\n" +
                "Thank you for choosing Clubhouse Cinemas. Your booking for the film " + Main.getSelectedFilmTitle() + " has been confirmed."
        		+ " Please, keep this email with the PDF receipt as proof of your booking.\n"
        		+ "\nLooking forward to seeing you on " + Confirmation.finalDate +", at "
        		+ Confirmation.finalTime + 
        		"!\n\nEnjoy the film!\n\nMickey Mouse\nCEO of Clubhouse Cinemas";

        try { //TRY-CATCH Block
        	//Creates a 'MimeBodyPart' and adds the message content to it
        	MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content);
        	
            //Assigns 'outputStream' to a new ByteArrayOutputStream
        	outputStream = new ByteArrayOutputStream();
            writePdf(outputStream); //Calls the 'writePdf()' method
            byte[] bytes = outputStream.toByteArray(); //Creates a byte array called 'bytes'
            
            //Creates a link between the application and the pdf so data can be written to it
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            //Creates a 'MimeBodyPart' and adds the pdf to it
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("BookingConfirmation.pdf");
            
            //Creates a 'MimeMultipart' and adds the 'textBodyPart' and 'pdfBodyPart' to it
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
        	
            //Creates a new message setting the sender and reciever addresses
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("clubhousecinemas@gmail.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            
            if (type.equals("confirmation")) { //IF the email is a "confirmation" email
            	//Set the subject and message content
	            message.setSubject(Confirmation.bookingId+" - Booking Confirmation for "+Main.getSelectedFilmTitle());
	            message.setContent(mimeMultipart);
            }
           
            Transport.send(message); //Sends email
            
        } catch (Exception e) {
            throw new RuntimeException(e);
            
        } finally {
            //cleans off outputStream
            if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
        }
    }
	
	//Method 'writePdf' creates the 'BookingConfirmation.pdf' to be emailed to the user
    public static void writePdf(OutputStream outputStream) throws Exception {
    	//Creates fonts to be used in the PDF
    	Font title = FontFactory.getFont(FontFactory.HELVETICA, 36f, Font.BOLD);
    	Font subtitle = FontFactory.getFont(FontFactory.HELVETICA, 16f, Font.BOLD);
    	Font italics = FontFactory.getFont(FontFactory.HELVETICA, 12f, Font.ITALIC);
    	
    	//Creates a new document and writes it to the outputStream so it can be sent
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
         
        document.open(); //Opens the document
         
        //Adds document details
        document.addTitle("Booking Confirmation PDF");
        document.addSubject("Receipt PDF");
        document.addAuthor("Mickey Mouse");
        document.addCreator("Clubhouse Cinemas");
         
        //Creates new paragraph and adds the content of the receipt to the paragraph
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("Your Booking Receipt\n\n", title));
        paragraph.add(new Chunk("Booking ID: "+Confirmation.bookingId+"\n"+
        						"Cinema: Clubhouse Cinemas"+"\n\n"+
        						"Film: "+Main.getSelectedFilmTitle()+"\n"+
        						"Screen: "+TicketBooking.screenNum+"\n"+
        						"Date: "+Confirmation.finalDate+"\n"+
        						"Time: "+Confirmation.finalTime+"\n"+
        						"Tickets: "+TicketBooking.adultTickets+" x Adult, "+ TicketBooking.childTickets+" x Child, "+
        						TicketBooking.seniorTickets+" x Senior"+"\n"+
        						"Seats: "+SeatBooking.userSeats+"\n"+
        						"is VIP: "+Confirmation.vipConf+"\n\n"+
        						"Total Payment: "+"�"+String.format("%.2f", TicketBooking.total)+"\n\n"));
        
        
        paragraph.add(new Chunk("Face Covering Information\n", subtitle));
        paragraph.add(new Chunk("Please note that face coverings are optional in English cinemas. "
        		+ "They are mandatory in Scotland and Wales for guests over the age of 11, except those exempt for health reasons. "
        		+ "If you do not have a face covering when arriving at the cinema, in order to gain admission you will be required to "
        		+ "purchase one for �1.\n\n",italics));
        paragraph.add(new Chunk("Ticket Collection Information\n", subtitle));
        paragraph.add(new Chunk("There are several ways to gain admission to your film:\n"
        		+ "- Show this email on your smartphone to the usher to scan the barcode. And make your payment.\n"
        		+ "- Alternatively, please print out this confirmation, bring it with you to the cinema, and present it to the usher to"
        		+ " scan the barcode below for admittance, or show this email on your smartphone. And make your payment.", italics));
        
        //Adds the paragraph content to the document
        document.add(paragraph);
        
        //Creates a QR code that when scanned shows "Valid Booking - bookingID"
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode("Valid Booking - "+Confirmation.bookingId, 1000, 1000, null);
        Image codeQrImage = barcodeQRCode.getImage(); //Generates QR code image
        codeQrImage.scaleAbsolute(200, 200); //Resizes QR code
        document.add(codeQrImage); //Adds the QR code to the PDF
         
        //Creates an Image and assigns the Clubhouse Cinemas logo to it
        Image img = Image.getInstance("/Users/rubenodamo/eclipse-workspace/ClubhouseCinemas/Images/ClubhouseCinemas Logo.png");
        img.scaleAbsolute(312f, 129.5f); //Resizes image
        img.setAbsolutePosition(4, 22); //Positions image on the PDF
        document.add(img); //Adds image to PDF
        
        document.close(); //Closes the document
    }
}

