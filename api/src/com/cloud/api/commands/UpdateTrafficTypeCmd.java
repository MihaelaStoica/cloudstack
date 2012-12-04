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

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.TrafficTypeResponse;
import com.cloud.async.AsyncJob;
import com.cloud.event.EventTypes;
import com.cloud.network.PhysicalNetworkTrafficType;
import com.cloud.user.Account;

@Implementation(description="Updates traffic type of a physical network", responseObject=TrafficTypeResponse.class, since="3.0.0")
public class UpdateTrafficTypeCmd extends BaseAsyncCmd {
    public static final Logger s_logger = Logger.getLogger(UpdateTrafficTypeCmd.class.getName());

    private static final String s_name = "updatetraffictyperesponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    
    @IdentityMapper(entityTableName="physical_network_traffic_types")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, required=true, description="traffic type id")
    private Long id;
    
    @Parameter(name=ApiConstants.XEN_NETWORK_LABEL, type=CommandType.STRING, description="The network name label of the physical device dedicated to this traffic on a XenServer host")
    private String xenLabel;
    
    @Parameter(name=ApiConstants.KVM_NETWORK_LABEL, type=CommandType.STRING, description="The network name label of the physical device dedicated to this traffic on a KVM host")
    private String kvmLabel;
    
    @Parameter(name=ApiConstants.VMWARE_NETWORK_LABEL, type=CommandType.STRING, description="The network name label of the physical device dedicated to this traffic on a VMware host")
    private String vmwareLabel;
    

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
    
    public Long getId() {
        return id;
    }
    public String getXenLabel() {
        return xenLabel;
    }

    public String getKvmLabel() {
        return kvmLabel;
    }
    
    public String getVmwareLabel() {
        return vmwareLabel;
    }    
    
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }
    
    @Override
    public void execute(){
        PhysicalNetworkTrafficType result = _networkService.updatePhysicalNetworkTrafficType(getId(), getXenLabel(), getKvmLabel(), getVmwareLabel());
        if (result != null) {
            TrafficTypeResponse response = _responseGenerator.createTrafficTypeResponse(result);
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        }else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to update traffic type");
        }
    }

    @Override
    public String getEventDescription() {
        return  "Updating Traffic Type: " + getId();
    }
    
    @Override
    public String getEventType() {
        return EventTypes.EVENT_TRAFFIC_TYPE_UPDATE;
    }

    @Override
    public AsyncJob.Type getInstanceType() {
        return AsyncJob.Type.TrafficType;
    }
}
