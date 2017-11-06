/*
 * To change this license header, choose License Headers in Project Attributes. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.softavail.commsrouter.domain;

import com.softavail.commsrouter.api.dto.model.AgentState;
import com.softavail.commsrouter.api.dto.model.RouterObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author ikrustev
 */
@Entity
@Table(name = "agent")
public class Agent extends RouterObject {

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "capabilities_attribute_group_id")
  private AttributeGroup capabilities;

  private String address;

  @Enumerated(EnumType.STRING)
  private AgentState state;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "agent_queue", joinColumns = @JoinColumn(name = "agent_id"),
      inverseJoinColumns = @JoinColumn(name = "queue_id"))
  private List<Queue> queues = new ArrayList<>();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_time_at_busy_state")
  private Date lastTimeAtBusyState;

  public Agent() {}

  public Agent(RouterObjectId objectId) {
    super(objectId.getId());
  }

  public AttributeGroup getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(AttributeGroup capabilities) {
    this.capabilities = capabilities;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public AgentState getState() {
    return state;
  }

  public void setState(AgentState state) {
    this.state = state;
    if (state == AgentState.busy
        || state == AgentState.ready) {
      updateLastTimeAtBusyState();
    }
  }

  public List<Queue> getQueues() {
    return queues;
  }

  public void setQueues(List<Queue> queue) {
    this.queues = queue;
  }

  public Date getLastTimeAtBusyState() {
    return lastTimeAtBusyState;
  }

  public void updateLastTimeAtBusyState() {
    this.lastTimeAtBusyState = new Date();
  }

}
