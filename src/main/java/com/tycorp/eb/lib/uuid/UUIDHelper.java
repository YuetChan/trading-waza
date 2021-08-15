package com.tycorp.eb.lib.uuid;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class UUIDHelper {

    public static String generateUUID(){
        return generateType1UUID();
    }

    public static String generateType1UUID(){
        UUID uuid = new UUID(get64MostSignificantBitsForVersion1(), get64LeastSignificantBitsForVersion1());
        return uuid.toString();
    }

    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong + variant3BitFlag;
    }

    private static long get64MostSignificantBitsForVersion1() {
        LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
        Duration duration = Duration.between(start, LocalDateTime.now());
        long seconds = duration.getSeconds();
        long nanos = duration.getNano();
        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
    }

}
