package com.bee.ms.drawer;

import com.bee.ms.common.enums.PlatformEnum;

import java.util.Map;

/**
 * 应用上下文信息
 *
 * @author created by htt on 2018/7/23
 */
public class BeeContext {
    private Long shopId;
    private Long consumeId;
    private PlatformEnum platForm;
    private Boolean enterUp;
    private Map<Class, Boolean> headerTransparentMap;

    public BeeContext() {
    }

    public BeeContext(Long shopId, Long consumeId, PlatformEnum platForm,
                      Boolean enterUp, Map<Class, Boolean> headerTransparentMap) {
        this.shopId = shopId;
        this.consumeId = consumeId;
        this.platForm = platForm;
        this.enterUp = enterUp;
        this.headerTransparentMap = headerTransparentMap;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getConsumeId() {
        return consumeId;
    }

    public void setConsumeId(Long consumeId) {
        this.consumeId = consumeId;
    }

    public PlatformEnum getPlatForm() {
        return platForm;
    }

    public void setPlatForm(PlatformEnum platForm) {
        this.platForm = platForm;
    }

    public Boolean getEnterUp() {
        return enterUp;
    }

    public void setEnterUp(Boolean enterUp) {
        this.enterUp = enterUp;
    }

    public Map<Class, Boolean> getHeaderTransparentMap() {
        return headerTransparentMap;
    }

    public void setHeaderTransparentMap(Map<Class, Boolean> headerTransparentMap) {
        this.headerTransparentMap = headerTransparentMap;
    }
}
