package my.code.mycode.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Service
public class ClientServices {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClientServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String postRequest(String gitrepo, String name) {
        String url = "http://142.132.225.181";
        MyRequestBody myRequestBody = new MyRequestBody(gitrepo, name);
        return restTemplate.postForObject(url, myRequestBody, String.class);
    }

    public boolean checkRepo(String gitrepo) {
        // https://github.com/Coveros-GitHub-Sandbox/helloworld.git
        String str = Seperator(gitrepo, 4);
        String repoName = str.substring(0, str.length() - 4);
        String repoOwner = Seperator(gitrepo, 3);
        String githupRepo = "https://api.github.com/repos/" + repoOwner + "/" + repoName + "/languages";
        String jsonResponse = restTemplate.getForObject(githupRepo, String.class);
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            String java = jsonNode.get("Java").asText();
            return java != null;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The repo is invalid or doesn't contain java code");
            return false;
        }
    }

    public String Seperator(String input, int start) {
        String[] seperatedInput = input.split("/");
        return seperatedInput[start];
    }

    public boolean checkName(String name) throws JsonMappingException, JsonProcessingException {
        String url = "https://api.github.com/repos/bodacious-me/" + name;
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.has("id") ? false : true;
        } catch (HttpClientErrorException e) {
            return true;
        }
    }

    @Data
    public class MyRequestBody {
        private String gitrepo;
        private String name;

        public MyRequestBody(String gitrepo, String name) {
            this.name = name;
            this.gitrepo = gitrepo;
        }
    }
}
