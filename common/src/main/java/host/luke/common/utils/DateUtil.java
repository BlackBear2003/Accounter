package host.luke.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");




    public static Date getLastAMonthTime(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //c.add(java.util.Calendar.DATE, -7); // 向前一周；如果需要向后一周，用正数即可
        c.add(Calendar.DATE, -29); // 向前一月；如果需要向后一月，用正数即可
        return c.getTime();
    }

    public static Date getLastAWeekTime(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(java.util.Calendar.DATE, -6); // 向前一周；如果需要向后一周，用正数即可
        //c.add(Calendar.MONTH, -1); // 向前一月；如果需要向后一月，用正数即可
        return c.getTime();
    }

    public static Date getLastAYearTime(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        return c.getTime();
    }

    /**
     * 获得本天的开始时间
     *
     * @return
     */
    public static Date getDayStartTime(Date date) {
        Date dt = null;
        try {
            dt = shortSdf.parse(shortSdf.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获得本天的结束时间
     *
     * @return
     */
    public static Date getDayEndTime(Date date) {
        Date dt = null;
        try {
            dt = longSdf.parse(shortSdf.format(date) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }
    /**
     * 当前时间是星期几
     *
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.DAY_OF_WEEK);
        return week_of_year - 1;
    }

    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public static Date getWeekStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public static Date getWeekEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本月的开始时间
     *
     * @return
     */
    public static Date getMonthStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.DATE, 1);
            dt = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 本月的结束时间
     *
     * @return
     */
    public static Date getMonthEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前年的开始时间
     *
     * @return
     */
    public static Date getYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            dt = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前年的结束时间
     *
     * @return
     */
    public static Date getYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date dt = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的开始时间
     *
     * @return
     */
    public static Date getQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static Date getQuarterEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取前/后半年的开始时间
     *
     * @return
     */
    public static Date getHalfYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 6);
            }
            c.set(Calendar.DATE, 1);
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00.000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;

    }

    /**
     * 获取前/后半年的结束时间
     *
     * @return
     */
    public static Date getHalfYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date dt = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dt = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59.999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取月旬 三旬: 上旬1-10日 中旬11-20日 下旬21-31日
     *
     * @param date
     * @return
     */
    public static int getTenDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_MONTH);
        if (i < 11)
            return 1;
        else if (i < 21)
            return 2;
        else
            return 3;
    }


    /**
     * 获取所属旬开始时间
     *
     * @param date
     * @return
     */
    public static Date getTenDayStartTime(Date date) {
        int ten = getTenDay(date);
        try {
            if (ten == 1) {
                return getMonthStartTime(date);
            } else if (ten == 2) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-11");
                return shortSdf.parse(df.format(date));
            } else {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-21");
                return shortSdf.parse(df.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;


    }

    /**
     * 获取所属旬结束时间
     *
     * @param date
     * @return
     */
    public static Date getTenDayEndTime(Date date) {
        int ten = getTenDay(date);
        try {
            if (ten == 1) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-10 23:59:59.999");
                return longSdf.parse(df.format(date));
            } else if (ten == 2) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-20 23:59:59.999");
                return longSdf.parse(df.format(date));
            } else {
                return getMonthEndTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;


    }


    /**
     * 属于本年第几天
     *
     * @return
     */
    public static int getYearDayIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 属于本年第几周
     *
     * @return
     */
    public static int getYearWeekIndex(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 属于本年第几月
     *
     * @return
     */
    public static int getYearMonthIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 当前属于本年第几个季度
     *
     * @return
     */
    public static int getYearQuarterIndex(Date date) {
        int month = getYearMonthIndex(date);
        if (month <= 3)
            return 1;
        else if (month <= 6)
            return 2;
        else if (month <= 9)
            return 3;
        else
            return 4;
    }

    /**
     * 获取date所属年的所有天列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {
            Date st = getDayStartTime(calendar.getTime());
            Date et = getDayEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;

    }

    /**
     * 获取date所属年的所有星期列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearWeekList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        while (calendar.getTime().before(endtm)) {
            Date st = getWeekStartTime(calendar.getTime());
            Date et = getWeekEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return result;

    }

    /**
     * 获取date所属年的所有月列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearMonthList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        while (calendar.getTime().before(endtm)) {
            Date tm = calendar.getTime();
            Date st = getMonthStartTime(tm);
            Date et = getMonthEndTime(tm);
            result.add(new Date[]{st, et});
            calendar.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 获取date所属年的所有季度列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearQuarterList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);
        while (calendar.getTime().before(endtm)) {
            Date st = getQuarterStartTime(calendar.getTime());
            Date et = getQuarterEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.MONTH, 3);
        }
        return result;
    }

    /**
     * 获取date所属月份的所有旬列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> monthTenDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getMonthStartTime(date);
        Date endtm = getMonthEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {
            Date st = getTenDayStartTime(calendar.getTime());
            Date et = getTenDayEndTime(calendar.getTime());
            result.add(new Date[]{st, et});
            calendar.add(Calendar.DAY_OF_MONTH, 11);
        }
        return result;
    }

    /**
     * 获取date所属年的所有月旬列表及开始/结束时间 开始时间：date[0]，结束时间：date[1]
     *
     * @param date
     * @return
     */
    public static List<Date[]> yearTenDayList(Date date) {
        List<Date[]> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date starttm = getYearStartTime(date);
        Date endtm = getYearEndTime(date);
        calendar.setTime(starttm);

        while (calendar.getTime().before(endtm)) {//
            result.addAll(monthTenDayList(calendar.getTime()));
            calendar.add(Calendar.MONTH, 1);
        }
        return result;
    }
}
