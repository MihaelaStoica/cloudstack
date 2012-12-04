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
import org.apache.cloudstack.api.BaseListCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.PodResponse;
import com.cloud.dc.Pod;
import com.cloud.utils.Pair;

@Implementation(description="Lists all Pods.", responseObject=PodResponse.class)
public class ListPodsByCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListPodsByCmd.class.getName());

    private static final String s_name = "listpodsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="host_pod_ref")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="list Pods by ID")
    private Long id;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="list Pods by name")
    private String podName;

    @IdentityMapper(entityTableName="data_center")
    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.LONG, description="list Pods by Zone ID")
    private Long zoneId;
    
    @Parameter(name=ApiConstants.ALLOCATION_STATE, type=CommandType.STRING, description="list pods by allocation state")
    private String allocationState;    
    
    @Parameter(name=ApiConstants.SHOW_CAPACITIES, type=CommandType.BOOLEAN, description="flag to display the capacity of the pods")
    private Boolean showCapacities;
    
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public String getPodName() {
        return podName;
    }

    public Long getZoneId() {
        return zoneId;
    }
    
    public String getAllocationState() {
    	return allocationState;
    }    

    public Boolean getShowCapacities() {
		return showCapacities;
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
        Pair<List<? extends Pod>, Integer> result = _mgr.searchForPods(this);
        ListResponse<PodResponse> response = new ListResponse<PodResponse>();
        List<PodResponse> podResponses = new ArrayList<PodResponse>();
        for (Pod pod : result.first()) {
            PodResponse podResponse = _responseGenerator.createPodResponse(pod, showCapacities);
            podResponse.setObjectName("pod");
            podResponses.add(podResponse);
        }

        response.setResponses(podResponses, result.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
