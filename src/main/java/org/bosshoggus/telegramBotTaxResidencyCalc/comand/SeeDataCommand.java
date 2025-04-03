package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class SeeDataCommand implements Command {

    private final UsersProfileMongoRepository usersProfileMongoRepository;

    public SeeDataCommand( UsersProfileMongoRepository usersProfileMongoRepository) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        String message;
        if (userProfileData.getRidesList().isEmpty()) {
            message = "У вас нет записаных поездок,что бы добавить поездку воспользуйтесь командой /addmydata";
            sendMessage.setText(message);
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (String s : userProfileData.getRidesList()) {
                sb.append(count).append(") ").append(s).append("\n");
                count++;
            }
            message = sb.toString();
            sendMessage.setText(message);
        }
        return sendMessage;
    }
}
