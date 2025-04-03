package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.time.LocalDate;


@Component
public class DeleteTimerCommand implements Command {
    private final UsersProfileMongoRepository usersProfileMongoRepository;

    public DeleteTimerCommand( UsersProfileMongoRepository usersProfileMongoRepository) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        String message;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        if (userProfileData.getDateForTimer() == null) {
            message = "Вы не назначали напоминание,мне нечего удалять";
            sendMessage.setText(message);
        } else {
            LocalDate localDateTimer = userProfileData.getDateForTimer();
            userProfileData.setDateForTimer(null);
            usersProfileMongoRepository.save(userProfileData);
            message = "Вы удалили напоминание на дату: " + localDateTimer;
            sendMessage.setText(message);
        }
        return sendMessage;
    }
}
