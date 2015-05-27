package com.miaotu.result;


import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.RedPackage;

/**
 * @author Jayden
 *
 */
public class RedPackageListResult extends BaseResult{
	@JsonProperty("items")
	private List<RedPackage> redPackageList;
	@JsonProperty("lucky_money")
    private String luckMoney;

	public List<RedPackage> getRedPackageList() {
		return redPackageList;
	}

	public void setRedPackageList(List<RedPackage> redPackageList) {
		this.redPackageList = redPackageList;
	}

    public String getLuckMoney() {
        return luckMoney;
    }

    public void setLuckMoney(String luckMoney) {
        this.luckMoney = luckMoney;
    }
}
