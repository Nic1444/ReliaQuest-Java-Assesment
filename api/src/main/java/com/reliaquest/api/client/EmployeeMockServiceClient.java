package com.reliaquest.api.client;

import com.reliaquest.api.dto.EmplyeeClientMockResponse.EmployeeApiResponse;
import com.reliaquest.api.dto.EmplyeeClientMockResponse.EmployeeListApiResponse;
import com.reliaquest.api.dto.request.CreateEmployeeRequest;
import com.reliaquest.api.dto.response.EmployeeDto;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class EmployeeMockServiceClient {

    private final RestTemplate restTemplate;

    @Value("${mock.api.url}")
    private String mockApiUrl;

    public EmployeeMockServiceClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {

        /** Note ->
         *  WE never fetch ALL records at once if the data can be huge
         *  We use Pagination — External API should support page, size, or limit/offset parameters.
         *  But since it's clearly mentioned in Mock employee service's README, we should not modify service.
         *  (Implemented below logic considering there won't be huge employee data in external services)
         * */
        RetryTemplate retryTemplate = createRetryTemplate();

        return retryTemplate.execute(
                context -> {
                    ResponseEntity<EmployeeListApiResponse> response = restTemplate.exchange(
                            mockApiUrl,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<EmployeeListApiResponse>() {});

                    List<EmployeeDto> allEmployees = Optional.ofNullable(response.getBody())
                            .map(EmployeeListApiResponse::getData)
                            .orElse(Collections.emptyList());

                    if (CollectionUtils.isEmpty(allEmployees)) {
                        log.warn("Fetched empty employee list from API");
                        return ResponseEntity.noContent().build();
                    }

                    return ResponseEntity.ok(allEmployees);
                },
                context -> {
                    log.error("Failed to fetch employees after {} retries", context.getRetryCount());

                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.emptyList());
                });
    }

    public ResponseEntity<List<EmployeeDto>> getEmployeesByName(String name) {

        /** NOTE->
         * It's not good practice to fetch all the records from external service and then filter them in service using Java Streams.
         * Fetching millions of records means the entire response is loaded into app's memory (RAM) this can lead us to OutOfMemoryError or severely slow down our server.
         * Ideally, filtering, searching, sorting etc. should happen at the external API side, not on this side. we should ask for only what we need.
         *
         * But since it's clearly mentioned in Mock employee service's README  not to modify anything.
         *
         * (Implemented below logic considering there won't be huge employee data in external services)
         *
         * */
        ResponseEntity<EmployeeListApiResponse> response = restTemplate.exchange(
                mockApiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<EmployeeListApiResponse>() {});

        List<EmployeeDto> allEmployees = Optional.ofNullable(response.getBody())
                .map(EmployeeListApiResponse::getData)
                .orElse(Collections.emptyList());

        if (CollectionUtils.isEmpty(allEmployees)) {
            log.warn("No employees found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }

        List<EmployeeDto> filteredEmployees = allEmployees.stream()
                .filter(emp -> StringUtils.hasText(emp.getEmployeeName())
                        && emp.getEmployeeName().toLowerCase().contains(name.toLowerCase()))
                .toList();

        if (CollectionUtils.isEmpty(filteredEmployees)) {
            log.warn("No employee found with name: {}", name);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }
        return ResponseEntity.ok(filteredEmployees);
    }

    public ResponseEntity<EmployeeDto> getEmployeeById(String id) {
        String url = mockApiUrl + "/" + id;

        try {
            ResponseEntity<EmployeeApiResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<EmployeeApiResponse>() {});

            EmployeeDto employee = Objects.requireNonNull(response.getBody()).getData();
            return ResponseEntity.ok(employee);

        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Employee with id {} not found", id, ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (HttpClientErrorException ex) {
            log.error("Error while finding employee by Id ", ex);
            return ResponseEntity.status(ex.getStatusCode()).build();
        } catch (Exception ex) {
            log.error("Error while finding employee by Id ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        ResponseEntity<EmployeeListApiResponse> response = restTemplate.exchange(
                mockApiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<EmployeeListApiResponse>() {});

        List<EmployeeDto> allEmployees = Optional.ofNullable(response.getBody())
                .map(EmployeeListApiResponse::getData)
                .orElse(Collections.emptyList());

        Integer highestSalary = allEmployees.stream()
                .map(EmployeeDto::getEmployeeSalary)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);

        return ResponseEntity.ok(highestSalary);
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        ResponseEntity<EmployeeListApiResponse> response = restTemplate.exchange(
                mockApiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<EmployeeListApiResponse>() {});

        List<EmployeeDto> allEmployees = Optional.ofNullable(response.getBody())
                .map(EmployeeListApiResponse::getData)
                .orElse(Collections.emptyList());

        List<String> topTenNames = allEmployees.stream()
                .filter(e -> e.getEmployeeSalary() != null && e.getEmployeeName() != null)
                .sorted(Comparator.comparingInt(EmployeeDto::getEmployeeSalary).reversed())
                .limit(10)
                .map(EmployeeDto::getEmployeeName)
                .toList();

        return ResponseEntity.ok(topTenNames);
    }

    public ResponseEntity<EmployeeDto> createEmployee(CreateEmployeeRequest empRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateEmployeeRequest> httpEntity = new HttpEntity<>(empRequest, headers);

        ResponseEntity<EmployeeApiResponse> response = restTemplate.exchange(
                mockApiUrl, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<EmployeeApiResponse>() {});

        EmployeeDto createdEmployee = Optional.ofNullable(response.getBody())
                .map(EmployeeApiResponse::getData)
                .orElseThrow(() -> new RuntimeException("Failed to create employee"));

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    private RetryTemplate createRetryTemplate() {

        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
