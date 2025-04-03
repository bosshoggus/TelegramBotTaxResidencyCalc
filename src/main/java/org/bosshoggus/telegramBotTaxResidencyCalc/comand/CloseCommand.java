package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class CloseCommand implements Command {
    private final UsersProfileMongoRepository usersProfileMongoRepository;

    private final ChangeBotStatusTypeService changeBotStatusTypeService;

    public CloseCommand(
            UsersProfileMongoRepository usersProfileMongoRepository,
            ChangeBotStatusTypeService changeBotStatusTypeService
    ) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
        String message = "Воспользуйтесь любой другой командой";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }
}
