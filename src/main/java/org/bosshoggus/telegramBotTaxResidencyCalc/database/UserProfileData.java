package org.bosshoggus.telegramBotTaxResidencyCalc.database;

import lombok.Data;
import org.bosshoggus.telegramBotTaxResidencyCalc.botStatus.BotStatusType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Data
@Document("userProfileData")
public class UserProfileData implements Serializable {
    UsersProfileMongoRepository usersProfileMongoRepository;
    @Id
    private Long chatId;
    private String userName;
    private String firstName;
    private String secondName;
    private List<String> ridesList;
    private String registeredAt;
    private BotStatusType botStatusType;
    private LocalDate dateForTimer;

    public UserProfileData(Long chatId, String userName, String firstName, String secondName, List<String> ridesList,
                           String registeredAt, BotStatusType botStatusType, LocalDate dateForTimer) {
        super();
        this.chatId = chatId;
        this.userName = userName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.ridesList = ridesList;
        this.registeredAt = registeredAt;
        this.botStatusType = botStatusType;
        this.dateForTimer = dateForTimer;
    }

    public UserProfileData() {
    }


    @Override
    public String toString() {
        return "UserProfileData{" +
                "chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", ridesList=" + ridesList +
                ", registeredAt='" + registeredAt + '\'' +
                ", botStatusType=" + botStatusType +
                ", dateForTimer=" + dateForTimer +
                '}';
    }
}

