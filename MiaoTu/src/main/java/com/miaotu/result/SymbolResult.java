package com.miaotu.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miaotu.model.Symbol;


/**
 * 
 * @author zhangying
 *
 */
public class SymbolResult extends BaseResult{
	@JsonProperty("items")
	private Symbol symbol;

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
