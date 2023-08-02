package com.apiAutomation.LearningApi.tests;

import org.testng.ITestContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import java.io.File;
import io.restassured.response.Response;
import java.util.Random;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JiraApi {

	@BeforeMethod
	public void setUp() {
		baseURI = "https://manpret.atlassian.net";
	}

	public static String randomString() {
		// create a string of all characters
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		// create random string builder
		StringBuilder sb = new StringBuilder();

		// create an object of Random class
		Random random = new Random();

		// specify length of random string
		int length = 7;

		for (int i = 0; i < length; i++) {

			// generate random index number
			int index = random.nextInt(alphabet.length());

			// get character specified by index
			// from the string
			char randomChar = alphabet.charAt(index);

			// append the character to string builder
			sb.append(randomChar);
		}

		String randomString = sb.toString();
		System.out.println("Random String is: " + randomString);
		return randomString;

	}

	@Test(priority = 1)
	public void createIssueMetaData(ITestContext context) {
		Response response = given().header("Authorization",
				"Basic YXNobm9vci5wcmVldDEzNUBnbWFpbC5jb206QVRBVFQzeEZmR0YwamNBbVlhLXpZa3UyQ0hZSnZTME02YjZPSzRYdnRHYUtwU0l1QnNkTDdoS2d2NFhHMFJldTJNU2VrSjBzMTZoYjdZRzZncmtvcXIyMV9ldTFEWGRlYUhoM2lCUWYxNXBTOUxsMkpDMldpd2tXc3htVG41aTFFbGREZENyaVd2T1FKbE5FRklha3hEcnRPaDdPaE81VXJSWWphZGtpeUtCa0dacjltSDdycEY0PUQyRDQ4MTVE")
				.when().log().all().get("/rest/api/3/issue/createmeta");
		
		//Extracting the json path of project id and issue id.

		int issueId = response.body().jsonPath().getInt("projects[0].issuetypes[0].id");
		int projectId = response.body().jsonPath().getInt("projects[0].id");

		System.out.println("Issue Id od new generated Issue- " + issueId);
		context.setAttribute("issueId", issueId);
		System.out.println("Project Id : " + projectId);
		context.setAttribute("projectId", projectId);

	}

	@Test(priority = 2)
	public void createIssue(ITestContext context) {

		JsonObject createIssueRequest = new JsonObject();

		JsonObject fields = new JsonObject();

		fields.addProperty("summary", "Test Summary as shown in class");

		JsonObject project = new JsonObject();

		project.addProperty("id", "10000");

		fields.add("project", project);

		JsonObject description = new JsonObject();

		description.addProperty("type", "doc");
		description.addProperty("version", 1);

		JsonArray content = new JsonArray();

		JsonObject contentObject = new JsonObject();

		contentObject.addProperty("type", "paragraph");

		JsonObject contentInternalObject = new JsonObject();
		contentInternalObject.addProperty("text", "Test description as shown in class");

		contentInternalObject.addProperty("type", "text");

		JsonArray contentInternalArray = new JsonArray();
		contentInternalArray.add(contentInternalObject);

		contentObject.add("content", contentInternalArray);

		content.add(contentObject);

		description.add("content", content);

		fields.add("description", description);

		JsonObject issuetype = new JsonObject();
		issuetype.addProperty("id", 10001);

		fields.add("issuetype", issuetype);

		createIssueRequest.add("fields", fields);

		System.out.println(createIssueRequest.toString());

		// File json = new File("C:/Users/Gurwinder/OneDrive/Desktop/QA
		// class/jira.json");
		Response response = given().header("Authorization",
				"Basic YXNobm9vci5wcmVldDEzNUBnbWFpbC5jb206QVRBVFQzeEZmR0YwamNBbVlhLXpZa3UyQ0hZSnZTME02YjZPSzRYdnRHYUtwU0l1QnNkTDdoS2d2NFhHMFJldTJNU2VrSjBzMTZoYjdZRzZncmtvcXIyMV9ldTFEWGRlYUhoM2lCUWYxNXBTOUxsMkpDMldpd2tXc3htVG41aTFFbGREZENyaVd2T1FKbE5FRklha3hEcnRPaDdPaE81VXJSWWphZGtpeUtCa0dacjltSDdycEY0PUQyRDQ4MTVE")
				.contentType("application/json").body(createIssueRequest).when().log().all().post("/rest/api/3/issue/");

		int issueId = response.body().jsonPath().getInt("id");
		System.out.println("Issue Id od new generated Issue- " + issueId);
		context.setAttribute("issueId", issueId);

	}

	@Test(priority = 3)
	@Ignore
	public void editIssueMetaData(ITestContext context) {
		given().header("Authorization",
				"Basic YXNobm9vci5wcmVldDEzNUBnbWFpbC5jb206QVRBVFQzeEZmR0YwamNBbVlhLXpZa3UyQ0hZSnZTME02YjZPSzRYdnRHYUtwU0l1QnNkTDdoS2d2NFhHMFJldTJNU2VrSjBzMTZoYjdZRzZncmtvcXIyMV9ldTFEWGRlYUhoM2lCUWYxNXBTOUxsMkpDMldpd2tXc3htVG41aTFFbGREZENyaVd2T1FKbE5FRklha3hEcnRPaDdPaE81VXJSWWphZGtpeUtCa0dacjltSDdycEY0PUQyRDQ4MTVE")
				.when().log().all().get("/rest/api/3/issue/{issueId}/editmeta", context.getAttribute("issueId")).then()
				.log().all().statusCode(200);

	}

	@Test(priority = 4)
	public void editIssue(ITestContext context) {

		String updatedDescription = randomString();
		// String requestBody = "{\"update\": {\"summary\": [{\"set\": \"" +
		// updatedSummary + "\"}]}}";

		// System.out.println("Random String is: " + updatedSummary);
		// File json = new File("C:/Users/Gurwinder/OneDrive/Desktop/QA
		// class/editIssue.json");

		JsonObject createIssueRequest = new JsonObject();

		JsonObject fields = new JsonObject();

		fields.addProperty("summary", "Test Summary as shown in class");

		JsonObject project = new JsonObject();

		project.addProperty("id", "10000");

		fields.add("project", project);

		JsonObject description = new JsonObject();

		description.addProperty("type", "doc");
		description.addProperty("version", 1);

		JsonArray content = new JsonArray();

		JsonObject contentObject = new JsonObject();

		contentObject.addProperty("type", "paragraph");

		JsonObject contentInternalObject = new JsonObject();
		contentInternalObject.addProperty("text", updatedDescription);

		contentInternalObject.addProperty("type", "text");

		JsonArray contentInternalArray = new JsonArray();
		contentInternalArray.add(contentInternalObject);

		contentObject.add("content", contentInternalArray);

		content.add(contentObject);

		description.add("content", content);

		fields.add("description", description);

		JsonObject issuetype = new JsonObject();
		issuetype.addProperty("id", 10001);

		fields.add("issuetype", issuetype);

		createIssueRequest.add("fields", fields);
		given().header("Authorization",
				"Basic YXNobm9vci5wcmVldDEzNUBnbWFpbC5jb206QVRBVFQzeEZmR0YwamNBbVlhLXpZa3UyQ0hZSnZTME02YjZPSzRYdnRHYUtwU0l1QnNkTDdoS2d2NFhHMFJldTJNU2VrSjBzMTZoYjdZRzZncmtvcXIyMV9ldTFEWGRlYUhoM2lCUWYxNXBTOUxsMkpDMldpd2tXc3htVG41aTFFbGREZENyaVd2T1FKbE5FRklha3hEcnRPaDdPaE81VXJSWWphZGtpeUtCa0dacjltSDdycEY0PUQyRDQ4MTVE")
				.contentType("application/json").body(createIssueRequest).when().log().all()
				.put("/rest/api/3/issue/{issueId}", context.getAttribute("issueId")).then().log().all().statusCode(204);

	}

	@Test(priority = 5)
	@Ignore

	public void deleteIssue(ITestContext context) {
		given().header("Authorization",
				"Basic YXNobm9vci5wcmVldDEzNUBnbWFpbC5jb206QVRBVFQzeEZmR0YwamNBbVlhLXpZa3UyQ0hZSnZTME02YjZPSzRYdnRHYUtwU0l1QnNkTDdoS2d2NFhHMFJldTJNU2VrSjBzMTZoYjdZRzZncmtvcXIyMV9ldTFEWGRlYUhoM2lCUWYxNXBTOUxsMkpDMldpd2tXc3htVG41aTFFbGREZENyaVd2T1FKbE5FRklha3hEcnRPaDdPaE81VXJSWWphZGtpeUtCa0dacjltSDdycEY0PUQyRDQ4MTVE")
				.when().log().all().delete("/rest/api/3/issue/{issueId}", context.getAttribute("issueId")).then().log()
				.all().statusCode(204);

	}

}
