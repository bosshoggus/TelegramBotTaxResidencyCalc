package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class HelpCommand implements Command {

    public HelpCommand() {
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message = """
                Этот бот поможет вам узнать являетесь ли вы налоговым
                резидентом в РФ. Он так же может хранить даты ваших
                поездок и выдавать или удалять их по вашему запросу.
                Вы можете пользоваться несколькими типами команд:
                /start  -  начать работать с ботом
                /addmydata  -  добавить поездку
                /seemydata  -  увидеть ваши поездки
                /deletemytedata  -  удалить одну вашу поездку
                /deleteallmydata  -  удалить все ваши поездки
                /get  - расчитать количество дней оставшихся в запасе
                /close  -  Отменить любую команду
                /timer  -  установить напоминание в указаную дату и время
                /deletetimer  -  удалить ваше напоминание
                /close  -  для отмены какой либо команды""";
        sendMessage.setText(message);
        return sendMessage;
    }
}
