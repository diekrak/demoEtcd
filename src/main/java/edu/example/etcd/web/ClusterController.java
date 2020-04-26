package edu.example.etcd.web;

import edu.example.etcd.client.EtcdClient;
import io.etcd.jetcd.cluster.MemberListResponse;
import io.etcd.jetcd.maintenance.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ClusterController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    EtcdClient etcdClient;

    @GetMapping("/members")
    public MemberListResponse getMembers() {
        return etcdClient.getClusterMembers();
    }

    @GetMapping("/member_detail")
    public Map<String, StatusResponse> getMemberDetails() {
        return etcdClient.getDetailedData();
    }

    @GetMapping("/get")
    public String getValue(@RequestParam(value = "key") String key) {
        return etcdClient.getValue(key);
    }

    @GetMapping("/add")
    public String addValue(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
        return etcdClient.addValue(key, value);
    }
}