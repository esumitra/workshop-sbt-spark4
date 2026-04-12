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
docker exec -it spark-master /bin/bash
# Inside the container, run:
$SPARK_HOME/bin/spark-submit \
  --master spark://spark-master:7077 \
  --deploy-mode client \
  --conf "spark.sql.sources.partitionOverwriteMode=dynamic" \
  /opt/spark/work-dir/workshop-assembly.jar \
  file:///opt/spark/work-dir/input/input.txt \
  file:///opt/spark/work-dir/output/$(date +%Y%m%d_%HM%S)
```
To test:

```bash
docker exec -it spark-master /bin/bash
$SPARK_HOME/bin/spark-submit   --master spark://spark-master:7077  ${SPARK_HOME}/examples/src/main/python/pi.py  1000
```

Open Spark UI at http://localhost:9080 to monitor the job.
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

## Run on AWS EMR
1) Build the jar:
```bashsbt assembly
# produces: target/scala-2.13/workshop-assembly.jar
```
2) Upload the jar to S3:
```bash
aws s3 cp target/scala-2.13/workshop-assembly.jar s3://your-bucket/path/workshop-assembly.jar
```
3) Create an EMR serverless cluster and submit the job:
```bash

aws emr-serverless create-application --type spark \
  --release-label emr-spark-8.0-preview \
  --region us-east-1 --name spark4-preview-workshop

aws emr-serverless start-job-run \
    --application-id application-id \
    --execution-role-arn job-role-arn \
    --job-driver '{
        "sparkSubmit": {
            "entryPoint": "/usr/lib/spark/examples/jars/spark-examples.jar",
            "entryPointArguments": ["s3://es-emr-data/emr-serverless-spark/input/0001", "s3://es-emr-data/emr-serverless-spark/output/$(date +%Y%m%d_%H%M%S)"],
            "sparkSubmitParameters": "--class org.apache.spark.examples.SparkPi --conf spark.executor.cores=4 --conf spark.executor.memory=20g --conf spark.driver.cores=4 --conf spark.driver.memory=8g --conf spark.executor.instances=1"
        }
    }'
```

4) Monitor the cluster and job status in the AWS Management Console under EMR.

## Notes
- The Spark version in the Docker image (4.0.2) is newer than the EMR release (6.3.0 with Spark 3.2.1). Some features may differ between versions, so adjust the code accordingly if you encounter compatibility issues.
- Ensure that your AWS CLI is configured with the appropriate permissions to create EMR clusters and access S3 buckets.
- The input and output paths in the EMR step should be adjusted to point to the correct S3 locations if you want to read/write from S3 instead of HDFS.
- For local testing, you can also run the Spark job directly using `spark-submit` without Docker, but using Docker ensures consistency with the EMR environment.

## License
Ed Sumitra, 2026
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details

