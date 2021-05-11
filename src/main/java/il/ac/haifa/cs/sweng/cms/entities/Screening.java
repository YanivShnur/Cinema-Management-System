package il.ac.haifa.cs.sweng.cms.entities;

import java.util.Date;
import java.util.Vector;

import javax.persistence.*;

@Entity
@Table(name = "screenings")

public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    @ManyToOne
    @JoinColumn(name="movie")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name="theater")
    private Theater theater;
	private Date date;
	@OneToMany(mappedBy = "screening")
	private Vector<Ticket> tickets;
	
	public Screening() {
		this.movie = new Movie();
		this.theater = new Theater();
		this.date = new Date();
		this.setTickets(new Vector<Ticket>(theater.getSeatsCapacity()));
	}
	
	public Screening(Movie movie, Theater theater, Date date)
	{
		this();
		this.movie = movie;
		this.theater = theater;
		this.date = date;
		this.setTickets(new Vector<Ticket>(this.theater.getSeatsCapacity()));
		for(int i=0;i<this.theater.getSeatsCapacity();i++)
			tickets.set(i, new Ticket(this,i));
	}
	
	public int getId() { return id; }
	
	public Movie getMovie() { return movie; }
	
	public void setMovie(Movie movie) { this.movie = movie; }
	
	public Theater getTheater(Theater theater) { return theater; }
	
	public void setTheater(Theater theater) { this.theater = theater; }
	
	public Date getDate() { return date; }
	
	public void setDate(Date date) { this.date = date; }

	public Vector<Ticket> getTickets() {return tickets;}

	public void setTickets(Vector<Ticket> tickets) {this.tickets = tickets;}

	public void chooseTicket(Customer customer,int seat,boolean isPackage){
		this.tickets.elementAt(seat).setCustomer(customer);
		customer.addTicket(this.tickets.elementAt(seat),isPackage);
	}
	
	public void unChooseTicket(int seat){
		this.tickets.elementAt(seat).setCustomer(null);
	}
	
}
