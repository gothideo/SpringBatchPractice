package com.example.demo.chunk;

import java.rmi.UnexpectedException;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class HelloReader implements ItemReader<String> {
	
	private String[] input = {"Hello","World","hoge","fuga",null,"The World"};
	private int index = 0;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
		jobContext.put("jobKey", "jobValue");
		
		ExecutionContext stepContext = stepExecution.getExecutionContext();
		stepContext.put("stepKey", "stepValue");
	}
	
	//readerがnullを返すと、stepは終了する
	@Override
	public String read() throws Exception, UnexpectedException,ParseException,NonTransientResourceException{
		String message = input[index++];
		log.info("Read:{}",message);
		
		return message;
	}

}
