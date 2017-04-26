package com.mycompany.example.plugin.myhook;

import com.atlassian.bitbucket.commit.Commit;
import com.atlassian.bitbucket.commit.CommitService;
import com.atlassian.bitbucket.commit.CommitsBetweenRequest;
import com.atlassian.bitbucket.hook.repository.AsyncPostReceiveRepositoryHook;
import com.atlassian.bitbucket.hook.repository.RepositoryHookContext;
import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageRequestImpl;
import com.atlassian.sal.api.component.ComponentLocator;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyRepositoryHook implements AsyncPostReceiveRepositoryHook {

    private static  Logger logger = LoggerFactory.getLogger(MyRepositoryHook.class);
    private static final int MAX_PAGE_REQUEST = 100;
    private static final String URL = "http://platform.cbayel.com:8080/commit";
    @Override
    public void postReceive(@Nonnull RepositoryHookContext repositoryHookContext, @Nonnull Collection<RefChange> collection) {
        try {
            CommitService commitService = ComponentLocator.getComponent(CommitService.class);
            List<Commit> commitList = new ArrayList<>();
            logger.error("start get commit info");
            collection.stream().forEach(ref->{
                Page<Commit> page = getChangesetPage(repositoryHookContext.getRepository(),ref,commitService);
                logger.error("form={},to={} ",ref.getFromHash(),ref.getToHash());
                logger.error("page size:{}",page.getSize());
                page.getValues().forEach(commit -> {
                    commitList.add(commit);
                    logger.error("commit",commit.getMessage());
                });

            });
            sendSearchEngine(new ObjectMapper().writeValueAsString(commitList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error",e);
        }
    }

    public Page<Commit> getChangesetPage(Repository repository, RefChange refChange,CommitService commitService) {
        CommitsBetweenRequest commitsBetweenRequest = new CommitsBetweenRequest
                .Builder(repository)
                .exclude(refChange.getFromHash(), new String[0])
                .include(refChange.getToHash(), new String[0])
                .build();
        int maxChangeSets = MAX_PAGE_REQUEST;
        return commitService.getCommitsBetween(commitsBetweenRequest, new PageRequestImpl(0, maxChangeSets));
    }

    public static void sendSearchEngine(String json) {
        CloseableHttpClient httpclient = null;
        try {
            httpclient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(URL);


            final String requestBody = json;
            if (requestBody != null) {
                StringEntity stringEntity = new StringEntity(json,Consts.UTF_8);
                stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                post.setEntity(stringEntity);


                ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                    public String handleResponse(
                            final HttpResponse response) throws IOException {
                        int status = response.getStatusLine().getStatusCode();
                        HttpEntity entity = response.getEntity();
                        if (status >= 200 && status < 300) {
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } else {
                            logger.error("Bad response status {}, body: {}, payload: {}", status, (entity != null ? EntityUtils.toString(entity) : "NO BODY"), requestBody);
                            throw new ClientProtocolException("Unexpected response status: " + status);
                        }
                    }

                };
                String responseBody = httpclient.execute(post, responseHandler);
                logger.info(responseBody);
            } else {
                logger.error("*** Skipping slack notification because invalid payload");
            }
        } catch (Exception e) {
            logger.error("Exception talking to slack", e);
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    logger.error("Exception closing httpclient", e);
                }
            }
        }
    }
}