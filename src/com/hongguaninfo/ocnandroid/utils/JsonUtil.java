package com.hongguaninfo.ocnandroid.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hongguaninfo.ocnandroid.beans.AuthUser;
import com.hongguaninfo.ocnandroid.beans.ClientInfo;
import com.hongguaninfo.ocnandroid.beans.CustomAccount;
import com.hongguaninfo.ocnandroid.beans.EastVersionInfo;
import com.hongguaninfo.ocnandroid.beans.Fault;
import com.hongguaninfo.ocnandroid.beans.Notice;

public class JsonUtil {
	/**
	 * 返回维修客户信息
	 */
	public static CustomAccount getCustom(String str) {
		List<CustomAccount> cs = new ArrayList<CustomAccount>();
		try {
			org.json.JSONArray jsonArray = new org.json.JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					org.json.JSONObject tmp = jsonArray.getJSONObject(i);
					CustomAccount custom = new CustomAccount();
					custom.setCustomName(tmp.getString("customName"));
					custom.setCustomPhone(tmp.getString("customPhone"));
					custom.setCustomAddr(tmp.getString("customAddr"));
					cs.add(custom);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (cs.size() > 0) {
				return cs.get(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 返回模拟客户数据
	 * @param str
	 * @return
	 */
	public static CustomAccount getTestCustom(String str) {
		CustomAccount custom = new CustomAccount();
		custom.setCustomName("张三");
		custom.setCustomPhone("15834093324");
		custom.setCustomAddr("上海市徐家汇汇银广场2018号722室");
		return custom;
	}
	/**
	 * 故障相关列表
	 */
	public static List<Fault> getFaultDtList(String str) {
		List<Fault> busis = new ArrayList<Fault>();
		try {
			org.json.JSONArray jsonArray = new org.json.JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					org.json.JSONObject tmp = jsonArray.getJSONObject(i);
					Fault fc = new Fault();
					fc.setDtValue(tmp.getString("dtValue"));
					fc.setDtId(tmp.getString("dtId"));
					busis.add(fc);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return busis;
	}

	/**
	 * 故障相关列表
	 */
	public static List<Fault> getFaultDstList(String str) {
		List<Fault> busis = new ArrayList<Fault>();
		try {
			org.json.JSONArray jsonArray = new org.json.JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					org.json.JSONObject tmp = jsonArray.getJSONObject(i);
					Fault fc = new Fault();
					fc.setDstValue(tmp.getString("dstValue"));
					fc.setDstId(tmp.getString("dstId"));
					fc.setDtId(tmp.getString("dtId"));
					fc.setDstKey(tmp.getString("dstKey"));
					busis.add(fc);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return busis;
	}

	/**
	 * 获取当前用户信息
	 * 
	 * @param str
	 * @return
	 */
	public static List<AuthUser> parseUsers(String str) {
		List<AuthUser> users = new ArrayList<AuthUser>();
		try {
			org.json.JSONArray jsonArray = new org.json.JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					org.json.JSONObject tmp = jsonArray.getJSONObject(i);
					AuthUser user = new AuthUser();
					user.setRegion(tmp.getString("region"));
					user.setUserEmail(tmp.getString("userEmail"));
					user.setUserEmpno(tmp.getString("userEmpno"));
					user.setPassword(tmp.getString("password"));
					user.setUserType(tmp.getString("userType"));
					user.setUserId(tmp.getString("userId"));
					user.setUserName(tmp.getString("userName"));
					user.setUserPhone(tmp.getString("userPhone"));
					user.setUserStarLevel(tmp.getString("userStarLevel"));
					user.setStationName(tmp.getString("stationName"));
					// user.setDelStatus((short)tmp.getInt("delStatus"));
					user.setLoginName(tmp.getString("loginName"));
					user.setUserDesc(tmp.getString("userDesc"));
					BigDecimal big = new BigDecimal(tmp.getString("departid"));
					user.setDepartid(big);
					// user.setStatus((short)tmp.getInt("status"));
					user.setUserStatus(tmp.getString("userStatus"));
					user.setStation(tmp.getString("station"));
					user.setCoordinate(tmp.getString("coordinate"));
					user.setUserDept(tmp.getString("coordinate"));
					user.setTeam(tmp.getString("team"));
					user.setCreateUser(tmp.getLong("createUser"));
					user.setUserMobile(tmp.getString("userMobile"));
					// user.setUserSex((short)tmp.getInt("userSex"));
					// user.setUserAge((short)tmp.getInt("userAge"));
					user.setBranchCompany(tmp.getString("branchCompany"));
					users.add(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * 获取客户信息---预约列表
	 * 
	 * @param str
	 * @return
	 */
	public static List<ClientInfo> getClientInfo(String str) {
		List<ClientInfo> data = new ArrayList<ClientInfo>();
		try {
			org.json.JSONArray jsonArray = new org.json.JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					org.json.JSONObject tmp = jsonArray.getJSONObject(i);
					ClientInfo info = new ClientInfo();
					info.setOrderId(tmp.getString("ORDER_ID"));
					info.setOrderNo(tmp.getString("ORDER_NO"));
					info.setDeviceName(tmp.getString("deviceName"));
					info.setAddr(tmp.getString("CUST_ACCOUNT_ADDR"));
					info.setBug(tmp.getString("OPERATION_GUIDE"));
					info.setCreatDate(tmp.getString("CREATE_DATE"));
					info.setReservtionDate(tmp.getString("DISTRIBUTE_TIME"));
					info.setPhone(tmp.getString("CUST_ACCOUNT_CONNECT"));
					info.setCustName(tmp.getString("CUST_ACCOUNT_NAME"));
					info.setCustId(tmp.getString("CUST_ACCOUNT_ID"));
                    info.setAccountNo(tmp.getString("CUST_ACCOUNT_NO"));
                    if(tmp.getString("REPAIR_TYPE").equals("0")){
                        info.setRepairType("普通报修");
                    }else{
                        info.setRepairType("第"+tmp.getString("REPAIR_TYPE")+"次重复报修");
                    }
                    if(tmp.getString("REMARK").equals("null")){
                       info.setRemark(" ");
                    } else{
                       info.setRemark(tmp.getString("REMARK"));
                    }
					data.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取维修列表
	 * 
	 * @param str
	 * @return
	 */
	public static List<ClientInfo> getClientMaintainList(String str) {
		List<ClientInfo> data = new ArrayList<ClientInfo>();
		try {
			org.json.JSONArray jsonArray = new org.json.JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					org.json.JSONObject tmp = jsonArray.getJSONObject(i);
					ClientInfo info = new ClientInfo();
					info.setOrderId(tmp.getString("ORDER_ID"));
					info.setOrderNo(tmp.getString("ORDER_NO"));
					info.setDeviceName(tmp.getString("deviceName"));
					info.setAddr(tmp.getString("CUST_ACCOUNT_ADDR"));
                    info.setBug(tmp.getString("OPERATION_GUIDE"));
					info.setCreatDate(tmp.getString("CREATE_DATE"));
					String reserDate = tmp.getString("RESERVE_DATE");
					String phone = tmp.getString("CUST_ACCOUNT_CONNECT");
					String period = tmp.getString("RESERVE_PERIOD");
                    String reserveResult = tmp.getString("RESERVE_RESULT");
                    if(reserveResult.equals("CT_IN")){
                        reserveResult = "上门维修";
                    }else if(reserveResult.equals("CT_SD")){
                        reserveResult = "电话支持排障";
                    }else if(reserveResult.equals("CT_CH")){
                       reserveResult = "用户改期";
                    }else if(reserveResult.equals("CT_AD")){
                       reserveResult = "电话已联系好";
                    }else if(reserveResult.equals("CT_NA")){
                       reserveResult = "无法联系用户";
                    }else if(reserveResult.equals("CT_RG")){
                        reserveResult = "区域性故障维护";
                    }else if(reserveResult.equals("CT_OT")){
                        reserveResult = "其他";
                    } else {
                       reserveResult = " ";
                    }
					if (reserDate != null) {
						info.setReservtionDate(reserDate);
					}
					if (phone != null) {
						info.setPhone(phone);
					}
					if (period != null) {
						info.setPeriodDate(period);
					}
					info.setCustName(tmp.getString("CUST_ACCOUNT_NAME"));
					info.setCustId(tmp.getString("CUST_ACCOUNT_ID"));
                    info.setAccountNo(tmp.getString("CUST_ACCOUNT_NO"));
					if ("A".equals(info.getPeriodDate())) {
						info.setPeriodDate("上午");
					} else if ("P".equals(info.getPeriodDate())) {
						info.setPeriodDate("下午");
					} else if ("N".equals(info.getPeriodDate())) {
						info.setPeriodDate("晚上");
					}
					info.setStatus(tmp.getString("STATUS"));
                    if(tmp.getString("REPAIR_TYPE").equals("0")){
                        info.setRepairType("普通报修");
                    }else{
                        info.setRepairType("第"+tmp.getString("REPAIR_TYPE")+"次重复报修");
                    }
                    if(tmp.getString("REMARK").equals("null")){
                       info.setRemark(" ");
                    } else{
                       info.setRemark(tmp.getString("REMARK"));
                    }
                    info.setReserveResult(reserveResult);
					data.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取公告列表
	 * 
	 * @param str
	 * @return
	 */
	public static List<Notice> getNoticeList(String str) {
		List<Notice> data = new ArrayList<Notice>();
		try {
			JSONArray jsonArray = new JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					org.json.JSONObject tmp = jsonArray.getJSONObject(i);
					Notice notice = new Notice();
					notice.setContent(tmp.getString("CONTENT"));
					notice.setPublishDate(tmp.getString("PUBLISHDATE"));
					notice.setTitle(tmp.getString("TITLE"));
					data.add(notice);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取版本信息
	 * 
	 * @param str
	 * @return
	 */
	public static EastVersionInfo getVersionInfo(String str) {
		try {
			JSONArray jsonArray = new JSONArray(str);
			JSONObject obj = jsonArray.getJSONObject(0);
			EastVersionInfo version = new EastVersionInfo();
			version.setAppName(obj.getString("appname"));
			version.setVersionName(obj.getString("verName"));
			version.setVersionCode(obj.getString("verCode"));
			version.setApkName(obj.getString("apkName"));
			return version;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String listToJsonString(List<Map<String,String>> data){
		String sss="";
		for(int i=0;i<data.size();i++){
			Map<String,String> mp=data.get(i);
			String s=mp.keySet().iterator().next();
			String v=mp.get(s);
			if(i==data.size()-1){
				sss+="{'id':'"+s+"',"+"'num':'"+v+"'}";
			}else{ 
				sss+="{'id':'"+s+"',"+"'num':'"+v+"'},";
			}
		}
		return sss;
	}
	public static String listToJson(List<Map<String,String>> data){
		String sss="";
		for(int i=0;i<data.size();i++){
			Map<String,String> mp=data.get(i);
			String s=mp.keySet().iterator().next();
			String v=mp.get(s);
			if(i==data.size()-1){
				sss+=s+","+v;
			}else{ 
				sss+=s+","+v+";";
			}
		}
		return sss;
	}
	
	public static String snldToJsonString(List<Map<String,String>> data){
		String sss="";
		for(int i=0;i<data.size();i++){
			Map<String,String> mp=data.get(i);
			String s=mp.keySet().iterator().next();
			String v=mp.get(s);
			if(i==data.size()-1){
				sss+="{'id':'"+s+"',"+"'value':'"+v+"'}";
			}else{ 
				sss+="{'id':'"+s+"',"+"'value':'"+v+"'},";
			}
		}
		return sss;
	}
	public static List<ClientInfo> getTestClientInfo() {
		List<ClientInfo> data = new ArrayList<ClientInfo>();
			for (int i = 0; i < 10; i++) {
					ClientInfo info = new ClientInfo();
					info.setOrderId("24324");
					info.setOrderNo("2132");
					info.setDeviceName("身份加深对南方");
					info.setAddr("撒开发商打开附件三神展开了福建省将对方");
					info.setBug("斯蒂芬金速度快了附近的深刻雷锋精神");
					info.setCreatDate("2012-10-30 15:30:23");
					info.setReservtionDate("2012-11-1 15:23:23");
					info.setPhone("天宇");
					info.setCustName("张三");
					info.setCustId("2142");
                    info.setAccountNo("3242358");
                    info.setRepairType("普通报修");
                    info.setRemark("REMARK");
//                    if(tmp.getString("REPAIR_TYPE").equals("0")){
//                        info.setRepairType("普通报修");
//                    }else{
//                        info.setRepairType("第"+tmp.getString("REPAIR_TYPE")+"次重复报修");
//                    }
//                    if(tmp.getString("REMARK").equals("null")){
//                       info.setRemark(" ");
//                    } else{
//                       info.setRemark(tmp.getString("REMARK"));
//                    }
					data.add(info);
			}
		return data;
	}
	public static List<ClientInfo> getTestMaintainList() {
		List<ClientInfo> data = new ArrayList<ClientInfo>();
			for (int i = 0; i < 10; i++) {
					ClientInfo info = new ClientInfo();
					info.setOrderId("23847");
					info.setOrderNo("2357932");
					info.setDeviceName("第三方的设计费");
					info.setAddr("速度快了疯狂绝对是您发送的会计法上岛咖啡电视里放");
                    info.setBug("束带结发加深对雷锋精神的");
					info.setCreatDate("2012-09-12 12:12:34");
					String reserDate = "2012-09-12 12:12:34";
					String phone ="天宇";
					String period = "1周";
                    String reserveResult = "已修好";
                    if(reserveResult.equals("CT_IN")){
                        reserveResult = "上门维修";
                    }else if(reserveResult.equals("CT_SD")){
                        reserveResult = "电话支持排障";
                    }else if(reserveResult.equals("CT_CH")){
                       reserveResult = "用户改期";
                    }else if(reserveResult.equals("CT_AD")){
                       reserveResult = "电话已联系好";
                    }else if(reserveResult.equals("CT_NA")){
                       reserveResult = "无法联系用户";
                    }else if(reserveResult.equals("CT_RG")){
                        reserveResult = "区域性故障维护";
                    }else if(reserveResult.equals("CT_OT")){
                        reserveResult = "其他";
                    } else {
                       reserveResult = " ";
                    }
					if (reserDate != null) {
						info.setReservtionDate(reserDate);
					}
					if (phone != null) {
						info.setPhone(phone);
					}
					if (period != null) {
						info.setPeriodDate(period);
					}
					info.setCustName("李四");
					info.setCustId("23423");
                    info.setAccountNo("324u234");
					if ("A".equals(info.getPeriodDate())) {
						info.setPeriodDate("上午");
					} else if ("P".equals(info.getPeriodDate())) {
						info.setPeriodDate("下午");
					} else if ("N".equals(info.getPeriodDate())) {
						info.setPeriodDate("晚上");
					}
					if(i<5){
						info.setStatus("4");
					}else{
						info.setStatus("3");
					}
					info.setRepairType("第二次重复报修");
//                    if(tmp.getString("REPAIR_TYPE").equals("0")){
//                        info.setRepairType("普通报修");
//                    }else{
//                        info.setRepairType("第"+tmp.getString("REPAIR_TYPE")+"次重复报修");
//                    }
//                    if(tmp.getString("REMARK").equals("null")){
//                       info.setRemark(" ");
//                    } else{
//                       info.setRemark(tmp.getString("REMARK"));
//                    }
                    info.setRemark("REMARK");
                    info.setReserveResult(reserveResult);
					data.add(info);
			}
		return data;
	}
}