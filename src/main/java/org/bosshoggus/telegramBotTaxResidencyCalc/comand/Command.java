package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface Command {
    SendMessage executeCommand(Update update);
}

