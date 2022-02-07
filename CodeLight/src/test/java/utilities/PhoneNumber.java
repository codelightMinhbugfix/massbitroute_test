package utilities;

import java.time.LocalDateTime;

public class PhoneNumber {

    public static String createNew() {

        LocalDateTime currentDateTime = LocalDateTime.now();

        String year = String.valueOf(currentDateTime.getYear());
        String month = String.valueOf(currentDateTime.getMonthValue());
        String day = String.valueOf(currentDateTime.getDayOfMonth());
        String hour = String.valueOf(currentDateTime.getHour());
        String minute = String.valueOf(currentDateTime.getMinute());
        String second = String.valueOf(currentDateTime.getSecond());

        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        if (hour.length() == 1) hour = "0" + hour;
        if (minute.length() == 1) minute = "0" + minute;
        if (second.length() == 1) second = "0" + second;

        String uniquePhoneNumber = year.charAt(year.length() - 1) + month + day + hour + minute + second;

        return uniquePhoneNumber;
    }

    public static String[] createNew(int numberOfPhoneNumber) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        String year = String.valueOf(currentDateTime.getYear());
        String month = String.valueOf(currentDateTime.getMonthValue());
        String day = String.valueOf(currentDateTime.getDayOfMonth());
        String hour = String.valueOf(currentDateTime.getHour());
        String minute = String.valueOf(currentDateTime.getMinute());

        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        if (hour.length() == 1) hour = "0" + hour;
        if (minute.length() == 1) minute = "0" + minute;

        String[] uniquePhoneNumbers = new String[numberOfPhoneNumber+1];
        int secondInt = currentDateTime.getSecond();
        String second;

        for (int i = 1; i <= numberOfPhoneNumber; i++) {
            second = String.valueOf(secondInt + i);
            if (second.length() == 1) second = "0" + second;
            uniquePhoneNumbers[i] = year.charAt(year.length() - 1) + month + day + hour + minute + second;
        }

        return uniquePhoneNumbers;
    }
}
