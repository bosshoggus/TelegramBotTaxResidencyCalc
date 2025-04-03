package org.bosshoggus.telegramBotTaxResidencyCalc.botStatus;

import lombok.extern.slf4j.Slf4j;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.time.DateTimeException;
import java.time.LocalDate;

@Slf4j
@Component
public class FillingDateStatus implements BotStatusStrategy {

    private final UsersProfileMongoRepository usersProfileMongoRepository;

    private final ChangeBotStatusTypeService changeBotStatusTypeService;


    public FillingDateStatus(
            UsersProfileMongoRepository usersProfileMongoRepository,
            ChangeBotStatusTypeService changeBotStatusTypeService) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;

    }


    @Override
    public SendMessage executeStatus(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String incomeMessage = update.getMessage().getText();
        String message;
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        if (incomeMessage.equalsIgnoreCase("/close")) {
            message = "Воспользуйтесь другой командой";
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
            sendMessage.setText(message);
        } else {
            if (userProfileData.getRidesList().contains(incomeMessage)) {
                message = "Такая дата уже есть,попробуйте ввести еще раз,либо используйте команду /close";
                sendMessage.setText(message);
            } else if (isThisTheCorrectDate(incomeMessage)) {
                try {
                    LocalDate localDateStart = startDayForParsingStringToDate(incomeMessage);
                    LocalDate localDateEnd = endDayForParsingStringToDate(incomeMessage);
                    userProfileData.getRidesList().add(incomeMessage);
                    usersProfileMongoRepository.save(userProfileData);
                    changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
                    message = "Вы успешно добавили дату,если хотите добавить еще воспользуйтесь командой /addmydata";
                    sendMessage.setText(message);
                } catch (DateTimeException e) {
                    message = """
                            Некорректная дата,проверьте возможно в месяце нет столько дней, 
                            а может быть нет столько месяцев в году:)
                            Попробуйте еще раз,у вас получится!
                            Если передумали добавлять дату, отправьте /close""";
                    sendMessage.setText(message);
                    log.error("Error occurred " + e);
                }
            } else {
                message = """
                        Дата должна быть в правильном формате
                        Пример:  01 09 2022 - 31 12 2022
                        Если передумали добавлять дату, отправьте /close""";
                sendMessage.setText(message);
            }
        }
        return sendMessage;
    }

    private boolean isThisTheCorrectDate(String incomingMessage) {
        return incomingMessage.matches("^[0-9]{2} [0-9]{2} [0-9]{4} - [0-9]{2} [0-9]{2} [0-9]{4}$");
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
