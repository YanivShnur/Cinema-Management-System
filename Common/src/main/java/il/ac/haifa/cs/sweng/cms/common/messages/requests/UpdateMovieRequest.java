package il.ac.haifa.cs.sweng.cms.common.messages.requests;

import il.ac.haifa.cs.sweng.cms.common.entities.Movie;
import il.ac.haifa.cs.sweng.cms.common.entities.Screening;
import il.ac.haifa.cs.sweng.cms.common.messages.AbstractRequest;

import java.util.List;

/**
 * Request for movie update.
 *
 * @author Yuval Razieli
 */
public class UpdateMovieRequest extends AbstractRequest {

    private Movie movie;

    /**
     * Constructs an UpdateMovieRequest instance with the movie to update.
     * @param movie Movie to update.
     */
    public UpdateMovieRequest(Movie movie) {
        this.movie = movie;
    }

    /**
     * Returns the movie that is requested to update.
     * @return New movie details instance.
     */
    public Movie getMovie() {
        return movie;
    }
}
