package com.hongguaninfo.ocnandroid.beans;

/**
 * 客户信息
 * 
 * @author Administrator
 * 
 */
public class ClientInfo implements java.io.Serializable {
	private static final long serialVersionUID = -4157078561275930577L;
	/**
	 * 客户地址
	 */
	private String addr;
	/**
	 * 客户姓名
	 */
	private String custName;
	/**
	 * 客户id
	 */
	private String custId;
	/**
	 * 预约号
	 */
	private String orderNo;
	/**
	 * 预约时间
	 */
	private String creatDate;
	/**
	 * 维修时间？
	 */
	private String reservtionDate;
	/**
	 * 预约id
	 */
	private String orderId;

    	/**
	 * 设备名称
	 */
	private String deviceName;
	/**
	 * 客户状态
	 */
	private String status;
	/**
	 * 周期时间
	 */
	private String periodDate;

    /**
     * 报修类型
     */
    private String repairType;

    /**
     *  备注信息
     */
    private  String remark;

    /**
     * 预约结果
     * @return
     */
    private String reserveResult;

    /**
     * 用户证号
     * @return
     */
    private String accountNo;


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(String periodDate) {
		this.periodDate = periodDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * 故障信息,也称为操作指导
	 */
	private String bug;
	/**
	 * 客户电话
	 */
	private String phone;

	/**
	 * 预约单号
	 */

	public String getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(String creatDate) {
		this.creatDate = creatDate;
	}

	public String getReservtionDate() {
		return reservtionDate;
	}

	public void setReservtionDate(String reservtionDate) {
		this.reservtionDate = reservtionDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getBug() {
		return bug;
	}

	public void setBug(String bug) {
		this.bug = bug;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReserveResult() {
        return reserveResult;
    }

    public void setReserveResult(String reserveResult) {
        this.reserveResult = reserveResult;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
