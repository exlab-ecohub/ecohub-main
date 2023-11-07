package team.exlab.ecohub.recyclingpoint;

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
                .workingHoursAsString(convertWorkingHoursFromMapToString(recyclingPoint.getWorkingHours()))
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
                .workingHours(convertWorkingHoursFromStringToMap(recyclingPointDto.getWorkingHoursAsString()))
                .recyclableTypes(recyclableTypes)
                .displayed(recyclingPointDto.isDisplayed())
                .build();
    }

    public static Map<DayOfWeek, WorkingHours> convertWorkingHoursFromStringToMap(String workingHoursAsString) {
        Map<DayOfWeek, WorkingHours> map = new EnumMap<>(DayOfWeek.class);
        Pattern pattern = Pattern.compile("((Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?:\\s*([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])?;\\s*){0,6}(Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?:\\s*([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])?\\.$");
        if (pattern.matcher(workingHoursAsString).matches()) {
            List<String> daysOrRangesOfDaysWithTime = Arrays.asList(workingHoursAsString.split(";"));
            Pattern dayOrDaysPattern = Pattern.compile("(Пн|Вт|Ср|Чт|Пт|Сб|Вс)(-(Вт|Ср|Чт|Пт|Сб|Вс))?");
            Pattern workingTimePattern = Pattern.compile("([0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])[,|\\.]");
            Pattern lunchTimePattern = Pattern.compile("(,\\s*[0-2][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9])");
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

                Map<String, DayOfWeek> dayOfWeekMap = Map.of("Пн", DayOfWeek.MONDAY, "Вт", DayOfWeek.TUESDAY, "Ср", DayOfWeek.WEDNESDAY, "Чт", DayOfWeek.THURSDAY, "Пт", DayOfWeek.FRIDAY, "Сб", DayOfWeek.SATURDAY, "Вс", DayOfWeek.SUNDAY);
                int startDay;
                int endDay;
                if (dayOrDays.contains("-")) {
                    String[] startAndEndDays = dayOrDays.split("-");
                    startDay = dayOfWeekMap.get(startAndEndDays[0]).getValue();
                    endDay = dayOfWeekMap.get(startAndEndDays[1]).getValue();
                } else {
                    startDay = dayOfWeekMap.get(dayOrDays).getValue();
                    endDay = startDay;
                }

                for (int i = startDay; i <= endDay; i++) {
                    switch (i) {
                        case 1:
                            map.put(DayOfWeek.MONDAY, new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                            break;
                        case 2:
                            map.put(DayOfWeek.TUESDAY, new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                            break;
                        case 3:
                            map.put(DayOfWeek.WEDNESDAY, new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                            break;
                        case 4:
                            map.put(DayOfWeek.THURSDAY, new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                            break;
                        case 5:
                            map.put(DayOfWeek.FRIDAY, new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                            break;
                        case 6:
                            map.put(DayOfWeek.SATURDAY, new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                            break;
                        case 7:
                            map.put(DayOfWeek.SUNDAY, new WorkingHours(openingTime, closingTime, lunchStartTime, lunchEndTime));
                            break;
                    }
                }
            }
        }
        return map;
    }

    public static String convertWorkingHoursFromMapToString(Map<DayOfWeek, WorkingHours> map) {
        Map<DayOfWeek, String> dayOfWeekMap = Map.of(DayOfWeek.MONDAY, "Пн", DayOfWeek.TUESDAY, "Вт", DayOfWeek.WEDNESDAY, "Ср", DayOfWeek.THURSDAY, "Чт", DayOfWeek.FRIDAY, "Пт", DayOfWeek.SATURDAY, "Сб", DayOfWeek.SUNDAY, "Вс");
        List<DayOfWeek> nonWorkingDays = Arrays.stream(DayOfWeek.values()).collect(Collectors.toList());
        List<DayOfWeek> dayOfWeekFromMap = new ArrayList<>(map.keySet());
        for (DayOfWeek day : dayOfWeekFromMap) {
            nonWorkingDays.remove(day);
        }
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
                sb.append(dayOfWeekMap.get(days.get(i)));
                if (i < days.size() - 1) {
                    sb.append(",");
                } else {
                    sb.append(": ");
                }
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
        if (!nonWorkingDays.isEmpty()) {
            for (int i = 0; i <= nonWorkingDays.size() - 1; i++) {
                sb.append(dayOfWeekMap.get(nonWorkingDays.get(i)));
                if (i < nonWorkingDays.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(": выходной");
        }
        return sb.toString();
    }
}
