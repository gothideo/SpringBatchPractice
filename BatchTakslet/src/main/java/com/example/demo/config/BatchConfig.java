package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("HelloTasklet")
	private Tasklet helloTasklet;
	
	@Autowired
	@Qualifier("HelloTasklet2")
	private Tasklet helloTasklet2;
	
	@Bean
	public JobParametersValidator defaultValidator() {
		DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
		
		// RunIdIncrementerを使ってバッチIDをインクリメントしている場合、"run.id"を必須入力パラメータに指定する必要がある。ただしパラメータとしてrun.idを渡す必要はない。
		String[] requiredKeys = new String[] {"run.id","require1"};
		validator.setRequiredKeys(requiredKeys);
		
		String[] optionalKeys = new String[] {"option1"};
		validator.setOptionalKeys(optionalKeys);
		
		validator.afterPropertiesSet();
		
		return validator;
		
	}
	
	@Bean
	public Step taskletStep1() {
		return stepBuilderFactory.get("HelloTaskletStep1").tasklet(helloTasklet).build();
	}
	
	@Bean
	public Step taskletStep2() {
		return stepBuilderFactory.get("HelloTaskletStep2").tasklet(helloTasklet2).build();
	}
	
	@Bean
	public Job taskletJob() throws Exception {
		return jobBuilderFactory.get("HelloWorldTaskletJob")
				.incrementer(new RunIdIncrementer())
				.start(taskletStep1()).next(taskletStep2()).validator(defaultValidator()).build();
	}

}
