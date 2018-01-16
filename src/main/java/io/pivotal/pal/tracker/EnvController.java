package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private final static String PORT = "PORT";
    private final static String MEMORY_LIMIT = "MEMORY_LIMIT";
    private final static String CF_INSTANCE_INDEX = "CF_INSTANCE_INDEX";
    private final static String CF_INSTANCE_ADDR = "CF_INSTANCE_ADDR";

    private Map<String, String> env;

    public EnvController(
            @Value("${PORT:NOT SET}") String port,
            @Value("${MEMORY_LIMIT:NOT SET}") String memoryLimit,
            @Value("${CF_INSTANCE_INDEX:NOT SET}") String cfInstanceIndex,
            @Value("${CF_INSTANCE_ADDR:NOT SET}") String cfInstanceAddr) {
        env = new HashMap<>();
        env.put(PORT, port);
        env.put(MEMORY_LIMIT, memoryLimit);
        env.put(CF_INSTANCE_INDEX, cfInstanceIndex);
        env.put(CF_INSTANCE_ADDR, cfInstanceAddr);
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return env;
    }
}
