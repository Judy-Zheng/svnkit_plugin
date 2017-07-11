/**
 * Copyright 2015-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycompany.example.plugin.myhook;

import java.util.Date;

/**
 * <pre>
 *     {
 * "displayId": "8cacc1f7be1",
 * "id": "8cacc1f7be1e588445f77b53faa976c433561c9a",
 * "author": {"name": "how", "emailAddress": "704728332@qq.com"},
 * "authorTimestamp": 1492355386000,
 * "message": "测试提交",
 * "parents": [{"displayId": "2f6488863a5", "id": "2f6488863a55416ea4710a3e7f157232156c568f"}],
 * "properties": {},
 * "repository": {
 * "forkable": true,
 * "hierarchyId": "2d06d9e89e81c58acb17",
 * "id": 121,
 * "name": "docker-library",
 * "origin": null,
 * "project": {
 * "description": "docker 镜像构建scripts",
 * "id": 149,
 * "key": "DOC",
 * "name": "docker_builds",
 * "namespace": null,
 * "type": "NORMAL",
 * "public": false
 * },
 * "scmId": "git",
 * "slug": "docker-library",
 * "state": "AVAILABLE",
 * "public": false,
 * "fork": false,
 * "statusMessage": "Available"
 * }
 * }
 * </pre>
 *
 * @author how
 * @date 17/4/27
 */
public class CommitVO {
    private String id;
    private Author author;
    private String message;
    private Date authorTimestamp;

    private RepositoryVO repository;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getAuthorTimestamp() {
        return authorTimestamp;
    }

    public void setAuthorTimestamp(Date authorTimestamp) {
        this.authorTimestamp = authorTimestamp;
    }

    public RepositoryVO getRepository() {
        return repository;
    }

    public void setRepository(RepositoryVO repository) {
        this.repository = repository;
    }
}
