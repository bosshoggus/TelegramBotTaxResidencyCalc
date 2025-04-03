package org.bosshoggus.telegramBotTaxResidencyCalc.botStatus;

import lombok.extern.slf4j.Slf4j;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
public class DeleteOneDateStatus implements BotStatusStrategy {

    private final UsersProfileMongoRepository usersProfileMongoRepository;
    private final ChangeBotStatusTypeService changeBotStatusTypeService;

    public DeleteOneDateStatus(
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
        String incomingMessage = update.getMessage().getText();
        String message;
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);


        if (incomingMessage.equals("/close")) {
            changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
            message = "Воспользуйтесь другой командой";
            sendMessage.setText(message);
        } else {
            try {
                userProfileData.getRidesList().remove(Integer.parseInt(incomingMessage) - 1);
                usersProfileMongoRepository.save(userProfileData);
                changeBotStatusTypeService.changeBotStatus(BotStatusType.STATUS_LOAFER, chatId);
                message = """
                        Вы успешно удалили дату поездки,если хотите удалить еще одну, 
                        воспользуйтесь командой /deletemydata
                        Если хотите увидеть ваши поездки воспользуйтесь командой /seemydata""";
                sendMessage.setText(message);
            } catch (NumberFormatException e) {
                message = """
                        Вы явно прислали мне не цифру.
                        Пришлите цифру существующей поездки.
                        Если передумали удалять поездку, отправьте команду /close""";
                log.error("Error occured " + e);
                sendMessage.setText(message);
            } catch (IndexOutOfBoundsException e) {
                message = """
                        Некорректная цифра! У вас их поменьше! :)
                        Попробуйте еще раз, у вас получится!
                        Если передумали удалять поездку, отправьте команду /close""";
                log.error("Error occurred " + e);
                sendMessage.setText(message);

            }
        }
        return sendMessage;
    }

}
