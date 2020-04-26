package edu.example.etcd.client;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.cluster.MemberListResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.maintenance.StatusResponse;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Charsets.UTF_8;

@Component
public class EtcdClient {

    public static final Map<String, String> NODES = new HashMap<>();
    static {
        NODES.put("etcd1_1", "http://172.28.1.1:2379");
        NODES.put("etcd2_1", "http://172.28.1.2:2379");
        NODES.put("etcd3_1", "http://172.28.1.3:2379");
        NODES.put("etcd4_1", "http://172.28.1.4:2379");
        NODES.put("etcd5_1", "http://172.28.1.5:2379");
    }

    /**
     * Put a value in cluster
     * @param key name of key to add
     * @param value value to add
     * @return String with results
     */
    public String addValue(final String key, final String value) {
        Client client = Client.builder().endpoints(NODES.values().toArray(new String[0])).build();
        KV kvClient = client.getKVClient();
        ByteSequence byteKey = ByteSequence.from(key.getBytes());
        ByteSequence byteValue = ByteSequence.from(value.getBytes());

        try {
            kvClient.put(byteKey, byteValue).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error adding value: " + e.getMessage();
        }
        return "Value Added";
    }

    /**
     * Gets the value of a key
     * @param key to get data
     * @return String with results
     */
    public String getValue(final String key) {
        Client client = Client.builder().endpoints(NODES.values().toArray(new String[0])).build();
        KV kvClient = client.getKVClient();
        ByteSequence byteKey = ByteSequence.from(key.getBytes());
        try {
            CompletableFuture<GetResponse> getFuture = kvClient.get(byteKey);
            GetResponse response = getFuture.get();
            List<KeyValue> kvs = response.getKvs();
            if (kvs != null && !kvs.isEmpty()) {
                String value = kvs.get(0).getValue().toString(UTF_8);
                return "Key: " + key + "   Value: " + value;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "No Values Found for key: " + key;
    }

    /**
     * Gets cluster members with basic data
     * @return MemberListResponse
     */
    public MemberListResponse getClusterMembers() {
        Client client = Client.builder().endpoints(NODES.values().toArray(new String[0])).build();
        try {
            MemberListResponse memberListResponse = client.getClusterClient().listMember().get();
            return memberListResponse;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * Gets detailed data from cluster node
     * @return Map with name of node and its data
     */
    public Map<String, StatusResponse> getDetailedData() {

        Map<String, StatusResponse> results = new HashMap<>(5);
        Client client = Client.builder().endpoints(NODES.values().toArray(new String[0])).build();
        try {
            for (Map.Entry<String, String> nameUrl : NODES.entrySet()) {
                results.put(nameUrl.getKey(), client.getMaintenanceClient().statusMember(URI.create(nameUrl.getValue())).get());
            }
            return results;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
