/* 
 * Copyright 2017 SoftAvail Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.softavail.commsrouter.api.dto.arg;

/**
 *
 * @author ikrustev
 */
public class CreateRouterArg {

  private String name;
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public static class Builder {

    private CreateRouterArg arg = new CreateRouterArg();

    public Builder() {
      arg.setDescription("default");
      arg.setName("default");
    }

    public Builder description(String description) {
      arg.setDescription(description);
      return this;
    }

    public Builder name(String name) {
      arg.setName(name);
      return this;
    }

    public CreateRouterArg build() {
      return arg;
    }
  }
}
