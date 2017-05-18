package com.jifenke.lepluslive.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.inject.Inject;
import javax.sql.DataSource;


/**
 * Created by wcg on 16/3/27.
 */
@Configuration
public class SchedulerConfigration {

  @Inject
  private DataSource dataSource;

  @Autowired
  private ResourceLoader resourceLoader;

  @Bean(name = "qrCodeJob")
  public JobDetailFactoryBean qrCodeJob() {
    JobDetailFactoryBean bean = new JobDetailFactoryBean();
    bean.setJobClass(QrCodeJob.class);
    bean.setDurability(false);
    return bean;
  }

  @Bean(name = "qrCodeJobTrigger")
  public CronTriggerFactoryBean qrCodeJobTriggerBean() {
    CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
    tigger.setJobDetail(qrCodeJob().getObject());
    try {
      tigger.setCronExpression("0 0 12 * * ? ");//每天中午12点执行
//      tigger.setCronExpression("0 */1 * * * ?");//测试：每隔一分钟执行一次
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tigger;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactory() {
    SchedulerFactoryBean bean = new SchedulerFactoryBean();
    bean.setConfigLocation(resourceLoader.getResource("classpath:quartz.properties"));
    bean.setApplicationContextSchedulerContextKey("applicationContextKey");
    bean.setDataSource(dataSource);
    bean.setTriggers(qrCodeJobTriggerBean().getObject());
    bean.setSchedulerName("qrCodeJob");
    return bean;
  }
}
