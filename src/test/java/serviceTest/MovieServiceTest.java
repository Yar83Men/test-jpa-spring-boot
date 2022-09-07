package serviceTest;

import com.sbtest.model.Movie;
import com.sbtest.repository.MovieRepository;
import com.sbtest.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;
    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void init() {
        movie1 = new Movie();
        movie1.setId(1L);
        movie1.setName("Terminator 2");
        movie1.setGenera("Action");
        movie1.setReleaseDate(LocalDate.of(1992, Month.AUGUST, 23));

        movie2 = new Movie();
        movie2.setId(2L);
        movie2.setName("Titanic");
        movie2.setGenera("History");
        movie2.setReleaseDate(LocalDate.of(1998, Month.APRIL, 11));
        movieRepository.save(movie2);
    }

    @Test
    @DisplayName("Save Movie")
    void save() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);

        Movie newMovie = movieService.save(movie1);
        assertNotNull(newMovie);
        assertThat(newMovie.getName()).isEqualTo("Terminator 2");
    }

    @Test
    @DisplayName("Get all movies")
    void getMovies() {
        List<Movie> list = new ArrayList<>();
        list.add(movie1);
        list.add(movie2);
        when(movieRepository.findAll()).thenReturn(list);

        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies);
        assertThat(movies.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Get movie by id")
    void getMovieById() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie1));
        Movie existingMovie = movieService.getMovieById(1L);

        assertNotNull(existingMovie);
        assertThat(existingMovie.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Exception test")
    void getMovieByIdForException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));

        assertThrows(RuntimeException.class, () -> {
            movieService.getMovieById(4L);
        });
    }

    @Test
    @DisplayName("Update movie")
    void updateMovie() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie1));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);

        movie1.setGenera("Fantasy");

        Movie updated = movieService.updateMovie(movie1, 1L);

        assertNotNull(updated);
        assertEquals("Fantasy", updated.getGenera());
    }

    @Test
    @DisplayName("Delete movie")
    void delete() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie1));
        doNothing().when(movieRepository).delete(any(Movie.class));

        movieService.deleteMovie(1L);
        verify(movieRepository, times(1)).delete(movie1);
    }
}
