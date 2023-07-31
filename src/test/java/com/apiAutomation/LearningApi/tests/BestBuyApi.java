package com.apiAutomation.LearningApi.tests;

import org.testng.ITestContext;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class BestBuyApi {
	
	@Test@Ignore
	public void getProducts() {
		baseURI="http://localhost:3030/";
		
		given().queryParam("$limit", 4).when().log().all().get("products").then().log().all().statusCode(200).assertThat().
		body("data[0].price", equalTo(5.49F)).body("data[0].id", notNullValue());;

}
	
	
	@Test
	public void extractProductId(ITestContext context) {
		baseURI="http://localhost:3030/";
		
		Response response =given().queryParam("$limit", 4).when().log().all().get("products");
		int productId=response.body().jsonPath().getInt("data[1].id");
		System.out.println("Product Id -" +productId);
		
		context.setAttribute("productId", productId);

}
	
	@Test
	public void getProductById(ITestContext context) {
		baseURI="http://localhost:3030/";
		
	given().when().get("products/{productId}",context.getAttribute("productId")).then().log().all().statusCode(200);

}
	
	}
