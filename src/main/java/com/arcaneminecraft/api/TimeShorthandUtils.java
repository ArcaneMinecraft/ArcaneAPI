package com.arcaneminecraft.api;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * TimeShorthandUtils Class.
 *
 * This class contains all the necessary methods for dealing with shorthand
 * times.
 *
 * This class is to be shared between all the other plugins, favorably with
 * Arcane plugins.
 *
 * @author Simon Chuu (SimonOrJ)
 */
@SuppressWarnings({"unused"})
public interface TimeShorthandUtils {

    /**
     * Parses into number of TimeUnit.SECONDS
     * @param shorthand shorthand in one or more "\d[wdhms]" notation
     * @return time taken specified in seconds, or -1 if parsing error exists
     */
    static int parse(String shorthand) {
        String[] parts = shorthand.split("(?<=[wdhms])");
        if (parts.length == 1)
            return 0;

        int ret = 0;

        for (int i = parts.length - 1; i != 0; i++) {
            String part = parts[i];
            int num;
            try {
                num = Integer.parseInt(part.substring(0, part.length() - 1));
            } catch (NumberFormatException e) {
                return -1;
            }
            char len = part.charAt(part.length() - 1);

            switch (len) {
                case 'w':
                    ret += num * 604800;
                    break;
                case 'd':
                    ret += num * 86400;
                    break;
                case 'h':
                    ret += num * 3600;
                    break;
                case 'm':
                    ret += num * 60;
                    break;
                case 's':
                    ret += num;
                    break;
            }
        }

        return ret;
    }

    /**
     * Returns valid options for shorthand
     * @param arg Argument to parse
     * @return list of valid command options
     */
    static List<String> tabComplete(String arg) {
        if (arg.isEmpty()) {
            return ImmutableList.of("0","1","2","3","4","5","6","7","8","9");
        }

        if (!arg.matches("(\\d+[wdhms]?)*")) {
            return ImmutableList.of();
        }

        if ("wdhms".indexOf(arg.charAt(arg.length() - 1)) == -1) {
            return ImmutableList.of(
                    arg + "s",
                    arg + "m",
                    arg + "h",
                    arg + "d",
                    arg + "w",
                    arg + 0,
                    arg + 1,
                    arg + 2,
                    arg + 3,
                    arg + 4,
                    arg + 5,
                    arg + 6,
                    arg + 7,
                    arg + 8,
                    arg + 9
            );
        } else {
            return ImmutableList.of(
                    arg + 1,
                    arg + 2,
                    arg + 3,
                    arg + 4,
                    arg + 5,
                    arg + 6,
                    arg + 7,
                    arg + 8,
                    arg + 9
            );
        }
    }
}
