package com.sbtest;

import com.sbtest.model.Movie;
import com.sbtest.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class SbTestApplicationTests {
	@Autowired
	private MovieRepository movieRepository;

	private Movie movie1;
	private Movie movie2;
	private Movie movie3;

	@BeforeEach
	void init() {
		movie1 = new Movie();
		movie1.setName("Terminator 2");
		movie1.setGenera("Action");
		movie1.setReleaseDate(LocalDate.of(1992, Month.AUGUST, 23));

		movie2 = new Movie();
		movie2.setName("Titanic");
		movie2.setGenera("History");
		movie2.setReleaseDate(LocalDate.of(1998, Month.APRIL, 11));
		movieRepository.save(movie2);

		movie3 = new Movie();
		movie3.setName("Avatar");
		movie3.setGenera("Fantasy");
		movie3.setReleaseDate(LocalDate.of(2008, Month.APRIL, 7));
		movieRepository.save(movie3);
	}

	@Test
	@DisplayName("Test saving to db")
	void save() {
		Movie newMovie = movieRepository.save(movie1);
		assertNotNull(newMovie);

		assertThat(newMovie.getId()).isNotEqualTo(null);
	}

	@Test
	@DisplayName("Show all movies")
	void getAllMovies() {
		List<Movie> movieList = movieRepository.findAll();
		assertNotNull(movieList);
		assertEquals(2, movieList.size());
	}

	@Test
	@DisplayName("Find by id")
	void gindById() {
		Movie existingMovie = movieRepository.findById(movie3.getId()).get();
		assertNotNull(existingMovie);
		assertEquals("Fantasy", existingMovie.getGenera());
		assertThat(existingMovie.getReleaseDate()).isBefore(LocalDate.of(2008, Month.APRIL, 10));
	}

	@Test
	@DisplayName("Update movie")
	void updateMovie(){
		Movie movie = movieRepository.findById(3L).get();
		movie.setGenera("Super Action");
		movieRepository.save(movie);

		assertEquals("Super Action", movie.getGenera());
		assertEquals("Terminator 2", movie.getName());
	}

	@Test
	@DisplayName("Delete movie")
	void delete() {
		Movie movie = movieRepository.findById(4L).get();

		movieRepository.delete(movie);
		List<Movie> list = movieRepository.findAll();
		assertEquals(2, list.size());
		assertThat(movieRepository.findById(4L)).isEmpty();
	}
}
