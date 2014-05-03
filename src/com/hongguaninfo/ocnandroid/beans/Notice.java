package com.hongguaninfo.ocnandroid.beans;

/**
 * 公告
 * 
 * @author Administrator
 * 
 */
public class Notice implements java.io.Serializable {
	// 主题
	private String title;
	private String publishDate;
	private String updateDate;
	// 创建时间
	private String createDate;
	// 公告内容
	private String content;
	private String publishRangeStation;
	private String publishRangeRegion;

	private int validityType;
	private int publishRangleCity;
	private int noticeLevel;
	private int publishUser;
	private int publishStatus;
	// 创建用户
	private int createUser;
	private int noticeId;
	private int delStatus;
	private int updateUser;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPublishRangeStation() {
		return publishRangeStation;
	}

	public void setPublishRangeStation(String publishRangeStation) {
		this.publishRangeStation = publishRangeStation;
	}

	public String getPublishRangeRegion() {
		return publishRangeRegion;
	}

	public void setPublishRangeRegion(String publishRangeRegion) {
		this.publishRangeRegion = publishRangeRegion;
	}

	public int getValidityType() {
		return validityType;
	}

	public void setValidityType(int validityType) {
		this.validityType = validityType;
	}

	public int getPublishRangleCity() {
		return publishRangleCity;
	}

	public void setPublishRangleCity(int publishRangleCity) {
		this.publishRangleCity = publishRangleCity;
	}

	public int getNoticeLevel() {
		return noticeLevel;
	}

	public void setNoticeLevel(int noticeLevel) {
		this.noticeLevel = noticeLevel;
	}

	public int getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(int publishUser) {
		this.publishUser = publishUser;
	}

	public int getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public int getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public int getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(int updateUser) {
		this.updateUser = updateUser;
	}

}
