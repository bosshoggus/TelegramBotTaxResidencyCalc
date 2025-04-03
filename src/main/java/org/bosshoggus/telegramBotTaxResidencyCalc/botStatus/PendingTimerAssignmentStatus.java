package org.bosshoggus.telegramBotTaxResidencyCalc.botStatus;

import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.time.LocalDate;


@Component
public class PendingTimerAssignmentStatus implements BotStatusStrategy {

    private final ChangeBotStatusTypeService changeBotStatusTypeService;
    private final UsersProfileMongoRepository usersProfileMongoRepository;

    public PendingTimerAssignmentStatus(
            UsersProfileMongoRepository usersProfileMongoRepository,
            ChangeBotStatusTypeService changeBotStatusTypeService){

        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
    }

    @Override
    public SendMessage executeStatus(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String incomingMessage = update.getMessage().getText();
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        String message;
        if (incomingMessage.equals("/close")) {
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
            message = "Воспользуйтесь другой командой";
            sendMessage.setText(message);
        } else {
            try {
                long days = Long.parseLong(incomingMessage);
                LocalDate localDateTimer = LocalDate.now().plusDays(days);
                userProfileData.setDateForTimer(localDateTimer);
                usersProfileMongoRepository.save(userProfileData);
                changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
                message = "Вы назначили таймер - напоминание на " + localDateTimer;
                sendMessage.setText(message);
            } catch (NumberFormatException e) {
                message = "Вы прислали мне не цифру, пришлите пожалуйста цифру через сколько вам прислать сообщение-напоминание\n" +
                        "Если передумали пришлите мне /close";
                sendMessage.setText(message);
            }
        }
        return sendMessage;
    }
}
