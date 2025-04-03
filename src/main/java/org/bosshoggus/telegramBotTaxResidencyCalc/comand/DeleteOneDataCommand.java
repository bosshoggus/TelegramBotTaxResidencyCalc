package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DeleteOneDataCommand implements Command {

    private final UsersProfileMongoRepository usersProfileMongoRepository;
    private final ChangeBotStatusTypeService changeBotStatusTypeService;

    public DeleteOneDataCommand(
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
            message = "У вас нет записаных поездок и я не могу ничего удалить,что бы добавить поездку воспользуйтесь командой /addmydata";
            sendMessage.setText(message);
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (String s : userProfileData.getRidesList()) {
                sb.append(count).append(") ").append(s).append("\n");
                count++;
            }
            sb.append("Отправьте цифру,той поездки которую хотите удалить\nЕсли передумали отправьте команду /close");
            message = sb.toString();
            sendMessage.setText(message);
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_DELETE_ONE_DATE, chatId);
        }
        return sendMessage;
    }
}
