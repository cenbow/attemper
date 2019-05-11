package com.github.attemper.config.scheduler.conf;

import com.github.attemper.autoconfigure.GrpcClientConfiguration;
import com.github.attemper.config.base.conf.ConfigConfiguration;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.config.scheduler.service.JobCallingService;
import com.github.attemper.factory.DiscoveryClientChannelFactory;
import com.github.quartz.spring.boot.autoconfigure.SseQuartzAutoConfiguration;
import io.grpc.Channel;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        ConfigConfiguration.class,
        GrpcClientConfiguration.class,
        SseQuartzAutoConfiguration.class
})
@EnableAutoConfiguration(exclude = {
        QuartzAutoConfiguration.class
})
@Configuration
@ComponentScan(basePackageClasses = {
        JobCallingService.class
})
public class ConfigSchedulerConfiguration {

    @Bean
    public Channel channel(DiscoveryClientChannelFactory channelFactory) {
        return channelFactory.create();
    }

    @Autowired
    private AppProperties appProperties;

    /**
     * @param schedulerFactory
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        if (appProperties.getWeb().isEnableScheduling()) {
            if (appProperties.getScheduler().getDelayedInSecond() > 0) {
                scheduler.startDelayed(appProperties.getScheduler().getDelayedInSecond());
            } else {
                scheduler.start();
            }
        }
        return scheduler;
    }
}