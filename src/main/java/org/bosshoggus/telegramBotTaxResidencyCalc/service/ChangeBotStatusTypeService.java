package org.bosshoggus.telegramBotTaxResidencyCalc.service;

import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UserProfileData;
import org.bosshoggus.telegramBotTaxResidencyCalc.database.UsersProfileMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChangeBotStatusTypeService {
    private final UsersProfileMongoRepository usersProfileMongoRepository;

    @Autowired
    public ChangeBotStatusTypeService( UsersProfileMongoRepository usersProfileMongoRepository) {
        this.usersProfileMongoRepository = usersProfileMongoRepository;
    }

    public void changeBotStatus(BotStatusType botStatusType, long chatId) {
        UserProfileData userProfileData = usersProfileMongoRepository.findByChatId(chatId);
        userProfileData.setBotStatusType(botStatusType);
        usersProfileMongoRepository.save(userProfileData);
    }
}
