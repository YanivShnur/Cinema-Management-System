package il.ac.haifa.cs.sweng.cms;

import il.ac.haifa.cs.sweng.cms.common.entities.*;
import il.ac.haifa.cs.sweng.cms.common.messages.AbstractResponse;
import il.ac.haifa.cs.sweng.cms.common.messages.requests.*;
import il.ac.haifa.cs.sweng.cms.common.messages.responses.*;
import il.ac.haifa.cs.sweng.cms.ocsf.AbstractClient;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.List;

/**
 * Extension of the OCSF AbstractClient class.
 *
 * @author Yuval Razieli
 */
public class OCSFClient extends AbstractClient {

    private Initializable controller;

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    public OCSFClient(String host, int port) {
        super(host, port);
    }

    /**
     * Receives a response sent from the server and handles it.
     * @param msg The message received.
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        if(msg instanceof AbstractResponse) {
            handleResponse((AbstractResponse) msg);
        } else {
            // TODO: Show "Received an unexpected message from client".
        }
    }

    /**
     * Handles responses.
     * @param response Response to handle.
     */
    private void handleResponse(AbstractResponse response) {
        if (response instanceof ListAllCinemasResponse) {
            if(controller instanceof ViewMoviesController)
                ((ViewMoviesController) controller).setCinemas(((ListAllCinemasResponse) response).getCinemaList());
            else if(controller instanceof PurpleBadgeController)
                ((PurpleBadgeController) controller).setCinemas(((ListAllCinemasResponse) response).getCinemaList());
            else if(controller instanceof OperationalReportsController)
                ((OperationalReportsController) controller).setCinemas(((ListAllCinemasResponse) response).getCinemaList());
        
        }
        if (response instanceof ListAllMoviesResponse) {
            if(controller instanceof ViewMoviesController) {
                ((ViewMoviesController) controller).setMovies(((ListAllMoviesResponse) response).getMovieList());
            }
            else if(controller instanceof PriceChangeSubmissionController) {
                ((PriceChangeSubmissionController) controller).setMovies(((ListAllMoviesResponse) response).getMovieList());
            }
            else if(controller instanceof OperationalReportsController) {
                ((OperationalReportsController) controller).setMovies(((ListAllMoviesResponse) response).getMovieList());
            }
            
        }
        if (response instanceof ListAllTicketsResponse) {
            ((CancelTicketController) controller).setTickets(((ListAllTicketsResponse) response).getTicketsList());
        }
        if (response instanceof ListAllLinksResponse) {
            ((CancelLinkController) controller).setLinks(((ListAllLinksResponse) response).getLinksList());
        }
        if(response instanceof UpdateMovieResponse) {
            // TODO: Update GUI with screenings.
            System.out.println();
        }
        if (response instanceof LoginResponse) {
            ((UserLoginController) controller).onReplyReceived((LoginResponse) response);
        }
        if (response instanceof MailResponse) {
            ((UserLoginController) controller).onReplyReceived2((MailResponse) response);
        }
        if (response instanceof DeleteMovieResponse) {
            ((EditMovieScreenController) controller).onReplyReceived((DeleteMovieResponse) response);
        }
        if (response instanceof ComplaintFileResponse) {
            ((ComplaintAddController) controller).handleComplaintFileResponse();
        }
        if (response instanceof ListAllComplaintsResponse) {
            if(controller instanceof ComplaintAddController) {
                ((ComplaintAddController) controller).setComplaints(((ListAllComplaintsResponse) response).getComplaints());
            } else if(controller instanceof ComplaintHandlingController) {
                ((ComplaintHandlingController) controller).setComplaints(((ListAllComplaintsResponse) response).getComplaints());
            } else if(controller instanceof OperationalReportsController) {
                ((OperationalReportsController) controller).setComps(((ListAllComplaintsResponse) response).getComplaints());
            }
            
        }
        if (response instanceof ComplaintReplyResponse) {
            ((ComplaintHandlingController) controller).onReplyReceived();
        }
        if(response instanceof UpdatePurpleBadgeResponse) {
            // TODO: Update GUI with screenings.
        }
        if(response instanceof getPurpleBadgeResponse) {
        	((PurpleBadgeController) controller).setPb(((getPurpleBadgeResponse)response).getPb());
        }

        if (response instanceof ListAllPriceChangesResponse) {
            if(controller instanceof PriceChangeSubmissionController) {
                ((PriceChangeSubmissionController) controller).setPriceChanges(((ListAllPriceChangesResponse) response).getPriceChanges());
            } else if(controller instanceof PriceChangeHandlingController) {
                ((PriceChangeHandlingController) controller).setPriceChanges(((ListAllPriceChangesResponse) response).getPriceChanges());
            }
        }
        if (response instanceof PriceChangeSubmissionResponse) {
            ((PriceChangeSubmissionController) controller).handlePriceChangeSubmissionResponse();
        }
        if (response instanceof PriceChangeReplyResponse) {
            ((PriceChangeHandlingController) controller).onReplyReceived();
        }
        if (response instanceof ListAllPaymentsResponse) {
        	((OperationalReportsController) controller).setPayments(((ListAllPaymentsResponse) response).getPayments());
        }
        if (response instanceof UpdateTicketsResponse) {
            ((PaymentController) controller).setNewTickets(((UpdateTicketsResponse) response).getTicketList());
            ((PaymentController) controller).setMessageStatus(true);
        }
        if (response instanceof UpdateLinksResponse) {
            ((PaymentController) controller).setNewLink(((UpdateLinksResponse) response).getLink());
            ((PaymentController) controller).setMessageStatus(true);
        }
        if (response instanceof UpdateCustomerResponse) {
            ((PaymentController) controller).setMessageStatus(true);
        }
        if (response instanceof UpdateCinemaResponse) {
            // TODO: Check if successful or not and show it on the screen.
            //((PurpleBadgeController) controller).changeMeToYourSuccessFunction();
        }
        if (response instanceof AlertMessageResponse) {
            Alert.AlertType alertType = Alert.AlertType.values()[((AlertMessageResponse) response).getAlertType()];
            String header = ((AlertMessageResponse) response).getHeader();
            String message = ((AlertMessageResponse) response).getMessage();
            Platform.runLater(() -> showAlert(alertType, header, message));
        }
        if (response instanceof BlockReleaseSeatResponse) {
            ((PaymentController) controller).handleBlockSeatResponse(response.getStatus());
        }
        if (response instanceof ListAllBlockedSeatsResponse) {
            ((PaymentController) controller).setFlag(((ListAllBlockedSeatsResponse) response).getBlockedSeatsList());
        }
        // TODO: Show "Unidentified response".

    }

    private void showAlert(Alert.AlertType alertType, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType.name().substring(0, 1).toUpperCase() + alertType.name().substring(1).toLowerCase());
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Sends a request to the server to get the list of all movies.
     */
    protected void getListOfCinemas() {
        try {
            sendToServer(new ListAllCinemasRequest());
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sends a request to the server to get the list of all movies.
     */
    protected void getListOfMovies() {
        try {
            sendToServer(new ListAllMoviesRequest());
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sends a request to the server to get the list of all tickets.
     */
    protected void getListOfTickets() {
        try {
            sendToServer(new ListAllTicketsRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to get the list of all links.
     */
    protected void getListOfLinks() {
        try {
            sendToServer(new ListAllLinksRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getListOfComplaints(User user) {
        try {
            sendToServer(new ListAllComplaintsRequest(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getListOfPriceChanges(User user) {
        try {
            sendToServer(new ListAllPriceChangesRequest(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getListOfPayments() {
        try {
            sendToServer(new ListAllPaymentsRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to update a movie.
     * @param movie Movie to update.
     */
    protected void updateMovie(Movie movie) {
        try {
            sendToServer(new UpdateMovieRequest(movie));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    protected void deleteMovie(Movie movie) {
        try {
            sendToServer(new DeleteMovieRequest(movie));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sends a request to the server to update the list of tickets by adding or removing a ticket.
     * @param tickets New list of screenings.
     */

    protected void updateTickets(List <Ticket> tickets, boolean addOrRemove) {
        try {
            sendToServer(new UpdateTicketsRequest(tickets, addOrRemove));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    protected void updateLinks(Link link, boolean addOrRemove) {
        try {
            sendToServer(new UpdateLinksRequest(link,addOrRemove ));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    protected void updateCustomer(Customer customer) {
        try {
            sendToServer(new UpdateCustomerRequest(customer));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    protected void updateCinema(Cinema cinema) {
        try {
            sendToServer(new UpdateCinemaRequest(cinema));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sets the calling controller.
     * @param controller Controller which called the OCSFClient.
     */
    protected void setController(Initializable controller) {
        this.controller = controller;
    }

    protected void tryLogin(String userName, String password) {
        try {
            sendToServer(new LoginRequest(userName, password));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendMail(String emailAddressToSend, String subject, String msg) {
        try {
            sendToServer(new MailRequest(emailAddressToSend, subject, msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to file a complaint.
     * @param complaint Complaint to file.
     */
    public void fileComplaint(Complaint complaint) {
        try {
            sendToServer(new ComplaintFileRequest(complaint));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sends a request to the server to reply to a complaint.
     * @param complaint Complaint to reply to.
     */
    public void replyToComplaint(Complaint complaint) {
        try {
            sendToServer(new ComplaintReplyRequest(complaint));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sends a request to the server to update the purple badge.
     * @param pb and status status to update.
     */
    public void updatePurpleBadge(PurpleBadge pb) {
        try {
            sendToServer(new UpdatePurpleBadgeRequest(pb));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }
    public void getPurpleBadge() {
    	
    	try {
            sendToServer(new getPurpleBadgeRequest());
        } catch (IOException e) {
        	// TODO: Show "IO exception while sending request to server."
        }
    }
    /**
     * Sends a request to the server to submit a price change.
     * @param priceChange Price change to submit.
     */
    public void submitPriceChange(PriceChange priceChange) {
        try {
            sendToServer(new PriceChangeSubmissionRequest(priceChange));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sends a request to the server to reply to a price change.
     * @param priceChange Price change to reply to.
     */
    public void replyToPriceChange(PriceChange priceChange) {
        try {
            sendToServer(new PriceChangeReplyRequest(priceChange));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    /**
     * Sends a request to the server to block seats.
     * @param screening Screening to block a seat on.
     * @param row Row of seat.
     * @param col Column of seat.
     * @param block Whether to block the seat or release it.
     */
    public void blockSeat(Screening screening, int row, int col, boolean block) {
        try {
            sendToServer(new BlockReleaseSeatRequest(screening, row, col, block));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

    public void getListOfBlockedSeats(Screening screening, int row, int col) {
        try {
            sendToServer(new ListAllBlockedSeatsRequest(screening, row, col));
        } catch (IOException e) {
            // TODO: Show "IO exception while sending request to server."
        }
    }

}
