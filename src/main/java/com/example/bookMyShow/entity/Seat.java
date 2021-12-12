package com.example.bookMyShow.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Table(name = "seat")
public class Seat
{
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "seat_id")
	private String seatId;

	@Column(name = "seat_no", nullable = false)
	private String seatNo;

	@Column(name = "row_no", nullable = false)
	private int rowNo;

	@Column(name = "column_no", nullable = false)
	private int columnNo;

	@ManyToOne
	@JoinColumn(name = "cinema_hall")
	private CinemaHall cinemaHall;

	@Column(name = "created_at")
	private Date createdAt;
}
