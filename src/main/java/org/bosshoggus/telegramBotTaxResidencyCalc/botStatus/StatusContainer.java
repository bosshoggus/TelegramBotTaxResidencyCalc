package org.bosshoggus.telegramBotTaxResidencyCalc.botStatus;

import com.google.common.collect.ImmutableMap;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.bosshoggus.telegramBotTaxResidencyCalc.service.ChangeBotStatusTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StatusContainer {
    private final ImmutableMap<BotStatusType, BotStatusStrategy> statusMap;
    private final UsersProfileMongoRepository usersProfileMongoRepository;
    private final ChangeBotStatusTypeService changeBotStatusTypeService;
    private final ApplicationContext context;

    @Autowired
    public StatusContainer(
            UsersProfileMongoRepository usersProfileMongoRepository,
            ChangeBotStatusTypeService changeBotStatusTypeService,
            ApplicationContext context) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
        this.changeBotStatusTypeService = changeBotStatusTypeService;
        this.context = context;
        this.statusMap = ImmutableMap.<BotStatusType, BotStatusStrategy>builder()
                .put(BotStatusType.STATUS_LOAFER, new LoaferStatus())
                .put(BotStatusType.STATUS_FILLING_DATES, context.getBean(FillingDateStatus.class))
                .put(BotStatusType.STATUS_DELETE_ONE_DATE, context.getBean(DeleteOneDateStatus.class))
                .put(BotStatusType.STATUS_DELETE_ALL_DATE, context.getBean(DeleteAllDateStatus.class))
                .put(BotStatusType.STATUS_ADD_TIMER, context.getBean(PendingTimerAssignmentStatus.class))
                .build();

    }


    public void setBotStatus(BotStatusType botStatusType, long chatId) {
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        userProfileData.setBotStatusType(botStatusType);
        usersProfileMongoRepository.save(userProfileData);
    }

    public BotStatusStrategy findBotStatus(BotStatusType botStatusType) {
        return statusMap.get(botStatusType);
    }
}
