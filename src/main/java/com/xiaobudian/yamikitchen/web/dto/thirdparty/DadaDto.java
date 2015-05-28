package com.xiaobudian.yamikitchen.web.dto.thirdparty;

/**
 * @author Liuminglu 2015/5/19.
 */
public class DadaDto {
    private DadaResult result;
    private Integer status;
    private String errorCode;

    private String client_id;
    private String order_id;
    // 订单状态(1待接单 2待取货 3执行中 4已完成 5已取消)
    private Integer order_status;
    // 订单取消原因,其他状态下默认值为空字符串
    private String cancel_reason;
    // 达达配送员id
    private Integer dm_id;
    // 达达配送员姓名
    private String dm_name;
    // 达达配送员手机号
    private String dm_mobile;
    // 更新时间，时间戳
    private Integer update_time;

    public class DadaResult {
        private String grant_code;
        private String access_token;
        private Long expires_in;
        private String refresh_token;
        private String scope;

        public String getGrant_code() {
            return grant_code;
        }

        public void setGrant_code(String grant_code) {
            this.grant_code = grant_code;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    public DadaResult getResult() {
        return result;
    }

    public void setResult(DadaResult result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getDm_name() {
        return dm_name;
    }

    public void setDm_name(String dm_name) {
        this.dm_name = dm_name;
    }

    public String getDm_mobile() {
        return dm_mobile;
    }

    public void setDm_mobile(String dm_mobile) {
        this.dm_mobile = dm_mobile;
    }

    public Integer getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Integer update_time) {
        this.update_time = update_time;
    }

    public Integer getDm_id() {
        return dm_id;
    }

    public void setDm_id(Integer dm_id) {
        this.dm_id = dm_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}