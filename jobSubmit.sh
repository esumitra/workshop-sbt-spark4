#!/bin/bash

echo "Waiting for Spark master to be ready..."

# Wait for the master to be reachable
for i in {1..30}; do
  if timeout 5 bash -c "</dev/tcp/spark-master/7077" >/dev/null 2>&1; then
    echo "Spark master is ready!"
    break
  fi
  echo "Attempt $i: Spark master not ready yet, waiting..."
  sleep 10
done

# Give a bit more time for workers to connect
echo "Waiting additional time for workers to register..."
sleep 20

# Clean up output directory to make job idempotent
echo "Cleaning up output directory..."
rm -rf /opt/spark/work-dir/output/*

# Submit to the standalone cluster in docker-compose
# Input/output are mounted via docker-compose volumes
/opt/spark/bin/spark-submit \
  --class "com.example.workshop.spark.WordCount" \
  --master "spark://spark-master:7077" \
  --conf "spark.sql.adaptive.enabled=true" \
  --conf "spark.sql.adaptive.coalescePartitions.enabled=true" \
  --conf "spark.serializer=org.apache.spark.serializer.KryoSerializer" \
  --conf "spark.sql.sources.partitionOverwriteMode=dynamic" \
  /opt/spark/work-dir/workshop-assembly.jar \
  file:///opt/spark/work-dir/input/input.txt \
  file:///opt/spark/work-dir/output/$(date +%Y%m%d_%H%M%S)