package com.ayudarg.restapi.payload.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TransactionRequest {
	
	@NotNull
	private Long resource_id;
	
	private Long demand_id;
	
	@NotNull
	private Double amount;
	
	private String comment;
}
