package com.izzydrive.backend.jobs;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.service.driving.DrivingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PaymentSessionExpiredTask {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSessionExpiredTask.class);

    private final DrivingService drivingService;

    @Scheduled(cron = "${payment-session-expired-job.cron}")
    public void cancelDrivingForExpiredPaymentSession() {
        LOG.info("> job started");
        /**
         * Putnik ima pravo da pravi od 15 - 10 min pre pocetka voznje
         * TODO:: proveriti ako se radi o rezervaciji i ako je ostalo manje od 10 min
         * do pocetka onda je potrebno obrisati rezervaciju i baciti svim putnicima, your
         * payment session expired, canceling reserved driving.
         * Obrisati driving, i skinuti reserved from client reservation na driveru
         * i reservation na putniku
         * */
        List<Driving> drivings = drivingService.getAllDrivingsInStatusPayment();
        for (Driving d : drivings) {
            if (drivingService.drivingExpiredForPayment(d) && !d.isLocked()) {
                try {
                    drivingService.removeDrivingPaymentSessionExpired(d.getId());
                    LOG.info(String.format("> driving with id:%d successfully removed", d.getId()));
                } catch (OptimisticLockingFailureException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }
}
