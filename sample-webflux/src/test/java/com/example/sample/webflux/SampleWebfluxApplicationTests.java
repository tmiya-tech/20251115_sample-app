package com.example.sample.webflux;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.sample.webflux.todo.repository.TodoRepository;
import com.fasterxml.jackson.databind.JsonNode;

import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SampleWebfluxApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

	@Autowired
	private TodoRepository todoRepository;

	@BeforeEach
	void setUp() {
        StepVerifier.create(todoRepository.deleteAll()).verifyComplete();
        StepVerifier.create(todoRepository.count()).expectNext(0L).verifyComplete();
	}

	@Test
	void contextLoads() {
	}

    @Test
    void testEcEndpoints() {
        webTestClient.post()
            .uri("/api/ec/buy")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of(
                "itemId", "ABC-123",
                "quantity", 2
            ))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testTodoEndpoints() {
        AtomicReference<Long> todoId = new AtomicReference<>();

        // 一覧取得(0件)
        webTestClient.get()
            .uri("/api/todos")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(JsonNode.class)
            .value(todos -> {
                assertThat(todos).isEmpty();
            });
        
        // 登録
        webTestClient.post()
            .uri("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of(
                "title", "買い物",
                "description", "牛乳を買う"
            ))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(JsonNode.class)
            .value(todo -> {
                assertThat(todo.get("id")).isNotNull();
                todoId.set(todo.get("id").asLong());
                assertThat(todo.get("title").asText()).isEqualTo("買い物");
                assertThat(todo.get("description").asText()).isEqualTo("牛乳を買う");
                assertThat(todo.get("done").asBoolean()).isFalse();
            });

        // 一覧取得(1件)
        webTestClient.get()
            .uri("/api/todos")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(JsonNode.class)
            .value(todos -> {
                assertThat(todos).hasSize(1);
                JsonNode todo = todos.get(0);
                assertThat(todo.get("id").asLong()).isEqualTo(todoId.get());
                assertThat(todo.get("title").asText()).isEqualTo("買い物");
                assertThat(todo.get("description").asText()).isEqualTo("牛乳を買う");
                assertThat(todo.get("done").asBoolean()).isFalse();
            });

        // 更新
        webTestClient.put()
            .uri("/api/todos/{id}", todoId.get())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of(
                "title", "買い物 (更新)",
                "description", "牛乳とパンを買う",
                "done", false
            ))
            .exchange()
            .expectStatus().isOk()
            .expectBody(JsonNode.class)
            .value(todo -> {
                assertThat(todo.get("id").asLong()).isEqualTo(todoId.get());
                assertThat(todo.get("title").asText()).isEqualTo("買い物 (更新)");
                assertThat(todo.get("description").asText()).isEqualTo("牛乳とパンを買う");
                assertThat(todo.get("done").asBoolean()).isFalse();
            });

        // 取得
        webTestClient.get()
            .uri("/api/todos/{id}", todoId.get())
            .exchange()
            .expectStatus().isOk()
            .expectBody(JsonNode.class)
            .value(todo -> {
                assertThat(todo.get("id").asLong()).isEqualTo(todoId.get());
                assertThat(todo.get("title").asText()).isEqualTo("買い物 (更新)");
                assertThat(todo.get("description").asText()).isEqualTo("牛乳とパンを買う");
                assertThat(todo.get("done").asBoolean()).isFalse();
            });

        // 完了
        webTestClient.patch()
            .uri("/api/todos/{id}/complete", todoId.get())
            .exchange()
            .expectStatus().isOk()
            .expectBody(JsonNode.class)
            .value(todo -> {
                assertThat(todo.get("id").asLong()).isEqualTo(todoId.get());
                assertThat(todo.get("done").asBoolean()).isTrue();
            });

        // 一覧取得(1件)
        webTestClient.get()
            .uri("/api/todos")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(JsonNode.class)
            .value(todos -> {
                assertThat(todos).hasSize(1);
                JsonNode todo = todos.get(0);
                assertThat(todo.get("id").asLong()).isEqualTo(todoId.get());
                assertThat(todo.get("done").asBoolean()).isTrue();
            });

        // 削除
        webTestClient.delete()
            .uri("/api/todos/{id}", todoId.get())
            .exchange()
            .expectStatus().isNoContent();

        // 一覧取得(0件)
        webTestClient.get()
            .uri("/api/todos")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(JsonNode.class)
            .value(todos -> {
                assertThat(todos).isEmpty();
            });
    }
}
