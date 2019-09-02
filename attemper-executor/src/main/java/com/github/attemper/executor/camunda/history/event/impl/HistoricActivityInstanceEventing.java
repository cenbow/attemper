package com.github.attemper.executor.camunda.history.event.impl;

import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.result.dispatch.instance.InstanceAct;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.service.instance.InstanceService;
import com.github.attemper.executor.camunda.history.event.EndEventing;
import com.github.attemper.executor.camunda.history.event.EventingAdapter;
import com.github.attemper.executor.camunda.history.event.StartEventing;
import com.github.attemper.executor.util.CamundaUtil;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;

public class HistoricActivityInstanceEventing extends EventingAdapter<HistoricActivityInstanceEventEntity> implements StartEventing<HistoricActivityInstanceEventEntity>, EndEventing<HistoricActivityInstanceEventEntity> {
    public HistoricActivityInstanceEventing(HistoricActivityInstanceEventEntity historyEvent) {
        super(historyEvent);
    }

    @Override
    public void start() {
        InstanceService instanceService = SpringContextAware.getBean(InstanceService.class);
        InstanceAct instanceAct = toInstanceAct(historyEvent);
        instanceAct.setStatus(InstanceStatus.RUNNING.getStatus());
        instanceAct.setStartTime(historyEvent.getStartTime());
        instanceService.addAct(instanceAct);
    }

    @Override
    public void end() {
        InstanceService instanceService = SpringContextAware.getBean(InstanceService.class);
        InstanceAct instanceAct = new InstanceAct().setId(CamundaUtil.extractIdFromActInstanceId(historyEvent.getActivityInstanceId()));
        instanceAct.setStatus(InstanceStatus.SUCCESS.getStatus());
        instanceAct.setEndTime(historyEvent.getEndTime());
        if (historyEvent.getEndTime() != null && historyEvent.getStartTime() != null) {
            instanceAct.setDuration(historyEvent.getEndTime().getTime() - historyEvent.getStartTime().getTime());
        }
        instanceService.updateAct(instanceAct);
    }

    private InstanceAct toInstanceAct(HistoricActivityInstanceEventEntity historyEvent) {
        return new InstanceAct()
                .setId(CamundaUtil.extractIdFromActInstanceId(historyEvent.getId()))
                .setActInstId(historyEvent.getActivityInstanceId())
                .setParentActInstId(historyEvent.getParentActivityInstanceId())
                .setExecutionId(historyEvent.getExecutionId())
                .setProcInstId(historyEvent.getProcessInstanceId())
                .setRootProcInstId(historyEvent.getRootProcessInstanceId())
                .setActId(historyEvent.getActivityId())
                .setActName(historyEvent.getActivityName())
                .setActType(historyEvent.getActivityType());
    }

}
