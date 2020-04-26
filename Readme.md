#Demo: Create and etcd cluster with a java client

This repository contains a simple dome to show how wan we set up  a etcd cluster
locally, and how can be used by a java application using Jetcd library (https://github.com/etcd-io/jetcd).


##**Etcd cluster**
In the _resources_ folder can find the docker-compose.yml already preconfigured

we can see each container use as the base image bitnami/etcd:3. This image is crated by
Bitnami (more https://github.com/bitnami/bitnami-docker-etcd) 

So we will create the number of nodes required, in this case 5.
After that we will go to resource folder and run:

docker-compose up

This should be enough to start cluster, then can use the etcdctl preinstalled in any container to use cluster. e.g. (https://etcd.io/docs/v3.4.0/demo/)

##**Java Client**

Its a simple springboot application which uses teh Jetcd lib using maven

#### Maven
```xml
<dependency>
  <groupId>io.etcd</groupId>
  <artifactId>jetcd-core</artifactId>
  <version>${jetcd-version}</version>
</dependency>
```

We start then application locally and then can use any of the 4 rest enpoint provided.

###### Get key
http://localhost:8080/get?key=demoKey

###### Add a key
http://localhost:8080/add?key=demoKey&value=newVal

###### get detailed information of cluster members
http://localhost:8080/member_detail

###### Base data about cluster members
http://localhost:8080/members


