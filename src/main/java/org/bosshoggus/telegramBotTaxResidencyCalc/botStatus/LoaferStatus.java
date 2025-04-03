package org.bosshoggus.telegramBotTaxResidencyCalc.botStatus;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class LoaferStatus implements BotStatusStrategy {

    public LoaferStatus() {
    }

    @Override
    public SendMessage executeStatus(Update update) {
        return null;
    }
}
