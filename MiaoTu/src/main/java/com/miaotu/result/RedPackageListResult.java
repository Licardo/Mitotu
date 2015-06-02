package com.miaotu.result;


import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.RedPackage;

/**
 * @author Jayden
 *
 */
public class RedPackageListResult extends BaseResult{
	@JsonProperty("Items")
	private List<RedPackage> redPackageList;

	public List<RedPackage> getRedPackageList() {
		return redPackageList;
	}

	public void setRedPackageList(List<RedPackage> redPackageList) {
		this.redPackageList = redPackageList;
	}

}
