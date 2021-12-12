package com.example.bookMyShow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookMyShow.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>
{
	Movie findByMovieId(String movieId);

	@Override
	List<Movie> findAll();

	List<Movie> findByTitle(String movieTitle);
}
