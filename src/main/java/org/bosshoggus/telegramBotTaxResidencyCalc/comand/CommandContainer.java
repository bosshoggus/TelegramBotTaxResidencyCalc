package org.bosshoggus.telegramBotTaxResidencyCalc.comand;

import com.google.common.collect.ImmutableMap;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.TripCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final UsersProfileMongoRepository usersProfileMongoRepository;
    private final ChangeBotStatusTypeService changeBotStatusTypeService;
    private final TripCalculationService tripCalculationService;
    private final ApplicationContext context;

    @Autowired
    public CommandContainer(UsersProfileMongoRepository usersProfileMongoRepository,
                            ChangeBotStatusTypeService changeBotStatusTypeService, TripCalculationService tripCalculationService, ApplicationContext context) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
        this.tripCalculationService = tripCalculationService;
        this.context = context;
        this.commandMap = ImmutableMap.<String, Command>builder()
                .put(CommandType.START.getCommandName(), context.getBean(StartCommand.class))
                .put(CommandType.HELP.getCommandName(), context.getBean(HelpCommand.class))
                .put(CommandType.ADD_DATA.getCommandName(), context.getBean(AddDataCommand.class))
                .put(CommandType.SEE_DATA.getCommandName(), context.getBean(SeeDataCommand.class))
                .put(CommandType.DELETE_ONE_DATA.getCommandName(), context.getBean(DeleteOneDataCommand.class))
                .put(CommandType.DELETE_ALL_DATA.getCommandName(), context.getBean(DeleteAllDataCommand.class))
                .put(CommandType.GET.getCommandName(), context.getBean(GetCalculateDateCommand.class))
                .put(CommandType.TIMER.getCommandName(), context.getBean(TimerCommand.class))
                .put(CommandType.DELETE_TIMER.getCommandName(), context.getBean(DeleteTimerCommand.class))
                .put(CommandType.CLOSE.getCommandName(), context.getBean(CloseCommand.class))
                .put(CommandType.UNKNOWN.getCommandName(), context.getBean(UnknownCommand.class))
                .build();
    }

    public Command findCommand(String commandName) {
        if (commandMap.containsKey(commandName)) {
            return commandMap.get(commandName);
        }
        return context.getBean(UnknownCommand.class);
    }
}
