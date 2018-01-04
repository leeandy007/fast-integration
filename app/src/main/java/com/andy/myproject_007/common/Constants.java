package com.andy.myproject_007.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by leeandy007 on 16/9/18.
 */
public class Constants {

    public static final String APP_NAME = "OA";
    public static final String EXIT_PROCESS = "再按一次退出程序";
    public static final String SUBMIT = "上报";
    public static final String SELECT = "筛选";

    public static final String PATENT_SUBMIT = "知识产权上报";
    public static final String PATENT_QUERY = "知识产权查询";
    public static final String SALT_SUBMIT = "食盐检查上报";
    public static final String SALT_QUERY = "食盐检查查询";
    public static final String DATA_SYNC = "数据同步";
    public static final String USER_ACCOUNT = "账号信息";
    public static final String ABOUT = "关于";
    public static final String MORE = "更多";
    public static final String SEARCH = "基础信息查询";
    public static final String MY_RECORD = "个人记录查询";
    public static final String LOGIN = "登录";

    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";
    public static final String KEY_TAG = "tag";

    public static final String NO_SD = "未检测到SD卡";
    public static final String MSG_EMPTY = "暂无数据";
    public static final String MSG_LOADING_OVER = "加载完毕";

    public static final String KEY_PAGE = "page";
    public static final String KEY_LIMIT = "limit";

    public static final int VALUE_LIMIT = 20;
    public static final int CHOOSE_LIMIT = 9;

    public final static String SDPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    public final static String PATH_IMAGE = SDPATH + File.separator + APP_NAME
            + File.separator + "image" + File.separator;


    /***************************缓存Key***************************/
    //登录key
    public static final String KEY_USER_CACHE = "mUserBean";

    /***************************SharedPreferences-Key***************************/

    public static final String KEY_SP_FILE = "knowledge";

    public static final String KEY_SP_IsRemember = "IsRemember";

    public static final String KEY_SP_LoginName = "LoginName";

    public static final String KEY_SP_Password = "Password";

    /***************************服务器接口相关***************************/
    /**
     * 所有接口命名规则（）
     * */

    //登录接口
    public static final String Get_Login = "/login/aLogin";
    //专利检查对象查询列表接口
    public static final String Get_CheckObject_Patent_List = "/caseInfoController/queryAndroidRObjInfo";
    //专利检查上报接口（带多文件上传）
    public static final String Post_Patent_submit = "/caseInfoController/uploadAndroidRD";
    //食盐检查对象查询列表接口
    public static final String Get_CheckObject_Salt_List = "/caseInfoController/queryAndroidSObjInfo";
    //食盐检查上报接口（带多文件上传）
    public static final String Post_Salt_submit = "/caseInfoController/uploadAndroidSD";
    //专利案件查询列表
    public static final String Get_Patent_CaseList = "/caseInfoController/queryAndroidRL";
    //食盐案件查询列表
    public static final String Get_Salt_CaseList = "/caseInfoController/queryAndroidSL";
    //专利案件查询详情
    public static final String Get_Patent_CaseInfo = "/caseInfoController/queryAndroidRInfo";
    //食盐案件查询详情
    public static final String Get_Salt_CaseInfo = "/caseInfoController/queryAndroidSInfo";
    //食盐案件查询详情
    public static final String Get_Goods_Fuzzy_Query = "/caseInfoController/getAndroidFormAssociate";
    //专利案件上次详情
    public static final String Get_Last_Patent_CaseInfo = "/caseInfoController/getAndroidFormEcho";
    //基础信息查询
    public static final String Get_Search_CaseInfo = "/caseInfoController/getAndroidRSLData";
    //个人记录查询
    public static final String Get_Search_MyCaseInfo = "/caseInfoController/getOwnAndroidRSLData";




}
