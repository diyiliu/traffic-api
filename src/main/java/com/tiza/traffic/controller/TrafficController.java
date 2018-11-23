package com.tiza.traffic.controller;

import com.tiza.traffic.jpa.CityJpa;
import com.tiza.traffic.jpa.CityLicenseJpa;
import com.tiza.traffic.jpa.QueryLogJpa;
import com.tiza.traffic.jpa.ViolationJpa;
import com.tiza.traffic.jpa.dto.City;
import com.tiza.traffic.jpa.dto.CityLicense;
import com.tiza.traffic.jpa.dto.QueryLog;
import com.tiza.traffic.jpa.dto.Violation;
import com.tiza.traffic.support.RespBody;
import com.tiza.traffic.support.util.DateUtil;
import com.tiza.traffic.support.util.JacksonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description: TrafficController
 * Author: DIYILIU
 * Update: 2018-11-07 15:01
 */

@RestController
@RequestMapping("/")
@Api(description = "违章查询接口")
public class TrafficController {
    private final static String KEY = "5257517ebcd972dbe1020847819bb66c";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CityJpa cityJpa;

    @Resource
    private CityLicenseJpa cityLicenseJpa;

    @Resource
    private ViolationJpa violationJpa;

    @Resource
    private QueryLogJpa queryLogJpa;

    @PostMapping("/city/criteria")
    @ApiOperation("获取城市查询标准")
    public RespBody city(String prefix) {
        City city = cityJpa.findByLicensePrefix(prefix);

        RespBody respBody = new RespBody();
        respBody.setSuccess(true);
        respBody.setMessage("操作成功");
        respBody.setData(city);

        return respBody;
    }

    @PostMapping("/city/pull")
    @ApiOperation("更新城市信息接口")
    public RespBody citys() throws Exception {
        RespBody respBody = new RespBody();
        List<City> cityList = new ArrayList();

        String url = "http://v.juhe.cn/wz/citys";
        StringBuffer strBuf = new StringBuffer("?");
        strBuf.append("province=").
                append("&dtype=").
                append("&format=").
                append("&callback=").
                append("&key=").append(KEY);
        url += strBuf.toString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String json = responseEntity.getBody();
        Map data = JacksonUtil.toObject(json, HashMap.class);
        int resultCode = Integer.parseInt(String.valueOf(data.get("resultcode")));
        if (resultCode == 200) {
            Map provinceMap = (Map) data.get("result");

            provinceMap.keySet().stream().forEach(e -> {
                Map pMap = (Map) provinceMap.get(e);

                String provinceName = (String) pMap.get("province");
                String provinceCode = (String) pMap.get("province_code");
                List<HashMap> citys = (List) pMap.get("citys");
                for (Map c : citys) {
                    String cityName = (String) c.get("city_name");
                    String cityCode = (String) c.get("city_code");

                    int reqEngine = Integer.parseInt(String.valueOf(c.get("engine")));
                    int engineno = Integer.parseInt(String.valueOf(c.get("engineno")));
                    int reqChassis = Integer.parseInt(String.valueOf(c.get("classa")));
                    int chassisno = Integer.parseInt(String.valueOf(c.get("classno")));

                    City city = new City();
                    city.setProvinceName(provinceName);
                    city.setProvinceCode(provinceCode);
                    city.setCityName(cityName);
                    city.setCityCode(cityCode);
                    city.setRequireEngine(reqEngine);
                    city.setEngineDigits(engineno);
                    city.setRequireChassis(reqChassis);
                    city.setChassisDigits(chassisno);
                    cityList.add(city);
                }
            });
        }

        if (!CollectionUtils.isEmpty(cityList)) {

            cityJpa.saveAll(cityList);
            respBody.setSuccess(true);
            respBody.setMessage("更新成功");

            return respBody;
        }
        respBody.setSuccess(false);
        respBody.setMessage("操作失败");
        respBody.setData(data);

        return respBody;
    }


    @GetMapping("/violation")
    @ApiOperation("查询违章信息")
    public RespBody violation(String license, String engine, String chassis) throws Exception {
        RespBody respBody = new RespBody();

        // 一天内是否查询过
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date limitDate = calendar.getTime();

        List<QueryLog> queryLogList = queryLogJpa.findByLicenseAndQueryDateAfter(license, limitDate);
        if (!CollectionUtils.isEmpty(queryLogList)) {
            List<Violation> violationList = violationJpa.findByLicenseAndHandled(license, 0);
            respBody.setData(violationList);
            respBody.setSuccess(true);
            respBody.setMessage("查询成功");

            return respBody;
        }


        String prefix = license.substring(0, 2);
        CityLicense cityLicense = cityLicenseJpa.findByLicensePrefix(prefix);
        if (cityLicense == null || StringUtils.isEmpty(cityLicense.getCityCode())) {

            respBody.setSuccess(false);
            respBody.setMessage("不支持[" + prefix + "]城市违章查询");

            return respBody;
        }

        String url = "http://v.juhe.cn/wz/query";
        StringBuffer strBuf = new StringBuffer("?");
        strBuf.append("city=").append(cityLicense.getCityCode()).
                append("&hphm=").append(license).
                append("&classno=").append(chassis).
                append("&engineno=").append(engine).
                append("&key=").append(KEY);
        url += strBuf.toString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String json = responseEntity.getBody();
        Map data = JacksonUtil.toObject(json, HashMap.class);
        int resultCode = Integer.parseInt(String.valueOf(data.get("resultcode")));
        if (resultCode == 200) {
            // 记录查询历史
            QueryLog queryLog = new QueryLog();
            queryLog.setLicense(license);
            queryLog.setQueryDate(new Date());
            queryLogJpa.save(queryLog);

            Map resultMap = (Map) data.get("result");
            List list = (List) resultMap.get("lists");
            List<Violation> violationList = new ArrayList();
            if (!CollectionUtils.isEmpty(list)) {

                List<Violation> history = violationJpa.findByLicenseAndHandled(license, 0);
                list.forEach(e -> {
                    Map m = (Map) e;
                    String occurTime = (String) m.get("date");
                    String code = (String) m.get("code");
                    String archiveno = (String) m.get("archiveno");
                    String location = (String) m.get("area");
                    String act = (String) m.get("act");
                    String score = (String) m.get("fen");
                    String money = (String) m.get("money");
                    String handled = (String) m.get("handled");
                    String city = (String) m.get("wzcity");

                    Violation violation = new Violation();
                    violation.setLicense(license);
                    violation.setOccurTime(DateUtil.stringToDate(occurTime));
                    violation.setArchiveno(archiveno);
                    violation.setCode(code);
                    violation.setLocation(location);
                    violation.setAct(act);
                    violation.setScore(Integer.parseInt(score));
                    violation.setMoney(Integer.parseInt(money));
                    violation.setHandled(Integer.parseInt(handled));
                    violation.setCity(city);
                    violationList.add(violation);

                    if (history.contains(violation)) {
                        history.remove(violation);
                    }
                });
                // 修改处理状态
                history.forEach(e -> e.setHandled(1));
                violationJpa.saveAll(history);

                violationJpa.saveAll(violationList);
                respBody.setData(violationList);
            }
            respBody.setSuccess(true);
            respBody.setMessage("查询成功");

            return respBody;
        }

        respBody.setSuccess(false);
        respBody.setMessage("查询失败");
        respBody.setData(data);

        return respBody;
    }


    @GetMapping("/violation/history")
    @ApiOperation(value = "查询违章历史", notes = "日期格式: yyyy-MM-dd")
    public RespBody hisViolation(String license, String begin, String end) {
        RespBody respBody = new RespBody();

        Date d1 = DateUtil.strToDate(begin, "yyyy-MM-dd");
        Date d2 = DateUtil.strToDate(end, "yyyy-MM-dd");

        if (d1 == null || d2 == null) {
            respBody.setSuccess(false);
            respBody.setMessage("日期格式错误");

            return respBody;
        }

        List<Violation> violationList = violationJpa.findByLicenseAndOccurTimeBetweenOrderByOccurTime(license, d1, d2);
        respBody.setSuccess(true);
        respBody.setMessage("查询成功");
        respBody.setData(violationList);

        return respBody;
    }

}
