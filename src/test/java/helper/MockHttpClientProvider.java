package helper;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockHttpClientProvider{

    public static CloseableHttpClient get(int statusCode, String content) throws Exception{
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(statusCode);
        HttpEntity entity = mock(HttpEntity.class);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        when(httpClient.execute(any())).thenReturn(response);
        return httpClient;
    }

}
