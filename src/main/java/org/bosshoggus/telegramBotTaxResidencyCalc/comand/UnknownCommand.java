package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class UnknownCommand implements Command {


    public UnknownCommand() {
    }

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message = "Я вас не понимаю, воспользуйтесь командой /help для помощи";
        sendMessage.setText(message);

        return sendMessage;
    }
}
