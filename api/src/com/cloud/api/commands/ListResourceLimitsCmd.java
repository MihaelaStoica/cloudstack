// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseListProjectAndAccountResourcesCmd;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.ResourceLimitResponse;
import com.cloud.configuration.ResourceLimit;

@Implementation(description="Lists resource limits.", responseObject=ResourceLimitResponse.class)
public class ListResourceLimitsCmd extends BaseListProjectAndAccountResourcesCmd {
    public static final Logger s_logger = Logger.getLogger(ListResourceLimitsCmd.class.getName());

    private static final String s_name = "listresourcelimitsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="Lists resource limits by ID.")
    private Long id;

    @Parameter(name=ApiConstants.RESOURCE_TYPE, type=CommandType.INTEGER, description="Type of resource to update. Values are 0, 1, 2, 3, and 4. 0 - Instance. Number of instances a user can create. " +
																						"1 - IP. Number of public IP addresses a user can own. " +
																						"2 - Volume. Number of disk volumes a user can create." +
																						"3 - Snapshot. Number of snapshots a user can create." +
																						"4 - Template. Number of templates that a user can register/create.")
	private Integer resourceType;
			
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    
    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public void execute(){
        List<? extends ResourceLimit> result = _resourceLimitService.searchForLimits(id, finalyzeAccountId(this.getAccountName(), this.getDomainId(), this.getProjectId(), false), this.getDomainId(), resourceType, this.getStartIndex(), this.getPageSizeVal());
        ListResponse<ResourceLimitResponse> response = new ListResponse<ResourceLimitResponse>();
        List<ResourceLimitResponse> limitResponses = new ArrayList<ResourceLimitResponse>();
        for (ResourceLimit limit : result) {
            ResourceLimitResponse resourceLimitResponse = _responseGenerator.createResourceLimitResponse(limit);
            resourceLimitResponse.setObjectName("resourcelimit");
            limitResponses.add(resourceLimitResponse);
        }

        response.setResponses(limitResponses);
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
