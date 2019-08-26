package com.cashloan.jumidai.ui.homeMine.bean.recive;

/**
 * Created by chenming
 * Created Date 17/5/6 11:26
 * mail:cm1@erongdu.com
 * Describe: 身份证正面照信息
 */
public class OcrFrontRec {
    /** 出身年月日 */
    private String   race;
    /** 出身年月日 */
    private String   name;
    /** 出身年月日 */
    private String   gender;
    /** 出身年月日 */
    private String   id_card_number;
    /** 出身年月日 */
    private String   address;
    /** 出身年月日 */
    private String   side;
    /** 出身年月日 */
    private Birthday birthday;
    /** 图片合法可能性 */
    private Legality legality;

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public void setBirthday(Birthday birthday) {
        this.birthday = birthday;
    }

    public Legality getLegality() {
        return legality;
    }

    public void setLegality(Legality legality) {
        this.legality = legality;
    }

    class Birthday {
        private String year;
        private String month;
        private String day;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }

    class Legality{
        /** 用工具合成或 者编辑过的身 份证图片 */
        private String Edited;
        /** 正式身份证的 复印件 */
        private String Photocopy;

        /** 正式身份证照 片 */
        private String IDPhoto;

        /** 手机或电脑屏 幕翻拍的照片 */
        private String Screen;

        /** 临时身份证 */
        private String TemporaryIDPhoto;

        public String getEdited() {
            return Edited;
        }

        public void setEdited(String edited) {
            Edited = edited;
        }

        public String getPhotocopy() {
            return Photocopy;
        }

        public void setPhotocopy(String photocopy) {
            Photocopy = photocopy;
        }

        public String getIDPhoto() {
            return IDPhoto;
        }

        public void setIDPhoto(String IDPhoto) {
            this.IDPhoto = IDPhoto;
        }

        public String getScreen() {
            return Screen;
        }

        public void setScreen(String screen) {
            Screen = screen;
        }

        public String getTemporaryIDPhoto() {
            return TemporaryIDPhoto;
        }

        public void setTemporaryIDPhoto(String temporaryIDPhoto) {
            TemporaryIDPhoto = temporaryIDPhoto;
        }
    }
}
