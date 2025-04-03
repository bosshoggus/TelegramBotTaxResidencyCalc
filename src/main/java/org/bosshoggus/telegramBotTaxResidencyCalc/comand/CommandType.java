package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import lombok.Getter;


public enum CommandType {
    START("/start", "Начни работу с ботом"),
    HELP("/help", "Информация для пользователя"),
    ADD_DATA("/addmydata", "Добавить дату"),
    SEE_DATA("/seemydata", "Увидеть список поездок"),
    DELETE_ONE_DATA("/deletemydata", "Удалить одну поездку"),
    DELETE_ALL_DATA("/deleteallmydata", "Удалить все поездки"),
    GET("/get", "Сколько дней осталось?"),
    TIMER("/timer", "Назначить дату для сообщения - напоминания"),
    DELETE_TIMER("/deletetimer", "Удалить дату для сообщения - напоминания"),
    CLOSE("/close", "Отменить любую команду"),
    UNKNOWN("/default", "Дефолтная команда");

    @Getter
    private final String commandName;
    @Getter
    private final String description;

    CommandType(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }

}
