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
@Table(name = "pre_booking")
public class PreBooking implements Serializable
{
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "pre_booking_id")
	private String preBookingId;

	@ManyToOne
	@JoinColumn(name = "seat", nullable = false)
	private Seat seat;

	@ManyToOne
	@JoinColumn(name = "show", nullable = false)
	private Show show;

	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Column(name = "expiry_at", nullable = false)
	private Date expiryAt;
}
