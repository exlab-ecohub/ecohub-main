package team.exlab.ecohub.recyclingpoint;

import team.exlab.ecohub.exception.PatternMismatchException;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointDto;
import team.exlab.ecohub.recyclingpoint.dto.RecyclingPointPartInfoDto;
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
    private static final String regExpIncommingString = "((Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?:\\s*([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])?;\\s*){0,6}(Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?:\\s*([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])?\\.$";
    private static final Map<String, DayOfWeek> dayOfWeekMap = Map.of("Пн", DayOfWeek.MONDAY, "Вт", DayOfWeek.TUESDAY, "Ср", DayOfWeek.WEDNESDAY, "Чт", DayOfWeek.THURSDAY, "Пт", DayOfWeek.FRIDAY, "Сб", DayOfWeek.SATURDAY, "Вс", DayOfWeek.SUNDAY);
    private static final Map<DayOfWeek, String> dayOfWeekReverseMap = Map.of(DayOfWeek.MONDAY, "Пн", DayOfWeek.TUESDAY, "Вт", DayOfWeek.WEDNESDAY, "Ср", DayOfWeek.THURSDAY, "Чт", DayOfWeek.FRIDAY, "Пт", DayOfWeek.SATURDAY, "Сб", DayOfWeek.SUNDAY, "Вс");
    private static final Pattern dayOrDaysPattern = Pattern.compile("(Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?");
    private static final Pattern workingTimePattern = Pattern.compile("([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])[,|\\.]");
    private static final Pattern lunchTimePattern = Pattern.compile("(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])");

    private RecyclingPointMapper() {
    }

    public static RecyclingPointPartInfoDto toPartInfoDto(RecyclingPoint recyclingPoint) {
        return RecyclingPointPartInfoDto.builder()
                .id(recyclingPoint.getId())
                .name(recyclingPoint.getName())
                .address(recyclingPoint.getAddress())
                .phoneNumber(recyclingPoint.getPhoneNumber())
                .website(recyclingPoint.getWebsite())
                .displayed(recyclingPoint.isDisplayed())
                .build();
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
                .recyclableTypes(recyclingPoint.getRecyclableTypes().stream().map(type -> type.getName().name()).collect(Collectors.toSet()))
                .displayed(recyclingPoint.isDisplayed())
                .build();
    }

    public static RecyclingPoint toPoint(RecyclingPointDto recyclingPointDto, Set<RecyclableType> recyclableTypes) {
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

    public static Map<DayOfWeek, WorkingHours> workingHoursToMap(String workingHoursAsString) {
        Map<DayOfWeek, WorkingHours> map = new EnumMap<>(DayOfWeek.class);
        if (!workingHoursAsString.matches(regExpIncommingString)) {
            throw new PatternMismatchException("Provided String does not correspond to pattern required");
        } else {
            List<String> daysOrRangesOfDaysWithTime = Arrays.asList(workingHoursAsString.split(";"));
            String dayOrDays = "";
            String workingTime = "";
            String lunchTime = "";
            for (String dayOrDaysWithTime : daysOrRangesOfDaysWithTime) {
                Matcher matcherDayOrDays = dayOrDaysPattern.matcher(dayOrDaysWithTime);
                Matcher matcherWorkingTime = workingTimePattern.matcher(dayOrDaysWithTime);
                Matcher matcherLunchTime = lunchTimePattern.matcher(dayOrDaysWithTime);
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
                    lunchTime = matcherLunchTime.group();
                    lunchTime = lunchTime.substring(1).trim();
                    lunchStartTime = LocalTime.parse(lunchTime.subSequence(0, lunchTime.indexOf("-")), DateTimeFormatter.ofPattern("HH:mm"));
                    lunchEndTime = LocalTime.parse(lunchTime.subSequence(lunchTime.indexOf("-") + 1, lunchTime.length()), DateTimeFormatter.ofPattern("HH:mm"));
                }
                LocalTime openingTime = LocalTime.parse(workingTime.subSequence(0, workingTime.indexOf("-")).toString(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime closingTime = LocalTime.parse(workingTime.subSequence(workingTime.indexOf("-") + 1, workingTime.indexOf(",")), DateTimeFormatter.ofPattern("HH:mm"));
                int startDay;
                int endDay;
                String[] startAndEndDays = dayOrDays.split("-");
                startDay = dayOfWeekMap.get(startAndEndDays[0]).getValue();
                endDay = startAndEndDays.length == 2 ? dayOfWeekMap.get(startAndEndDays[1]).getValue() : startDay;
                for (int i = startDay; i <= endDay; i++) {
                    map.put(DayOfWeek.of(i), new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                }
            }
        }
        return map;
    }

    public static String workingHoursToString(Map<DayOfWeek, WorkingHours> map) {
        Map<WorkingHours, ArrayList<DayOfWeek>> reverseMap = map.entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry<DayOfWeek, WorkingHours>::getValue))
                .values()
                .stream()
                .collect(Collectors.toMap(item -> item.get(0).getValue(), item -> new ArrayList<>(item.stream()
                        .map(Map.Entry::getKey)
                        .sorted()
                        .collect(Collectors.toList()))));
        StringBuilder sb = new StringBuilder();
        sb.append("Время работы: ");
        for (Map.Entry<WorkingHours, ArrayList<DayOfWeek>> entry : reverseMap.entrySet()) {
            WorkingHours workingHours = entry.getKey();
            List<DayOfWeek> days = entry.getValue();
            for (int i = 0; i < days.size(); i++) {
                sb.append(dayOfWeekReverseMap.get(days.get(i)));
                if (i < days.size() - 1) sb.append(",");
                else sb.append(": ");
            }
            if (workingHours.getOpeningTime() != null && workingHours.getClosingTime() != null) {
                sb.append(workingHours.getOpeningTime()).append("-").append(workingHours.getClosingTime());
            }
            if (workingHours.getLunchStartTime() != null && workingHours.getLunchEndTime() != null) {
                sb.append(", обед: ").append(workingHours.getLunchStartTime()).append("-").append(workingHours.getLunchEndTime()).append("; ");
            } else {
                sb.append("; ");
            }
        }
        List<DayOfWeek> nonWorkingDays = Arrays.stream(DayOfWeek.values()).collect(Collectors.toList());
        List<DayOfWeek> dayOfWeekFromMap = new ArrayList<>(map.keySet());
        for (DayOfWeek day : dayOfWeekFromMap) {
            nonWorkingDays.remove(day);
        }
        if (!nonWorkingDays.isEmpty()) {
            for (int i = 0; i <= nonWorkingDays.size() - 1; i++) {
                sb.append(dayOfWeekReverseMap.get(nonWorkingDays.get(i)));
                if (i < nonWorkingDays.size() - 1) sb.append(",");
            }
            sb.append(": выходной");
        }
        return sb.toString();
    }
}
