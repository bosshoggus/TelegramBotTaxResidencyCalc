package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class DeleteAllDataCommand implements Command {
    private final UsersProfileMongoRepository usersProfileMongoRepository;
    private final ChangeBotStatusTypeService changeBotStatusTypeService;

    public DeleteAllDataCommand(
            UsersProfileMongoRepository usersProfileMongoRepository,
            ChangeBotStatusTypeService changeBotStatusTypeService){
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
    }


    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        String message;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userProfileData.getRidesList().isEmpty()) {
            message = "У вас нет записаных поездок и я не могу ничего удалить.\n" +
                    "Что бы добавить поездку воспользуйтесь командой /addmydata";
            sendMessage.setText(message);
            return sendMessage;
        } else {
            message = "Вы уверены что хотите удалить все поездки?\nОтветьте да или нет";
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_DELETE_ALL_DATE, chatId);
            sendMessage.setText(message);
        }
        return sendMessage;
    }
}
