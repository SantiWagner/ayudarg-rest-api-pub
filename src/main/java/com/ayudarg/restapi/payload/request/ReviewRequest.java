package com.ayudarg.restapi.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRequest {
	
	private Long transaction_id;
	
	private String comment;
	
	private String title;
	
	private Double rating;

}
