package com.hongguaninfo.ocnandroid.beans;

public class Fault implements java.io.Serializable {
	private String dstKey;
	private String dstValue;
	private String dstId;
	private String dtId;
	private String type;
	private String groupId;
	private String dtKey;
	private String dtValue;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDtKey() {
		return dtKey;
	}

	public void setDtKey(String dtKey) {
		this.dtKey = dtKey;
	}

	public String getDtValue() {
		return dtValue;
	}

	public void setDtValue(String dtValue) {
		this.dtValue = dtValue;
	}

	public String getDstKey() {
		return dstKey;
	}

	public void setDstKey(String dstKey) {
		this.dstKey = dstKey;
	}

	public String getDstValue() {
		return dstValue;
	}

	public void setDstValue(String dstValue) {
		this.dstValue = dstValue;
	}

	public String getDstId() {
		return dstId;
	}

	public void setDstId(String dstId) {
		this.dstId = dstId;
	}

	public String getDtId() {
		return dtId;
	}

	public void setDtId(String dtId) {
		this.dtId = dtId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
