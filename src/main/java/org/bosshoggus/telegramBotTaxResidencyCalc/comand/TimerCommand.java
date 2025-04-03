package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class TimerCommand implements Command {
    private final UsersProfileMongoRepository usersProfileMongoRepository;
    private final ChangeBotStatusTypeService changeBotStatusTypeService;

    public TimerCommand(
            UsersProfileMongoRepository usersProfileMongoRepository,
            ChangeBotStatusTypeService changeBotStatusTypeService) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message;
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        if (userProfileData.getDateForTimer() != null) {
            message = "У вас уже есть дата для напоминания, если хотите ее изменить сначала удалите старую командой /deletetimer";
            sendMessage.setText(message);
        } else {
            message = "Напишите цифрами количество дней,через сколько вам напомнить\n" +
                    "Если хотите отменить этой действие, введите /close";
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_ADD_TIMER, chatId);
            sendMessage.setText(message);
        }
        return sendMessage;
    }
}
