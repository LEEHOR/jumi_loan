package com.cashloan;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/30$
 * 描述：
 */

public class BB {


    private List<CallLogsBean> CallLogs;

    public List<CallLogsBean> getCallLogs() {
        return CallLogs;
    }

    public void setCallLogs(List<CallLogsBean> CallLogs) {
        this.CallLogs = CallLogs;
    }

    public static class CallLogsBean {
        /**
         * type : 10
         * call_time : 1563803064763
         * duration : 64
         * phone : 13084026485
         * create_time : 1563879553222
         */

        private String type;
        private long call_time;
        private String duration;
        private String phone;
        private long create_time;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getCall_time() {
            return call_time;
        }

        public void setCall_time(long call_time) {
            this.call_time = call_time;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }
    }
}
