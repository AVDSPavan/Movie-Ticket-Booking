package com.example.bookMyShow.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Table(name = "cinema_hall")
public class CinemaHall implements Serializable
{
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "cinema_hall_id")
	private String cinemaHallId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "total_seats", nullable = false)
	private int totalSeats;

	@Column(name = "seating_layout", nullable = false, columnDefinition="TEXT")
	private String seatingLayout;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;
}
