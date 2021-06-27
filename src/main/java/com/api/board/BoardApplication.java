package com.api.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.board.scheduler.aCmaQuartzScheduler;

@SpringBootApplication
public class BoardApplication {
	@SuppressWarnings("unused")
	@Autowired
	private aCmaQuartzScheduler scheduler;
	@Autowired
	private aCmaQuartzScheduler scheduler1;

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
