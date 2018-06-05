/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.eventnotification.config;

import static org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.egov.eventnotification.scheduler.NotificationSchedulerJob;
import org.egov.infra.config.scheduling.QuartzSchedulerConfiguration;
import org.egov.infra.config.scheduling.SchedulerConfigCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@Conditional(SchedulerConfigCondition.class)
public class NotificationSchedulerConfiguration extends QuartzSchedulerConfiguration {

    @Bean(destroyMethod = "destroy")
    public SchedulerFactoryBean notificationScheduler(DataSource dataSource) {
        SchedulerFactoryBean ptisScheduler = createScheduler(dataSource);
        ptisScheduler.setSchedulerName("eventnotification-scheduler");
        ptisScheduler.setAutoStartup(true);
        ptisScheduler.setOverwriteExistingJobs(true);
        ptisScheduler.setTriggers(
                eventnotificationCronTrigger().getObject());
        return ptisScheduler;
    }

    @Bean(name = "eventnotificationScheduler", destroyMethod = "destroy")
    public SchedulerFactoryBean eventnotificationScheduler(DataSource dataSource) {
        SchedulerFactoryBean recoveryNoticeScheduler = createScheduler(dataSource);
        recoveryNoticeScheduler.setSchedulerName("notification-scheduler");
        recoveryNoticeScheduler.setAutoStartup(true);
        recoveryNoticeScheduler.setOverwriteExistingJobs(true);
        recoveryNoticeScheduler.setStartupDelay(1500);
        return recoveryNoticeScheduler;
    }

    @Bean
    public CronTriggerFactoryBean eventnotificationCronTrigger() {
        CronTriggerFactoryBean demandActivationCron = new CronTriggerFactoryBean();
        demandActivationCron.setJobDetail(eventnotificationJobDetail().getObject());
        demandActivationCron.setGroup("EVENT_NOTIFICATION_TRIGGER_GROUP");
        demandActivationCron.setName("EVENT_NOTIFICATION_TRIGGER");
        demandActivationCron.setCronExpression("0 15 0 * * ?");
        demandActivationCron.setMisfireInstruction(MISFIRE_INSTRUCTION_DO_NOTHING);
        return demandActivationCron;
    }

    @Bean(name = "eventnotificationJobDetail")
    public JobDetailFactoryBean eventnotificationJobDetail() {
        JobDetailFactoryBean notificationJobDetail = new JobDetailFactoryBean();
        notificationJobDetail.setGroup("EVENT_NOTIFICATION_JOB_GROUP");
        notificationJobDetail.setName("EVENT_NOTIFICATION_JOB");
        notificationJobDetail.setDurability(true);
        notificationJobDetail.setJobClass(NotificationSchedulerJob.class);
        notificationJobDetail.setRequestsRecovery(true);
        Map<String, String> jobDetailMap = new HashMap<>();
        jobDetailMap.put("jobBeanName", "notificationJob");
        jobDetailMap.put("userName", "system");
        jobDetailMap.put("cityDataRequired", "true");
        jobDetailMap.put("moduleName", "eventnotification");
        notificationJobDetail.setJobDataAsMap(jobDetailMap);
        return notificationJobDetail;
    }

    @Bean("notificationJob")
    public NotificationSchedulerJob notificationJob() {
        return new NotificationSchedulerJob();
    }

}
