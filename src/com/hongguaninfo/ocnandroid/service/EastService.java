package com.hongguaninfo.ocnandroid.service;

import java.util.Map;

public interface EastService {
	/**
	 * 验证登录--
	 * 
	 * @param name
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public String loginCheck(String name, String password);

	/**
	 * 用于签到签退
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public String signInOrOut(String userId, String type);

	/**
	 * 获取公告列表
	 * 
	 * @return
	 */
	public String getNoticeList(String branchCompany, String station);

	/**
	 * 获取预约列表
	 * 
	 * @param userId
	 * @param branchCompany
	 * @return
	 */
	public String reservationList(String userId, String branchCompany);

	/**
	 * 提交预约表单
	 * 
	 * @param map
	 * @return
	 */
	public String reservationSave(Map map);

	/**
	 * 获取提交返回信息
	 * 
	 * @param map
	 * @return
	 */
	public String reservationDistributeorBackorReturn(Map map);

	/**
	 * 改约（维修阶段）
	 */
	public String maintainReservation(Map<String, String> map);

	/**
	 * 改派 退单（维修阶段）
	 */
	public String maintainBack(Map<String, String> map);

	/**
	 * 修改密码
	 */
	public String updatePwd(String userId, String oldPwd, String newPwd);

	/**
	 * 获取维修列表
	 */
	public String maintainList(String userId, String branchCompany);

	/**
	 * 维修登记
	 */
	public String checkIn(String orderId, String userId);

	/**
	 * 请求故障类型列表
	 * 
	 * @return
	 */
	public String faultClassList();

	/**
	 * 故障原因
	 * 
	 * @return
	 */
	public String faultCauseList();

	/**
	 * 故障表象
	 * 
	 * @return
	 */
	public String faultDispList();

	/**
	 * 排障手段
	 * 
	 * @return
	 */
	public String faultMethodList();

	/**
	 * 解决手段
	 */
	public String faultSolutionList(String faultClassId);

	/**
	 * 根据原因获取故障等级
	 */
	public String faultLevelList(String faultCauseDtId);

	/**
	 * 获取业务类型列表
	 */
	public String digTypeList();

	/**
	 * 根据业务类型获取用户室内终端信号
	 */
	public String snSignalParams(String digTypeId);

	/**
	 * 根据业务类型获取楼道分支TAP口信号 指标参数
	 */
	public String ldSignalParams(String digTypeId);

	/**
	 * 获取辅料列表
	 */
	public String materialList();

	/**
	 * 得到维修客户信息
	 * @param custAccountId
	 * @return
	 */
	public String custAccount(String custAccountId);
	/**
	 * 回单确认接口
	 */
	public String backOrderSave(String jsonStr,String snUserSignal,
			String snLouSignal,String material);
	/**
	 * 所有的回单接口
	 */
	public String backOrderType();
	/**
	 * 室内楼道
	 */
	 public String signalParams(String digTypeId);
	 /**
	  * 
	  * @return
	  */
	 public String selectProcessCount();
}