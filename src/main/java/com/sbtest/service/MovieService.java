package com.sbtest.service;

import com.sbtest.model.Movie;
import com.sbtest.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public Movie updateMovie(Movie movie, Long id) {
        Movie movie1 = movieRepository.findById(id).get();
        movie1.setName(movie.getName());
        movie1.setGenera(movie.getGenera());
        movie1.setReleaseDate(movie.getReleaseDate());
        return movieRepository.save(movie1);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).get();
        movieRepository.delete(movie);
    }
}
