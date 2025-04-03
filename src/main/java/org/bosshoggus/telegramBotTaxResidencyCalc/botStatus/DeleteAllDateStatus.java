package org.bosshoggus.telegramBotTaxResidencyCalc.botStatus;

import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DeleteAllDateStatus implements BotStatusStrategy {

    private final UsersProfileMongoRepository usersProfileMongoRepository;
    private final ChangeBotStatusTypeService changeBotStatusTypeService;

    public DeleteAllDateStatus( UsersProfileMongoRepository usersProfileMongoRepository,
                               ChangeBotStatusTypeService changeBotStatusTypeService) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
    }

    @Override
    public SendMessage executeStatus(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String incomingMessage = update.getMessage().getText();
        String message;
        String yes = "Да";
        String no = "Нет";
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        if (incomingMessage.equalsIgnoreCase(yes)) {
            userProfileData.getRidesList().clear();
            usersProfileMongoRepository.save(userProfileData);
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
            message = "Вы успешно удалили все ваши поездки";
            sendMessage.setText(message);
        } else if (incomingMessage.equalsIgnoreCase(no)) {
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
            message = "Вы ничего не удалили,воспользуйтесь другой командой";
            sendMessage.setText(message);
        } else {
            message = "Напишите да, если хотите удалить все ваши поездки\n" +
                    "Напишите нет если передумали удалять";
            sendMessage.setText(message);
        }
        return sendMessage;
    }

}
