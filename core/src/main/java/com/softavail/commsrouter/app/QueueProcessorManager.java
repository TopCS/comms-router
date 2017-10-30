package com.softavail.commsrouter.app;

import com.google.common.collect.Maps;

import com.softavail.commsrouter.domain.dto.mappers.EntityMappers;
import com.softavail.commsrouter.jpa.JpaDbFacade;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by @author mapuo on 23.10.17.
 */
public class QueueProcessorManager {

  private static final boolean DO_NOT_INTERRUPT_IF_RUNNING = false;

  private static final QueueProcessorManager instance = new QueueProcessorManager();

  private final Map<String, QueueProcessor> queueProcessors = Maps.newHashMap();
  private final Map<String, ScheduledFuture> scheduledFutures = Maps.newHashMap();

  private QueueProcessorManager() {
  }

  public static QueueProcessorManager getInstance() {
    return instance;
  }

  public synchronized void processQueue(
      String queueId,
      JpaDbFacade db,
      EntityMappers mappers,
      TaskDispatcher taskDispatcher,
      CoreConfiguration configuration,
      ScheduledThreadPoolExecutor threadPool) {

    Optional.ofNullable(scheduledFutures.get(queueId))
        .ifPresent(scheduledFuture -> scheduledFuture.cancel(DO_NOT_INTERRUPT_IF_RUNNING));

    QueueProcessor queueProcessor = queueProcessors.get(queueId);
    if (queueProcessor == null) {
      queueProcessor = new QueueProcessor.Builder()
          .setQueueId(queueId)
          .setDb(db)
          .setMappers(mappers)
          .setTaskDispatcher(taskDispatcher)
          .setThreadPool(threadPool)
          .setProcessRetryDelaySeconds(configuration.getQueueProcessRetryDelay())
          .setStateChangeListener((StateIdleListener) processedQueueId -> {
            ScheduledFuture<?> schedule = threadPool.schedule(
                () -> removeQueueProcessor(processedQueueId),
                configuration.getQueueProcessorEvictionDelay(), TimeUnit.MINUTES);
            scheduledFutures.put(processedQueueId, schedule);
          })
          .build();
      queueProcessors.put(queueId, queueProcessor);
    }
    queueProcessor.process();
  }

  private synchronized void removeQueueProcessor(String queueId) {
    QueueProcessor queueProcessor = queueProcessors.get(queueId);
    if (!queueProcessor.isWorking()) {
      queueProcessors.remove(queueId);
    }
  }

}