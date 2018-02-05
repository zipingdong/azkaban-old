package azkaban.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class PublicVariable {
    private Logger logger = Logger.getLogger(PublicVariable.class);

    // 记录文件的修改时间
    private long parameterFileModiTime = 0;
    // 参数的分隔符
    private final String parBigSep = " ";
    private final String parSmlSep = "=";

    private Properties properties = new Properties();

    private SimpleDateFormat sd_day = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sd_hour = new SimpleDateFormat("HH");

    private Date currentDateDay = new Date();
    private Calendar currentCalendarDay = Calendar.getInstance();

    private Date currentDateHour = new Date();
    private Calendar currentCalendarHour = Calendar.getInstance();

    private Date currentDate = new Date();
    private Calendar currentCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");

    private SimpleDateFormat sdf_value = new SimpleDateFormat();

    private Properties publicVariable = new Properties();

    public Properties getProperties(Props props) {
        // 如果输入了小时（必须天、小时同时输入）
        if (props.containsKey("date") && props.containsKey("hour")) {
            logger.info("传入日期（1/2）：" + props.get("date"));
            logger.info("传入小时（1/2）：" + props.get("hour"));
            return getPublicVariable(props.get("date"), props.get("hour"));
        }
        // 如果只输入了天，小时使用当前小时
        else if (props.containsKey("date")) {
            logger.info("传入日期：" + props.get("date"));
            return getPublicVariable(props.get("date"));
        }
        // 如果没有输入时间相关变量，则使用 昨天、当前小时
        else {
            return getPublicVariable();
        }
    }

    private Properties getPublicVariable() {
        // 获得当前时间
        currentDateDay = new Date();
        currentCalendarDay.setTime(currentDateDay);
        currentDateHour = new Date();
        currentCalendarHour.setTime(currentDateHour);

        // 减去一天、一小时，因为如果传入日期、小时的话，要先加一天、一小时
        currentCalendarDay.add(Calendar.DAY_OF_MONTH, -1);
        currentCalendarHour.add(Calendar.HOUR_OF_DAY, -1);

        currentDateDay = currentCalendarDay.getTime();
        currentDateHour = currentCalendarHour.getTime();

        return getPublicVariable(sd_day.format(currentDateDay), sd_hour.format(currentDateHour));
    }

    private Properties getPublicVariable(String date) {
        // 获得当前时间
        currentDateHour = new Date();
        currentCalendarHour.setTime(currentDateHour);

        // 减去一天、一小时，因为如果传入日期、小时的话，要先加一天、一小时
        currentCalendarHour.add(Calendar.HOUR_OF_DAY, -1);

        currentDateHour = currentCalendarHour.getTime();

        return getPublicVariable(date, sd_hour.format(currentDateHour));
    }

    private Properties getPublicVariable(String date, String hour) {
        publicVariable.clear();

        try {
            File parameterFile = new File("conf/parameter.properties");

            // 如果不相等，说明文件修改了（新增、删除变量，修改变量的逻辑），要重新加载，以便获得最新的参数、参数值规则
            if (parameterFileModiTime != parameterFile.lastModified()) {
                logger.info("参数配置文件改动，从新加载");
                parameterFileModiTime = parameterFile.lastModified();
                InputStream inputStream = new BufferedInputStream(new FileInputStream(parameterFile));
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                properties.clear();
                properties.load(reader);
                inputStream.close();
                inputStream = null;
                reader.close();
                reader = null;
            } else {
                logger.info("准备从新初始化参数配置");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("加载参数配置文件错误：文件不存在", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("加载参数配置文件错误", e);
        } catch (IOException e) {
            throw new RuntimeException("加载参数配置文件错误：获取内容错误", e);
        }

        /*
         * 如果传入日期，则设置当前时间为传入日期向未来一天 如果传入小时，则设置当前时间维传入小时向未来一小时 如果没有传入，则为当前时间
         */
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6)) - 1;
        int day = Integer.parseInt(date.substring(6, 8));
        currentCalendarDay.set(year, month, day);
        currentCalendarDay.add(Calendar.DAY_OF_MONTH, 1);
        currentDateDay = currentCalendarDay.getTime();

        currentCalendarHour.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        currentCalendarHour.add(Calendar.HOUR_OF_DAY, 1);
        currentDateHour = currentCalendarHour.getTime();

        try {
            currentDate = sdf.parse(sd_day.format(currentDateDay) + sd_hour.format(currentDateHour));
        } catch (ParseException e) {
            throw new RuntimeException("初始化参考日期错误", e);
        }
        currentCalendar.setTime(currentDate);

        logger.info("参考日期:" + currentDate);

        Set<Object> keys = properties.keySet();
        String key;
        String value;
        logger.info("共需要处理变量 " + properties.size());
        for (Object keyo : keys) {
            key = (String) keyo;
            value = (String) properties.getProperty(key);
            // 如果value包含大分隔符则为变量
            if (value.indexOf(parBigSep) > 0) {
                publicVariable.put(key, getParValue(value));

                // 重新把日历 对象的时间置为参考日期
                currentCalendar.setTime(currentDate);
            } else {
                publicVariable.put(key, value);
            }
        }

        return publicVariable;
    }

    // date=yyyyMMdd day=-1
    private String getParValue(String value) {
        String[] values = value.split(parBigSep);
        String format = values[0];
        String val_type;
        int val_expr;
        for (int i = 1; i < values.length; i++) {
            val_type = values[i].split(parSmlSep)[0];
            val_expr = Integer.parseInt(values[i].split(parSmlSep)[1]);
            switch (val_type) {
                case "year":
                    currentCalendar.add(Calendar.YEAR, val_expr);
                    break;
                case "month":
                    currentCalendar.add(Calendar.MONTH, val_expr);
                    break;
                case "day":
                    currentCalendar.add(Calendar.DAY_OF_MONTH, val_expr);
                    break;
                case "hour":
                    currentCalendar.add(Calendar.HOUR_OF_DAY, val_expr);
                    break;
                case "minute":
                    currentCalendar.add(Calendar.MINUTE, val_expr);
                    break;
            }

            // if (val_type.equals("year"))
            // currentCalendar.add(Calendar.YEAR, val_expr);
            // else if (val_type.equals("month"))
            // currentCalendar.add(Calendar.MONTH, val_expr);
            // else if (val_type.equals("day"))
            // currentCalendar.add(Calendar.DAY_OF_MONTH, val_expr);
            // else if (val_type.equals("hour"))
            // currentCalendar.add(Calendar.HOUR_OF_DAY, val_expr);
            // else if (val_type.equals("minute"))
            // currentCalendar.add(Calendar.MINUTE, val_expr);
        }

        sdf_value.applyPattern(format);
        return sdf_value.format(currentCalendar.getTime());
    }
}

