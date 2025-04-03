package org.bosshoggus.telegramBotTaxResidencyCalc.botStatus;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotStatusStrategy {
    SendMessage executeStatus(Update update);
}
