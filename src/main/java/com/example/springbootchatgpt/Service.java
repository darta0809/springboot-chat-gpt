package com.example.springbootchatgpt;

import static com.theokanning.openai.service.OpenAiService.defaultClient;
import static com.theokanning.openai.service.OpenAiService.defaultObjectMapper;
import static com.theokanning.openai.service.OpenAiService.defaultRetrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.time.Duration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Retrofit;

@org.springframework.stereotype.Service
public class Service {

  @Value("${token}")
  private String token;

  void test() {
    OpenAiService service = new OpenAiService(token);

    CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt("今天天氣如何")
        .model("text-davinci-003")
        .echo(true)
        .temperature(0.5)
        .maxTokens(2048)
        .topP(1D)
        .frequencyPenalty(0D)
        .presencePenalty(0D)
        .build();

    service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

    // Custom OpenAiService
    ObjectMapper mapper = defaultObjectMapper();
    OkHttpClient client = defaultClient(token,
        Duration.ofSeconds(10))
        .newBuilder()
        .addInterceptor(doNothingInterceptor())
        .build();
    Retrofit retrofit = defaultRetrofit(client, mapper);

    OpenAiApi api = retrofit.create(OpenAiApi.class);
    new OpenAiService(api).createCompletion(completionRequest).getChoices()
        .forEach(System.out::println);

    // add proxy
    Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(8080));
    OkHttpClient httpClient = defaultClient(token,
        Duration.ofSeconds(10)).newBuilder().proxy(proxy).build();
    retrofit = defaultRetrofit(httpClient, mapper);
    api = retrofit.create(OpenAiApi.class);
//    new OpenAiService(api).createCompletion(completionRequest).getChoices()
//        .forEach(System.out::println);
  }

  public static Interceptor doNothingInterceptor() {
    return chain -> chain.proceed(chain.request());
  }
}
