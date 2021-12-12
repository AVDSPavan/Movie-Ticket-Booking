package com.example.bookMyShow.util;

import lombok.AllArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.bookMyShow.repository.PreBookingRepository;

@Component
@AllArgsConstructor
public class ScheduledTasks
{
	private final PreBookingRepository preBookingRepository;
	private final Utils utils;

	@Scheduled(cron = "*/5 * * * * ?") // this method will be executed every 5 min
	public void deleteExpiredRowsFromPreBookingEntity() {
		preBookingRepository.deleteByExpiryAtBefore(utils.getCurrentDate());
	}
}
