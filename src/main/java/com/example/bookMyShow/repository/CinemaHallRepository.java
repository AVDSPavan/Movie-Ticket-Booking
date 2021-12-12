package com.example.bookMyShow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookMyShow.entity.CinemaHall;

@Repository
public interface CinemaHallRepository extends JpaRepository<CinemaHall, String>
{
	CinemaHall findByCinemaHallId(String cinemaId);

	@Override
	List<CinemaHall> findAll();
}
