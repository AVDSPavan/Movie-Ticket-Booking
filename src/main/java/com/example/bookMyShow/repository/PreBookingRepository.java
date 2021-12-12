package com.example.bookMyShow.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookMyShow.entity.PreBooking;
import com.example.bookMyShow.entity.Show;

@Repository
public interface PreBookingRepository extends JpaRepository<PreBooking, String>
{
	PreBooking findByPreBookingId(String preBookingId);

	@Query(value = "select pb.* from pre_booking pb where pb.seat = :seatId and pb.show = :showId", nativeQuery = true)
	PreBooking findBySeatIdAndShowId(@Param("seatId") String seatId, @Param("showId") String showId);

	List<PreBooking> findByShow(Show show);

	@Modifying
	@Transactional
	void deleteByExpiryAtBefore(Date expiryDate);
}
