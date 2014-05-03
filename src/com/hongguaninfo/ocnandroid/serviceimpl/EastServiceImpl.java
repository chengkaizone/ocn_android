package com.hongguaninfo.ocnandroid.serviceimpl;

import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;

import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.LinkInfo;
import com.hongguaninfo.ocnandroid.utils.OSUtil;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;

public class EastServiceImpl implements EastService {
	// 调用webService的命名空间
	String nameSpaceUri = OcnUtil.getNameSpace();
	// webService的描述文件连接
	String wsdlUrl = OcnUtil.getWsdl();
	// 手机串号
	String imsi = null;

	public EastServiceImpl(Context context) {
		imsi = OSUtil.getIMSI(context);
		imsi=OcnUtil.getTestImsi();
	}

	/**
	 * 验证登陆
	 */
	public String loginCheck(String name, String password) {
		String result = null;
		String methodName = "login";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("username", name);
		request.addProperty("pwd", password);
		request.addProperty("deviceId",imsi);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 0表示签到1表示签退
	 */
	public String signInOrOut(String userId, String type) {
		String result = null;
		String methodName = "";
		if (type.equals("0")) {
			methodName = "signIn";
		} else {
			methodName = "signOut";
		}

		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", userId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 获取预约列表
	 */
	public String reservationList(String userId, String branchCompany) {
		String result = null;
		String methodName = "reservationList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", userId);
		request.addProperty("branchCompany", branchCompany);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 获取维修列表
	 */
	public String maintainList(String userId, String branchCompany) {
		String result = null;
		String methodName = "maintainList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", userId);
		request.addProperty("branchCompany", branchCompany);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 获取公告列表
	 */
	public String getNoticeList(String branchCompany, String station) {
		String result = null;
		String methodName = "getNotices";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("branchCompany", branchCompany);
		request.addProperty("station", station);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 改约或预约（预约阶段）
	 */
	public String reservationSave(Map map) {
		String result = null;
		String methodName = "reservationSave";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", map.get("userId").toString());
		request.addProperty("reservResult", map.get("reservResult").toString());
		request.addProperty("reservDate", map.get("reservDate").toString());
		request.addProperty("reservPeriod", map.get("reservPeriod").toString());
		request.addProperty("option", map.get("option").toString());
		request.addProperty("remark", map.get("remark").toString());
		request.addProperty("orderId", map.get("orderId").toString());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 回单退单改派（预约阶段）
	 */
	public String reservationDistributeorBackorReturn(Map map) {
		String result = null;
		String methodName = "reservationDistributeorBackorReturn";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", map.get("userId").toString());
		request.addProperty("reservResult", map.get("reservResult").toString());
		request.addProperty("type", map.get("type").toString());
		request.addProperty("remark", map.get("remark").toString());
		request.addProperty("orderId", map.get("orderId").toString());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 改约（维修阶段）
	 */
	public String maintainReservation(Map<String, String> map) {
		String result = null;
		String methodName = "maintainReservationSave";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", map.get("userId"));
		request.addProperty("resultDate", map.get("resultDate"));
		request.addProperty("reservPeriod", map.get("reservPeriod"));
		request.addProperty("mtainResult", map.get("mtainResult"));
		request.addProperty("remark", map.get("remark"));
		request.addProperty("orderId", map.get("orderId"));
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 改派 退单（维修阶段）
	 */
	public String maintainBack(Map<String, String> map) {
		String result = null;
		String methodName = "maintainDistributeSave";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", map.get("userId"));
		request.addProperty("orderId", map.get("orderId"));
		request.addProperty("mtainResult", map.get("mtainResult"));
		request.addProperty("remark", map.get("remark"));
		request.addProperty("type", map.get("type"));
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 修改密码
	 */
	public String updatePwd(String userId, String oldPwd, String newPwd) {
		String result = null;
		String methodName = "updatePwd";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("userId", userId);
		request.addProperty("oldPwd", oldPwd);
		request.addProperty("newPwd", newPwd);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 维修登记
	 */
	public String checkIn(String orderId, String userId) {
		String result = null;
		String methodName = "checkIn";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("orderId", orderId);
		request.addProperty("userId", userId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String faultClassList() {
		String result = null;
		String methodName = "faultClassList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String faultCauseList() {
		String result = null;
		String methodName = "faultCauseList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String faultDispList() {
		String result = null;
		String methodName = "faultDispList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String faultMethodList() {
		String result = null;
		String methodName = "faultMethodList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String faultSolutionList(String faultClassId) {
		String result = null;
		String methodName = "faultSolutionList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("faultClassId", faultClassId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String faultLevelList(String faultCauseDtId) {
		String result = null;
		String methodName = "faultLevelList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("faultCauseDtId", faultCauseDtId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String snSignalParams(String digTypeId) {
		String result = null;
		String methodName = "snSignalParams";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("digTypeId", digTypeId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String ldSignalParams(String digTypeId) {
		String result = null;
		String methodName = "ldSignalParams";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("digTypeId", digTypeId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String digTypeList() {
		String result = null;
		String methodName = "digTypeList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String materialList() {
		String result = null;
		String methodName = "materialList";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String custAccount(String custAccountId) {
		String result = null;
		String methodName = "custAccount";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("custAccountId", custAccountId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}
	public String backOrderSave(String jsonStr,String snUserSignal,
			String snLouSignal,String material) { 
		String result = null;
		String methodName = "backOrderSave";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("jsonStr", jsonStr);
		request.addProperty("snUserSignal", snUserSignal);
		request.addProperty("snLouSignal", snLouSignal);
		request.addProperty("material", material);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String backOrderType() {
		String result = null;
		String methodName = "backOrderType";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}


	public String signalParams(String digTypeId) {
		String result = null;
		String methodName = "signalParams";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		request.addProperty("digTypeId", digTypeId);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	@Override
	public String selectProcessCount() {
		String result = null;
		String methodName = "selectProcessCount";
		String SOAP_ACTION = nameSpaceUri + methodName;
		SoapObject request = new SoapObject(nameSpaceUri, methodName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdlUrl);
			// web service请求
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}
}
