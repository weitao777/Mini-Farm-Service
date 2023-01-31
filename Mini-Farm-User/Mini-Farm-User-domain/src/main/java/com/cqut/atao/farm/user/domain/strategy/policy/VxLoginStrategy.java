package com.cqut.atao.farm.user.domain.strategy.policy;

import cn.hutool.json.JSONObject;
import com.cqut.atao.farm.user.domain.converter.UserConverter;
import com.cqut.atao.farm.user.domain.model.res.LoginRes;
import com.cqut.atao.farm.user.domain.model.vo.VxUserLoginVO;
import com.cqut.atao.farm.user.domain.properites.WechatAuthProperites;
import com.cqut.atao.farm.user.domain.repository.UserRepository;
import com.cqut.atao.farm.user.domain.strategy.LoginStrategy;
import com.cqut.atao.farm.user.domain.util.VxUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author atao
 * @version 1.0.0
 * @ClassName VxLoginStrategy.java
 * @Description 微信登录策略
 * @createTime 2023年01月13日 16:51:00
 */
@Component
public class VxLoginStrategy implements LoginStrategy{

    @Resource
    private UserRepository userRepository;

    @Resource
    private WechatAuthProperites wechatAuthProperites;

    @Override
    public LoginRes login(Map<String, String> data) {
        // 1.开发者服务器 登录凭证校验接口 appId + appSecret + 接收小程序发送的code
        JSONObject SessionKeyOpenId = VxUtil.getSessionKeyOrOpenId(data.get("code"),wechatAuthProperites.getSessionHost(),wechatAuthProperites.getAppId(),wechatAuthProperites.getSecret(),wechatAuthProperites.getGrantType());
        // 2.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.get("openid", String.class);
        // 3.检测是否首次登录(存储用户信息)
        LoginRes loginRes = userRepository.checkUserInfo(openid);
        // 4.自定义登录态
        String token = VxUtil.generateAccessToken(loginRes.getCustomerUserId());
        loginRes.setAccessToken(token);
        // 5.返回响应结果
        return loginRes;
    }
}

