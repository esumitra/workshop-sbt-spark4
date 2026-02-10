# workshop (Scala 2.13.12 + Spark 4.0.2)

Single-module sbt project named **workshop**.

## What’s included
- `com.example.workshop.basics` — List examples using `map`, `filter`, `reduce`
- `com.example.workshop.spark` — Spark wordcount example
- Unit tests: `src/test`
- Integration tests: `src/it`
- Docker Compose Spark cluster (master + 2 workers) using the official Spark image
- Dockerfile that runs `WordCount` via `spark-submit` against the Compose cluster

## Local dev

### Run unit tests
```bash
sbt test
```

### Run integration tests
```bash
sbt IntegrationTest/test
```

### Build a fat jar for Docker
```bash
sbt assembly
# produces: target/scala-2.13/workshop-assembly.jar
```

## Run on Docker Compose Spark cluster

1) Build the jar:
```bash
sbt assembly
```

2) Start the cluster and submit the job:
```bash
docker compose up --build
```

The submit container will run:
- input: `/opt/spark/work-dir/input/input.txt`
- output: `/opt/spark/work-dir/output/$(date +%Y%m%d_%H%M%S)`


3) To run spark shell interactively:
```bash
docker run -it apache/spark:4.0.2-scala2.13-java17-python3-ubuntu /opt/spark/bin/spark-shell
```
In spark shell,
```scala
sc.setLogLevel("ERROR")
import spark.implicits._
```

4) To start the cluster without submitting the job, and then submit the job manually:
```bash
docker compose up --build -d
docker exec -it workshop-wordcount-submit /bin/bash
# Inside the container, run:
/opt/spark/bin/spark-submit \
  --master spark://spark-master:7077 \
  --deploy-mode client \
  --conf "spark.sql.sources.partitionOverwriteMode=dynamic" \
  /opt/spark/work-dir/workshop-assembly.jar \
  file:///opt/spark/work-dir/input/input.txt \
  file:///opt/spark/work-dir/output/$(date +%Y%m%d_%HM%S)
```

5) To run the cluster and submit the job, and then view the Spark UI:
```bash
docker compose up --build
```
- Spark UI: http://localhost:8080
- Spark Master logs: `docker compose logs spark-master`
- Spark Worker logs: `docker compose logs spark-worker-1` and `docker compose logs spark-worker-2`
- Job submit logs: `docker compose logs wordcount-submit`

6) To stop the cluster:
```bash
docker compose down
```

