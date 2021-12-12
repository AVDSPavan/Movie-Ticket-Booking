package com.example.bookMyShow.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Getter
@Setter
@Table(name = "booking")
public class Booking implements Serializable
{
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "booking_id")
	private String bookingId;

	@ManyToOne
	@JoinColumn(name = "seat", nullable = false)
	private Seat seat;

	@ManyToOne
	@JoinColumn(name = "show")
	private Show show;

	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status")
	private BookingStatus bookingStatus;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;
}
