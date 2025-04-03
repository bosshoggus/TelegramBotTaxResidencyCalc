package org.bosshoggus.telegramBotTaxResidencyCalc.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersProfileMongoRepository extends MongoRepository<UserProfileData, Long> {
    UserProfileData findByChatId(long chatId);
}
