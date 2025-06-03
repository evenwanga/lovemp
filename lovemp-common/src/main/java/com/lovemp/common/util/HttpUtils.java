package com.lovemp.common.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP工具类
 * 
 * <p>基于Spring RestTemplate提供常用的HTTP请求方法，支持各种请求方式、参数传递、文件上传下载和重试机制。</p>
 * 
 * <h2>功能分类</h2>
 * <ol>
 *   <li>基础请求：GET、POST、PUT、DELETE等HTTP方法支持</li>
 *   <li>参数处理：URL参数、表单参数和JSON格式请求体</li>
 *   <li>文件传输：文件上传与下载</li>
 *   <li>请求头管理：自定义和默认请求头设置</li>
 *   <li>请求增强：超时设置、重试机制</li>
 *   <li>URL构建：带参数的URL构建</li>
 * </ol>
 * 
 * <h2>适用场景</h2>
 * <ol>
 *   <li>微服务间HTTP通信</li>
 *   <li>调用第三方RESTful API</li>
 *   <li>向前端提供数据获取接口</li>
 *   <li>文件上传与下载服务</li>
 *   <li>WebHook事件通知</li>
 *   <li>需要重试机制的不稳定网络环境</li>
 * </ol>
 * 
 * <h2>代码示例</h2>
 * <pre>
 * // 基本GET请求
 * String response = HttpUtils.get("https://api.example.com/users");
 * 
 * // 带参数GET请求
 * Map&lt;String, String&gt; params = new HashMap&lt;&gt;();
 * params.put("id", "123");
 * params.put("page", "1");
 * String response = HttpUtils.get("https://api.example.com/users", params);
 * 
 * // 发送JSON格式POST请求
 * UserDTO user = new UserDTO();
 * user.setName("张三");
 * user.setAge(30);
 * String response = HttpUtils.postJson("https://api.example.com/users", user);
 * 
 * // 上传文件
 * Map&lt;String, String&gt; fileParams = new HashMap&lt;&gt;();
 * fileParams.put("description", "用户头像");
 * String response = HttpUtils.uploadFile(
 *     "https://api.example.com/upload",
 *     "/path/to/avatar.jpg",
 *     "file",
 *     fileParams
 * );
 * 
 * // 下载文件
 * boolean success = HttpUtils.downloadFile(
 *     "https://api.example.com/files/report.pdf",
 *     "/downloads/report.pdf"
 * );
 * 
 * // 带重试的请求
 * String response = HttpUtils.requestWithRetry(
 *     () -&gt; HttpUtils.get("https://api.example.com/users"),
 *     3,
 *     1000
 * );
 * </pre>
 * 
 * <h2>使用注意事项</h2>
 * <ol>
 *   <li>所有请求默认10秒超时时间，长耗时操作应考虑调整超时设置</li>
 *   <li>文件上传下载操作可能耗费大量网络资源，建议异步处理大文件</li>
 *   <li>敏感信息（如密码、令牌）不应明文传输，使用HTTPS并考虑加密</li>
 *   <li>请求重试应用于幂等操作，非幂等操作（如资源创建）慎用重试机制</li>
 *   <li>错误处理：工具类会将网络异常转换为RuntimeException，调用方需妥善处理</li>
 *   <li>大批量请求应考虑限流和熔断机制，避免压垮目标服务</li>
 *   <li>在领域层使用时，建议封装为专用的基础设施服务，而非直接调用工具类</li>
 *   <li>默认使用UTF-8编码和JSON格式响应，特殊格式需自定义请求头</li>
 * </ol>
 */
public final class HttpUtils {

    private HttpUtils() {
        // 工具类不允许实例化
    }

    /**
     * 默认的RestTemplate实例
     */
    private static final RestTemplate REST_TEMPLATE = createRestTemplate();

    /**
     * 默认请求超时时间（毫秒）
     */
    private static final int DEFAULT_TIMEOUT = 10000;

    /**
     * 创建带有超时设置的RestTemplate实例
     * 
     * @return RestTemplate实例
     */
    private static RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // 使用SimpleClientHttpRequestFactory设置超时
        org.springframework.http.client.SimpleClientHttpRequestFactory factory = 
            new org.springframework.http.client.SimpleClientHttpRequestFactory();
        
        // 设置连接超时和读取超时
        factory.setConnectTimeout(DEFAULT_TIMEOUT);
        factory.setReadTimeout(DEFAULT_TIMEOUT);
        
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }

    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @return 响应内容
     */
    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * 发送带参数的GET请求
     *
     * @param url 请求URL
     * @param params 请求参数
     * @return 响应内容
     */
    public static String get(String url, Map<String, String> params) {
        return get(url, params, null);
    }

    /**
     * 发送带参数和请求头的GET请求
     *
     * @param url 请求URL
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应内容
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        // 构建请求URI
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (params != null) {
            params.forEach(builder::queryParam);
        }
        URI uri = builder.build().encode().toUri();

        // 设置请求头
        HttpHeaders httpHeaders = buildHeaders(headers);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);

        // 发送请求
        ResponseEntity<String> response = REST_TEMPLATE.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        return response.getBody();
    }

    /**
     * 发送POST请求（JSON格式）
     *
     * @param url 请求URL
     * @param body 请求体
     * @return 响应内容
     */
    public static String postJson(String url, Object body) {
        return postJson(url, body, null);
    }

    /**
     * 发送带请求头的POST请求（JSON格式）
     *
     * @param url 请求URL
     * @param body 请求体
     * @param headers 请求头
     * @return 响应内容
     */
    public static String postJson(String url, Object body, Map<String, String> headers) {
        // 设置请求头
        HttpHeaders httpHeaders = buildHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求体
        String requestBody = body instanceof String ? (String) body : JsonUtils.toJson(body);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        // 发送请求
        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * 发送POST请求（表单格式）
     *
     * @param url 请求URL
     * @param params 表单参数
     * @return 响应内容
     */
    public static String postForm(String url, Map<String, String> params) {
        return postForm(url, params, null);
    }

    /**
     * 发送带请求头的POST请求（表单格式）
     *
     * @param url 请求URL
     * @param params 表单参数
     * @param headers 请求头
     * @return 响应内容
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) {
        // 设置请求头
        HttpHeaders httpHeaders = buildHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 设置表单参数
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        if (params != null) {
            params.forEach(formParams::add);
        }

        // 设置请求体
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formParams, httpHeaders);

        // 发送请求
        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * 发送文件上传请求
     *
     * @param url 请求URL
     * @param filePath 文件路径
     * @param fileParam 文件参数名
     * @param params 其他表单参数
     * @return 响应内容
     */
    public static String uploadFile(String url, String filePath, String fileParam, Map<String, String> params) {
        return uploadFile(url, filePath, fileParam, params, null);
    }

    /**
     * 发送带请求头的文件上传请求
     *
     * @param url 请求URL
     * @param filePath 文件路径
     * @param fileParam 文件参数名
     * @param params 其他表单参数
     * @param headers 请求头
     * @return 响应内容
     * @throws IllegalArgumentException 如果文件不存在或不可读
     */
    public static String uploadFile(String url, String filePath, String fileParam, 
                                   Map<String, String> params, Map<String, String> headers) {
        // 检查文件
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("文件不存在或不可读: " + filePath);
        }

        // 设置请求头
        HttpHeaders httpHeaders = buildHeaders(headers);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 设置表单参数和文件
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        if (params != null) {
            params.forEach(formData::add);
        }
        formData.add(fileParam, new FileSystemResource(file));

        // 设置请求体
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, httpHeaders);

        // 发送请求
        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * 下载文件
     *
     * @param url 文件URL
     * @param savePath 保存路径
     * @return 是否下载成功
     */
    public static boolean downloadFile(String url, String savePath) {
        return downloadFile(url, savePath, null);
    }

    /**
     * 下载文件（带请求头）
     *
     * @param url 文件URL
     * @param savePath 保存路径
     * @param headers 请求头
     * @return 是否下载成功
     */
    public static boolean downloadFile(String url, String savePath, Map<String, String> headers) {
        try {
            // 设置请求头
            HttpHeaders httpHeaders = buildHeaders(headers);
            HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);

            // 发送请求获取文件字节
            ResponseEntity<byte[]> response = REST_TEMPLATE.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            );

            // 检查响应状态
            if (response.getStatusCode() != HttpStatus.OK) {
                return false;
            }

            // 保存文件
            byte[] fileBytes = response.getBody();
            if (fileBytes == null) {
                return false;
            }
            
            Path path = Paths.get(savePath);
            FileUtils.createParentDirectories(path);
            Files.write(path, fileBytes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 发送PUT请求（JSON格式）
     *
     * @param url 请求URL
     * @param body 请求体
     * @return 响应内容
     */
    public static String putJson(String url, Object body) {
        return putJson(url, body, null);
    }

    /**
     * 发送带请求头的PUT请求（JSON格式）
     *
     * @param url 请求URL
     * @param body 请求体
     * @param headers 请求头
     * @return 响应内容
     */
    public static String putJson(String url, Object body, Map<String, String> headers) {
        // 设置请求头
        HttpHeaders httpHeaders = buildHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 设置请求体
        String requestBody = body instanceof String ? (String) body : JsonUtils.toJson(body);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        // 发送请求
        ResponseEntity<String> response = REST_TEMPLATE.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        
        return response.getBody();
    }

    /**
     * 发送DELETE请求
     *
     * @param url 请求URL
     * @return 响应内容
     */
    public static String delete(String url) {
        return delete(url, null);
    }

    /**
     * 发送带请求头的DELETE请求
     *
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应内容
     */
    public static String delete(String url, Map<String, String> headers) {
        // 设置请求头
        HttpHeaders httpHeaders = buildHeaders(headers);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);

        // 发送请求
        ResponseEntity<String> response = REST_TEMPLATE.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        
        return response.getBody();
    }

    /**
     * 构建请求头
     *
     * @param headers 自定义请求头
     * @return HttpHeaders对象
     */
    private static HttpHeaders buildHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        
        // 设置默认的请求头
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        
        // 添加自定义请求头
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        
        return httpHeaders;
    }

    /**
     * 发送请求并重试（当请求失败时）
     *
     * @param supplier 请求提供者函数
     * @param retryCount 重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @param <T> 响应类型
     * @return 响应结果
     */
    public static <T> T requestWithRetry(java.util.function.Supplier<T> supplier, int retryCount, long retryInterval) {
        int attempts = 0;
        Throwable lastException = null;

        while (attempts <= retryCount) {
            try {
                return supplier.get();
            } catch (Exception e) {
                lastException = e;
                attempts++;
                
                // 如果已达到最大重试次数，则抛出异常
                if (attempts > retryCount) {
                    break;
                }
                
                // 等待一段时间后重试
                try {
                    TimeUnit.MILLISECONDS.sleep(retryInterval);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("请求被中断", ie);
                }
            }
        }
        
        throw new RuntimeException("请求失败，已重试" + retryCount + "次", lastException);
    }

    /**
     * 构建带参数的URL
     *
     * @param url 基础URL
     * @param params 参数
     * @return 完整URL
     */
    public static String buildUrl(String url, Map<String, String> params) {
        if (StringUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return url;
        }
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        params.forEach(builder::queryParam);
        return builder.build().encode().toUriString();
    }
} 