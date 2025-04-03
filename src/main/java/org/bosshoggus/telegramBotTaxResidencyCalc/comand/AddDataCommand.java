package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AddDataCommand implements Command {
    final private UsersProfileMongoRepository usersProfileMongoRepository;
    final private ChangeBotStatusTypeService changeBotStatusTypeService;


    public AddDataCommand(UsersProfileMongoRepository usersProfileMongoRepository,
                          ChangeBotStatusTypeService changeBotStatusTypeService){
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        String message = """
                Давайте добавим даты поездок!
                Введи ее в формате дд мм гггг - дд мм гггг
                Где первая дата - дата прибытия, а вторая дата убытия,
                Если передумали писать дату напишите /close""";
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_FILLING_DATES, chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }
}
