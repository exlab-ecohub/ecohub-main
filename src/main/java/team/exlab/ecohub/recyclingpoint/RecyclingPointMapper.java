package team.exlab.ecohub.recyclingpoint;

import team.exlab.ecohub.exception.PatternMismatchException;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.model.RecyclableType;
import team.exlab.ecohub.recyclingpoint.model.RecyclingPoint;
import team.exlab.ecohub.recyclingpoint.model.WorkingHours;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RecyclingPointMapper {
    private static final String WORKING_HOURS_REGEXP = "((Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?:\\s*([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])?;\\s*){0,6}(Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?:\\s*([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])?\\.$";
    private static final Map<String, DayOfWeek> RUS_DAY_TO_DAY_OF_WEEK_MAP = Map.of("Пн", DayOfWeek.MONDAY, "Вт", DayOfWeek.TUESDAY, "Ср", DayOfWeek.WEDNESDAY, "Чт", DayOfWeek.THURSDAY, "Пт", DayOfWeek.FRIDAY, "Сб", DayOfWeek.SATURDAY, "Вс", DayOfWeek.SUNDAY);
    private static final Map<DayOfWeek, String> DAY_OF_WEEK_TO_RUS_DAY_MAP = Map.of(DayOfWeek.MONDAY, "Пн", DayOfWeek.TUESDAY, "Вт", DayOfWeek.WEDNESDAY, "Ср", DayOfWeek.THURSDAY, "Чт", DayOfWeek.FRIDAY, "Пт", DayOfWeek.SATURDAY, "Сб", DayOfWeek.SUNDAY, "Вс");
    private static final Pattern DAY_OR_DAYS_PATTERN = Pattern.compile("(Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?");
    private static final Pattern WORKING_TIME_PATTERN = Pattern.compile("([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])[,|\\.]");
    private static final Pattern LUNCH_TIME_PATTERN = Pattern.compile("(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])");

    private RecyclingPointMapper() {
    }

    public static RecyclingPointDto toDto(RecyclingPoint recyclingPoint) {
        return RecyclingPointDto.builder()
                .id(recyclingPoint.getId())
                .name(recyclingPoint.getName())
                .address(recyclingPoint.getAddress())
                .phoneNumber(recyclingPoint.getPhoneNumber())
                .website(recyclingPoint.getWebsite())
                .location(recyclingPoint.getLocation())
                .workingHours(workingHoursToString(recyclingPoint.getWorkingHours()))
                .recyclableTypes(recyclingPoint.getRecyclableTypes().stream()
                        .map(RecyclableType::getRusName)
                        .collect(Collectors.toSet()))
                .displayed(recyclingPoint.isDisplayed())
                .build();
    }

    public static RecyclingPoint toPoint(RecyclingPointDto recyclingPointDto,
                                         Set<RecyclableType> recyclableTypes) {
        return RecyclingPoint.builder()
                .id(recyclingPointDto.getId())
                .name(recyclingPointDto.getName())
                .address(recyclingPointDto.getAddress())
                .phoneNumber(recyclingPointDto.getPhoneNumber())
                .website(recyclingPointDto.getWebsite())
                .location(recyclingPointDto.getLocation())
                .workingHours(workingHoursToMap(recyclingPointDto.getWorkingHours()))
                .recyclableTypes(recyclableTypes)
                .displayed(recyclingPointDto.isDisplayed())
                .build();
    }

    /*Приведение данных о времени работы пункта переработки к Map<DayOfWeek, WorkingHours> из вида:
    Время работы: Пн-Пт: 09:00-17:00, обед: 13:00-14:00; Сб: 09:00-13:00, обед: 12:00-12:30; Вс: выходной*/
    public static Map<DayOfWeek, WorkingHours> workingHoursToMap(String workingHoursAsString) {
        Map<DayOfWeek, WorkingHours> result = new EnumMap<>(DayOfWeek.class);
        if (workingHoursAsString.matches(WORKING_HOURS_REGEXP)) {
            String[] daysOrRangesOfDaysWithTime = workingHoursAsString.split(";");
            String dayOrDays = "";
            String workingTime = "";
            for (String dayOrDaysWithTime : daysOrRangesOfDaysWithTime) {
                Matcher matcherDayOrDays = DAY_OR_DAYS_PATTERN.matcher(dayOrDaysWithTime);
                Matcher matcherWorkingTime = WORKING_TIME_PATTERN.matcher(dayOrDaysWithTime);
                Matcher matcherLunchTime = LUNCH_TIME_PATTERN.matcher(dayOrDaysWithTime);
                if (matcherDayOrDays.find()) {
                    dayOrDays = matcherDayOrDays.group();
                }
                if (matcherWorkingTime.find()) {
                    workingTime = matcherWorkingTime.group();
                    if (workingTime.endsWith(".")) {
                        workingTime = workingTime.replace(".", ",");
                    }
                }
                LocalTime lunchStartTime = null;
                LocalTime lunchEndTime = null;
                if (matcherLunchTime.find()) {
                    String lunchTime = matcherLunchTime.group().substring(1).trim();
                    lunchStartTime = LocalTime.parse(lunchTime.subSequence(0, lunchTime.indexOf("-")), DateTimeFormatter.ofPattern("HH:mm"));
                    lunchEndTime = LocalTime.parse(lunchTime.subSequence(lunchTime.indexOf("-") + 1, lunchTime.length()), DateTimeFormatter.ofPattern("HH:mm"));
                }
                LocalTime openingTime = LocalTime.parse(workingTime.subSequence(0, workingTime.indexOf("-")).toString(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime closingTime = LocalTime.parse(workingTime.subSequence(workingTime.indexOf("-") + 1, workingTime.indexOf(",")), DateTimeFormatter.ofPattern("HH:mm"));

                String[] startAndEndDays = dayOrDays.split("-");
                int startDay = RUS_DAY_TO_DAY_OF_WEEK_MAP.get(startAndEndDays[0]).getValue();
                int endDay = startAndEndDays.length == 2 ? RUS_DAY_TO_DAY_OF_WEEK_MAP.get(startAndEndDays[1]).getValue() : startDay;
                for (int i = startDay; i <= endDay; i++) {
                    result.put(DayOfWeek.of(i), new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                }
            }
        } else {
            throw new PatternMismatchException("Provided string does not correspond to pattern required");
        }
        return result;
    }

    /*Приведение данных о времени работы пункта переработки к String вида:
    Время работы: Пн-Пт: 09:00-17:00, обед: 13:00-14:00; Сб: 09:00-13:00, обед: 12:00-12:30; Вс: выходной*/
    public static String workingHoursToString(Map<DayOfWeek, WorkingHours> workingHoursMap) {
        StringBuilder sb = new StringBuilder();
        List<DayOfWeek> weekends = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            DayOfWeek firstDay = DayOfWeek.of(i);
            DayOfWeek lastDay = DayOfWeek.of(i);
            WorkingHours firstDayHours = workingHoursMap.get(DayOfWeek.of(i));
            if (firstDayHours == null) {
                weekends.add(firstDay);
                continue;
            }
            for (int j = i + 1; j <= 8; j++) {
                if (j == 8 || !firstDayHours.equals(workingHoursMap.get(DayOfWeek.of(j)))) {
                    lastDay = DayOfWeek.of(j - 1);
                    i = j - 1;
                    break;
                }
            }
            sb.append(DAY_OF_WEEK_TO_RUS_DAY_MAP.get(firstDay))
                    .append(lastDay.equals(firstDay) ? "" : "-" + DAY_OF_WEEK_TO_RUS_DAY_MAP.get(lastDay))
                    .append(": ");
            if (firstDayHours.getOpeningTime() != null && firstDayHours.getClosingTime() != null) {
                sb.append(firstDayHours.getOpeningTime()).append("-").append(firstDayHours.getClosingTime());
                if (firstDayHours.getLunchStartTime() != null && firstDayHours.getLunchEndTime() != null) {
                    sb.append(", обед: ").append(firstDayHours.getLunchStartTime()).append("-").append(firstDayHours.getLunchEndTime());
                }
            }
            sb.append("; ");
        }

        if (!weekends.isEmpty()) {
            for (DayOfWeek weekend : weekends) {
                sb.append(DAY_OF_WEEK_TO_RUS_DAY_MAP.get(weekend)).append(", ");
            }
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append(": выходной");
        }
        return sb.toString();
    }
}
