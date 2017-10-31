/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.softavail.commsrouter.jpa.test;

import com.softavail.commsrouter.api.dto.misc.PaginatedList;
import com.softavail.commsrouter.api.dto.model.ApiObjectId;
import com.softavail.commsrouter.api.dto.model.PlanDto;
import com.softavail.commsrouter.api.dto.model.RouterObjectId;
import com.softavail.commsrouter.api.exception.CommsRouterException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author G.Ivanov
 */
public class CorePlanServiceJpaTest extends TestBase {

  private static final String ROUTER_ID = "router-id";

  // Testing the create method of the CorePlanService class
  @Test
  public void createTest() throws CommsRouterException {
    RouterObjectId id = new RouterObjectId("plan-id", ROUTER_ID);
    String queueId = "queueId_one";
    routerService.create(newCreateRouterArg("router-name", ""), ROUTER_ID);
    queueService.create(newCreateQueueArg("1==1", "queue 1"), new RouterObjectId(queueId, id));
    planService.create(newCreatePlanArg("desctiption_one", "1==1", queueId), id);
    PlanDto createdPlan = planService.get(id);
    assertEquals(createdPlan.getDescription(), "desctiption_one");
  }

  // Testing the update method of the CorePlanService class
  @Test
  public void updateTest() throws CommsRouterException {
    String queueId1 = "queueId_one";
    RouterObjectId id = new RouterObjectId("plan-id", ROUTER_ID);
    routerService.create(newCreateRouterArg("router-name", ""), ROUTER_ID);
    queueService.create(newCreateQueueArg("1==1", "queue 1"), new RouterObjectId(queueId1, id));
    planService.create(newCreatePlanArg("desctiption_one", "1==1", queueId1), id);
    planService.update(newUpdatePlanArg("desctiption_two"), id);
    PlanDto updatedPlan = planService.get(id);
    assertEquals(updatedPlan.getDescription(), "desctiption_two");
  }

  // Testing method list from CoreRouterObjectService
  @Test
  public void listTest() throws CommsRouterException {
    String queueId = "queueId_one";
    routerService.create(newCreateRouterArg("router-name", ""), ROUTER_ID);
    queueService.create(newCreateQueueArg("1==1", "queue 1"),
        new RouterObjectId(queueId, ROUTER_ID));
    planService.create(newCreatePlanArg("desctiption_one", "1==1", queueId), ROUTER_ID);
    planService.create(newCreatePlanArg("desctiption_two", "1==1", queueId), ROUTER_ID);
    List<PlanDto> plans = planService.list(ROUTER_ID);
    assertEquals(plans.size(), 2);
  }

  // Testing method delete from CoreRouterObjectService
  @Test
  public void deleteTest() throws CommsRouterException {
    String queueId = "queueId_one";
    RouterObjectId id = new RouterObjectId("plan-id", ROUTER_ID);
    routerService.create(newCreateRouterArg("router-name", ""), ROUTER_ID);
    queueService.create(newCreateQueueArg("1==1", "queue 1"), new RouterObjectId(queueId, id));
    planService.create(newCreatePlanArg("desctiption_one", "1==1", queueId), id);
    List<PlanDto> plans = planService.list(id.getRouterId());
    assertEquals(plans.size(), 1);
    planService.delete(id);
    plans = planService.list(id.getRouterId());
    assertEquals(plans.size(), 0);
  }

  // Testing PaginatedList (list method from CoreRouterService)
  @Test
  public void listPagesTest() throws CommsRouterException {
    String queueId = "queueId_one";
    RouterObjectId id = new RouterObjectId("plan-id", ROUTER_ID);
    routerService.create(newCreateRouterArg("router-name", ""), ROUTER_ID);
    queueService.create(newCreateQueueArg("1==1", "queue 1"), new RouterObjectId(queueId, id));
    ApiObjectId plan = planService.create(newCreatePlanArg("desctiption_one", "1==1", queueId), id);
    PaginatedList<PlanDto> list = planService.list("router-id", 0, 0);
  }

}
