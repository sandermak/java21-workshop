package virtualthreads;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpAsync {
  private static HttpClient httpClient = HttpClient.newHttpClient();
  private static ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String... args) {
    long startTime = System.currentTimeMillis();

    CompletableFuture<List<Post>> postsFuture =
        doHttpGet("https://jsonplaceholder.typicode.com/posts", Post.class);

    postsFuture
        .thenCompose(
            posts -> {
              List<CompletableFuture<List<Comment>>> commentFutures =
                  posts.stream()
                      .map(
                          post ->
                              doHttpGet(
                                  "https://jsonplaceholder.typicode.com/posts/"
                                      + post.id()
                                      + "/comments",
                                  Comment.class))
                      .toList();
              return CompletableFuture.allOf(commentFutures.toArray(new CompletableFuture[0]))
                  .thenApply(
                      ignored ->
                          commentFutures.stream()
                              .map(CompletableFuture::join)
                              .flatMap(List::stream)
                              .toList());
            })
        .thenAccept(
            comments ->
                System.out.println(
                    "#comments: %s, in %sms"
                        .formatted(comments.size(), System.currentTimeMillis() - startTime)))
        .join();
  }

  static <T> CompletableFuture<List<T>> doHttpGet(String uri, Class<T> resultClass) {
    System.out.println("GET " + uri);
    HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).GET().build();

    CompletableFuture<HttpResponse<InputStream>> response =
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());

    return response.thenApply(
        res -> {
          try {
              System.out.printf("GET finished with %s for %s%n", res.statusCode(), res.uri());
            return objectMapper.readerForListOf(resultClass).readValue(res.body());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  record Post(Long userId, Long id, String title, String body) {}

  record Comment(Long postId, Long id, String name, String email, String body) {}
}
