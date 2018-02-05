package azkaban.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class Phoner {
    private static final Logger logger = Logger.getLogger(Phoner.class);

    private static final String URL_DOMAIN = "http://"
            + System.getenv("SMS_API_DOMAIN") + ":" + System.getenv("SMS_API_PORT")
            + System.getenv("SMS_API_PATH") + "?" + System.getenv("SMS_API_QUERY");

    public static String sendMessage(String mobile, String msg) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = URL_DOMAIN + "Mobile=" + URLEncoder.encode(mobile, "UTF-8") +
                    "&Msg=" + URLEncoder.encode(msg, "UTF-8");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //
            connection.setConnectTimeout(1000 * 10);
            connection.setReadTimeout(1000 * 10);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.defaultCharset()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            logger.error("发送短信成功");
        } catch (Exception e) {
            logger.error("发送短信异常", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
        return result;
    }
}
