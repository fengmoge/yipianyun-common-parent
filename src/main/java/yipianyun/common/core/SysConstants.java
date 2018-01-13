package yipianyun.common.core;
 
public class SysConstants {
	/**
	 * 登录用户
	 */
	public static final String LOGINUSERS = "LOGINUSERS";
	/*
	 * 默认学生角色编码
	 */
	public static final String ROLE_STU_DEFAULT = "RG999";
	/*
	 * 默认老师角色编码
	 */
	public static final String ROLE_TEACH_DEFAULT = "RG905";
	
	/**
	 * 登录地址
	 */
//	public static final String LOGIN_PATH = "/login/login.jsp";
	public static final String LOGIN_PATH = "/KY_M/login/login.jsp";
	
	public static final String ERROR_PAGE = "errorPage";

	/* Web应用根路径 */
	public static final String REAL_PATH = "REAL_PATH";

	/* Web ContextPath */
	public static final String ROOT_PATH = "ROOT_PATH";

	/* Servlet Path */
	public static final String CORE_PATH = "CORE_PATH";
	
	/*登录不过滤的url*/
	public static final String ESCAPE_URL = "ESCAPE_URL";

	/**
	 * 失败标示
	 */
	public static final int ERROR = 500;
	
	/**
	 * 成功标示
	 */
	public static final int SUCCESS = 200;
	
	/**
	 * 审计字段--创建人
	 */
	public static final String CREATOR = "creator";
	
	/**
	 * 审计字段--创建时间
	 */
	public static final String CREATETIME = "createtime";
	
	/**
	 * 审计字段--修改人
	 */
	public static final String MODIFIER = "modifier";
	
	/**
	 * 审计字段--修改时间
	 */
	public static final String MODIFYTIME = "modifytime";
	
	/**
	 * 添加操作
	 */
	public static final String INSERT = "insert";
	
	/**
	 * 修改操作
	 */
	public static final String UPDATE = "update";
	
	/**
	 * 公告状态	0--->未发布
	 */
	public static final String STATUS_0 = "0";
	
	/**
	 * 公告状态	1--->已发布
	 */
	public static final String STATUS_1 = "1";
	/**
	 * 公司logo
	 */
	public static final String LOGO_FILEPATH = "/resources/logo/";

	/**
	 * ISAAS访问加密码过期时间(毫秒)
	 */
	public static final long INTEVAL = 20*1000L;
	
	/**
	 * 课运接入isaas后是否拦截登录及主页(0:关)
	 */
	public static final String SWITCH_OFF = "0";
	/**
	 * 课运接入isaas后是否拦截登录及主页(1:开)
	 */
	public static final String SWITCH_ON = "1";
}
