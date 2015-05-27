package com.miaotu.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Money;

/**
 * @author Jayden
 *
 */
public class MoneyResult extends BaseResult{
	@JsonProperty("items")
	private Money money;

	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
	}

	
}
