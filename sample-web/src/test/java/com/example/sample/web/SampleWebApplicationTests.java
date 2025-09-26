package com.example.sample.web;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.sample.web.todo.repository.TodoRepository;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("null")
class SampleWebApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TodoRepository todoRepository;

	@BeforeEach
	void setUp() {
		todoRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testEcEndpoints() {
		ResponseEntity<JsonNode> response = restTemplate.postForEntity(
			"/api/ec/buy",
			Map.of(
				"itemId", "ABC-123",
				"quantity", 2
			),
			JsonNode.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().get("status").asText()).isEqualTo("ok");
	}

	@Test
	void testTodoEndpoints() {
		AtomicReference<Long> todoId = new AtomicReference<>();

		// 一覧取得(0件)
		ResponseEntity<JsonNode> listEmptyResponse = restTemplate.getForEntity("/api/todos", JsonNode.class);
		assertThat(listEmptyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(listEmptyResponse.getBody()).isNotNull();
		assertThat(listEmptyResponse.getBody().get("content").size()).isZero();

		// 登録
		ResponseEntity<JsonNode> createResponse = restTemplate.postForEntity(
			"/api/todos",
			Map.of(
				"title", "買い物",
				"description", "牛乳を買う"
			),
			JsonNode.class
		);

		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(createResponse.getBody()).isNotNull();
		assertThat(createResponse.getBody().get("id")).isNotNull();
		todoId.set(createResponse.getBody().get("id").asLong());
		assertThat(createResponse.getBody().get("title").asText()).isEqualTo("買い物");
		assertThat(createResponse.getBody().get("description").asText()).isEqualTo("牛乳を買う");
		assertThat(createResponse.getBody().get("done").asBoolean()).isFalse();

		// 一覧取得(1件)
		ResponseEntity<JsonNode> listSingleResponse = restTemplate.getForEntity("/api/todos", JsonNode.class);
		assertThat(listSingleResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(listSingleResponse.getBody()).isNotNull();
		JsonNode firstTodo = listSingleResponse.getBody().get("content").get(0);
		assertThat(listSingleResponse.getBody().get("content").size()).isEqualTo(1);
		assertThat(firstTodo.get("id").asLong()).isEqualTo(todoId.get());
		assertThat(firstTodo.get("title").asText()).isEqualTo("買い物");
		assertThat(firstTodo.get("description").asText()).isEqualTo("牛乳を買う");
		assertThat(firstTodo.get("done").asBoolean()).isFalse();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 更新
		ResponseEntity<JsonNode> updateResponse = restTemplate.exchange(
			"/api/todos/{id}",
			HttpMethod.PUT,
			new HttpEntity<>(
				Map.of(
					"title", "買い物 (更新)",
					"description", "牛乳とパンを買う",
					"done", false
				),
				headers
			),
			JsonNode.class,
			todoId.get()
		);

		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updateResponse.getBody()).isNotNull();
		assertThat(updateResponse.getBody().get("id").asLong()).isEqualTo(todoId.get());
		assertThat(updateResponse.getBody().get("title").asText()).isEqualTo("買い物 (更新)");
		assertThat(updateResponse.getBody().get("description").asText()).isEqualTo("牛乳とパンを買う");
		assertThat(updateResponse.getBody().get("done").asBoolean()).isFalse();

		// 取得
		ResponseEntity<JsonNode> getResponse = restTemplate.getForEntity("/api/todos/" + todoId.get(), JsonNode.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody()).isNotNull();
		assertThat(getResponse.getBody().get("id").asLong()).isEqualTo(todoId.get());
		assertThat(getResponse.getBody().get("title").asText()).isEqualTo("買い物 (更新)");
		assertThat(getResponse.getBody().get("description").asText()).isEqualTo("牛乳とパンを買う");
		assertThat(getResponse.getBody().get("done").asBoolean()).isFalse();

		// 完了
		ResponseEntity<JsonNode> completeResponse = restTemplate.exchange(
			"/api/todos/{id}/complete",
			HttpMethod.PATCH,
			new HttpEntity<>(null, headers),
			JsonNode.class,
			todoId.get()
		);

		assertThat(completeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(completeResponse.getBody()).isNotNull();
		assertThat(completeResponse.getBody().get("id").asLong()).isEqualTo(todoId.get());
		assertThat(completeResponse.getBody().get("done").asBoolean()).isTrue();

		// 一覧取得(1件)
		ResponseEntity<JsonNode> listCompletedResponse = restTemplate.getForEntity("/api/todos", JsonNode.class);
		assertThat(listCompletedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(listCompletedResponse.getBody()).isNotNull();
		JsonNode completedTodo = listCompletedResponse.getBody().get("content").get(0);
		assertThat(completedTodo.get("id").asLong()).isEqualTo(todoId.get());
		assertThat(completedTodo.get("done").asBoolean()).isTrue();

		// 削除
		ResponseEntity<Void> deleteResponse = restTemplate.exchange(
			"/api/todos/{id}",
			HttpMethod.DELETE,
			HttpEntity.EMPTY,
			Void.class,
			todoId.get()
		);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// 一覧取得(0件)
		ResponseEntity<JsonNode> listAfterDeleteResponse = restTemplate.getForEntity("/api/todos", JsonNode.class);
		assertThat(listAfterDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(listAfterDeleteResponse.getBody()).isNotNull();
		assertThat(listAfterDeleteResponse.getBody().get("content").size()).isZero();
	}
}
