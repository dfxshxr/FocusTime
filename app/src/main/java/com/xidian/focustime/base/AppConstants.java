package com.xidian.focustime.base;



public class AppConstants {

    public static final String LOCK_STATE = "app_lock_state"; //应用锁开关(状态，true开，false关)
    public static final String ADVANCED_LOCK_STATE = "advanced_app_lock_state"; //高级锁定引擎开关(状态，true开，false关)
    public static final String LOCK_RECOMMEND_NUM = "lock_recommend_num"; //推荐加锁应用个数
    public static final String LOCK_SYS_APP_NUM = "lock_sys_app_num"; //系统应用个数
    public static final String LOCK_USER_APP_NUM = "lock_user_app_num"; //非系统应用个数
    public static final String LOCK_IS_INIT_RECOMMEND = "lock_is_init_recommend"; //是否初始化了recommend数据表
    public static final String LOCK_IS_INIT_DB = "lock_is_init_db"; //是否初始化了数据库表
    public static final String APP_PACKAGE_NAME = "com.xidian.qualitytime"; //包名
    public static final String LOCK_IS_HIDE_LINE = "lock_is_hide_line"; //是否隐藏路径
    public static final String LOCK_USER = "lock_user";//应用锁密码
    public static final String LOCK_PWD = "lock_pwd";//应用锁密码
    public static final String LOCK_IS_FIRST_LOCK = "is_lock"; //是否加过锁
    public static final String LOCK_AUTO_SCREEN = "lock_auto_screen"; //是否在手机屏幕关闭后再次锁定
    public static final String LOCK_AUTO_SCREEN_TIME = "lock_auto_screen_time"; //是否在手机屏幕关闭后一段时间再次锁定
    public static final String LOCK_CURR_MILLISENCONS = "lock_curr_milliseconds"; //记录当前的时间（毫秒）
    public static final String LOCK_APART_MILLISENCONS = "lock_apart_milliseconds"; //记录相隔的时间（毫秒）
    public static final String LOCK_APART_TITLE = "lock_apart_title"; ///记录相隔的时间对应的标题
    public static final String LOCK_LAST_LOAD_PKG_NAME = "last_load_package_name";
    public static final String LOCK_PACKAGE_NAME = "lock_package_name"; //点开的锁屏应用的包名
    public static final String LOCK_FROM = "lock_from"; //解锁后转跳的action
    public static final String LOCK_FROM_FINISH = "lock_from_finish"; //解锁后转跳的action是finish
    public static final String LOCK_FROM_SETTING = "lock_from_setting"; //解锁后转跳的action是setting
    public static final String LOCK_FROM_UNLOCK = "lock_from_unlock"; //解锁后转跳的action
    public static final String LOCK_FROM_LOCK_MAIN_ACITVITY = "lock_from_lock_main_activity";
    public static final String LOCK_IS_SELECT_ALL_APP = "lock_is_select_all_APP";
    public static final String LOCK_IS_SELECT_ALL_SYS = "lock_is_select_all_SYS";
    public static final String RUN_LOCK_STATE = "run_lock_state";
    public static final String LOCK_INSTALL = "lock_install";//锁定安装、卸载应用及修改权限界面

    public static final String LOCK_START_MILLISENCONS = "lock_start_milliseconds"; //记录开始学习的时间（毫秒）
    public static final String LOCK_CONTINUE_MILLISENCONS = "lock_continue_milliseconds"; //记录学校持续的时间（毫秒）
    public static final String LOCK_PLAY_START_MILLISENCONS = "lock_play_start_milliseconds"; //记录开始玩的时间（毫秒）
    public static final String LOCK_PLAY_SETTING_MILLISENCONS = "lock_play_setting_milliseconds"; //记录还能玩的设定值（毫秒）
    public static final String LOCK_PLAY_REMAIN_MILLISENCONS = "lock_play_remain_milliseconds"; //记录还能玩的时间（毫秒）

    public interface NOTIFICATION_ID {
        public static int SERVICE = 101;
        public static int FOREGROUND_SERVICE = 101;
    }
}
