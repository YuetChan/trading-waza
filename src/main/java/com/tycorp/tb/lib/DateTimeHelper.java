package com.tycorp.tb.lib;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeHelper {

    public static Long zonedDateTimeToEpoch(ZonedDateTime zdt) {
        return zdt.toInstant().toEpochMilli();
    }

    public static ZonedDateTime truncateTime(ZonedDateTime zdt) {
        ZoneId zoneId  = zdt.getZone();
        return zdt.toLocalDate().atStartOfDay(zoneId);
    }

}
