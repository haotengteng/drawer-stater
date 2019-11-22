package com.bee.ms.drawer.autoconfigure;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author created by htt on 2018/8/9
 */
@ConfigurationProperties(prefix = "drawer")
public class DrawerProperties {
    /**
     * http请求响应日志打印开关，默认关闭
     */
    private Boolean logSwitch = false;

    /**
     * 取号器
     */
    private IdentTooker identTooker = new IdentTooker();

    /**
     * 取号器
     */
    public static class IdentTooker {
        /**
         * 是否启用取号器功能，默认关闭
         */
        private String enable = "false";
        /**
         * 当队列中数量小于该阀值时，获取新号码
         */
        private Integer limitAllowExistNumber = 30;
        /**
         * 取号池中号码最大数量
         */
        private Integer maxKeepNumber = 200;
        /**
         * 每次获取的号码数量(目前id生成器最大支持100)
         */
        private Integer perRequestStep = 100;

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public Integer getLimitAllowExistNumber() {
            return limitAllowExistNumber;
        }

        public void setLimitAllowExistNumber(Integer limitAllowExistNumber) {
            this.limitAllowExistNumber = limitAllowExistNumber;
        }

        public Integer getMaxKeepNumber() {
            return maxKeepNumber;
        }

        public void setMaxKeepNumber(Integer maxKeepNumber) {
            this.maxKeepNumber = maxKeepNumber;
        }

        public Integer getPerRequestStep() {
            return perRequestStep;
        }

        public void setPerRequestStep(Integer perRequestStep) {
            this.perRequestStep = perRequestStep;
        }
    }

    public IdentTooker getIdentTooker() {
        return identTooker;
    }

    public void setIdentTooker(IdentTooker identTooker) {
        this.identTooker = identTooker;
    }

    public Boolean getLogSwitch() {
        return logSwitch;
    }


    public void setLogSwitch(Boolean logSwitch) {
        this.logSwitch = logSwitch;
    }


}
