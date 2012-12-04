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
import com.cloud.api.response.HypervisorCapabilitiesResponse;
import com.cloud.api.response.ListResponse;
import com.cloud.hypervisor.Hypervisor.HypervisorType;
import com.cloud.hypervisor.HypervisorCapabilities;
import com.cloud.utils.Pair;

@Implementation(description="Lists all hypervisor capabilities.", responseObject=HypervisorCapabilitiesResponse.class, since="3.0.0")
public class ListHypervisorCapabilitiesCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListHypervisorCapabilitiesCmd.class.getName());

    private static final String s_name = "listhypervisorcapabilitiesresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="hypervisor_capabilities")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, description="ID of the hypervisor capability")
    private Long id;

    @Parameter(name=ApiConstants.HYPERVISOR, type=CommandType.STRING, description="the hypervisor for which to restrict the search")
    private String hypervisor;


    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public HypervisorType getHypervisor() {
        if(hypervisor != null){
            return HypervisorType.getType(hypervisor);
        }else{
            return null;
        }
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
        Pair<List<? extends HypervisorCapabilities>, Integer> hpvCapabilities = _mgr.listHypervisorCapabilities(getId(),
                getHypervisor(), getKeyword(), this.getStartIndex(), this.getPageSizeVal());
        ListResponse<HypervisorCapabilitiesResponse> response = new ListResponse<HypervisorCapabilitiesResponse>();
        List<HypervisorCapabilitiesResponse> hpvCapabilitiesResponses = new ArrayList<HypervisorCapabilitiesResponse>();
        for (HypervisorCapabilities capability : hpvCapabilities.first()) {
            HypervisorCapabilitiesResponse hpvCapabilityResponse = _responseGenerator.createHypervisorCapabilitiesResponse(capability);
            hpvCapabilityResponse.setObjectName("hypervisorCapabilities");
            hpvCapabilitiesResponses.add(hpvCapabilityResponse);
        }

        response.setResponses(hpvCapabilitiesResponses, hpvCapabilities.second());
        response.setResponseName(getCommandName());
        this.setResponseObject(response);
    }
}
