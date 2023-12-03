package io.axual.ksml.rest.server.resources;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmCompilationMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmInfoMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;

/**
 * Implements endpoints for managing log levels at runtime. The settings are not persisted
 */
@Slf4j
@Path("/metrics")
public class MetricsResource implements AutoCloseable {

  public final PrometheusMeterRegistry prometheusRegistry;
  private final JvmGcMetrics gcMetrics;
  private final JvmHeapPressureMetrics heapPressureMetrics;

  private final LogbackMetrics logbackMetrics;

  public MetricsResource() {
    prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    new ClassLoaderMetrics().bindTo(prometheusRegistry);
    new JvmCompilationMetrics().bindTo(prometheusRegistry);
    gcMetrics = new JvmGcMetrics();
    gcMetrics.bindTo(prometheusRegistry);

    heapPressureMetrics = new JvmHeapPressureMetrics();
    heapPressureMetrics.bindTo(prometheusRegistry);
    new JvmInfoMetrics().bindTo(prometheusRegistry);
    new JvmMemoryMetrics().bindTo(prometheusRegistry);
    new JvmThreadMetrics().bindTo(prometheusRegistry);

    new ProcessorMetrics().bindTo(prometheusRegistry);
    new UptimeMetrics().bindTo(prometheusRegistry);
    logbackMetrics = new LogbackMetrics();
    logbackMetrics.bindTo(prometheusRegistry);
  }

  private KafkaStreamsMetrics streamsMetrics = null;

  public void registerKafkaStreams(KafkaStreams streams) {
    streamsMetrics = new KafkaStreamsMetrics(streams);
    streamsMetrics.bindTo(prometheusRegistry);
  }

  public void registerConfigDir(File configDir) {
    new DiskSpaceMetrics(configDir,
        List.of(Tag.of("ksml.dir.config", configDir.getPath())))
        .bindTo(prometheusRegistry);
  }

  public void registerSchemaDir(File schemaDir) {
    new DiskSpaceMetrics(schemaDir,
        List.of(Tag.of("ksml.dir.schema", schemaDir.getPath())))
        .bindTo(prometheusRegistry);
  }

  public void registerStorageDir(File storageDir) {
    new DiskSpaceMetrics(storageDir,
        List.of(Tag.of("ksml.dir.storage", storageDir.getPath())))
        .bindTo(prometheusRegistry);
  }

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response metric() {
    return Response.ok(prometheusRegistry.scrape()).build();
  }

  @Override
  public void close() throws Exception {
    heapPressureMetrics.close();
    gcMetrics.close();
    prometheusRegistry.close();

    logbackMetrics.close();
    Optional.ofNullable(streamsMetrics).ifPresent(KafkaStreamsMetrics::close);
  }
}
