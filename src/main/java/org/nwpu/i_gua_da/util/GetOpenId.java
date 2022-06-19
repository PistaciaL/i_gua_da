package org.nwpu.i_gua_da.util;

import org.nwpu.i_gua_da.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class GetOpenId {

	@Autowired
	private HttpClientUtil httpClientUtil;

	@Value("${constants.wxlogin.appid}")
    private String tempAppid;
    @Value("${constants.wxlogin.secret}")
    private String tempSecret;

	public Map<String, Object> wxCheck(@RequestParam("code") String code) {
		String appid = tempAppid;
		String secret = tempSecret;
		// 拼接sql
		String loginUrl = "https://api.weixin.qq.com/sns/jscode2session?"
				+"appid=" + appid + "&secret=" + secret
				+"&js_code=" + code + "&grant_type=authorization_code";
		System.out.println(loginUrl);
		String result = httpClientUtil.sendHttpGet(loginUrl);
		// 将json字符串转化为jsonobject
		JSONObject jsonObject = JSON.parseObject(result);
		// 获取openId
		System.out.println(jsonObject.toString());
		String openid = jsonObject.get("openid").toString();
		String session_key = jsonObject.get("session_key").toString();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("openid", openid);
		resultMap.put("session_key", session_key);
		return resultMap;
	}

}
