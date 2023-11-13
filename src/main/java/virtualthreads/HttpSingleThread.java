package virtualthreads;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpSingleThread {
  private static HttpClient httpClient = HttpClient.newHttpClient();
  private static ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String... args) throws IOException, InterruptedException {
    long startTime = System.currentTimeMillis();
    List<Post> posts = doHttpGet("https://jsonplaceholder.typicode.com/posts", Post.class);
    List<Comment>
        comments =
            posts.stream()
                .flatMap(
                    post ->
                        doHttpGet(
                            "https://jsonplaceholder.typicode.com/posts/" + post.id() + "/comments",
                            Comment.class)
                            .stream())
                .toList();

    System.out.println(
        "#comments: %s, in %sms".formatted(comments.size(), System.currentTimeMillis() - startTime));
  }

  static <T> List<T> doHttpGet(String uri, Class<T> resultClass) {
    System.out.println("GET " + uri);
    HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).GET().build();
    try {
      HttpResponse<InputStream> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
      System.out.printf("GET finished with %s for %s%n", response.statusCode(), response.uri());
      return objectMapper.readerForListOf(resultClass).readValue(response.body());
    } catch (Exception e) {
      throw new RuntimeException("Could not complete HTTP call", e);
    }
  }

  record Post(Long userId, Long id, String title, String body) {}

  record Comment(Long postId, Long id, String name, String email, String body) {}
}
