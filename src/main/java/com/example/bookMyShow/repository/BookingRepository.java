package com.example.bookMyShow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookMyShow.entity.Booking;
import com.example.bookMyShow.entity.BookingStatus;
import com.example.bookMyShow.entity.Show;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String>
{
	Booking findByBookingId(String bookingId);

	List<Booking> findByShowAndBookingStatus(Show show, BookingStatus bookingStatus);

	@Query(value = "select b.* from booking b where b.seat = :seatId and b.show = :showId and booking_status = 'CONFIRMED' ", nativeQuery = true)
	Booking findBySeatIdAndShowId(@Param("seatId") String seatId, @Param("showId") String showId);
}
