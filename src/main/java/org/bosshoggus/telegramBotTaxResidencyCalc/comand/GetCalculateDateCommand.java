package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import lombok.extern.slf4j.Slf4j;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.TripCalculationService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class GetCalculateDateCommand implements Command {

    private final UsersProfileMongoRepository usersProfileMongoRepository;


    public GetCalculateDateCommand(UsersProfileMongoRepository usersProfileMongoRepository) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message;
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        if (userProfileData.getRidesList().isEmpty()) {
            message = "У вас нет записаных поездок, сначала добавьте их командой /addmydata";
            sendMessage.setText(message);
        } else {
            TripCalculationService tripCalculationService = new TripCalculationService(new ArrayList<>());
            List<String> userList = userProfileData.getRidesList();
            for (String s : userList) {
                tripCalculationService.addRidesToList(startDayForParsingStringToDate(s),endDayForParsingStringToDate(s));
            }
            long howMuchDays = tripCalculationService.getDaysCountFoFreedom();
            message = "Количество оставшихся дней до окончания налогового резидентства: " + howMuchDays;
            sendMessage.setText(message);
            log.info(userProfileData.getUserName() + "Calculate command execute");
        }
        return sendMessage;
    }

    private LocalDate startDayForParsingStringToDate(String messageForParsing) {
        String[] arr = messageForParsing.trim().split(" ");
        int day = Integer.parseInt(arr[0]);
        int mounth = Integer.parseInt(arr[1]);
        int year = Integer.parseInt(arr[2]);
        return LocalDate.of(year, mounth, day);
    }

    private LocalDate endDayForParsingStringToDate(String forParsing) {
        String[] arr = forParsing.trim().split(" ");
        int day = Integer.parseInt(arr[4]);
        int mounth = Integer.parseInt(arr[5]);
        int year = Integer.parseInt(arr[6]);
        return LocalDate.of(year, mounth, day);
    }
}
