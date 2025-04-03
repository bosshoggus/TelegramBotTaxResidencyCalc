package org.bosshoggus.telegramBotTaxResidencyCalc.service;


import lombok.extern.slf4j.Slf4j;
import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.StatusContainer;
import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.bosshoggus.telegramBotTaxResidencyCalc.comand.*;
import org.bosshoggus.telegramBotTaxResidencyCalc.config.BotConfig;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    @Autowired
    private UsersProfileMongoRepository usersProfileMongoRepository;
    @Autowired
    private final CommandContainer commandContainer;
    @Autowired
    private final StatusContainer statusContainer;

    public TelegramBot(BotConfig botConfig, CommandContainer commandContainer, StatusContainer statusContainer,
                       UsersProfileMongoRepository usersProfileMongoRepository) {
        this.botConfig = botConfig;
        this.commandContainer = commandContainer;
        this.statusContainer = statusContainer;
        List<BotCommand> listOfCommands = createListOfCommands();
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list " + e.getMessage());
        }
    }

    private List<BotCommand> createListOfCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(CommandType.START.getCommandName(), CommandType.START.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.HELP.getCommandName(), CommandType.HELP.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.ADD_DATA.getCommandName(), CommandType.ADD_DATA.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.SEE_DATA.getCommandName(), CommandType.SEE_DATA.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.DELETE_ONE_DATA.getCommandName(), CommandType.DELETE_ONE_DATA.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.DELETE_ALL_DATA.getCommandName(), CommandType.DELETE_ALL_DATA.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.GET.getCommandName(), CommandType.GET.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.TIMER.getCommandName(), CommandType.TIMER.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.DELETE_TIMER.getCommandName(), CommandType.DELETE_TIMER.getDescription()));
        listOfCommands.add(new BotCommand(CommandType.CLOSE.getCommandName(), CommandType.CLOSE.getDescription()));
        return listOfCommands;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            Message message = update.getMessage();
            SendMessage sendMessage;
            UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
            try {
                if (userProfileData == null) {
                    registerUser(update);
                    sendMessage = commandContainer.findCommand(message.getText()).executeCommand(update);
                    this.execute(sendMessage);
                } else {
                    if (userProfileData.getBotStatusType() != BotStatusType.STATUS_LOAFER) {
                        sendMessage = statusContainer.findBotStatus(userProfileData.getBotStatusType()).executeStatus(update);
                        execute(sendMessage);
                    } else {
                        sendMessage = commandContainer.findCommand(message.getText()).executeCommand(update);
                        this.execute(sendMessage);
                    }
                }
            } catch (TelegramApiException e) {
                log.error("Error occured " + e);
            }
        }
    }

    private void registerUser(Update update) {
        if (usersProfileMongoRepository.findById(update.getMessage().getChatId()).isEmpty()) {
            var chatId = update.getMessage().getChatId();
            var chat = update.getMessage().getChat();

            UserProfileData userProfileData = new UserProfileData();
            userProfileData.setChatId(chatId);
            userProfileData.setUserName(chat.getUserName());
            userProfileData.setFirstName(chat.getFirstName());
            userProfileData.setSecondName(chat.getLastName());
            userProfileData.setRidesList(new ArrayList<>());
            userProfileData.setRegisteredAt(LocalDateTime.now().toString());
            userProfileData.setBotStatusType(BotStatusType.STATUS_LOAFER);
            userProfileData.setDateForTimer(null);

            usersProfileMongoRepository.save(userProfileData);
            log.info("User saved: " + userProfileData);
        }
    }
}
