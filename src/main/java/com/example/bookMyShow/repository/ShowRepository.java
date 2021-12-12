package com.example.bookMyShow.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookMyShow.entity.CinemaHall;
import com.example.bookMyShow.entity.Movie;
import com.example.bookMyShow.entity.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, String>
{
	Show findByShowId(String showId);

	List<Show> findByDateAndMovieAndCinemaHallOrderByStartTime(Date date, Movie movie, CinemaHall cinemaHall);

	@Query(value = "select s.* from show s inner join movie m on s.movie = m.movie_id where "
		+ "lower(m.title) like lower(concat('%', :movieTitle,'%')) and s.start_time > CURRENT_DATE "
		+ "order by s.start_time", nativeQuery = true)
	List<Show> findByMovieTitle(@Param("movieTitle") String movieTitle);
}
