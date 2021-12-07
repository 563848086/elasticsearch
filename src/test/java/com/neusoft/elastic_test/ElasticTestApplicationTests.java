package com.neusoft.elastic_test;

import com.alibaba.fastjson.JSON;
import com.neusoft.elastic_test.bean.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ElasticTestApplicationTests {
    @Autowired
    public RestHighLevelClient restHighLevelClient;

    /**
     * @description: 索引操作
     */


    /**
     * @author: Junliang
     * @description: 创建索引
     * @date: 2021/12/6 14:21
     */
    @Test
    void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("test_index");
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        //查看索引是否建立成功
        System.out.println("索引是否创建成功:"+response.isAcknowledged());
        restHighLevelClient.close();
    }

    /**
     * @author: Junliang
     * @description: 获取索引是否存在
     * @date: 2021/12/6 14:21
     */
    @Test
    void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("test_index");
        Boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        //查看索引是否建立成功
        System.out.println("索引是否存在:"+exists);
        restHighLevelClient.close();
    }

    /**
     * @author: Junliang
     * @description: 删除索引
     * @date: 2021/12/6 14:21
     */
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("test_index");
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        //查看索引是否建立成功
        System.out.println("索引是否删除成功:"+response.isAcknowledged());
        restHighLevelClient.close();
    }



    /**
     * @description: 文档操作
     */


    /**
     * @author: Junliang
     * @description: 创建文档
     * @date: 2021/12/6 14:21
     */
    @Test
    void createDocument() throws IOException {
        User user = new User("Junliang", "男", "足球音乐编程学习睡觉", 18);
        IndexRequest indexRequest = new IndexRequest("test_index");
        //可以给文档设置一个id
        indexRequest.id("666");
        indexRequest.timeout(TimeValue.timeValueMillis(1000));
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("创建文档结果:"+response.status());
        restHighLevelClient.close();
    }

    /**
     * @author: Junliang
     * @description: 修改文档
     * @date: 2021/12/6 14:21
     */
    @Test
    void updateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        //定位到要更新的索引和文档
        updateRequest.index("test_index").id("666");
        //设置更新内容
        updateRequest.doc(XContentType.JSON,"gender","女");
        UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println("更新结果："+ response.getResult());
        restHighLevelClient.close();
    }

    /**
     * @author: Junliang
     * @description: 查询文档
     * @date: 2021/12/6 14:21
     */
    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("test_index").id("666");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println("查询到的结果："+ response.getSourceAsString());
        restHighLevelClient.close();
    }

    /**
     * @author: Junliang
     * @description: 删除文档
     * @date: 2021/12/6 14:21
     */
    @Test
    void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("test_index").id("666");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println("删除结果："+ response.toString());
        restHighLevelClient.close();
    }


    /**
     * @author: Junliang
     * @description: 批量新增文档
     * @date: 2021/12/6 14:21
     */
    @Test
    void bulkCreateDocument() throws IOException {
        User user1 = new User("Jinxin", "男", "编程学习读书算命盘棍", 16);
        User user2 = new User("Xiaopeng", "男", "编程打麻将打牌学习", 18);
        User user3 = new User("Guangqi", "男", "编程游戏学习读书打牌", 20);
        BulkRequest request = new BulkRequest();
        //批量新增
        request.add(new IndexRequest().index("test_index").id("777").source(JSON.toJSONString(user1),XContentType.JSON));
        request.add(new IndexRequest().index("test_index").id("888").source(JSON.toJSONString(user1),XContentType.JSON));
        request.add(new IndexRequest().index("test_index").id("999").source(XContentType.JSON,JSON.toJSONString(user3)));
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println("批量结果："+ response.getItems());
        restHighLevelClient.close();
    }





}
