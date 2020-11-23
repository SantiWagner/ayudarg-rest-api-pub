package com.ayudarg.restapi.payload.request;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationRequest {
	
	private Long receiver_id;
	
	private List<TransactionRequest> transactions;

}
