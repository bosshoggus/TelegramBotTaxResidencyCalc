package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
public class StartCommand implements Command {

    public StartCommand() {
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String name = update.getMessage().getChat().getFirstName();
        String message = "Привет, " + name + ". Спасибо что воспользовались ботом";
        sendMessage.setText(message);
        return sendMessage;
    }
}
