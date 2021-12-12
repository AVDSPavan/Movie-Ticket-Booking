package com.example.bookMyShow.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
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
@Table(name = "show")
public class Show implements Serializable
{
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "show_id")
	private String showId;

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "start_time", nullable = false)
	private Date startTime;

	@Column(name = "end_time", nullable = false)
	private Date endTime;

	@ManyToOne
	@JoinColumn(name = "movie")
	private Movie movie;

	@ManyToOne
	@JoinColumn(name = "cinema_hall")
	private CinemaHall cinemaHall;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;
}
