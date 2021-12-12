package com.example.bookMyShow.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.stereotype.Component;
import com.example.bookMyShow.exception.ServiceException;

@Slf4j
@Component
public class Utils
{
	public void throwServiceException(String message) throws ServiceException
	{
		log.info(message);
		throw new ServiceException(message);
	}

	public Timestamp getCurrentTimeStamp() {
		return Timestamp.valueOf(LocalDateTime.now());
	}

	@SneakyThrows
	public Date getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateStr = dateFormat.format(new Date());
		return dateFormat.parse(currentDateStr);
	}

	@SneakyThrows
	public Date getFormattedDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateStr = dateFormat.format(date);
		return dateFormat.parse(currentDateStr);
	}

	@SneakyThrows
	public Date getDateFromStr(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat.parse(date);
	}

	@SneakyThrows
	public String getStrDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat.format(date);
	}

}
