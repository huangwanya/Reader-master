package xyz.fycz.myreader.common;

import xyz.fycz.myreader.util.SharedPreUtils;

public class URLCONST {

    public static final String OFFICIAL_WEB = "https://reader." + getDefaultDomain() + "/";

    public static String APP_DIR_UR = "https://www.lanzous.com/b00ngso7e";

    public static String LAN_ZOUS_URL = "https://fycz.lanzoui.com";

    //字体下载
    public static final String FONT_DOWNLOAD_URL = "https://novel." + getDefaultDomain() + "/app/fonts/";

    public static final String APP_WEB_URL = "https://fyreader." + getDefaultDomain() + "/";

    public static final String BAI_DU_SEARCH = "https://m.baidu.com/s?word={key}";

    public static final String GOOGLE_SEARCH = "https://www.google.com/search?q={key}";

    public static final String YOU_DAO_SEARCH = "http://m.youdao.com/dict?le=eng&q={key}";


    public static final String FY_READER_URL = "https://fyreader." + getDefaultDomain();

    public static final String AD_URL = FY_READER_URL + "/ad";
    public static final String LOG_UPLOAD_URL = FY_READER_URL + "/logUpload";
    public static final String THANKS_URL = FY_READER_URL + "/thanks/";
    public static final String USER_URL = "http://101.43.83.105:12123";

    public static final String DONATE = "https://gitee.com/fengyuecanzhu/Donate/raw/master";

    public static final String WX_ZSM = DONATE + "/wx_zsm.png";
    public static final String ZFB_SKM = DONATE + "/zfb_skm.jpg";
    public static final String QQ_SKM = DONATE + "/qq_skm.png";

    public static final String QUOTATION = "https://v1.hitokoto.cn/?encode=json&charset=utf-8";

    public static String getDefaultDomain() {
        return SharedPreUtils.getInstance().getString("domain", "fycz.tk");
    }

}

