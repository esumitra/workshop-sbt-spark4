# Runs the WordCount job against the docker-compose Spark cluster
FROM apache/spark:4.0.2-scala2.13-java17-python3-ubuntu

# Copy the fat jar (built with: sbt assembly)
# Note: this path exists on the host after assembly.
COPY target/scala-2.13/workshop-assembly.jar /opt/spark/work-dir/workshop-assembly.jar

WORKDIR /opt/spark/work-dir

# Copy the job submission script with executable permissions
COPY --chmod=755 jobSubmit.sh /opt/spark/work-dir/jobSubmit.sh

ENTRYPOINT ["/opt/spark/work-dir/jobSubmit.sh"]
