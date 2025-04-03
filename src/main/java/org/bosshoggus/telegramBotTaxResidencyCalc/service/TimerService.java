package org.bosshoggus.telegramBotTaxResidencyCalc.service;

import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDate;
import java.util.List;

@Service
public class TimerService {
    private final TelegramBot telegramBot;
    private final UsersProfileMongoRepository usersProfileMongoRepository;

    @Autowired
    public TimerService(TelegramBot telegramBot, UsersProfileMongoRepository usersProfileMongoRepository) {
        this.telegramBot = telegramBot;
        this.usersProfileMongoRepository = usersProfileMongoRepository;
    }

    @Scheduled(cron = "0 30 12 * * *")
    public void compareDateForAllUsers() {
        List<UserProfileData> allUsers = usersProfileMongoRepository.findAll();
        for (UserProfileData users : allUsers) {
            if (users.getDateForTimer().equals(LocalDate.now())) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(users.getChatId());
                sendMessage.setText("НАПОМИНАНИЕ!");
                try {
                    telegramBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
