package il.ac.haifa.cs.sweng.cms;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import il.ac.haifa.cs.sweng.cms.common.entities.Cinema;
import il.ac.haifa.cs.sweng.cms.common.entities.PurpleBadge;
import il.ac.haifa.cs.sweng.cms.common.entities.Screening;
import il.ac.haifa.cs.sweng.cms.common.entities.Theater;
import il.ac.haifa.cs.sweng.cms.common.entities.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;

public class PurpleBadgeController implements Initializable {
	private int permission= App.getUserPermission();
	private PurpleBadge pb=null;
	/**
	 * @return the pb
	 */

	private List<Cinema> cinemas = new ArrayList<Cinema>();
	private List<Theater> theaters = new ArrayList<Theater>();
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button backButton;

	@FXML
	private ToggleButton toggleSwitch;

	@FXML
	private Text capacityText;

	@FXML
	private Button editButton;

	@FXML
	private TextField capacityAmount;

	@FXML
	private Button okButton;

	@FXML
	private DatePicker fromDate;

	@FXML
	private DatePicker toDate;

	@FXML
	private Text FromText;

	@FXML
	private Text toText;

	@FXML
	private Button setButton;

	@FXML
	private TextArea datesText;


	@FXML
	void editCapacity(ActionEvent event) {
		editButton.setVisible(false);
		capacityText.setText("Set maximum capacity");
		capacityAmount.setVisible(true);
		okButton.setVisible(true);
	}
	@FXML
	void handheldsBackButton(ActionEvent event) {
		App.getOcsfClient(this).updatePurpleBadge(this.pb);
		if (permission == 1){//Employee
			try {

				App.setRoot("EmployeeHome.fxml");//load edit movie screen
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		else if (permission == 3 ){//Employee
			try {

				App.setRoot("CinemaManagerHome.fxml");//load edit movie screen
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}else if ( permission == 4){//Employee
			try {

				App.setRoot("GeneralManagerHome.fxml");//load edit movie screen
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	@FXML
	void setCapacityAmount(ActionEvent event) {

		int num = Integer.parseInt(capacityAmount.getText()) ;
		pb.setY(num);
		App.getOcsfClient(this).updatePurpleBadge(this.pb);
		capacityText.setText("Current Capacity is: "+pb.getY());
		editButton.setVisible(true);
		capacityAmount.setVisible(false);
		okButton.setVisible(false);

	}

	@FXML
	void setPurpleBadge(ActionEvent event) {
		pb.setStatus(!pb.getStatus());
		setMode();
		App.getOcsfClient(this).updatePurpleBadge(this.pb);
	}

	@FXML
	void setDates(ActionEvent event) {
		if (fromDate.getValue() != null && toDate.getValue() != null) {
			pb.addClosingDates(new GregorianCalendar(fromDate.getValue().getYear(),fromDate.getValue().getMonthValue()-1,fromDate.getValue().getDayOfMonth()), new GregorianCalendar(toDate.getValue().getYear(),toDate.getValue().getMonthValue()-1,toDate.getValue().getDayOfMonth()));
			if (pb.getClosingDates().size()!=0)
				datesText.setVisible(true);
			setDatesText();
			App.getOcsfClient(this).updatePurpleBadge(this.pb);
		}
	}
	public void setDatesText() {
		datesText.setText("");
		SimpleDateFormat formattedDate= new SimpleDateFormat("dd-MMM-yyyy");
		GregorianCalendar today = new GregorianCalendar();
		for (GregorianCalendar gc:pb.getClosingDates()) {
			String dateFormatted= formattedDate.format( gc.getTime());
			if (gc.after(today)) {
				if(datesText.getText().equals(""))
					datesText.setText("Future Pupple Badge days:\n");
				datesText.setText(datesText.getText()+dateFormatted+"\n");
			}
		}
		if(!datesText.getText().equals(""))
			datesText.setVisible(true);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		App.getOcsfClient(this).getPurpleBadge();
		App.getOcsfClient(this).getListOfCinemas();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMode();


	}
	public void setCinemas(List<Cinema> cinemaList) {
		// TODO Auto-generated method stub
		this.cinemas=cinemaList;
	}
	/**
	 * @return the theaters
	 */
	public List<Theater> getTheaters() {
		return theaters;
	}
	/**
	 * @param theaters the theaters to set
	 */
	public void setTheaters(List<Theater> theaters) {
		this.theaters = theaters;
	}
	public void setPb(PurpleBadge pb) {
		// TODO Auto-generated method stub
		this.pb=PurpleBadge.getInstance(pb);

	}
	public void setMode() {
		toggleSwitch.setSelected(pb.getStatus());
		if(pb.getStatus()) {
			capacityText.setVisible(true);
			capacityText.setText("Current Capacity is: "+pb.getY());
			editButton.setVisible(true);
			capacityAmount.setVisible(false);
			okButton.setVisible(false);
			fromDate.setVisible(true);
			FromText.setVisible(true);
			toDate.setVisible(true);
			toText.setVisible(true);
			setButton.setVisible(true);
			datesText.setVisible(false);
			if (pb.getClosingDates().size()!=0) {
				setDatesText();
			}
			toggleSwitch.setStyle("-fx-background-color: mediumpurple;");
		}
		else {
			pb.CoronaFree();
			capacityText.setVisible(false);
			editButton.setVisible(false);
			capacityAmount.setVisible(false);
			okButton.setVisible(false);
			fromDate.setVisible(false);
			FromText.setVisible(false);
			toDate.setVisible(false);
			toText.setVisible(false);
			setButton.setVisible(false);
			datesText.setVisible(false);
			datesText.setText("");
			toggleSwitch.setStyle("-fx-background-color: lightgreen;");
		}
	}
	public void coronaCheck(){
		for(Theater t:theaters) {
			for (Screening s: t.getScreeningList()){
				if(PurpleBadge.getInstance().getClosingDates().contains(s.getDate()))
					s.setRealSeatsCapacity(t.getRealSeatsCapacity());

				//stack<-ticketList
				//pop * ticketList.size-real (send cancelation massage)
				//ticketList<-stack(change the seats No. , if not same customer space) (send changed seat massage)
				Stack<Ticket> stack = new Stack();
				stack.addAll(s.getTickets());

				/*cancel seats*/
				while(stack.size()>t.getRealSeatsCapacity()){
					Ticket tic = stack.pop();
					
					// TODO: 25/06/2021 sand massege of cancelation
				}

				/*change taken seats*/
				int i =1;
				int j = 1;
				while(!stack.isEmpty()){
					Ticket tic = stack.pop();
					if (!stack.peek().getCustomer().equals(tic.getCustomer())){//skip seat
						s.addTicket(new Ticket(null,null,i,j, false));
						j++;
						j=j%10;
						if (i==0){
							j=1;
							i+=2;
						}
					}

					tic.setSeat(i,j);
					s.addTicket(tic);

					// TODO: 25/06/2021 sand massege of changing seats.

					j++;
					j=j%10;
					if (i==0){
						j=1;
						i+=2;
					}
				}
				//				for(Ticket t: s.getTickets()) {
				//TODO:					//notify(t.getCustomer());//Send alert to the customer about canceling
				//					if(t.getCustomer().getTicket().contains(t))
				//						t.getCustomer().removeTicket(t, true);
				//					else
				//						t.getCustomer().removeTicket(t, false);
				//					cancel.add(t.getCustomer());
			}
		}
		//		}
		
	}

}