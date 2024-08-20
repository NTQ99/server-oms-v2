package ntq.server.oms.config;

import ntq.server.common.mapper.Entity2ErrorMessageResponseMapper;
import ntq.server.common.util.RequestLoggingFilter;
import ntq.server.common.util.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class ApplicationConfiguration {
  @Bean
  public RestClient restClient() {
    return new RestClient(HttpClient.newHttpClient());
  }
  @Bean
  public RequestLoggingFilter loggingFilter() {
    var filter = new RequestLoggingFilter(false);
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(10000);
    return filter;
  }
  @Bean
  public Entity2ErrorMessageResponseMapper entity2ErrorMessageResponseMapper() {
    return Entity2ErrorMessageResponseMapper.INSTANCE;
  }
}
