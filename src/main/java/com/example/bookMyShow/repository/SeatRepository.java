package com.example.bookMyShow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookMyShow.entity.CinemaHall;
import com.example.bookMyShow.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String>
{
	Seat findBySeatId(String seatId);

	List<Seat> findByCinemaHall(CinemaHall cinemaHall);
}
