import com.tiza.traffic.support.util.JacksonUtil;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: TestMain
 * Author: DIYILIU
 * Update: 2018-11-07 14:19
 */
public class TestMain {


    @Test
    public void test1() throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://v.juhe.cn/wz/citys";

        StringBuffer strBuf = new StringBuffer("?");
        strBuf.append("province=").
                append("&dtype=").
                append("&format=").
                append("&callback=").
                append("&key=").append("5257517ebcd972dbe1020847819bb66c");
         url += strBuf.toString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        System.out.println(responseEntity.getBody());

        String json = responseEntity.getBody();
        Map result = JacksonUtil.toObject(json, HashMap.class);
        int resultcode = Integer.parseInt(String.valueOf(result.get("resultcode")));
        if (resultcode == 200){
            Map provinceMap = (Map) result.get("result");

            provinceMap.keySet().stream().forEach(e -> {

                Map m = (Map) provinceMap.get(e);

                String province = (String) m.get("province");
                String province_code = (String) m.get("province_code");

                List<HashMap> citys = (List) m.get("citys");
                for (Map c: citys){
                    String city = (String) c.get("city_name");
                    String cityCode = (String) c.get("city_code");

                    System.out.println(province + ":" + city);
                }
            });
        }
    }

    @Test
    public void test2() throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://v.juhe.cn/wz/query";

        StringBuffer strBuf = new StringBuffer("?");
        strBuf.append("city=JS_XZ").
                append("&hphm=苏C307CN").
                append("&classno=049323").
                append("&engineno=580168").
                append("&key=").append("5257517ebcd972dbe1020847819bb66c");
        url += strBuf.toString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        System.out.println(responseEntity.getBody());
        String json = responseEntity.getBody();
    }

    @Test
    public void test3(){
        String license = "苏C307CN";

        String prefix = license.substring(0, 2);

        System.out.println(prefix);
    }
}
